package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends SingletonBaseDAO{
    public ProductDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    public List<Product> getAll() {
        open();
        List<Product> listProduct = new ArrayList<>();
        String query = "SELECT * FROM product";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Product product = new Product(
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
                listProduct.add(product);
            } while(cursor.moveToNext());
        }
        cursor.close();
        close();
        return listProduct;
    }

    public Product getOne(int productId, int isActived) {
        open();
        Product product = null;
        String query;
        Cursor cursor;

        if(isActived == 0 || isActived == 1) {
            query = "SELECT * FROM product WHERE pro_id = ? AND isDelete = ? LIMIT 1";
            cursor = db.rawQuery(query, new String[]{String.valueOf(productId), String.valueOf(isActived)});
        } else {
            query = "SELECT * FROM product WHERE pro_id = ? LIMIT 1";
            cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});
        }

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

    public Product getProductById(int productId) {
        open();
        Product product = null;
        String query = "SELECT * FROM product WHERE pro_id = ? LIMIT 1";
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

    public boolean updateProduct(Product product) {
        open();
        ContentValues values = new ContentValues();
        values.put("pro_name", product.getProName());
        values.put("cat_id", product.getCatId());
        values.put("pro_price", product.getProPrice());
        values.put("pro_quantity", product.getProQuantity());
        values.put("description", product.getDescription());
        values.put("pro_image", product.getProImage());
        int rowsAffected = db.update("product", values, "pro_id = ?", new String[]{String.valueOf(product.getProId())});
        close();
        return rowsAffected > 0;
    }
}