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
}
