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

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCusId() {
        return cusId;
    }

    public void setCusId(int cusId) {
        this.cusId = cusId;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public int getProQuantity() {
        return proQuantity;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }

    public double getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(double cartPrice) {
        this.cartPrice = cartPrice;
    }
}
