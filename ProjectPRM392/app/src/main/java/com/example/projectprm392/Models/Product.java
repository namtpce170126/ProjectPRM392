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

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProImage() {
        return proImage;
    }

    public void setProImage(String proImage) {
        this.proImage = proImage;
    }

    public int getProQuantity() {
        return proQuantity;
    }

    public void setProQuantity(int proQuantity) {
        this.proQuantity = proQuantity;
    }

    public double getProPrice() {
        return proPrice;
    }

    public void setProPrice(double proPrice) {
        this.proPrice = proPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
