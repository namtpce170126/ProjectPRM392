package com.example.projectprm392.ProductControlU;

import static android.widget.Toast.makeText;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.DAOs.CartDAOU;
import com.example.projectprm392.DAOs.ProductDetailDAOU;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

public class ProductActivity extends AppCompatActivity {
    private ProductDetailDAOU productDAO;
    private TextView pricePro, namePro, desPro, quantityText;
    private ImageView imgPro,imgPro2,imgPro3,imgPro4;
    int quantity = 1;
    private ImageButton shareButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_customer);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        productDAO = new ProductDetailDAOU(databaseHelper);

        pricePro = findViewById(R.id.textView2);
        namePro = findViewById(R.id.textView8);
        desPro = findViewById(R.id.textView13);
        imgPro = findViewById(R.id.imageView2);
        imgPro2 = findViewById(R.id.imageView4);
        imgPro3 = findViewById(R.id.imageView11);
        imgPro4 = findViewById(R.id.imageView12);
        quantityText = findViewById(R.id.textView12);

        findViewById(R.id.button3).setOnClickListener(view -> addToCart());


        Product getProductDetail = productDAO.getProductDetailsByProductId(5);
        pricePro.setText(getProductDetail.getProPrice()+"");
        namePro.setText(getProductDetail.getProName());
        desPro.setText(getProductDetail.getDescription());
        Bitmap bitmap = BitmapFactory.decodeFile(getProductDetail.getProImage());
         imgPro.setImageBitmap(bitmap);
        imgPro2.setImageBitmap(bitmap);
        imgPro3.setImageBitmap(bitmap);
        imgPro4.setImageBitmap(bitmap);

        quantityText.setText(String.valueOf(quantity));

        // Xử lý sự kiện nút cộng
        findViewById(R.id.imageButton9).setOnClickListener(view -> increaseQuantity());

        // Xử lý sự kiện nút trừ
        findViewById(R.id.imageButton8).setOnClickListener(view -> decreaseQuantity());




    }

    private void addToCart() {
        int customerId = 1;

        // Lấy thông tin sản phẩm từ DAO
        Product product = productDAO.getProductDetailsByProductId(5);

        // Tạo đối tượng Cart
        Cart cartItem = new Cart(
                0, //cartId tự động tăng
                customerId,
                product.getProId(),
                quantity,
                product.getProPrice() * quantity
        );

        // Lưu vào database (hoặc danh sách giỏ hàng tạm thời)
        CartDAOU cartDAO = new CartDAOU(new DatabaseHelper(this));
        boolean success = cartDAO.addToCart(cartItem);

        if (success) {
            makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            makeText(this, "Lỗi khi thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        }
    }

    //  tăng số lượng
    private void increaseQuantity() {
        quantity++;
        quantityText.setText(String.valueOf(quantity));
    }

    //  giảm số lượng (không cho xuống dưới 1)
    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            quantityText.setText(String.valueOf(quantity));
        }
    }



    public void goToCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }







//    private void shareToAnyApp(String name, String url) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, "🔥 " + name + " - Xem ngay tại: " + url);
//
//        // Mở trình chọn ứng dụng
//        startActivity(Intent.createChooser(intent, "Chia sẻ sản phẩm"));
//    }

}
