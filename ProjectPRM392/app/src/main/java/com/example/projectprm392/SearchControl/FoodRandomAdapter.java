package com.example.projectprm392.SearchControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.HomeControl.DiscoutProductViewHolder;
import com.example.projectprm392.HomeControl.Food;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;

public class FoodRandomAdapter extends RecyclerView.Adapter<FoodRandomAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> foodList;

    public FoodRandomAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
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

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView textFoodName, textFoodPrice;
        ImageView imgFood;
        ImageButton btnAddToCart;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textFoodPrice = itemView.findViewById(R.id.textFoodPrice);
            imgFood = itemView.findViewById(R.id.imgFood);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
