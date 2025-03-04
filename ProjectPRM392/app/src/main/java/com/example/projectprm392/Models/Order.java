package com.example.projectprm392.Models;

public class Order {
    private int orderId;
    private int accountId;
    private String payment;
    private String address;
    private String status;
    private String orderDate;
    private double totalPrice;
    private int isDelete;

    public Order(int orderId, int accountId, String payment, String address, String status, String orderDate, double totalPrice, int isDelete) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.payment = payment;
        this.address = address;
        this.status = status;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.isDelete = isDelete;
    }
}
