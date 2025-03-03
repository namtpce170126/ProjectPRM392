package com.example.projectprm392.OrderControl;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;

public class OrderViewHolder extends RecyclerView.ViewHolder{
    TextView txtFoodName, txtFoodPrice, txtQuantity, txtTotalPrice;
    RecyclerView recyclerViewDetails;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
//        txtOrderId = itemView.findViewById(R.id.txtOrderId);
        recyclerViewDetails = itemView.findViewById(R.id.OrderHistoryRecycleView);
        txtFoodName = itemView.findViewById(R.id.txtFoodName);
        txtFoodPrice = itemView.findViewById(R.id.txtFoodPrice);
        txtQuantity = itemView.findViewById(R.id.txtFoodQuantity);
        txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
    }
}
