package com.example.projectprm392.OrderControl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.OrderDetailDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.Models.OrderDetail;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private OnCancelOrderListener cancelOrderListener; // Thêm listener

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    // Interface để xử lý sự kiện hủy đơn hàng
    public interface OnCancelOrderListener {
        void onCancelOrderClicked(int orderId);
    }

    // Thiết lập listener
    public void setOnCancelOrderListener(OnCancelOrderListener listener) {
        this.cancelOrderListener = listener;
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

        // Hiển thị thông tin đơn hàng
        holder.txtOrderStatus.setText("Status: " + order.getStatus());
        holder.txtOrderDate.setText("Date: " + order.getOrderDate());
        holder.txtTotalPrice.setText(String.format("%.2f", order.getTotalPrice()));

        // Hiển thị hoặc ẩn nút CancelOrder dựa trên trạng thái
        if ("Ordered".equals(order.getStatus())) {
            holder.btnCancelOrder.setVisibility(View.VISIBLE);
            holder.btnCancelOrder.setOnClickListener(v -> {
                if (cancelOrderListener != null) {
                    cancelOrderListener.onCancelOrderClicked(order.getOrderId());
                }
            });
        } else {
            holder.btnCancelOrder.setVisibility(View.GONE);
        }

        // Lấy danh sách chi tiết đơn hàng từ OrderDetailDAO
        DatabaseHelper dbHelper = new DatabaseHelper(holder.itemView.getContext());
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO(dbHelper);
        List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId());

        // Thiết lập RecyclerView cho chi tiết đơn hàng
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(orderDetails);
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
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderStatus, txtOrderDate, txtTotalPrice;
        RecyclerView recyclerViewDetails;
        Button btnCancelOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            recyclerViewDetails = itemView.findViewById(R.id.recyclerViewDetails);
            btnCancelOrder = itemView.findViewById(R.id.btnCancelOrder);
        }
    }
}