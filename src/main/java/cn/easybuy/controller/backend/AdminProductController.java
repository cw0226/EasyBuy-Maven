package cn.easybuy.controller.backend;

import cn.easybuy.entity.Product;
import cn.easybuy.entity.ProductCategory;
import cn.easybuy.param.ProductCategoryParam;
import cn.easybuy.param.ProductParam;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.ReturnResult;
import cn.easybuy.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    @Autowired
    private ProductCategoryService productCategoryService;
    private static final String TMP_DIR_PATH = "c:\\tmp";
    private File tmpDir;
    private static final String DESTINATION_DIR_PATH = "/files";
    private File destinationDir;
    @Autowired
    private ProductService productService;

    /**
     * 商品的主页面
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model, String currentPage, String pageSize)throws Exception{
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPageStr = EmptyUtils.isEmpty(currentPage)?1:Integer.parseInt(currentPage);
        int total=productService.count(null, null, null);
        Pager pager=new Pager(total,rowPerPage,currentPageStr);
        pager.setUrl("/admin/product/index");
        List<Product> productList=productService.getProductList((currentPageStr-1)*rowPerPage,rowPerPage, null, null, null);
        model.addAttribute("productList", productList);
        model.addAttribute("pager", pager);
        model.addAttribute("menu",5);
        return "backend/product/productList";
    }
    /**
     * 添加商品
     * @return
     */
    @RequestMapping("/toAddProduct")
    public String toAddProduct(Model model)throws Exception{
        model.addAttribute("menu",6);
        model.addAttribute("product",new Product());
        //查询一级分类
        ProductCategoryParam params =new ProductCategoryParam();
        params.setType(1);
        List<ProductCategory> productCategoryList=productCategoryService.queryProductCategoryList(params);
        //一级分类列表
        model.addAttribute("productCategoryList1", productCategoryList);
        return "backend/product/toAddProduct";
    }
    /**
     * 添加商品
     * @throws Exception
     */
    @RequestMapping("/addProduct")
    public String addProduct(HttpServletRequest request)throws Exception {
        System.out.println("aaa");
        Map<String, String> params = new HashMap<String, String>();
        Product product=null;
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
        fileItemFactory.setRepository(tmpDir);
        String fileName = null;
        ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
        uploadHandler.setHeaderEncoding("utf-8");
        try {
            List items = uploadHandler.parseRequest(request);
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()) {
                    params.put(item.getFieldName(), item.getString("utf-8"));
                } else {
                    if (item.getSize() > 0) {//修改图片
                        fileName = item.getName().substring(
                                item.getName().lastIndexOf("."));
                        File file = new File(destinationDir, fileName);
                        fileName = StringUtils.randomUUID()
                                + item.getName().substring(
                                item.getName().lastIndexOf("."));
                        file = new File(destinationDir, fileName);//图片名与商品ID一致
                        item.write(file);//保存商品图片
                    }
                }
            }
            //获取商品参数
            product=copyToProduct(params);
            product.setFileName(fileName);
            Integer id = product.getId();
            if (EmptyUtils.isEmpty(id) || id.equals("0")) {
                productService.add(product);
            } else {
                if(EmptyUtils.isEmpty(product.getFileName())|| product.getFileName().length()<5){
                    Product productDemo=productService.getProductById(id);
                    product.setFileName(productDemo.getFileName());
                }
                productService.update(product);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:index";
    }
    /**
     * 添加商品
     * @return
     */
    @RequestMapping("/toUpdateProduct/{id}")
    public String toUpdateProduct(Model model,@PathVariable String id)throws Exception{
        Product product=productService.getProductById(Integer.valueOf(id));
        model.addAttribute("menu",6);
        //查询一级分类
        ProductCategoryParam params =new ProductCategoryParam();
        params.setType(1);
        List<ProductCategory> productCategoryList1=productCategoryService.queryProductCategoryList(params);
        params.setType(2);
        List<ProductCategory> productCategoryList2=productCategoryService.queryProductCategoryList(params);
        params.setType(3);
        List<ProductCategory> productCategoryList3=productCategoryService.queryProductCategoryList(params);
        //一级分类列表
        model.addAttribute("productCategoryList1", productCategoryList1);
        model.addAttribute("productCategoryList2", productCategoryList2);
        model.addAttribute("productCategoryList3", productCategoryList3);
        model.addAttribute("product", product);
        return "backend/product/toAddProduct";
    }
    /**
     * 根据id删除商品
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteById")
    public String deleteById(String id)throws Exception{
        ReturnResult result=new ReturnResult();
        productService.deleteProductById(Integer.parseInt(id));
        result.returnSuccess();
        return JSON.toJSONString(result);
    }

    private Product copyToProduct(Map<String,String> params)throws Exception{
        Product product=new Product();
        String id=params.get("id");
        String name=params.get("name");
        String description=params.get("description");
        String price=params.get("price");
        String stock=params.get("stock");
        String categoryLevel1Id=params.get("categoryLevel1Id");
        String categoryLevel2Id=params.get("categoryLevel2Id");
        String categoryLevel3Id=params.get("categoryLevel3Id");
        product.setName(name);
        product.setDescription(description);
        product.setPrice(Float.valueOf(price));
        product.setStock(Integer.parseInt(stock));
        product.setCategoryLevel1Id(EmptyUtils.isNotEmpty(categoryLevel1Id)?Integer.parseInt(categoryLevel1Id):0);
        product.setCategoryLevel2Id(EmptyUtils.isNotEmpty(categoryLevel2Id)?Integer.parseInt(categoryLevel2Id):0);
        product.setCategoryLevel3Id(EmptyUtils.isNotEmpty(categoryLevel3Id)?Integer.parseInt(categoryLevel3Id):0);
        product.setId(EmptyUtils.isNotEmpty(id)?Integer.parseInt(id):null);
        return product;
    }
}
