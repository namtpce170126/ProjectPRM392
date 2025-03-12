package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends SingletonBaseDAO{
    public ProductDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    public List<Product> getAll(int isDelete) {
        open();
        List<Product> listProduct = new ArrayList<>();
        String query;
        Cursor cursor;

        if(isDelete == 0 || isDelete == 1) {
            query = "SELECT * FROM product WHERE isDelete = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(isDelete)});
        } else {
            query = "SELECT * FROM product";
            cursor = db.rawQuery(query, null);
        }

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

    public boolean insertProduct(Product product) {
        open();
        ContentValues values = new ContentValues();
        values.put("pro_name", product.getProName());
        values.put("cat_id", product.getCatId());
        values.put("pro_price", product.getProPrice());
        values.put("pro_quantity", product.getProQuantity());
        values.put("description", product.getDescription());
        values.put("pro_image", product.getProImage());
        values.put("discount", product.getDiscount());
        values.put("create_date", product.getCreateDate());
        values.put("isDelete", 0);
        long rowsAffected = db.insert("product", null, values);
        close();
        return rowsAffected != -1; // Trả về true nếu chèn thành công
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
        values.put("isDelete", 0);
        int rowsAffected = db.update("product", values, "pro_id = ?", new String[]{String.valueOf(product.getProId())});
        close();
        return rowsAffected > 0;
    }

    public boolean deleteProduct(int productId) {
        open();
        int rowsAffected = db.delete("product", "pro_id = ?", new String[]{String.valueOf(productId)});
        close();
        return rowsAffected > 0;
    }

    public List<Product> getRandomProduct(int numberProduct) {
        open();
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY RANDOM() LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(numberProduct)});

        if (cursor.moveToFirst()) {
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
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return productList;
    }

    public List<Product> getProductsByKeyword(String productName) {
        open();
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM product WHERE pro_name LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + productName + "%"});

        if (cursor.moveToFirst()) {
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
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return productList;
    }

    //xuan update quanity
    public void updateProductQuantity(int productId, int quantityChange) {
        open();

        // Lấy số lượng hiện tại của sản phẩm
        Cursor cursor = db.rawQuery("SELECT pro_quantity FROM product WHERE pro_id = ?", new String[]{String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(0);
            int newQuantity = currentQuantity + quantityChange;

            // Đảm bảo số lượng không âm
            if (newQuantity < 0) {
                newQuantity = 0;
            }

            // Cập nhật số lượng mới
            ContentValues values = new ContentValues();
            values.put("pro_quantity", newQuantity);

            int rowsAffected = db.update("product", values, "pro_id = ?", new String[]{String.valueOf(productId)});
            if (rowsAffected > 0) {
                Log.d("DB_SUCCESS", "Cập nhật số lượng thành công! Số lượng mới: " + newQuantity);
            } else {
                Log.e("DB_ERROR", "Cập nhật số lượng thất bại!");
            }
        }
        cursor.close();
        close();
    }
    //get pro by cat_id
    public List<Product> getListProByCatId(int catId) {
        open(); // Đảm bảo database mở trước khi thao tác
        List<Product> listProduct = new ArrayList<>();

        String query = "SELECT * FROM product WHERE cat_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(catId)}); // Truyền tham số an toàn

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product(
                            cursor.getInt(0),  // ID sản phẩm
                            cursor.getInt(1),  // Cat ID
                            cursor.getString(2), // Tên sản phẩm
                            cursor.getString(3), // Mô tả
                            cursor.getInt(4),  // Số lượng
                            cursor.getDouble(5), // Giá
                            cursor.getDouble(6), // Giảm giá
                            cursor.getString(7), // Ảnh
                            cursor.getString(8), // Ngày tạo
                            cursor.getInt(9)  // Trạng thái
                    );
                    listProduct.add(product);
                } while (cursor.moveToNext());
            }
            cursor.close(); // Đóng Cursor sau khi sử dụng
        }

        close(); // Đóng database sau khi xử lý xong
        return listProduct;
    }

}