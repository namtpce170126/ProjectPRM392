package com.example.projectprm392.OrderControlX;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.DAOs.CartDAO;
import com.example.projectprm392.DAOs.OrderDAOX;
import com.example.projectprm392.DAOs.OrderDetailDAO;
import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.Models.OrderDetail;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private static TextView txtAddress, txtPriceTamTinh, txtToTalPrice;
    ImageButton btnEditAddress, btnDecreaseQuantity, btnIncreaseQuantity;
    Button btnThanhToan;
    private RecyclerView recyclerView;
    private OrderMyAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private List<Cart> cartList = new ArrayList<>();
    private static OrderDAOX orderDAOX;
    private static AccountDAO accountDAO;
    private static OrderDetailDAO orderDetailDAO;
    private static CartDAO cartDAO;
    private  static ProductDAO productDAO;
    private static int accountId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order);

// Khởi tạo database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        orderDAOX = new OrderDAOX(dbHelper);
        accountDAO = new AccountDAO(dbHelper);
        orderDetailDAO = new OrderDetailDAO(dbHelper);
        cartDAO = new CartDAO(dbHelper);
        productDAO = new ProductDAO(dbHelper);
        // get Id của account
        accountId = getCurrentUserId();
        //find id text view
        txtAddress = findViewById(R.id.txtAddress);
        btnEditAddress = findViewById(R.id.buttonEditAddress);
        txtPriceTamTinh = findViewById(R.id.txtTamTinh);
        txtToTalPrice = findViewById(R.id.txtTotalPrice);
        btnThanhToan = findViewById(R.id.buttonThanhToan);
        updateTotalPrice(); // Gọi lần đầu tiên để hiển thị tổng tiền đúng

        // Ánh xạ RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách sản phẩm cần thanh toán


        Account account = accountDAO.getAccountById(accountId);
        txtAddress.setText(account.getAddress());
        txtPriceTamTinh.setText(orderDAOX.getTotalCartPrice(accountId)+"");
        txtToTalPrice.setText(txtPriceTamTinh.getText().toString());
        btnEditAddress.setOnClickListener(v -> showEditAddressDialog());
        accountDAO.updateAddress(1, txtAddress.getText().toString());
//        if (account != null) {
//
//        } else {
//            Toast.makeText(this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
//        }


        productList = orderDAOX.getProductsByAccountId(accountId);
        cartList = orderDAOX.getCartsByAccountId(accountId);

        // Kiểm tra danh sách sản phẩm trước khi hiển thị
        if (!productList.isEmpty()) {
            adapter = new OrderMyAdapter(this, productList, cartList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không có sản phẩm nào trong giỏ hàng!", Toast.LENGTH_SHORT).show();
        }


        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                Order order = new Order(0,accountId, "pay later", "cho xac nhan", txtAddress.getText().toString().trim(),"7-3-2025",orderDAOX.getTotalCartPrice(accountId) ,0);
                long o_id = orderDAOX.createOrder(order);
                List<Cart> listCartOrder = orderDAOX.getCartsByAccountId(accountId);

                for (Cart cart :listCartOrder) {
                    orderDetailDAO.insertOrderDetail(new OrderDetail((int)o_id,cart.getProId(),cart.getProQuantity()));
                    productDAO.updateProductQuantity(cart.getProId(), -cart.getProQuantity());
                    cartDAO.deleteCartById(cart.getCartId());
                }


                //thanh toán thành công thì đi đâu
                Toast.makeText(OrderActivity.this, "order thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderActivity.this, AfterOrderActivity.class);
               startActivity(intent);
            }
        });

    }

    // Giả lập lấy ID người dùng hiện tại (thực tế có thể lấy từ SharedPreferences hoặc Intent)
    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }


    private void showEditAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_address_order, null);
        builder.setView(dialogView);

        EditText editAddress = dialogView.findViewById(R.id.editAddress);
        Button btnSaveEdit = dialogView.findViewById(R.id.btnSaveEdit);

        // Hiển thị địa chỉ hiện tại trong EditText
        editAddress.setText(txtAddress.getText().toString());

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSaveEdit.setOnClickListener(v -> {
            String newAddress = editAddress.getText().toString().trim();
            if (!newAddress.isEmpty()) {
                boolean success = accountDAO.updateAddress(getCurrentUserId(), newAddress);
                if (success) {
                    txtAddress.setText(newAddress);
                    Toast.makeText(OrderActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } else {
                Toast.makeText(OrderActivity.this, "Vui lòng nhập địa chỉ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Hàm tĩnh để cập nhật tổng tiền
    public static void updateTotalPrice() {
        if (txtPriceTamTinh != null && txtToTalPrice != null && orderDAOX != null) {
            double totalPrice = orderDAOX.getTotalCartPrice(accountId);
            txtPriceTamTinh.setText(String.format("$ %.2f", totalPrice));
            txtToTalPrice.setText(String.format("$ %.2f", totalPrice));
        }
    }
}