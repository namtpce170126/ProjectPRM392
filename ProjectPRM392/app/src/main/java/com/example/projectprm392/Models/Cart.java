package com.example.projectprm392.Models;

public class Cart {
    private int cartId;
    private int cusId;
    private int proId;
    private int proQuantity;
    private double cartPrice;

    public Cart(int cartId, int cusId, int proId, int proQuantity, double cartPrice) {
        this.cartId = cartId;
        this.cusId = cusId;
        this.proId = proId;
        this.proQuantity = proQuantity;
        this.cartPrice = cartPrice;
    }
}
