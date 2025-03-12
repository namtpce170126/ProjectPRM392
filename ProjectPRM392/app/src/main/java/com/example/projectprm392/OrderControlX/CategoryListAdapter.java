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
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder>{

    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryId);
    }

    public CategoryListAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textViewCategory.setText(category.getCatName());
        // Kiểm tra và hiển thị ảnh sản phẩm
        String imageName = "sample_food.png";
        if (imageName != null && !imageName.isEmpty()) {
            if (imageName.equals("default_food.png")) {
                // Nếu là ảnh mặc định, hiển thị từ drawable
                holder.imageViewCatgory.setImageResource(R.drawable.default_food);
            } else {
                // Nếu là ảnh thật, load từ bộ nhớ trong
                Bitmap productImage = ImageDAO.getImageFromDatabase(holder.itemView.getContext(), imageName);
                if (productImage != null) {
                    holder.imageViewCatgory.setImageBitmap(productImage);
                } else {
                    // Nếu file bị mất hoặc không tồn tại, hiển thị ảnh mặc định
                    holder.imageViewCatgory.setImageResource(R.drawable.default_food);
                }
            }
        } else {
            // Nếu database NULL hoặc trống, hiển thị ảnh mặc định
            holder.imageViewCatgory.setImageResource(R.drawable.default_food);
        }

        // Bắt sự kiện click
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category.getCatId()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategory;
        ImageView imageViewCatgory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategory = itemView.findViewById(R.id.textCategory);
            imageViewCatgory = itemView.findViewById(R.id.imgCategory);
        }
    }
}
