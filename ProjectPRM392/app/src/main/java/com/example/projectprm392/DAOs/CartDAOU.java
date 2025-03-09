package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDAOU extends SingletonBaseDAO{
    private DatabaseHelper dbHelper;
    public CartDAOU(DatabaseHelper dbHelper) {
        super(dbHelper);
        this.dbHelper = dbHelper; // Gán dbHelper để tránh bị null
    }
    public List<Cart> getCartByCustomerId(int cusId) {
        open();
        List<Cart> cartList = new ArrayList<>();

        String query = "SELECT * FROM cart WHERE acc_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cusId)});

        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart(
                        cursor.getInt(0), // cart_id
                        cursor.getInt(1), // acc_id
                        cursor.getInt(2), // pro_id
                        cursor.getInt(3), // pro_quantity
                        cursor.getDouble(4) // cart_price
                );
                cartList.add(cart);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return cartList;
    }

    public Map<Integer, String> getProductImages(List<Cart> cartList) {
        open();
        Map<Integer, String> productImages = new HashMap<>();

        for (Cart cart : cartList) {
            String query = "SELECT pro_image FROM Product WHERE pro_id = ? AND isDelete = 0";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cart.getProId())});

            if (cursor.moveToFirst()) {
                productImages.put(cart.getProId(), cursor.getString(0));
            }
            cursor.close();
        }

        close();
        return productImages;
    }

    public boolean addToCart(Cart cart) {

        if (dbHelper == null) {
            Log.e("DatabaseError", "dbHelper is null!");
        } else {
            Log.d("DatabaseHelper", "dbHelper initialized successfully.");
        }

        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("acc_id", cart.getCusId());
            values.put("pro_id", cart.getProId());
            values.put("pro_quantity", cart.getProQuantity());
            values.put("cart_price", cart.getCartPrice());

            long result = db.insert("cart", null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseError", "Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
