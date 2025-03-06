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

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
