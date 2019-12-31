package cn.easybuy.controller.backend;

import cn.easybuy.entity.ProductCategory;
import cn.easybuy.param.ProductCategoryParam;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.utils.Constants;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.ReturnResult;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.Request;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin/productCategory")
public class AdminProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    /**
     * 订单的主页面
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model, String currentPage, String pageSize)throws Exception{
        //获取页大小
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?8:Integer.parseInt(pageSize);
        int currentPageStr = EmptyUtils.isEmpty(currentPage)?1:Integer.parseInt(currentPage);
        ProductCategoryParam params =new ProductCategoryParam();
        int total=productCategoryService.queryProductCategoryCount(params);
        Pager pager=new Pager(total,rowPerPage,currentPageStr);
        params.openPage((pager.getCurrentPage()-1)*pager.getRowPerPage(),pager.getRowPerPage());
        pager.setUrl("/admin/productCategory/index");
        List<ProductCategory> productCategoryList=productCategoryService.queryProductCategoryList(params);
        model.addAttribute("productCategoryList", productCategoryList);
        model.addAttribute("pager", pager);
        model.addAttribute("menu", 4);
        return "backend/productCategory/productCategoryList";
    }
    /**
     * 添加商品分类
     * @return
     */
    @RequestMapping(value = "/toAddProductCategory",method = RequestMethod.POST)
    public String toAddProductCategory(Model model)throws Exception{
        //查询一级分类 全部
        List<ProductCategory> productCategoryList=null;
        ProductCategoryParam params =new ProductCategoryParam();
        params.setType(1);
        productCategoryList=productCategoryService.queryProductCategoryList(params);
        model.addAttribute("productCategoryList1",productCategoryList);
        model.addAttribute("productCategory",new ProductCategory());
        return "backend/productCategory/toAddProductCategory";
    }
    /**
     * 修改商品分类
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toUpdateProductCategory",method = RequestMethod.POST)
    public String toUpdateProductCategory(Model model,String id)throws Exception{
        ProductCategory productCategory=productCategoryService.getById(Integer.parseInt(id));
        List<ProductCategory> productCategoryList1=null;
        List<ProductCategory> productCategoryList2=null;
        List<ProductCategory> productCategoryList3=null;
        model.addAttribute("productCategory",productCategory);
        //判断分类级别
        if(productCategory.getType()>=1){
            ProductCategoryParam params =new ProductCategoryParam();
            params.setType(1);
            productCategoryList1=productCategoryService.queryProductCategoryList(params);
        }
        if(productCategory.getType()>=2){
            ProductCategoryParam params =new ProductCategoryParam();
            params.setType(2);
            productCategoryList2=productCategoryService.queryProductCategoryList(params);
            model.addAttribute("parentProductCategory",productCategoryService.getById(productCategory.getParentId()));
        }
        if(productCategory.getType()>=3){
            List<ProductCategory> productCategoryList=null;
            ProductCategoryParam params =new ProductCategoryParam();
            params.setType(3);
            productCategoryList3=productCategoryService.queryProductCategoryList(params);
        }
        model.addAttribute("productCategoryList1",productCategoryList1);
        model.addAttribute("productCategoryList2",productCategoryList2);
        model.addAttribute("productCategoryList3",productCategoryList3);
        return JSON.toJSONString("backend/productCategory/toAddProductCategory");
    }

    /**
     * 查询子分类
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/queryProductCategoryList",method = RequestMethod.POST)
    public String queryProductCategoryList(String parentId)throws Exception{
        ReturnResult result=new ReturnResult();
        List<ProductCategory> productCategoryList=null;
        ProductCategoryParam params =new ProductCategoryParam();
        params.setParentId(EmptyUtils.isEmpty(parentId)?0:Integer.parseInt(parentId));
        productCategoryList=productCategoryService.queryProductCategoryList(params);
        result.setStatus(Constants.ReturnResult.SUCCESS);
        result.setData(productCategoryList);
        return JSON.toJSONString(result);
    }
    /**
     * 修改商品分类
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/modifyProductCategory",method = RequestMethod.POST)
    public String  modifyProductCategory(String id,String productCategoryLevel1,String productCategoryLevel2,String name,String type)throws Exception{
        ReturnResult result=new ReturnResult();
        Integer parentId=0;
        if(type.equals("1")){
            parentId =0;
        }else if(type.equals("2")){
            parentId =Integer.parseInt(productCategoryLevel1);
        }else{
            parentId =Integer.parseInt(productCategoryLevel2);
        }
        ProductCategory productCategory  =new ProductCategory();
        productCategory.setId(Integer.parseInt(id));
        productCategory.setParentId(parentId);
        productCategory.setName(name);
        productCategory.setType(Integer.parseInt(type));
        productCategoryService.modifyProductCategory(productCategory);
        result.returnSuccess();
        return JSON.toJSONString(result);
    }
    /**
     * 添加商品分类
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/addProductCategory",method = RequestMethod.POST)
    public String addProductCategory(String type,String productCategoryLevel1,String productCategoryLevel2,String name)throws Exception{
        System.out.println("addproduct");
        ReturnResult result=new ReturnResult();
        Integer parentId=0;
        //获取分类级别
        if(type.equals("1")){
            parentId =0;
        }else if(type.equals("2")){
            parentId =Integer.parseInt(productCategoryLevel1);
        }else{
            parentId =Integer.parseInt(productCategoryLevel2);
        }
        ProductCategory productCategory =new ProductCategory();
        productCategory.setName(name);
        productCategory.setParentId(parentId);
        productCategory.setType(Integer.parseInt(type));
        productCategory.setIconClass("");
        productCategoryService.addProductCategory(productCategory);
        result.returnSuccess();
        return JSON.toJSONString(result);
    }
    /**
     * 删除商品分类
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "deleteProductCategory",method = RequestMethod.POST)
    public String deleteProductCategory(String id,String type)throws Exception{
        ReturnResult result=new ReturnResult();
        //查询是否有子类
        ProductCategoryParam productCategoryParam=new ProductCategoryParam();
        productCategoryParam.setParentId(Integer.parseInt(id));
        int count=productCategoryService.queryProductCategoryCount(productCategoryParam);
        if(count>0){
            return JSON.toJSONString(result.returnFail("已经存在子分类，不能删除"));
        }
        //查询是否有商品
        count=productService.count(null,Integer.parseInt(id),Integer.parseInt(type));
        if(count>0){
            return JSON.toJSONString(result.returnFail("已经存在商品，不能删除"));
        }
        productCategoryService.deleteById(Integer.parseInt(id));
        result.returnSuccess();
        return JSON.toJSONString(result);
    }
}
