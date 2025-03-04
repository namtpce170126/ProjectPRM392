package com.example.projectprm392.Models;

public class Product {
    private int proId;
    private int catId;
    private String proName;
    private String proImage;
    private int proQuantity;
    private double proPrice;
    private double discount;
    private String description;
    private String createDate;
    private int isDelete;

    public Product(int proId, int catId, String proName, String proImage, int proQuantity, double proPrice, double discount, String description, String createDate, int isDelete) {
        this.proId = proId;
        this.catId = catId;
        this.proName = proName;
        this.proImage = proImage;
        this.proQuantity = proQuantity;
        this.proPrice = proPrice;
        this.discount = discount;
        this.description = description;
        this.createDate = createDate;
        this.isDelete = isDelete;
    }
}
