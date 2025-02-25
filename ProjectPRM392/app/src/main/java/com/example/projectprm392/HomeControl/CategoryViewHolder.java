package com.example.projectprm392.HomeControl;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView textCategory;
    ImageView imgCategory;
    CardView cardView;


    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        textCategory = itemView.findViewById(R.id.textCategory);
        imgCategory = itemView.findViewById(R.id.imgCategory);
        cardView = itemView.findViewById(R.id.cardViewCategory);
    }
}
