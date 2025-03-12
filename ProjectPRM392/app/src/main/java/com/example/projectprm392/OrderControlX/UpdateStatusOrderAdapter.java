package com.example.projectprm392.OrderControlX;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.OrderDAOX;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.R;

import java.util.List;
import java.util.Locale;

public class UpdateStatusOrderAdapter extends RecyclerView.Adapter<UpdateStatusOrderAdapter.ViewHolder>{

    private OrderDAOX orderDAOX;
    private final Context context;
    private final String[] statusOptions = {"Preparing", "Delivering","Completed", "Cancelled" };
    private List<Order> orderList;
    public  UpdateStatusOrderAdapter(Context context, List<Order> orderList){
        this.orderList = orderList;
        this.context = context;
// ✅ Khởi tạo DatabaseHelper và OrderDAOX
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.orderDAOX = new OrderDAOX(dbHelper);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_update_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.textViewOId.setText(order.getOrderId()+"");
        holder.textViewTotalprice.setText(String.format(Locale.getDefault(), "%.2f VNĐ", order.getTotalPrice()));
        holder.textViewStatus.setText(order.getStatus());

        // Set up spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, R.id.textViewSpinnerItem, statusOptions);
        adapter.setDropDownViewResource(R.layout.spinner_item); // Optional: Custom dropdown style
        holder.spinnerOrderStatus.setAdapter(adapter);

        // Set spinner to current status of the order
        int statusPosition = getStatusPosition(order.getStatus());
        if (statusPosition >= 0) {
            holder.spinnerOrderStatus.setSelection(statusPosition);
        }

        // Handle spinner item selection
        holder.spinnerOrderStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedStatus = statusOptions[pos];

                // Chỉ update nếu status thay đổi
                if (!order.getStatus().equals(selectedStatus)) {
                    // Update status in database

                    boolean isUpdated = orderDAOX.updateOrderStatus(order.getOrderId(), selectedStatus);

                    if (isUpdated) {
                        order.setStatus(selectedStatus); // Cập nhật trong list
                        holder.textViewStatus.setText(selectedStatus); // Hiển thị status mới
                    } else {
                        Toast.makeText(context, "Failed to update status!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không chọn gì
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOId, textViewTotalprice, textViewStatus;
        Spinner spinnerOrderStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOId = itemView.findViewById(R.id.textVOId);
            textViewTotalprice = itemView.findViewById(R.id.textVPrice);
            textViewStatus = itemView.findViewById(R.id.textVStatus);
            spinnerOrderStatus = itemView.findViewById(R.id.spinnerOrderStatus);
        }
    }
    // Helper method to get status position
    private int getStatusPosition(String status) {
        for (int i = 0; i < statusOptions.length; i++) {
            if (statusOptions[i].equalsIgnoreCase(status)) {
                return i;
            }
        }
        return -1; // Status không tồn tại trong danh sách
    }
}
