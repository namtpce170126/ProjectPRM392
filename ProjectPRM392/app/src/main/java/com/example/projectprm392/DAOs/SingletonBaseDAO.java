package com.example.projectprm392.DAOs;

import android.database.sqlite.SQLiteDatabase;

import com.example.projectprm392.Database.DatabaseHelper;

public class SingletonBaseDAO {
    protected SQLiteDatabase db;
    private static DatabaseHelper dbHelper;

    public SingletonBaseDAO(DatabaseHelper dbHelper) {
        if (SingletonBaseDAO.dbHelper == null) {
            SingletonBaseDAO.dbHelper = dbHelper;
        }
    }

    // Mở kết nối DB
    public void open() {
        if (db == null || !db.isOpen()) {
            db = dbHelper.getWritableDatabase();
        }
    }

    // Đóng kết nối DB
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
