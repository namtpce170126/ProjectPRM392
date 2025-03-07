package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends SingletonBaseDAO {
    public OrderDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        open();
        List<Order> orders = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE isDelete = 0", null);
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(0),    // orderId
                        cursor.getInt(1),    // accountId
                        cursor.getString(2), // payment
                        cursor.getString(3), // address
                        cursor.getString(4), // status
                        cursor.getString(5), // orderDate
                        cursor.getDouble(6), // totalPrice
                        cursor.getInt(7)     // isDelete
                );
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return orders;
    }

    // Get order by ID
    public Order getOrderById(int orderId) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE o_id = ?", new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            Order order = new Order(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getDouble(6),
                    cursor.getInt(7)
            );
            cursor.close();
            close();
            return order;
        }
        cursor.close();
        close();
        return null;
    }

    // Insert new order
    public long insertOrder(Order order) {
        open();
        ContentValues values = new ContentValues();
        values.put("account_id", order.getAccountId());
        values.put("payment", order.getPayment());
        values.put("address", order.getAddress());
        values.put("status", order.getStatus());
        values.put("o_date", order.getOrderDate());
        values.put("total_price", order.getTotalPrice());
        values.put("isDelete", order.getIsDelete());

        long result = db.insert("orders", null, values);
        close();
        return result;
    }

    // Update order
    public int updateOrder(Order order) {
        open();
        ContentValues values = new ContentValues();
        values.put("account_id", order.getAccountId());
        values.put("payment", order.getPayment());
        values.put("address", order.getAddress());
        values.put("status", order.getStatus());
        values.put("o_date", order.getOrderDate());
        values.put("total_price", order.getTotalPrice());

        int result = db.update("orders", values, "o_id = ?", new String[]{String.valueOf(order.getOrderId())});
        close();
        return result;
    }

    // Delete order (soft delete)
    public int deleteOrder(int orderId) {
        open();
        ContentValues values = new ContentValues();
        values.put("isDelete", 1);
        int result = db.update("orders", values, "o_id = ?", new String[]{String.valueOf(orderId)});
        close();
        return result;
    }
}