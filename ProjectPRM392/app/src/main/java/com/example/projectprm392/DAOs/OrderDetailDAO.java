package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO extends SingletonBaseDAO {
    public OrderDetailDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    // Get all order details by order ID
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        open();
        List<OrderDetail> orderDetails = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM order_detail WHERE o_id = ?", new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            do {
                OrderDetail detail = new OrderDetail(
                        cursor.getInt(0), // orderId
                        cursor.getInt(1), // proId
                        cursor.getInt(2)  // quantity
                );
                orderDetails.add(detail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return orderDetails;
    }

    // Insert new order detail
    public long insertOrderDetail(OrderDetail orderDetail) {
        open();
        ContentValues values = new ContentValues();
        values.put("o_id", orderDetail.getOrderId());
        values.put("pro_id", orderDetail.getProId());
        values.put("quantity", orderDetail.getQuantity());

        long result = db.insert("order_detail", null, values);
        close();
        return result;
    }

    // Update order detail
    public int updateOrderDetail(OrderDetail orderDetail) {
        open();
        ContentValues values = new ContentValues();
        values.put("quantity", orderDetail.getQuantity());

        int result = db.update("order_detail", values,
                "o_id = ? AND o_id = ?",
                new String[]{String.valueOf(orderDetail.getOrderId()), String.valueOf(orderDetail.getProId())});
        close();
        return result;
    }

    // Delete order detail
    public int deleteOrderDetail(int orderId, int proId) {
        open();
        int result = db.delete("order_detail",
                "o_id = ? AND pro_id = ?",
                new String[]{String.valueOf(orderId), String.valueOf(proId)});
        close();
        return result;
    }
}