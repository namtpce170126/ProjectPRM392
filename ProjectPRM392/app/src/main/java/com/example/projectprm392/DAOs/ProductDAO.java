package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends SingletonBaseDAO {
    public ProductDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    // Get all products
    public List<Product> getAllProducts() {
        open();
        List<Product> products = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE isDelete = 0", null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(0),    // proId
                        cursor.getInt(1),    // catId
                        cursor.getString(2), // proName
                        cursor.getString(3), // proImage
                        cursor.getInt(4),    // proQuantity
                        cursor.getDouble(5), // proPrice
                        cursor.getDouble(6), // discount
                        cursor.getString(7), // description
                        cursor.getString(8), // createDate
                        cursor.getInt(9)     // isDelete
                );
                products.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return products;
    }

    // Get product by ID
    public Product getProductById(int proId) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE proId = ?", new String[]{String.valueOf(proId)});
        if (cursor.moveToFirst()) {
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
            cursor.close();
            close();
            return product;
        }
        cursor.close();
        close();
        return null;
    }

    // Insert new product
    public long insertProduct(Product product) {
        open();
        ContentValues values = new ContentValues();
        values.put("catId", product.getCatId());
        values.put("proName", product.getProName());
        values.put("proImage", product.getProImage());
        values.put("proQuantity", product.getProQuantity());
        values.put("proPrice", product.getProPrice());
        values.put("discount", product.getDiscount());
        values.put("description", product.getDescription());
        values.put("createDate", product.getCreateDate());
        values.put("isDelete", product.getIsDelete());

        long result = db.insert("products", null, values);
        close();
        return result;
    }

    // Update product
    public int updateProduct(Product product) {
        open();
        ContentValues values = new ContentValues();
        values.put("catId", product.getCatId());
        values.put("proName", product.getProName());
        values.put("proImage", product.getProImage());
        values.put("proQuantity", product.getProQuantity());
        values.put("proPrice", product.getProPrice());
        values.put("discount", product.getDiscount());
        values.put("description", product.getDescription());
        values.put("createDate", product.getCreateDate());

        int result = db.update("products", values, "proId = ?", new String[]{String.valueOf(product.getProId())});
        close();
        return result;
    }

    // Delete product (soft delete)
    public int deleteProduct(int proId) {
        open();
        ContentValues values = new ContentValues();
        values.put("isDelete", 1);
        int result = db.update("products", values, "proId = ?", new String[]{String.valueOf(proId)});
        close();
        return result;
    }
}