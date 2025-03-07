package com.example.projectprm392.OrderControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.OrderDetail;
import com.example.projectprm392.Models.Product;
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

        // Lấy thông tin sản phẩm từ ProductDAO
        DatabaseHelper dbHelper = new DatabaseHelper(holder.itemView.getContext());
        ProductDAO productDAO = new ProductDAO(dbHelper);
        Product product = productDAO.getProductById(detail.getProId());

        if (product != null) {
            // Hiển thị thông tin sản phẩm
            holder.txtFoodName.setText(product.getProName());
            holder.txtFoodPrice.setText(String.format("%.2f", product.getProPrice()));
            holder.txtFoodQuantity.setText(String.valueOf(detail.getQuantity()));

            // Hiển thị hình ảnh sản phẩm từ internal storage
            String imagePath = product.getProImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                // Đọc ảnh từ đường dẫn file
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (bitmap != null) {
                    holder.imgFood.setImageBitmap(bitmap);
                } else {
                    holder.imgFood.setImageResource(R.drawable.download); // Hình mặc định nếu không đọc được
                }
            } else {
                holder.imgFood.setImageResource(R.drawable.download); // Hình mặc định nếu imagePath rỗng
            }
        } else {
            // Nếu không tìm thấy sản phẩm
            holder.txtFoodName.setText("Unknown Product");
            holder.txtFoodPrice.setText("0.00");
            holder.txtFoodQuantity.setText(String.valueOf(detail.getQuantity()));
            holder.imgFood.setImageResource(R.drawable.download);
        }
    }

    @Override
    public int getItemCount() {
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