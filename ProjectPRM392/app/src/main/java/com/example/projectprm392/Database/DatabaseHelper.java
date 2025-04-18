package com.example.projectprm392.Database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;


import com.example.projectprm392.DAOs.ImageDAO;
import com.example.projectprm392.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ecommerce.db";
    private static final int DATABASE_VERSION = 3;
    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TẠO BẢNG ROLE
        String CREATE_ROLE_TABLE = "CREATE TABLE role (" +
                "role_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "role_name TEXT NOT NULL, " +
                "isDelete INTEGER DEFAULT 0)";
        db.execSQL(CREATE_ROLE_TABLE);

        // Chèn dữ liệu mặc định vào bảng role
        db.execSQL("INSERT INTO role (role_name, isDelete) VALUES ('customer', 0)");
        db.execSQL("INSERT INTO role (role_name, isDelete) VALUES ('saler', 0)");

        //TẠO BẢNG ACCOUNT
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE account (" +
                "acc_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "role_id INTEGER, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "fullname TEXT, " +
                "phone_number TEXT, " +
                "email TEXT, " +
                "birthday TEXT, " +
                "address TEXT, " +
                "isDelete INTEGER DEFAULT 0, " +
                "FOREIGN KEY(role_id) REFERENCES role(role_id))";
        db.execSQL(CREATE_ACCOUNT_TABLE);

        db.execSQL("INSERT INTO account (role_id, username, password, fullname, phone_number, email, birthday, address, isDelete) " +
                "VALUES (1, 'customer01', '123456', 'Nguyen Van A', '0123456789', 'customer01@example.com', '2000-01-01', 'Hanoi, Vietnam', 0)");

        db.execSQL("INSERT INTO account (role_id, username, password, fullname, phone_number, email, birthday, address, isDelete) " +
                "VALUES (1, 'customer02', '123456', 'Nguyen Van bb', '0123456789', 'customer01@example.com', '2000-01-01', 'Hanoi, Vietnam', 0)");

        //TẠO BẢNG CATEGORY
        String CREATE_CATEGORY_TABLE = "CREATE TABLE category (" +
                "cat_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cat_name TEXT NOT NULL, " +
                "cat_description TEXT, " +
                "is_deleted INTEGER DEFAULT 0)";
        db.execSQL(CREATE_CATEGORY_TABLE);
        // Chèn danh mục sản phẩm
        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Fast Food', 'Quick meals', 0)");
        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Drinks', 'Beverages and soft drinks', 0)");
        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Desserts', 'Sweet treats', 0)");
        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Pizza', 'Delicious pizzas', 0)");
        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Burgers', 'Juicy burgers', 0)");
        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Asian Food', 'Traditional Asian dishes', 0)");


        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Gà', 'Các món ăn gà', 0)");
        db.execSQL("INSERT INTO category (cat_name, cat_description, is_deleted) VALUES ('Pizza', 'Các món ăn về Pizza', 0)");

        //TẠO BẢNG PRODUCT
        String CREATE_PRODUCT_TABLE = "CREATE TABLE product (" +
                "pro_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cat_id INTEGER, " +
                "pro_name TEXT NOT NULL, " +
                "pro_image TEXT, " +
                "pro_quantity INTEGER, " +
                "pro_price DOUBLE, " +
                "discount DOUBLE, " +
                "description TEXT, " +
                "create_date TEXT, " +
                "isDelete INTEGER DEFAULT 0, " +
                "FOREIGN KEY(cat_id) REFERENCES category(cat_id))";
        db.execSQL(CREATE_PRODUCT_TABLE);

        // Lưu ảnh vào bộ nhớ và lấy tên file
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_food);
        String fileName = ImageDAO.uploadImage(context, bitmap, "sample_food.png");

        if (fileName != null) {
            db.execSQL("INSERT INTO product (cat_id, pro_name, pro_image, pro_quantity, pro_price, discount, description, create_date, isDelete) " +
                    "VALUES (1, 'Fried Chicken Drumstick', '" + fileName + "', 50, 5.99, 0.1, 'Crispy fried chicken', '2025-03-05', 0)");
            db.execSQL("INSERT INTO product (cat_id, pro_name, pro_image, pro_quantity, pro_price, discount, description, create_date, isDelete) " +
                    "VALUES (1, 'Chicken Nuggets', '" + fileName + "', 60, 4.99, 0.05, 'Golden crispy nuggets', '2025-03-05', 0)");
            db.execSQL("INSERT INTO product (cat_id, pro_name, pro_image, pro_quantity, pro_price, discount, description, create_date, isDelete) " +
                    "VALUES (1, 'French Fries', '" + fileName + "', 80, 2.99, 0, 'Crispy golden fries', '2025-03-05', 0)");
            db.execSQL("INSERT INTO product (cat_id, pro_name, pro_image, pro_quantity, pro_price, discount, description, create_date, isDelete) " +
                    "VALUES (1, 'Burger Combo', '" + fileName + "', 40, 7.99, 0.15, 'Burger with fries and drink', '2025-03-05', 0)");
            db.execSQL("INSERT INTO product (cat_id, pro_name, pro_image, pro_quantity, pro_price, discount, description, create_date, isDelete) " +
                    "VALUES (1, 'Hot Wings', '" + fileName + "', 45, 6.49, 0.1, 'Spicy chicken wings', '2025-03-05', 0)");
        }

        // TẠO BẢNG FAVORITES
        String CREATE_FAVORITES_TABLE = "CREATE TABLE favorites (" +
                "favorite_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "acc_id INTEGER, " +
                "pro_id INTEGER, " +
                "isDelete INTEGER DEFAULT 0, " +
                "FOREIGN KEY (acc_id) REFERENCES account(acc_id), " +
                "FOREIGN KEY (pro_id) REFERENCES product(pro_id))";
        db.execSQL(CREATE_FAVORITES_TABLE);
        // Chèn dữ liệu mẫu (tùy chọn)
        db.execSQL("INSERT INTO favorites (acc_id, pro_id, isDelete) VALUES (1, 1, 0)");
        db.execSQL("INSERT INTO favorites (acc_id, pro_id, isDelete) VALUES (1, 2, 0)");
        db.execSQL("INSERT INTO favorites (acc_id, pro_id, isDelete) VALUES (2, 3, 0)");

        // TẠO BẢNG CART
        String CREATE_CART_TABLE = "CREATE TABLE cart (" +
                "cart_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "acc_id INTEGER, " +
                "pro_id INTEGER, " +
                "pro_quantity INTEGER, " +
                "cart_price DOUBLE, " +
                "FOREIGN KEY (acc_id) REFERENCES account(acc_id), " +
                "FOREIGN KEY (pro_id) REFERENCES product(pro_id))";
        db.execSQL(CREATE_CART_TABLE);


        db.execSQL("INSERT INTO cart (acc_id,pro_id,pro_quantity,cart_price)"+
                " VALUES (1,5,5,6.0)");
        db.execSQL("INSERT INTO cart (acc_id,pro_id,pro_quantity,cart_price)"+
                " VALUES (2,5,5,6.0)");



        // Tạo bảng Orders
        String CREATE_ORDERS_TABLE = "CREATE TABLE orders (" +
                "o_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "account_id INTEGER, " +
                "payment TEXT, " +
                "address TEXT, " +
                "status TEXT, " +
                "o_date TEXT, " +
                "total_price DOUBLE, " +
                "isDelete INTEGER DEFAULT 0, " +
                "FOREIGN KEY(account_id) REFERENCES account(acc_id))";
        db.execSQL(CREATE_ORDERS_TABLE);
        // Chèn 10 đơn hàng

        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Ordered', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Ordered', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Preparing', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Preparing', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Delivering', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Delivering', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Delivered', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Delivered', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Cancelled', '2025-03-05', 15.99, 0)");
        db.execSQL("INSERT INTO orders (account_id, payment, address, status, o_date, total_price, isDelete) " +
                "VALUES (1, 'Credit Card', '123 Main St, Hanoi', 'Cancelled', '2025-03-05', 15.99, 0)");

        // Tạo bảng Order Detail
        String CREATE_ORDER_DETAIL_TABLE = "CREATE TABLE order_detail (" +
                "o_id INTEGER, " +
                "pro_id INTEGER, " +
                "quantity INTEGER, " +
                "PRIMARY KEY (o_id, pro_id), " +
                "FOREIGN KEY (o_id) REFERENCES orders(o_id), " +
                "FOREIGN KEY (pro_id) REFERENCES product(pro_id))";
        db.execSQL(CREATE_ORDER_DETAIL_TABLE);
        // Chèn 30 chi tiết đơn hàng (Mỗi đơn hàng có 3 sản phẩm ngẫu nhiên)
        for (int i = 1; i <= 10; i++) {
            db.execSQL("INSERT INTO order_detail (o_id, pro_id, quantity) VALUES (" + i + ", 1, 2)");
            db.execSQL("INSERT INTO order_detail (o_id, pro_id, quantity) VALUES (" + i + ", 2, 1)");
            db.execSQL("INSERT INTO order_detail (o_id, pro_id, quantity) VALUES (" + i + ", 3, 3)");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS role");
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS product");
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS order_detail");
        db.execSQL("DROP TABLE IF EXISTS favorites"); // Thêm bảng favorites vào onUpgrade
        onCreate(db);
    }

    public String saveImageToInternalStorage(Context context, Bitmap bitmap, String imageName) {
        File directory = context.getFilesDir(); // Thư mục nội bộ của app
        File file = new File(directory, imageName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Lưu ảnh dưới dạng PNG
            fos.flush();
            return file.getAbsolutePath(); // Trả về đường dẫn để lưu vào SQLite
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor getProductSales() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.pro_name, SUM(o.quantity) as totalQuantity " +
                "FROM order_detail o " +
                "JOIN product p ON o.pro_id = p.pro_id " +
                "GROUP BY o.pro_id";
        return db.rawQuery(query, null);
    }

    public Cursor getMonthlyOrderCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT strftime('%Y-%m', o_date) AS month, COUNT(o_id) AS totalOrders " +
                "FROM orders " + // Giả sử bạn có bảng Orders
                "WHERE isDelete = 0 " + // Lọc các đơn hàng chưa bị xóa
                "GROUP BY month " +
                "ORDER BY month";
        return db.rawQuery(query, null);
    }

}
