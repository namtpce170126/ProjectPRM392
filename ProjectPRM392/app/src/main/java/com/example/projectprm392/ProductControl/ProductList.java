package com.example.projectprm392.ProductControl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.Admin.Dashboard;
import com.example.projectprm392.R;

public class ProductList extends AppCompatActivity {

    private ImageButton btnBackDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);

        btnBackDashboard = findViewById(R.id.btnBackDashboard);

        btnBackDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProductList.this, Dashboard.class);
                startActivity(intent1);
            }
        });
    }
}