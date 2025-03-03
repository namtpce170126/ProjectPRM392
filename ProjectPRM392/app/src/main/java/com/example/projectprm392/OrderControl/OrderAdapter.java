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

import java.util.ArrayList;
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

        holder.txtOrderStatus.setText("Status: " + order.getOrderStatus());
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

    // Phương thức cập nhật danh sách và thông báo thay đổi
    public void updateList(List<Order> newList) {
        this.orderList = new ArrayList<>(newList);
        notifyDataSetChanged(); // Thông báo RecyclerView cập nhật giao diện
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderStatus, txtOrderDate, txtTotalPrice;
        RecyclerView recyclerViewDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            recyclerViewDetails = itemView.findViewById(R.id.recyclerViewDetails);
        }

    }
}
