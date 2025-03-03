package com.example.projectprm392;

import java.util.List;

public class Order {
    private String orderId;
    private String orderDate;
    private String orderStatus;
    private List<OrderDetail> orderDetails;

    public Order() {
    }

    public Order(String orderId, String orderDate, String orderStatus, List<OrderDetail> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderDetails = orderDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    // Tính tổng giá của toàn bộ order
    public double getTotalOrderPrice() {
        double total = 0;
        for (OrderDetail detail : orderDetails) {
            total += detail.getTotalPrice();
        }
        return total;
    }
}
