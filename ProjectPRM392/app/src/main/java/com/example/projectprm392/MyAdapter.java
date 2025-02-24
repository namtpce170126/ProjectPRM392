package com.example.projectprm392;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyHolderView> {

    Context context;
    List<Cart> items;

    public MyAdapter(Context context, List<Cart> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolderView(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView holder, int position) {
        holder.nameView.setText(items.get(position).getNameProductInCart());
        holder.name2View.setText(items.get(position).getNameProductInCart());

        holder.priceView.setText(String.valueOf(items.get(position).getPriceInCart()));
        holder.price2View.setText(String.valueOf(items.get(position).getPriceInCart()));

        holder.quantityView.setText(String.valueOf(items.get(position).getQuantityInCart()));
        holder.quantity2View.setText(String.valueOf(items.get(position).getQuantityInCart()));



        holder.imageView.setImageResource(items.get(position).getImageProductInCart());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
