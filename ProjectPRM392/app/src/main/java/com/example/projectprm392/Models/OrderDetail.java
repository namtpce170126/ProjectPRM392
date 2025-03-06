package com.example.projectprm392.Models;

public class OrderDetail {
    private int orderId;
    private int proId;
    private int quantity;

    public OrderDetail(int orderId, int proId, int quantity) {
        this.orderId = orderId;
        this.proId = proId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
