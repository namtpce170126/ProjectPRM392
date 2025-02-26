package com.example.projectprm392;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapterViewHolder extends RecyclerView.ViewHolder{
    TextView txtOrderId;
    RecyclerView recyclerViewDetails;
    public OrderAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderId = itemView.findViewById(R.id.txtOrderId);
        recyclerViewDetails = itemView.findViewById(R.id.recycleViewOrder);
    }
}
