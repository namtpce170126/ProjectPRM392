package com.example.projectprm392.HomeControl;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.OrderControlX.ViewProductByCategoryActivity;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private Context context;
    private ArrayList<Category> categoryList;
    private int[] colors = {
            Color.parseColor("#FFFFFFFF"), // White
            Color.parseColor("#EF5222"), // Orange
            Color.parseColor("#FFD504"), // Yellow
            Color.parseColor("#00613D"), // Green
            Color.parseColor("#DAF0FE")  // Light Blue
    };

    public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textCategory.setText(category.getCategoryName());
        holder.imgCategory.setImageResource(category.getCategoryImgId());

        // Gán màu nền dựa trên vị trí để tránh trùng lặp màu khi RecyclerView tái sử dụng ViewHolder
        int colorIndex = position % colors.length;
        holder.cardView.setCardBackgroundColor(colors[colorIndex]);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewProductByCategoryActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
