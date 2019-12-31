package cn.easybuy.controller.pre;

import cn.easybuy.entity.Order;
import cn.easybuy.entity.Product;
import cn.easybuy.entity.User;
import cn.easybuy.entity.UserAddress;
import cn.easybuy.service.order.CartService;
import cn.easybuy.service.order.OrderService;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.service.user.UserAddressService;
import cn.easybuy.service.user.UserService;
import cn.easybuy.utils.*;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/Cart")
public class CartController {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;

    /**
     * 添加到购物车
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(HttpServletRequest request,  String entityId,String quantity) throws Exception {
        ReturnResult result = new ReturnResult();
        Integer quantityStr = 1;
        if (!EmptyUtils.isEmpty(quantity))
            quantityStr = Integer.parseInt(quantity);
        //查询出商品
        Product product = productService.getProductById(Integer.valueOf(entityId));
        if(product.getStock()<quantityStr){
            return JSON.toJSONString(result.returnFail("商品数量不足"));
        }
        //获取购物车
        ShoppingCart cart = getCartFromSession(request);
        //往购物车放置商品
        result=cart.addItem(product, quantityStr);
        if(result.getStatus()== Constants.ReturnResult.SUCCESS){
            cart.setSum((EmptyUtils.isEmpty(cart.getSum()) ? 0.0 : cart.getSum()) + (product.getPrice() * quantityStr * 1.0));
        }
        return JSON.toJSONString(result);
    }



    /**
     * 刷新购物车
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refreshCart",method = RequestMethod.POST)
    public String refreshCart(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        session.setAttribute("cart", cart);//全部的商品
        return "common/pre/searchBar";
    }

    /**
     * 跳到结算页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/toSettlement",method = RequestMethod.POST)
    public String toSettlement(HttpServletRequest request) throws Exception {
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        //封装返回
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        return "pre/settlement/toSettlement";
    }

    /**
     * 跳转到购物车页面
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/settlement1")
    public String settlement1(HttpServletRequest request) throws Exception {
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        request.getSession().setAttribute("cart",cart);
        return "pre/settlement/settlement1";
    }

    /**
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/settlement2")
    public String settlement2(HttpServletRequest request) throws Exception {
        User user = getUserFromSession(request);
        List<UserAddress> userAddressList = userAddressService.queryUserAdressList(user.getId());
        request.setAttribute("userAddressList", userAddressList);
        return "pre/settlement/settlement2";
    }

    /**
     * 生成订单
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/settlement3")
    public Object settlement3(HttpServletRequest request,String addressId,String newAddress,String newRemark) throws Exception {
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        User user = getUserFromSession(request);
        ReturnResult result=checkCart(request);
        if(result.getStatus()==Constants.ReturnResult.FAIL){
            return result;
        }
        //新增地址
        UserAddress userAddress=new UserAddress();
        if(addressId.equals("-1")){
            userAddress.setRemark(newRemark);
            userAddress.setAddress(newAddress);
            userAddress.setId(userAddressService.addUserAddress(user.getId(),newAddress,newRemark));
        }else{
            userAddress=userAddressService.getUserAddressById(Integer.parseInt(addressId));
        }

        //生成订单
        Order order = orderService.payShoppingCart(cart,user,userAddress.getAddress());
        clearCart(request);
        request.setAttribute("currentOrder", order);
        return "pre/settlement/settlement3";
    }

    /**
     * 清空购物车
     *
     * @param request
     */
    @ResponseBody
    @RequestMapping(value = "/clearCart",method = RequestMethod.POST)
    public ReturnResult clearCart(HttpServletRequest request) throws Exception {
        ReturnResult result = new ReturnResult();
        //结账后清空购物车
        request.getSession().removeAttribute("cart");
        result.returnSuccess(null);
        return result;
    }

    /**
     * 修改购物车信息
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modCart",method = RequestMethod.POST)
    public String modCart(HttpServletRequest request, String entityId, String quantity) throws Exception {
        ReturnResult result = new ReturnResult();
        HttpSession session = request.getSession();
        ShoppingCart cart = getCartFromSession(request);
        Product product=productService.getProductById(Integer.valueOf(entityId));
        if(EmptyUtils.isNotEmpty(quantity)){
            if(Integer.parseInt(quantity)>product.getStock()){
                return JSON.toJSONString(result.returnFail("商品数量不足"));
            }
        }
        cart = cartService.modifyShoppingCart(entityId, quantity, cart);
        session.setAttribute("cart", cart);//全部的商品
        return JSON.toJSONString(result.returnSuccess());
    }

    /**
     * 从session中获取购物车
     *
     * @param request
     * @return
     */
    private ShoppingCart getCartFromSession(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private ReturnResult checkCart(HttpServletRequest request) throws Exception {
        ReturnResult result = new ReturnResult();
        HttpSession session = request.getSession();
        ShoppingCart cart = getCartFromSession(request);
        cart = cartService.calculate(cart);
        for (ShoppingCartItem item : cart.getItems()) {
            Product product=productService.getProductById(item.getProduct().getId());
            if(product.getStock()<item.getQuantity()){
                return result.returnFail(product.getName()+"商品数量不足");
            }
        }
        return result.returnSuccess();
    }

    /**
     * @param request
     * @return
     */
    private User getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        return user;
    }
}
