package com.example.projectprm392.ProductControl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.DAOs.CategoryDAO;
import com.example.projectprm392.DAOs.ImageDAO;
import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductDetailAdmin extends AppCompatActivity {
    ImageButton btnBackListProduct;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imgProduct;
    private EditText edtProname, edtPrice, edtQuantity, edtDescription;
    private Spinner spinnerCategory;
    private Button btnUpdateProduct;
    private Uri imageUri;
    private CategoryDAO categoryDAO;
    private ProductDAO productDAO;
    private List<Category> categoryList;
    private int selectedCategoryId;
    private int productId;
    private String product_image;

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
        productDAO = new ProductDAO(new DatabaseHelper(this));
        categoryList = categoryDAO.getAll();

        // Load danh sách category vào Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        //Sự kiện khi chọn mới 1 category thì lấy được categoryId thay vì categoryName
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = categoryList.get(position).getCatId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Sự kiện trở về list product
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

        //Su kien nhan nut Update
        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu từ EditText
                String productName = edtProname.getText().toString().trim();
                int categoryId = selectedCategoryId;
                String productDescription = edtDescription.getText().toString().trim();
                double productPrice = 0;
                int productQuantity = 0;

                try {
                    productPrice = Double.parseDouble(edtPrice.getText().toString().trim());
                    productQuantity = Integer.parseInt(edtQuantity.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Toast.makeText(view.getContext(), "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra dữ liệu hợp lệ
                if (productName.isEmpty() || productDescription.isEmpty()) {
                    Toast.makeText(view.getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy ảnh từ ImageButton
                imgProduct.setDrawingCacheEnabled(true);
                imgProduct.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgProduct.getDrawable()).getBitmap();

                String savedImagePath;
                if (imageUri != null) {
                    // Nếu chọn ảnh mới, lưu ảnh mới
                    String fileName = "product_" + System.currentTimeMillis() + ".png";
                    savedImagePath = ImageDAO.uploadImage(view.getContext(), bitmap, fileName);
                } else {
                    // Nếu không chọn ảnh mới, giữ ảnh cũ
                    savedImagePath = product_image;
                }

                // Cập nhật sản phẩm vào database
                if(productId <= 0) {
                    Toast.makeText(view.getContext(), "Product ID not existing", Toast.LENGTH_SHORT).show();
                    return;
                }
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                Product updatedProduct = new Product(productId, categoryId, productName, savedImagePath,
                        productQuantity, productPrice, 0, productDescription, currentDate, 0);
                boolean isUpdated = productDAO.updateProduct(updatedProduct);
                if (isUpdated) {
                    Toast.makeText(view.getContext(), "Cập nhật sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            productId = intent.getIntExtra("product_id", -1);
            product_image = intent.getStringExtra("product_image");
            edtProname.setText(intent.getStringExtra("product_name"));
            edtPrice.setText(String.valueOf(intent.getDoubleExtra("product_price", 0)));
            edtQuantity.setText(String.valueOf(intent.getIntExtra("product_quantity", 0)));
            edtDescription.setText(intent.getStringExtra("product_description"));

            // Load ảnh sản phẩm
            String imageFileName = intent.getStringExtra("product_image");

            if (imageFileName != null && !imageFileName.isEmpty()) {
                Bitmap bitmap = ImageDAO.getImageFromDatabase(this, imageFileName);
                if (bitmap != null) {
                    imgProduct.setImageBitmap(bitmap);
                } else {
                    imgProduct.setImageResource(R.drawable.sample_food); // Ảnh mặc định nếu không tìm thấy file
                }
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
