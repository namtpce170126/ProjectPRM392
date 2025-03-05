package com.example.projectprm392.ProductControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.ArrayList;

public class AdProductAdapter extends RecyclerView.Adapter<AdProductAdapter.AdProductViewHolder> {
    private Context context;
    private ArrayList<Product> listProduct;

    public AdProductAdapter(Context context, ArrayList<Product> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public AdProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_product_list, parent, false);
        return new AdProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdProductViewHolder holder, int position) {
        Product product = listProduct.get(position);
        holder.itemName.setText(product.getProName());
        holder.itemCatgory.setText(product.getCatId());
        holder.itemPrice.setText((int) product.getProPrice());
    }

    @Override
    public int getItemCount() {
        return 0;
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
