package com.example.projectprm392.ProductControlU;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.CartDAOU;
import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements MyAdapter.TotalPriceChangeListener {
//    private RecyclerView recyclerView;
    private MyAdapter cartAdapter;
    private CartDAOU cartDAO;
    private ProductDAO productDAO;
    private int productId = -1;
    private TextView TongCartCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
    RecyclerView recyclerView = findViewById(R.id.recycleView);

        TongCartCheck = findViewById(R.id.tongTien);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        cartDAO = new CartDAOU(dbHelper);
        productDAO = new ProductDAO(dbHelper);


        ImageButton btnBack = findViewById(R.id.imageButton2);
        btnBack.setOnClickListener(v -> {
            if (productId != -1) {
                Intent intent = new Intent(CartActivity.this, ProductActivity.class);
                intent.putExtra("pro_id", productId);
                startActivity(intent);
                finish(); // Đóng CartActivity
            } else {
                finish(); // Nếu không có productId, chỉ đóng Activity
            }
        });





        Intent intent = getIntent();
        productId = intent.getIntExtra("pro_id", -1);


        int acc_id = 1; // Lấy ID khách hàng từ SharedPreferences hoặc Intent
        List<Cart> cartList = cartDAO.getCartByCustomerId(acc_id);
        Map<Integer, String> productImages = cartDAO.getProductImages(cartList);



        // Lấy danh sách sản phẩm từ database
        List<Product> productList = productDAO.getAll(0);
        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : productList) {
            productMap.put(product.getProId(), product);
        }



        cartAdapter = new MyAdapter(this, cartList, productImages, productMap, this);
        cartAdapter.setTotalPriceChangeListener(() -> updateTotalPrice());
        recyclerView.setAdapter(cartAdapter);

    }

    private void updateTotalPrice() {
        if (TongCartCheck != null) {
            double totalPrice = cartAdapter.getSelectedTotalPrice();
            TongCartCheck.setText(String.format("%.2f đ", totalPrice));
        }
    }
    @Override
    public void onTotalPriceChanged() {
        updateTotalPrice();
    }

//    public void goToProductDetail(View view) {
//        if (productId != -1) {
//            Intent intent = new Intent(this, ProductActivity.class);
//            intent.putExtra("pro_id", productId);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onBackPressed() {
        if (productId != -1) {
            Intent intent = new Intent(this, ProductActivity.class);
            intent.putExtra("pro_id", productId);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }



}