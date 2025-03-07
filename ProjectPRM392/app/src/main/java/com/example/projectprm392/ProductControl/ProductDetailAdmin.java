package com.example.projectprm392.ProductControl;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.DAOs.CategoryDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailAdmin extends AppCompatActivity {
    ImageButton btnBackListProduct;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imgProduct;
    private EditText edtProname, edtPrice, edtQuantity, edtDescription;
    private Spinner spinnerCategory;
    private Button btnUpdateProduct;
    private Uri imageUri;
    private CategoryDAO categoryDAO;
    private List<Category> categoryList;
    private int selectedCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        btnBackListProduct = findViewById(R.id.btnBackListProduct);
        imgProduct = findViewById(R.id.imgProduct);
        edtProname = findViewById(R.id.edtProname);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerCategory = findViewById(R.id.spnCategory);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);

        categoryDAO = new CategoryDAO(new DatabaseHelper(this));
        categoryList = categoryDAO.getAll();

        // Load danh sách category vào Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnBackListProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProductDetailAdmin.this, ProductList.class);
                startActivity(intent1);
            }
        });

        // Sự kiện khi nhấn vào ImageView để chọn ảnh mới
        imgProduct.setOnClickListener(view -> openFileChooser());

        // LOAD DATA FROM INTENT
        loadProductData();
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgProduct.setImageURI(imageUri);
        }
    }

    private void loadProductData() {
        Intent intent = getIntent();
        if(intent != null) {
            edtProname.setText(intent.getStringExtra("product_name"));
            edtPrice.setText(String.valueOf(intent.getIntExtra("product_price", 0)));
            edtQuantity.setText(String.valueOf(intent.getIntExtra("product_quantity", 0)));
            edtDescription.setText(intent.getStringExtra("product_description"));

            // Load ảnh sản phẩm
            String imagePath = intent.getStringExtra("product_image");
            if (imagePath != null && !imagePath.isEmpty()) {
                imgProduct.setImageBitmap(BitmapFactory.decodeFile(imagePath));
            } else {
                imgProduct.setImageResource(R.drawable.sample_food);
            }

            // Lấy catId của sản phẩm
            selectedCategoryId = intent.getIntExtra("cat_id", -1);

            // Thiết lập Spinner chọn đúng danh mục của sản phẩm
            int index = getCategoryIndexById(selectedCategoryId);
            if (index >= 0) {
                spinnerCategory.setSelection(index);
            }
        }
    }

    private List<String> getCategoryNames() {
        List<String> names = new ArrayList<>();
        for (Category category : categoryList) {
            names.add(category.getCatName());
        }
        return names;
    }

    private int getCategoryIndexById(int catId) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getCatId() == catId) {
                return i;
            }
        }
        return -1;
    }


}
