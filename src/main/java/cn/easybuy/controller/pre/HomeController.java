package cn.easybuy.controller.pre;

import cn.easybuy.entity.News;
import cn.easybuy.entity.Product;
import cn.easybuy.param.NewsParams;
import cn.easybuy.service.news.NewsService;
import cn.easybuy.service.product.ProductCategoryService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.ProductCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/Home")
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 商城主页的方法
     * @return
     */
    @RequestMapping("index")
    public String index(Model model ){
        //查询商品分裂
        List<ProductCategoryVo> productCategoryVoList = productCategoryService.queryAllProductCategoryList();
        Pager pager= new Pager(5, 5, 1);
        NewsParams params = new NewsParams();
        params.openPage((pager.getCurrentPage() - 1) * pager.getRowPerPage(),pager.getRowPerPage());
        List<News> news = newsService.queryNewsList(params);
        //查询一楼
        for (ProductCategoryVo vo : productCategoryVoList) {
            List<Product> productList = productService.getProductList(1, 8, null, vo.getProductCategory().getId(), 1);
            vo.setProductList(productList);
        }
        //封装返回
        model.addAttribute("productCategoryVoList", productCategoryVoList);
        model.addAttribute("news", news);
        return "pre/index";
    }
}
