package com.example.projectprm392.ProductControl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.Admin.Dashboard;
import com.example.projectprm392.DAOs.CategoryDAO;
import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity {
    private RecyclerView listProductAdmin;
    private ImageButton btnBackDashboard;
    private List<Product> listProduct;

    private AdProductAdapter adapter;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);

        btnBackDashboard = findViewById(R.id.btnBackDashboard);
        listProductAdmin = findViewById(R.id.listProductAdmin);
        productDAO = new ProductDAO(new DatabaseHelper(this));

        //TRỞ VỀ DASHBOARD
        btnBackDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProductList.this, Dashboard.class);
                startActivity(intent1);
            }
        });

        //XỬ LÝ HIỂN THỊ DANH SÁCH SẢN PHẨM
        loadProductList();
    }

    private void loadProductList() {
        listProduct = new ArrayList<>(productDAO.getAll());

        if(listProduct.isEmpty()) {
            Toast.makeText(this, "There is no product", Toast.LENGTH_SHORT).show();
        }

        adapter = new AdProductAdapter(this, listProduct, new DatabaseHelper(this));
        listProductAdmin.setLayoutManager(new LinearLayoutManager(this));
        listProductAdmin.setAdapter(adapter);
    }
}