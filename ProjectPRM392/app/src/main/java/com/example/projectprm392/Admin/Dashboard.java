package com.example.projectprm392.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.projectprm392.ProductControl.ProductList;
import com.example.projectprm392.R;

public class Dashboard extends AppCompatActivity {
    private CardView cardManageProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard);

        cardManageProduct = findViewById(R.id.cardManageProduct);

        cardManageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ProductList.class);
                startActivity(intent);
            }
        });
    }
}
