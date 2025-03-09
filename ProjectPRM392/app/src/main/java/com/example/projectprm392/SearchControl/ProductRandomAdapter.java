package com.example.projectprm392.SearchControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.ImageDAO;
import com.example.projectprm392.HomeControl.Food;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.List;
import java.util.Locale;

public class ProductRandomAdapter extends RecyclerView.Adapter<ProductRandomAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductRandomAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textFoodName.setText(product.getProName());
        holder.textFoodPrice.setText(String.format(Locale.getDefault(), "%.2f VNĐ", product.getProPrice()));

        // Kiểm tra và hiển thị ảnh sản phẩm
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

        // Xử lý sự kiện khi nhấn nút "Add to Cart"
        holder.btnAddToCart.setOnClickListener(v ->
                Toast.makeText(context, product.getProName() + " đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textFoodName, textFoodPrice;
        ImageView imgFood;
        ImageButton btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textFoodPrice = itemView.findViewById(R.id.textFoodPrice);
            imgFood = itemView.findViewById(R.id.imgFood);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
