package com.example.projectprm392.OrderControlX;


import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;

public class OrderMyViewHolder extends RecyclerView.ViewHolder{
    ImageView imageProduct;
    TextView txtProductName, txtPrice, txtQuantity;
    ImageButton btnDelete, btnAdd;
    public OrderMyViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.productName);
        txtPrice = itemView.findViewById(R.id.price);
        txtQuantity = itemView.findViewById(R.id.quantity);
        imageProduct = itemView.findViewById(R.id.imageViewProduct);
        btnDelete = itemView.findViewById(R.id.imageBtnDelete);
        btnAdd= itemView.findViewById(R.id.imageBtnAdd);
    }
}