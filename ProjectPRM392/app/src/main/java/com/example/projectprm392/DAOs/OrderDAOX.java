package com.example.projectprm392.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.Models.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDAOX extends SingletonBaseDAO{
    public OrderDAOX(DatabaseHelper dbHelper) {
        super(dbHelper);
    }
    ///xuan code order
    public List<Cart> getCartsByAccountId(int accountId) {
        open();
        List<Cart> cartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM cart WHERE acc_id = ?", new String[]{String.valueOf(accountId)});

        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart(
                        cursor.getInt(0), // cartId
                        cursor.getInt(1), // cusId
                        cursor.getInt(2), // proId
                        cursor.getInt(3), // proQuantity
                        cursor.getDouble(4) // cartPrice
                );
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return cartList;
    }

    public Product getProductById(int proId) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM product WHERE pro_id = ?", new String[]{String.valueOf(proId)});

        if (cursor.moveToFirst()) {
            Product product = new Product(cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(2), cursor.getString(3), cursor.getInt(4),
                    cursor.getDouble(5),cursor.getDouble(6), cursor.getString(7),
                    cursor.getString(8), cursor.getInt(9));
            cursor.close();
            close();
            return product;
        }
        cursor.close();
        close();
        return null;
    }
    //get list product by product id
    public List<Product> getProductsByAccountId(int accountId) {
        List<Cart> cartList = getCartsByAccountId(accountId);
        List<Product> productList = new ArrayList<>();

        for (Cart cart : cartList) {
            Product product = getProductById(cart.getProId());
            if (product != null) {
                productList.add(product);
            }
        }
        return productList;
    }
    public void updateCartQuantity(int customerId, int productId, int change) {
        open();

        // 🔹 Lấy số lượng tồn kho và giá sản phẩm từ bảng product
        Cursor productCursor = db.rawQuery("SELECT pro_quantity, pro_price FROM product WHERE pro_id = ?",
                new String[]{String.valueOf(productId)});

        int availableQuantity = 0;
        double productPrice = 0.0;

        if (productCursor.moveToFirst()) {
            availableQuantity = productCursor.getInt(productCursor.getColumnIndexOrThrow("pro_quantity"));
            productPrice = productCursor.getDouble(productCursor.getColumnIndexOrThrow("pro_price"));
        }
        productCursor.close();

        // 🔹 Lấy số lượng sản phẩm hiện có trong giỏ hàng
        Cursor cartCursor = db.rawQuery("SELECT pro_quantity FROM cart WHERE acc_id = ? AND pro_id = ?",
                new String[]{String.valueOf(customerId), String.valueOf(productId)});

        if (cartCursor.moveToFirst()) {
            int currentQuantity = cartCursor.getInt(cartCursor.getColumnIndexOrThrow("pro_quantity"));
            int newQuantity = currentQuantity + change;
            cartCursor.close();

            if (newQuantity >= 1 && newQuantity <= availableQuantity) {
                // 🔹 Cập nhật số lượng và giá tiền trong giỏ hàng
                ContentValues values = new ContentValues();
                values.put("pro_quantity", newQuantity);
                values.put("cart_price", newQuantity * productPrice); // Cập nhật giá

                db.update("cart", values, "acc_id = ? AND pro_id = ?",
                        new String[]{String.valueOf(customerId), String.valueOf(productId)});
            }
        } else if (change > 0 && change <= availableQuantity) {
            // 🔹 Nếu sản phẩm chưa có trong giỏ hàng, thêm vào với giá đúng
            ContentValues values = new ContentValues();
            values.put("acc_id", customerId);
            values.put("pro_id", productId);
            values.put("pro_quantity", change);
            values.put("cart_price", change * productPrice); // Cập nhật giá

            db.insert("cart", null, values);
        }

        close();
    }

    public double getTotalCartPrice(int customerId) {
        open(); // Mở database

        double total = 0.0;
        Cursor cursor = db.rawQuery(
                "SELECT SUM(cart_price) FROM cart WHERE acc_id = ?",
                new String[]{String.valueOf(customerId)}
        );

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        close(); // Đóng database

        return total;
    }

    // insert order
    public long createOrder(Order order) {
        open();
        ContentValues values = new ContentValues();

        values.put("account_id", order.getAccountId());
        values.put("payment", order.getPayment());
        values.put("address", order.getAddress());
        values.put("status", order.getStatus());
        values.put("o_date", order.getOrderDate());
        values.put("total_price", order.getTotalPrice());
        values.put("isDelete", order.getIsDelete());

        // Chèn dữ liệu và lấy ID của đơn hàng mới
        long orderId = db.insert("orders", null, values);
        close();

        return orderId; // Trả về ID của đơn hàng mới
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

    // Get all orders by status
    public List<Order> getAllOrdersByStatus(String status) {
        open();
        List<Order> orders = new ArrayList<>();

        // Sử dụng dấu ? để tránh lỗi SQL injection
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE isDelete = 0 AND status = ?", new String[]{status});

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
    // Update order status by orderId
    public boolean updateOrderStatus(int orderId, String newStatus) {
        open();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);

        // Cập nhật theo orderId và chỉ với những bản ghi chưa bị xóa (isDelete = 0)
        int rowsAffected = db.update("orders", values, "o_id = ? AND isDelete = 0", new String[]{String.valueOf(orderId)});

        close();
        return rowsAffected > 0;
    }

    // Get all orders excluding those with status 'Ordered'
    public List<Order> getAllOrdersExcludingOrdered() {
        open();
        List<Order> orders = new ArrayList<>();

        // Lấy tất cả đơn hàng trừ những đơn có status là 'Ordered'
        Cursor cursor = db.rawQuery(
                "SELECT * FROM orders WHERE isDelete = 0 AND status != ?",
                new String[]{"Ordered"}
        );

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

}
