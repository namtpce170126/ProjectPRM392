package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends SingletonBaseDAO {
    public AccountDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    // Lấy danh sách tất cả tài khoản
    public List<Account> getAllAccounts() {
        open();
        List<Account> accounts = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE isDelete = 0", null);
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getInt(9)
                );
                accounts.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return accounts;
    }

    // Lấy tài khoản theo ID
    public Account getAccountById(int accId) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE acc_id = ?", new String[]{String.valueOf(accId)});
        if (cursor.moveToFirst()) {
            Account account = new Account(
                    cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getInt(9)
            );
            cursor.close();
            close();
            return account;
        }
        cursor.close();
        close();
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

    // Tạo tài khoản
    public Account insertAccount(Account account) {
        open();
        ContentValues values = new ContentValues();
        values.put("role_id", account.getRoleId());
        values.put("username", account.getUsername());
        values.put("password", account.getPassword());
        values.put("fullname", account.getFullName());
        values.put("phone_number", account.getPhoneNumber());
        values.put("email", account.getEmail());
        values.put("birthday", account.getBirthday());
        values.put("address", account.getAddress());
        values.put("isDelete", account.getIsDelete());

        long id = db.insert("account", null, values);
        close();

        if (id == -1) {
            return null; // Thêm thất bại
        }

        // Cập nhật ID vào đối tượng và trả về
        account.setAccId((int) id);
        return account;
    }
}
