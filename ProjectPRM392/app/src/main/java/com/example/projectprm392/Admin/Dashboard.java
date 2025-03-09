package com.example.projectprm392.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectprm392.ProductControl.ProductList;
import com.example.projectprm392.R;

public class Dashboard extends AppCompatActivity {
    private TextView listOrderbtn;
    private CardView cardManageProduct;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard);

        cardManageProduct = findViewById(R.id.cardManageProduct);
        listOrderbtn = findViewById(R.id.xemThem);

        listOrderbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, AdminActivity.class);
            intent.putExtra("open_order_list_admin", true); // Gửi dữ liệu để MainActivity biết cần mở ClientProfileFragment
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        cardManageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ProductList.class);
                startActivity(intent);
            }
        });
    }
}
