package cn.easybuy.controller.backend;

import cn.easybuy.entity.News;
import cn.easybuy.param.NewsParams;
import cn.easybuy.service.news.NewsService;
import cn.easybuy.service.product.ProductService;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin/news")
public class AdminNewsController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private ProductService productService;

    /**
     * 查询新闻列表
     * @return
     */
    @RequestMapping("/queryNewsList")
    public String queryNewsList(Model model, String currentPage, String pageSize)throws Exception{
        int rowPerPage = EmptyUtils.isEmpty(pageSize)?10:Integer.parseInt(pageSize);
        int currentPageStr = EmptyUtils.isEmpty(currentPage)?1:Integer.parseInt(currentPage);
        int total=newsService.queryNewsCount(new NewsParams());
        Pager pager=new Pager(total,rowPerPage,currentPageStr);
        pager.setUrl("/admin/news/queryNewsList");
        NewsParams params = new NewsParams();
        params.openPage((pager.getCurrentPage() - 1) * pager.getRowPerPage(),pager.getRowPerPage());
        List<News> newsList = newsService.queryNewsList(params);
        model.addAttribute("newsList", newsList);
        model.addAttribute("pager", pager);
        model.addAttribute("menu", 7);
        return "backend/news/newsList";
    }
    /**
     * 查询新闻详情
     * @return
     */
    @RequestMapping("/newsDeatil/{id}")
    public String newsDeatil(Model model,@PathVariable String id)throws Exception{
        News news=newsService.findNewsById(id);
        model.addAttribute("news",news);
        model.addAttribute("menu", 7);
        return "backend/news/newsDetail";
    }
}
