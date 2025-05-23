package com.example.projectprm392.ProductControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.CategoryDAO;
import com.example.projectprm392.DAOs.ImageDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdProductAdapter extends RecyclerView.Adapter<AdProductAdapter.AdProductViewHolder> {
    private Context context;
    private List<Product> listProduct;
    private CategoryDAO categoryDAO;
    private OnItemClickListener listener;

    public AdProductAdapter(Context context, List<Product> listProduct, DatabaseHelper db, OnItemClickListener listener) {
        this.context = context;
        this.listProduct = listProduct;
        this.categoryDAO = new CategoryDAO(db);
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    @NonNull
    @Override
    public AdProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_product_item, parent, false);
        return new AdProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdProductViewHolder holder, int position) {
        Product product = listProduct.get(position);

        //LẤY CATEGORY NAME
        Category category = categoryDAO.getOne(product.getCatId(), 0);
        String categoryName = (category != null) ? category.getCatName() : "Không xác định";

        holder.itemName.setText(product.getProName());
        holder.itemCatgory.setText(categoryName);
        holder.itemPrice.setText(String.format(Locale.getDefault(), "%.2f VNĐ", product.getProPrice()));

        // Kiểm tra và hiển thị ảnh sản phẩm
        String imageName = product.getProImage();
        if (imageName != null && !imageName.isEmpty()) {
            if (imageName.equals("default_food.png")) {
                // Nếu là ảnh mặc định, hiển thị từ drawable
                holder.itemImage.setImageResource(R.drawable.default_food);
            } else {
                // Nếu là ảnh thật, load từ bộ nhớ trong
                Bitmap productImage = ImageDAO.getImageFromDatabase(holder.itemView.getContext(), imageName);
                if (productImage != null) {
                    holder.itemImage.setImageBitmap(productImage);
                } else {
                    // Nếu file bị mất hoặc không tồn tại, hiển thị ảnh mặc định
                    holder.itemImage.setImageResource(R.drawable.default_food);
                }
            }
        } else {
            // Nếu database NULL hoặc trống, hiển thị ảnh mặc định
            holder.itemImage.setImageResource(R.drawable.default_food);
        }

        //XỬ LÝ SỰ KIỆN CLICK VÀO ITEM
        holder.itemView.setOnClickListener(v -> {
            if(listener != null) {
                listener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public static class AdProductViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemCatgory, itemPrice;

        public AdProductViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemImage = itemView.findViewById(R.id.itemImage);
            this.itemName = itemView.findViewById(R.id.itemName);
            this.itemCatgory = itemView.findViewById(R.id.itemCategory);
            this.itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
