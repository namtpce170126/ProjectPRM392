package com.example.projectprm392.Models;

public class Favorite {
    private int favoriteId;
    private int accId;
    private int proId;

    public Favorite(int favoriteId, int accId, int proId) {
        this.favoriteId = favoriteId;
        this.accId = accId;
        this.proId = proId;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }
}
