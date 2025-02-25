package com.example.projectprm392;

import android.media.Image;

public class Cart {
    int imageProductInCart;
    String nameProductInCart;
 int quantityInCart;
    double priceInCart;

    public Cart( int imageProductInCart,String nameProductInCart,int quantityInCart, double priceInCart) {
        this.imageProductInCart = imageProductInCart;
        this.nameProductInCart = nameProductInCart;
        this.quantityInCart = quantityInCart;
        this.priceInCart = priceInCart;
    }

    public int getImageProductInCart() {
        return imageProductInCart;
    }

    public void setImageProductInCart(int imageProductInCart) {
        this.imageProductInCart = imageProductInCart;
    }

    public String getNameProductInCart() {
        return nameProductInCart;
    }

    public void setNameProductInCart(String nameProductInCart) {
        this.nameProductInCart = nameProductInCart;
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public double getPriceInCart() {
        return priceInCart;
    }

    public void setPriceInCart(double priceInCart) {
        this.priceInCart = priceInCart;
    }
}
