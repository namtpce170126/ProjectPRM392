package com.example.projectprm392.ProductControlU;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyHolderView> {

    private Context context;
    private List<Cart> cartList;
    private Map<Integer, String> productImages;

    private TotalPriceChangeListener totalPriceChangeListener;


    private Map<Integer, Boolean> selectedItems = new HashMap<>();

    private Map<Integer, Product> productMap; // Lưu danh sách sản phẩm theo ID

    public MyAdapter(Context context, List<Cart> cartList, Map<Integer, String> productImages, Map<Integer, Product> productMap, TotalPriceChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.productImages = productImages;
        this.productMap = productMap;
        this.totalPriceChangeListener = listener;
    }
    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView holder, int position) {
        Cart cart = cartList.get(position);

        Product product = productMap.get(cart.getProId());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("pro_id", cart.getProId()); // Truyền productId
            context.startActivity(intent);
        });



        holder.nameView.setText(String.valueOf(product.getProName()));
        holder.quantityView.setText(String.valueOf(cart.getProQuantity())); // Ép kiểu về String
        holder.price1View.setText(String.valueOf(cart.getCartPrice()));
        holder.quantityUpDownView.setText(String.valueOf(cart.getProQuantity()));

        double totalPrice = (product != null) ? product.getProPrice() * cart.getProQuantity() : 0;
        holder.priceTotalView.setText(String.format("%.2f", totalPrice));

        //Tắt sự kiện trước khi cập nhật CheckBox
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedItems.getOrDefault(position, false));


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedItems.put(position, isChecked);
            if (totalPriceChangeListener != null) {
                totalPriceChangeListener.onTotalPriceChanged();
            }
        });


//        TAĂNG GIẢM SỐ LƯỢNG TRONG CART
        holder.imageButtonIncrease.setOnClickListener(v -> {
            int newQuantity = cart.getProQuantity() + 1;
            cart.setProQuantity(newQuantity);
            holder.quantityView.setText(String.valueOf(newQuantity));
            holder.priceTotalView.setText(String.format("%.2f", product.getProPrice() * cart.getProQuantity()));
            totalPriceChangeListener.onTotalPriceChanged();
//            notifyItemChanged(position); // Cập nhật lại UI
//            holder.checkBox.setChecked(selectedItems.getOrDefault(position, false));
        });

        holder.imageButtonDecrease.setOnClickListener(v -> {
            if (cart.getProQuantity() > 1) {
                int newQuantity = cart.getProQuantity() - 1;
                cart.setProQuantity(newQuantity);
                holder.quantityView.setText(String.valueOf(newQuantity));
                holder.priceTotalView.setText(String.format("%.2f", product.getProPrice() * cart.getProQuantity()));
                totalPriceChangeListener.onTotalPriceChanged();
//                notifyItemChanged(position);
//                holder.checkBox.setChecked(selectedItems.getOrDefault(position, false));
            }
        });



        // Load ảnh sản phẩm từ map productImages
        String imagePath = productImages.get(cart.getProId()); // Lấy đường dẫn ảnh từ Map

        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.baseline_add_home_24); // Ảnh mặc định
            }
        } else {
            holder.imageView.setImageResource(R.drawable.baseline_add_home_24); // Ảnh mặc định
        }
    }

    public void setTotalPriceChangeListener(TotalPriceChangeListener listener) {
        this.totalPriceChangeListener = listener;
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
    public interface TotalPriceChangeListener {
        void onTotalPriceChanged();
    }

    public double getSelectedTotalPrice() {
        double total = 0;
        for (Map.Entry<Integer, Boolean> entry : selectedItems.entrySet()) {
            if (entry.getValue()) {
                int pos = entry.getKey();
                Cart cart = cartList.get(pos);
                Product product = productMap.get(cart.getProId());
                if (product != null) {
                    total += product.getProPrice() * cart.getProQuantity();
                }
            }
        }
        return total;
    }



}

