package com.example.projectprm392.OrderControlX;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.ImageDAO;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.List;
import java.util.Locale;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder>{


    private List<Product> productList;

    public ProductListAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewProduct.setText(product.getProName());
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f VNĐ", product.getProPrice()));
        // Kiểm tra và hiển thị ảnh sản phẩm
        String imageName = product.getProImage();
        if (imageName != null && !imageName.isEmpty()) {
            if (imageName.equals("default_food.png")) {
                // Nếu là ảnh mặc định, hiển thị từ drawable
                holder.imgPro.setImageResource(R.drawable.default_food);
            } else {
                // Nếu là ảnh thật, load từ bộ nhớ trong
                Bitmap productImage = ImageDAO.getImageFromDatabase(holder.itemView.getContext(), imageName);
                if (productImage != null) {
                    holder.imgPro.setImageBitmap(productImage);
                } else {
                    // Nếu file bị mất hoặc không tồn tại, hiển thị ảnh mặc định
                    holder.imgPro.setImageResource(R.drawable.default_food);
                }
            }
        } else {
            // Nếu database NULL hoặc trống, hiển thị ảnh mặc định
            holder.imgPro.setImageResource(R.drawable.default_food);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateProducts(List<Product> newProducts) {
        productList.clear();
        productList.addAll(newProducts);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProduct, textPrice;
        ImageView imgPro;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProduct = itemView.findViewById(R.id.textFoodName);
            textPrice = itemView.findViewById(R.id.textFoodPrice);
            imgPro = itemView.findViewById(R.id.imgFood);
        }
    }
}
