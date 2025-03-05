package com.example.projectprm392.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ecommerce.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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


        //TẠO BẢNG CATEGORY
        String CREATE_CATEGORY_TABLE = "CREATE TABLE category (" +
                "cat_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cat_name TEXT NOT NULL, " +
                "cat_description TEXT, " +
                "is_deleted INTEGER DEFAULT 0)";
        db.execSQL(CREATE_CATEGORY_TABLE);

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

        // TẠO BẢNG CART
        String CREATE_CART_TABLE = "CREATE TABLE cart (" +
                "cart_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cus_id INTEGER, " +
                "pro_id INTEGER, " +
                "pro_quantity INTEGER, " +
                "cart_price DOUBLE, " +
                "FOREIGN KEY (cus_id) REFERENCES account(acc_id), " +
                "FOREIGN KEY (pro_id) REFERENCES product(pro_id))";
        db.execSQL(CREATE_CART_TABLE);

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

        // Tạo bảng Order Detail
        String CREATE_ORDER_DETAIL_TABLE = "CREATE TABLE order_detail (" +
                "o_id INTEGER, " +
                "pro_id INTEGER, " +
                "quantity INTEGER, " +
                "PRIMARY KEY (o_id, pro_id), " +
                "FOREIGN KEY (o_id) REFERENCES orders(o_id), " +
                "FOREIGN KEY (pro_id) REFERENCES product(pro_id))";
        db.execSQL(CREATE_ORDER_DETAIL_TABLE);
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
        onCreate(db);
    }
}
