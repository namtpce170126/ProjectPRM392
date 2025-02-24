package com.example.projectprm392;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
        RecyclerView recyclerView = findViewById(R.id.recycleView);

        List<Cart> items = new ArrayList<Cart>();
        items.add(new Cart(R.drawable.voucher,"Banh Cam", 3,5.5));
        items.add(new Cart(R.drawable.voucher,"Banh Chung", 5,4.5));
        items.add(new Cart(R.drawable.voucher,"Banh Tieu", 38,2.5));
        items.add(new Cart(R.drawable.voucher,"Banh Cam", 3,5.5));
        items.add(new Cart(R.drawable.voucher,"Banh Chung", 5,4.5));
        items.add(new Cart(R.drawable.voucher,"Banh Tieu", 38,2.5));
        items.add(new Cart(R.drawable.voucher,"Banh Cam", 3,5.5));
        items.add(new Cart(R.drawable.voucher,"Banh Chung", 5,4.5));
        items.add(new Cart(R.drawable.voucher,"Banh Tieu", 38,2.5));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(this,items));


    }
}