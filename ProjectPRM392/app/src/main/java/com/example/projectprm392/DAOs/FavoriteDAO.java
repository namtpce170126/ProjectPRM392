package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO extends SingletonBaseDAO {
    public FavoriteDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    // Thêm sản phẩm yêu thích
    public boolean addToFavorite(int accId, int proId) {
        open();
        ContentValues values = new ContentValues();
        values.put("acc_id", accId);
        values.put("pro_id", proId);
        values.put("isDelete", 0);

        // Kiểm tra xem bản ghi đã tồn tại chưa
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE acc_id = ? AND pro_id = ? AND isDelete = 0",
                new String[]{String.valueOf(accId), String.valueOf(proId)});
        if (cursor.moveToFirst()) {
            cursor.close();
            close();
            return false; // Đã tồn tại, không thêm lại
        }
        cursor.close();

        long result = db.insert("favorites", null, values);
        close();
        return result != -1;
    }

    // Xóa sản phẩm yêu thích (xóa mềm)
    public int deleteFavorite(int accId, int proId) {
        open();
        ContentValues values = new ContentValues();
        values.put("isDelete", 1);
        int result = db.update("favorites", values, "acc_id = ? AND pro_id = ? AND isDelete = 0",
                new String[]{String.valueOf(accId), String.valueOf(proId)});
        close();
        return result;
    }

    // Lấy danh sách sản phẩm yêu thích của một tài khoản
    public List<Favorite> getFavoritesByAccountId(int accId) {
        open();
        List<Favorite> favorites = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE acc_id = ? AND isDelete = 0",
                new String[]{String.valueOf(accId)});
        if (cursor.moveToFirst()) {
            do {
                Favorite favorite = new Favorite(
                        cursor.getInt(0), // favorite_id
                        cursor.getInt(1), // acc_id
                        cursor.getInt(2)  // pro_id
                );
                favorites.add(favorite);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return favorites;
    }

    // Kiểm tra xem sản phẩm đã được yêu thích chưa
    public boolean isFavorite(int accId, int proId) {
        open();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM favorites WHERE acc_id = ? AND pro_id = ? AND isDelete = 0",
                new String[]{String.valueOf(accId), String.valueOf(proId)});
        boolean isFav = false;
        if (cursor.moveToFirst()) {
            isFav = cursor.getInt(0) > 0;
        }
        cursor.close();
        close();
        return isFav;
    }
}
