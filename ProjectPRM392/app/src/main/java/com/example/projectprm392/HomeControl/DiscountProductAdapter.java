package com.example.projectprm392.HomeControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;

import java.util.ArrayList;

public class DiscountProductAdapter extends RecyclerView.Adapter<DiscoutProductViewHolder> {
    private Context context;
    private ArrayList<Food> foodList;

    public DiscountProductAdapter(Context context, ArrayList<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public DiscoutProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new DiscoutProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoutProductViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.textFoodName.setText(food.getName());
        holder.textFoodPrice.setText(food.getPrice());
        holder.imgFood.setImageResource(food.getImageResId());

        // Xử lý sự kiện khi nhấn nút "Add to Cart"
        holder.btnAddToCart.setOnClickListener(v ->
                Toast.makeText(context, food.getName() + " đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
