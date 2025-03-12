package com.example.projectprm392.OrderControlX;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.OrderDAOX;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.R;

import java.util.List;
import java.util.Locale;

public class AcceptOrderAdapter extends RecyclerView.Adapter<AcceptOrderAdapter.ViewHolder>{

    Button accept, decline;
    private OrderDAOX orderDAOX;


    private List<Order> orderList;
    public AcceptOrderAdapter(Context context, List<Order> orderList){
        this.orderList = orderList;
        // ✅ Khởi tạo DatabaseHelper và OrderDAOX
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.orderDAOX = new OrderDAOX(dbHelper);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_xuan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.textViewOId.setText(order.getOrderId()+"");
        holder.textViewTotalprice.setText(String.format(Locale.getDefault(), "%.2f VNĐ", order.getTotalPrice()));
        holder.textViewStatus.setText(order.getStatus());

        holder.btnAccept.setOnClickListener(view -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                boolean check = orderDAOX.updateOrderStatus(order.getOrderId(), "Preparing");
                if (check) {
                    orderList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });


        holder.btnDecline.setOnClickListener(view -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                boolean check = orderDAOX.updateOrderStatus(order.getOrderId(), "Cancelled");
                if (check) {
                    orderList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOId, textViewTotalprice, textViewStatus;
        Button btnAccept, btnDecline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOId = itemView.findViewById(R.id.textViewOId);
            textViewTotalprice = itemView.findViewById(R.id.textViewPrice);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            btnAccept = itemView.findViewById(R.id.buttonAccept);
            btnDecline = itemView.findViewById(R.id.buttonDecline);
        }
    }
}
