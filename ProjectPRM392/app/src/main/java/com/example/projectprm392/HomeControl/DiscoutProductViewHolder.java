package com.example.projectprm392.HomeControl;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;

public class DiscoutProductViewHolder extends RecyclerView.ViewHolder {
    TextView textFoodName, textFoodPrice;
    ImageView imgFood, btnAddToFavorite;
    ImageButton btnAddToCart;

    public DiscoutProductViewHolder(@NonNull View itemView) {
        super(itemView);
        textFoodName = itemView.findViewById(R.id.textFoodName);
        textFoodPrice = itemView.findViewById(R.id.textFoodPrice);
        imgFood = itemView.findViewById(R.id.imgFood);
        btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        btnAddToFavorite = itemView.findViewById(R.id.btnAddToFavorite);
    }
}
