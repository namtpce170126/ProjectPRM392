package com.example.projectprm392.OrderControlX;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.CategoryDAO;
import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.HomeControl.MainActivity;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.ProductControlU.CartActivity;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;

public class ViewProductByCategoryActivity extends AppCompatActivity {
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private List<Category> listCat;
    private List<Product> listPro;
    RecyclerView recyclerViewCategory, recyclerViewProduct;
    private CategoryListAdapter categoryListAdapter;
    private ProductListAdapter productListAdapter;
    private ImageButton btnBack,btn_cart_category;
    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_product_by_category);





//        btn_cart_category = findViewById(R.id.btn_cart_category);
        recyclerViewCategory = findViewById(R.id.recyclerCategory);
        recyclerViewProduct = findViewById(R.id.recyclerProduct);
        btnBack = findViewById(R.id.imageButtonBackMain);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProductByCategoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Đảm bảo không quay lại Activity này nữa
            }
        });

        // Khởi tạo database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        categoryDAO = new CategoryDAO(dbHelper);
        productDAO = new ProductDAO(dbHelper);


        //get list cat
        listCat = categoryDAO.getAll();
        listPro = productDAO.getAll(0);
        // Cấu hình RecyclerView cho danh mục
        categoryListAdapter = new CategoryListAdapter(listCat, categoryId -> loadProductsByCategory(categoryId));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setAdapter(categoryListAdapter);

        // Cấu hình RecyclerView cho sản phẩm
        productListAdapter = new ProductListAdapter(listPro);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProduct.setAdapter(productListAdapter);




    }

    public void goToCartCategory(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    private void loadProductsByCategory(int categoryId) {
        List<Product> newProducts = new ArrayList<>();


        newProducts = productDAO.getListProByCatId(categoryId);
        // Cập nhật danh sách sản phẩm
        productListAdapter.updateProducts(newProducts);
    }
}