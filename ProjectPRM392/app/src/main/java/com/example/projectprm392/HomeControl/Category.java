package com.example.projectprm392.HomeControl;

public class Category {
    private int categoryId;
    private String categoryName;
    private int categoryImgId;

    public Category(int categoryId, String categoryName, int categoryImgId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImgId = categoryImgId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryImgId() {
        return categoryImgId;
    }

    public void setCategoryImgId(int categoryImgId) {
        this.categoryImgId = categoryImgId;
    }
}
