package com.example.projectprm392.DAOs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public AccountDAO(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Mở kết nối DB
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    // Đóng kết nối DB
    public void close() {
        dbHelper.close();
    }

    // READ - Lấy danh sách tất cả tài khoản
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE isDelete = 0", null);
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getInt(0), // acc_id
                        cursor.getInt(1), // role_id
                        cursor.getString(2), // username
                        cursor.getString(3), // password
                        cursor.getString(4), // fullname
                        cursor.getString(5), // phone_number
                        cursor.getString(6), // email
                        cursor.getString(7), // birthday
                        cursor.getString(8), // address
                        cursor.getInt(9) // isDelete
                );
                accounts.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return accounts;
    }

    // Lấy tài khoản theo ID
    public Account getAccountById(int accId) {
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE acc_id = ?", new String[]{String.valueOf(accId)});
        if (cursor.moveToFirst()) {
            Account account = new Account(
                    cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getInt(9)
            );
            cursor.close();
            return account;
        }
        cursor.close();
        return null;
    }

    public Account getAccountByUsername(int accId) {
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE username = ?", new String[]{String.valueOf(accId)});
        if (cursor.moveToFirst()) {
            Account account = new Account(
                    cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getInt(9)
            );
            cursor.close();
            return account;
        }
        cursor.close();
        return null;
    }


}
