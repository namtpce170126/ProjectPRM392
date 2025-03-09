package com.example.projectprm392.ProductControl;

import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class AddingProduct extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnBackListProduct, imgProduct;
    private EditText addProname, addPrice, addQuantity, addDescription;
    private Spinner spinnerAddCategory;
    private Button btnResetForm, btnAddProduct;
    private CategoryDAO categoryDAO;
    private ProductDAO productDAO;
    private Uri imageUri;
    private int selectedCategoryId;
    private List<Category> categoryList;
    private String product_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding_product);

        btnBackListProduct = findViewById(R.id.btnBackListProduct);
        imgProduct = findViewById(R.id.imgProduct);
        addProname = findViewById(R.id.addProname);
        addPrice = findViewById(R.id.addPrice);
        addQuantity = findViewById(R.id.addQuantity);
        addDescription = findViewById(R.id.addDescription);
        spinnerAddCategory = findViewById(R.id.spnAddCategory);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnResetForm = findViewById(R.id.btnResetForm);

        categoryDAO = new CategoryDAO(new DatabaseHelper(this));
        productDAO = new ProductDAO(new DatabaseHelper(this));
        categoryList = categoryDAO.getAll();

        // Load danh sách category vào Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddCategory.setAdapter(adapter);

        //Sự kiện khi chọn mới 1 category thì lấy được categoryId thay vì categoryName
        spinnerAddCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                Intent intent1 = new Intent(AddingProduct.this, ProductList.class);
                startActivity(intent1);
            }
        });

        // Sự kiện khi nhấn vào ImageView để chọn ảnh mới
        imgProduct.setOnClickListener(view -> openFileChooser());

        // Đảm bảo ảnh mặc định được lưu vào bộ nhớ (chỉ tạo nếu chưa có)
        ImageDAO.uploadDefaultImage(this);

        // Sự kiện nhấn nút Add Product
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu từ EditText
                String productName = addProname.getText().toString().trim();
                int categoryId = selectedCategoryId;
                String productDescription = addDescription.getText().toString().trim();
                double productPrice = 0;
                int productQuantity = 0;

                try {
                    productPrice = Double.parseDouble(addPrice.getText().toString().trim());
                    productQuantity = Integer.parseInt(addQuantity.getText().toString().trim());
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

                String savedImageName;
                if (imageUri != null) {
                    // Nếu chọn ảnh mới, lưu ảnh mới
                    String fileName = "product_" + System.currentTimeMillis() + ".png";
                    savedImageName = ImageDAO.uploadImage(view.getContext(), bitmap, fileName);
                } else {
                    savedImageName = "default_food.png"; // Thay bằng ảnh mặc định của bạn
                }

                // Lấy thời gian hiện tại
                String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                // Tạo đối tượng sản phẩm mới
                Product newProduct = new Product(0, categoryId, productName, savedImageName,
                        productQuantity, productPrice, 0, productDescription, currentDate, 0);

                // Chèn sản phẩm vào database
                boolean isInserted = productDAO.insertProduct(newProduct);
                if (isInserted) {
                    Toast.makeText(view.getContext(), "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    Intent intentAdd = new Intent(AddingProduct.this, ProductList.class);
                    startActivity(intentAdd);
                } else {
                    Toast.makeText(view.getContext(), "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Su kien nhan nut Ret
        btnResetForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đặt lại các EditText về rỗng
                addProname.setText("");
                addPrice.setText("");
                addQuantity.setText("");
                addDescription.setText("");

                // Đặt lại danh mục về vị trí đầu tiên
                spinnerAddCategory.setSelection(0);

                // Đặt lại ảnh về ảnh mặc định
                imgProduct.setImageResource(R.drawable.default_food);

                // Xóa đường dẫn ảnh đã chọn trước đó
                imageUri = null;

                // Hiển thị thông báo
                Toast.makeText(view.getContext(), "Form is reset!", Toast.LENGTH_SHORT).show();
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