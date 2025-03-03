package com.example.projectprm392.OrderControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.Order;
import com.example.projectprm392.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.txtOrderId.setText("Order ID: #" + order.getOrderId());
        holder.txtOrderDate.setText("Date: " + order.getOrderDate());
        holder.txtTotalPrice.setText(String.valueOf(order.getTotalOrderPrice()));

        // Thiết lập RecyclerView cho OrderDetails
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(order.getOrderDetails());
        holder.recyclerViewDetails.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerViewDetails.setAdapter(detailAdapter);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderDate, txtTotalPrice;
        RecyclerView recyclerViewDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            recyclerViewDetails = itemView.findViewById(R.id.recyclerViewDetails);
        }
    }
}
