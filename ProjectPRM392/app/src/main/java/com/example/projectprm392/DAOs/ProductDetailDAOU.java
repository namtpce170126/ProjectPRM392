package com.example.projectprm392.DAOs;

import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.Models.OrderDetail;
import com.example.projectprm392.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailDAOU extends SingletonBaseDAO {
    public ProductDetailDAOU(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    // Get all order details by order ID


    public Product getProductDetailsByProductId(int productId) {
        open();
        Product product = null;
        String query = "SELECT * FROM product WHERE pro_id = ? AND isDelete = 0";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if(cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getDouble(5),
                    cursor.getDouble(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getInt(9)
            );
        }
        cursor.close();
        close();
        return product;
    }

    public int getSumProid() {
        open();
        int sum = 0;
        String query = "SELECT COUNT(pro_id) FROM product WHERE isDelete = 0";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return sum;
    }

    public int getDangGiao() {
        open();
        int sum = 0;
        String query = "SELECT COUNT(o_id) FROM orders WHERE status = 'Delivering' AND isDelete = 0";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return sum;
    }

    public int getChoXacNhan() {
        open();
        int sum = 0;
        String query = "SELECT COUNT(o_id) FROM orders WHERE status = 'Ordered' AND isDelete = 0";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return sum;
    }

    public int getSumDoanhThu() {
        open();
        int sum = 0;
        String query = "SELECT SUM(total_price) FROM orders WHERE isDelete = 0";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            sum = cursor.getInt(0);
        }

        if (cursor != null) {
            cursor.close();
        }

        close();
        return sum;
    }



}
