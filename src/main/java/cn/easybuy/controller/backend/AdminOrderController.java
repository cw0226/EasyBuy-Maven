package cn.easybuy.controller.backend;

import cn.easybuy.entity.Order;
import cn.easybuy.entity.OrderDetail;
import cn.easybuy.service.order.OrderService;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单的主页面
     * @return
     */
    @RequestMapping("/index/{userId}")
    public String index(Model model, @PathVariable String userId, String currentPage, String pageSize)throws Exception{
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPageStr = EmptyUtils.isEmpty(currentPage)?1:Integer.parseInt(currentPage);
        int total=orderService.count(Integer.valueOf(userId));
        Pager pager=new Pager(total,rowPerPage,currentPageStr);
        pager.setUrl("/admin/order/index/"+userId);
        List<Order> orderList=orderService.getOrderList(Integer.valueOf(userId),(currentPageStr-1)*rowPerPage, rowPerPage);
        model.addAttribute("orderList", orderList);
        model.addAttribute("pager", pager);
        model.addAttribute("menu", 1);
        return "backend/order/orderList";
    }
    /**
     * 查询订单明细
     * @return
     */
    @RequestMapping("/queryOrderDeatil/{orderId}")
    public String queryOrderDeatil(Model model,@PathVariable String orderId)throws Exception{
        List<OrderDetail> orderDetailList=orderService.getOrderDetailList(Integer.valueOf(orderId));
        model.addAttribute("orderDetailList", orderDetailList);
        model.addAttribute("menu", 1);
        return "backend/order/orderDetailList";
    }
    @RequestMapping("/queryAllOrder")
    public String queryAllOrder(Model model,String currentPage,String pageSize)throws Exception{
        int rowPerPage  = EmptyUtils.isEmpty(pageSize)?5:Integer.parseInt(pageSize);
        int currentPageStr = EmptyUtils.isEmpty(currentPage)?1:Integer.parseInt(currentPage);
        int total=orderService.count(null);
        Pager pager=new Pager(total,rowPerPage,currentPageStr);
        pager.setUrl("/admin/order/queryAllOrder");
        List<Order> orderList=orderService.getOrderList(null,(currentPageStr-1)*rowPerPage, rowPerPage);
        model.addAttribute("orderList", orderList);
        model.addAttribute("pager", pager);
        model.addAttribute("menu", 9);
        return "backend/order/orderList";
    }
}
