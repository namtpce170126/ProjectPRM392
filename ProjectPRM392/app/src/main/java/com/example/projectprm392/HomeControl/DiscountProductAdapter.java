package com.example.projectprm392.HomeControl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.CartDAOU;
import com.example.projectprm392.DAOs.ImageDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.ProductControlU.ProductActivity;
import com.example.projectprm392.R;

import java.util.ArrayList;

public class DiscountProductAdapter extends RecyclerView.Adapter<DiscoutProductViewHolder> {
    private Context context;
    private ArrayList<Product> foodList;
    private CartDAOU cartDAO;

    public DiscountProductAdapter(Context context, ArrayList<Product> foodList) {
        this.context = context;
        this.foodList = foodList;
        this.cartDAO = new CartDAOU(new DatabaseHelper(context));
    }

    @NonNull
    @Override
    public DiscoutProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new DiscoutProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoutProductViewHolder holder, int position) {
        Product product = foodList.get(position);
        holder.textFoodName.setText(product.getProName());
        holder.textFoodPrice.setText(String.valueOf(product.getProPrice()));
        String imageName = product.getProImage();
        if (imageName != null && !imageName.isEmpty()) {
            if (imageName.equals("default_food.png")) {
                // Nếu là ảnh mặc định, hiển thị từ drawable
                holder.imgFood.setImageResource(R.drawable.default_food);
            } else {
                // Nếu là ảnh thật, load từ bộ nhớ trong
                Bitmap productImage = ImageDAO.getImageFromDatabase(holder.itemView.getContext(), imageName);
                if (productImage != null) {
                    holder.imgFood.setImageBitmap(productImage);
                } else {
                    // Nếu file bị mất hoặc không tồn tại, hiển thị ảnh mặc định
                    holder.imgFood.setImageResource(R.drawable.default_food);
                }
            }
        } else {
            // Nếu database NULL hoặc trống, hiển thị ảnh mặc định
            holder.imgFood.setImageResource(R.drawable.default_food);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("pro_id", product.getProId()); // Gửi productId sang trang chi tiết
            context.startActivity(intent);
        });


        // Xử lý sự kiện khi nhấn nút "Add to Cart"
        holder.btnAddToCart.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {
        int customerId = 1;
        int stockQuantity = product.getProQuantity();

        Cart existingCartItem = cartDAO.getCartItemByProductId(customerId, product.getProId());

        if (existingCartItem != null) {

            int newQuantity = existingCartItem.getProQuantity() + 1;

            if (newQuantity > stockQuantity) {
                Toast.makeText(context, "Không đủ hàng trong kho! Chỉ còn " + stockQuantity + " sản phẩm.", Toast.LENGTH_SHORT).show();
                return;
            }

            existingCartItem.setProQuantity(newQuantity);
            existingCartItem.setCartPrice(newQuantity * product.getProPrice());
            boolean success = cartDAO.updateCart(existingCartItem);

            if (success) {
                Toast.makeText(context, "Cập nhật giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Lỗi khi cập nhật giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (stockQuantity < 1) {
                Toast.makeText(context, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            Cart cartItem = new Cart(
                    0, // cartId tự động tăng
                    customerId,
                    product.getProId(),
                    1, // Thêm 1 sản phẩm vào giỏ hàng
                    product.getProPrice()
            );

            boolean success = cartDAO.addToCart(cartItem);

            if (success) {
                Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Lỗi khi thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
