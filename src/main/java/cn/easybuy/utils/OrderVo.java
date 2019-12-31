package cn.easybuy.utils;

import cn.easybuy.entity.Order;
import cn.easybuy.entity.OrderDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bdqn on 2016/5/6.
 */
public class OrderVo implements Serializable {
    private Order order;
    private List<OrderDetail> orderDetailList;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }
}
