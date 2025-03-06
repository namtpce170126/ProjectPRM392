package com.example.projectprm392.Models;

public class Category {
    private int catId;
    private String catName;
    private String catDescription;
    private int isDeleted;

    public Category(int catId, String catName, String catDescription, int isDeleted) {
        this.catId = catId;
        this.catName = catName;
        this.catDescription = catDescription;
        this.isDeleted = isDeleted;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatDescription() {
        return catDescription;
    }

    public void setCatDescription(String catDescription) {
        this.catDescription = catDescription;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
