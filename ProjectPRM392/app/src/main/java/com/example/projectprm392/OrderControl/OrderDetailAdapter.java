package com.example.projectprm392.OrderControl;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.OrderDetail;
import com.example.projectprm392.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private List<OrderDetail> orderDetails;

    public OrderDetailAdapter(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        holder.txtFoodName.setText(detail.getFoodName());
        holder.txtFoodPrice.setText(String.valueOf(detail.getPrice()));
        holder.txtFoodQuantity.setText(String.valueOf(detail.getQuantity()));
        Log.d("OrderDetailDebug", "Binding detail at position " + position + ": " + detail.getFoodName());
    }

    @Override
    public int getItemCount() {
        Log.d("OrderDetailDebug", "Total OrderDetails: " + orderDetails.size());
        return orderDetails.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView txtFoodName, txtFoodPrice, txtFoodQuantity;
        ImageView imgFood;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
            txtFoodPrice = itemView.findViewById(R.id.txtFoodPrice);
            txtFoodQuantity = itemView.findViewById(R.id.txtFoodQuantity);
            imgFood = itemView.findViewById(R.id.imgFood);
        }
    }
}
