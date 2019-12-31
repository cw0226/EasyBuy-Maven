package cn.easybuy.controller.pre;

import cn.easybuy.entity.Product;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.ProductCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 查询商品列表
     *
     * @return
     */
    @RequestMapping(value = "/queryProductList")
    public String queryProductList(Model model,  String category,  String level,String currentPage,
                                   String keyWord,String pageSize){
        System.out.println(currentPage);
        System.out.println(pageSize);



        int rowPerPage = EmptyUtils.isEmpty(pageSize) ? 20:Integer.parseInt(pageSize);
        int currentPageStr = EmptyUtils.isEmpty(currentPage) ? 1 : Integer.parseInt(currentPage);
        int  levelStr =EmptyUtils.isNotEmpty(level)?Integer.parseInt(level):0;
        int total = productService.count(keyWord, EmptyUtils.isEmpty(category)?null:Integer.valueOf(category), levelStr);
        Pager pager = new Pager(total, rowPerPage, currentPageStr);
        pager.setUrl("/Product/queryProductList?level="+level+"&category="+(EmptyUtils.isEmpty(category)?"":category));
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        List<Product> productList = productService.getProductList(currentPageStr, rowPerPage, keyWord, EmptyUtils.isEmpty(category)?null:Integer.valueOf(category), levelStr);
        model.addAttribute("productList", productList);
        model.addAttribute("pager", pager);
        model.addAttribute("total", total);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("productCategoryVoList", productCategoryVoList);
        return "pre/product/queryProductList";
    }
    /**
     *
     * @return
     */
    @RequestMapping("/queryProductDeatil/{id}")
    public String queryProductDeatil(HttpServletRequest request,@PathVariable String id) throws Exception {
        Product product = productService.getProductById(Integer.valueOf(id));
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        request.setAttribute("product", product);
        request.setAttribute("productCategoryVoList", productCategoryVoList);
        addRecentProduct(request,product);
        return "pre/product/productDeatil";
    }
    /**
     * 查询最近商品
     * @return
     */
    private List<Product> queryRecentProducts(HttpServletRequest request)throws Exception{
        HttpSession session=request.getSession();
        List<Product> recentProducts= (List<Product>) session.getAttribute("recentProducts");
        if(EmptyUtils.isEmpty(recentProducts)){
            recentProducts=new ArrayList<Product>();
        }
        return recentProducts;
    }
    /**
     * 添加最近浏览商品
     * @param request
     * @param product
     */
    private void addRecentProduct(HttpServletRequest request,Product product)throws Exception{
        HttpSession session=request.getSession();
        List<Product> recentProducts=queryRecentProducts(request);
        //判断是否满了
        if(recentProducts.size()>0 &&  recentProducts.size()==10){
            recentProducts.remove(0);
        }
        recentProducts.add(recentProducts.size(),product);
        session.setAttribute("recentProducts",recentProducts);
    }

}
