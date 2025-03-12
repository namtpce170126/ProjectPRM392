package com.example.projectprm392.ProfileControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.CategoryDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<Product> favoriteProducts;
    private CategoryDAO categoryDAO;
    private Map<Integer, Category> categoryCache = new HashMap<>(); // Khai báo và khởi tạo cache

    public FavoriteAdapter(List<Product> favoriteProducts, DatabaseHelper dbHelper) {
        this.favoriteProducts = favoriteProducts;
        this.categoryDAO = new CategoryDAO(dbHelper); // Khởi tạo CategoryDAO
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Product product = favoriteProducts.get(position);
        holder.txtProductName.setText(product.getProName());
        holder.txtProductPrice.setText(String.format("%.2f VND", product.getProPrice()));

        // Kiểm tra cache trước khi truy vấn
        Category category = categoryCache.get(product.getCatId());
        if (category == null) {
            category = categoryDAO.getOne(product.getCatId(), 0); // 0 là is_deleted = 0
            if (category != null) {
                categoryCache.put(product.getCatId(), category);
            }
        }
        holder.txtProductCategory.setText(category != null ? category.getCatName() : "Unknown Category");
    }

    @Override
    public int getItemCount() {
        return favoriteProducts.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtProductCategory;
        ImageView imgProduct;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductCategory = itemView.findViewById(R.id.txtProductCategory);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}