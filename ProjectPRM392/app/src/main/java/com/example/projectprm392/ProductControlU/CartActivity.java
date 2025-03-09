package com.example.projectprm392.ProductControlU;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    private TextView TongCartCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
    RecyclerView recyclerView = findViewById(R.id.recycleView);

        TongCartCheck = findViewById(R.id.tongTien);

//
//        List<Cart> items = new ArrayList<Cart>();
//        items.add(new Cart(R.drawable.voucher,"Banh Cam", 3,5.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Chung", 5,4.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Tieu", 38,2.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Cam", 3,5.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Chung", 5,4.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Tieu", 38,2.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Cam", 3,5.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Chung", 5,4.5));
//        items.add(new Cart(R.drawable.voucher,"Banh Tieu", 38,2.5));


//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new MyAdapter(this,items));
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);

//        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        cartDAO = new CartDAOU(dbHelper);
        productDAO = new ProductDAO(dbHelper);



        int acc_id = 1; // Lấy ID khách hàng từ SharedPreferences hoặc Intent
        List<Cart> cartList = cartDAO.getCartByCustomerId(acc_id);
        Map<Integer, String> productImages = cartDAO.getProductImages(cartList);



        // Lấy danh sách sản phẩm từ database
        List<Product> productList = productDAO.getAll();
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

    public void goToProductDetail(View view) {
        Intent intent = new Intent(this, ProductActivity.class);
        startActivity(intent);
    }
}