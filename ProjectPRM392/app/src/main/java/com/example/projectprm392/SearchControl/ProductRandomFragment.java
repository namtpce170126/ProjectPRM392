package com.example.projectprm392.SearchControl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectprm392.HomeControl.Food;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProductRandomFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodRandomAdapter foodAdapter;
    private List<Food> foodList;

    public ProductRandomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_random, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);

        int spanCount = calculateSpanCount();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        foodList = getRandomFoods();
        foodAdapter = new FoodRandomAdapter(this.getContext(), foodList);
        recyclerView.setAdapter(foodAdapter);

        return view;
    }

    private List<Food> getRandomFoods() {
        List<Food> allFoods = getAllFoods();
        Collections.shuffle(allFoods);
        return allFoods.subList(0, Math.min(5, allFoods.size())); // Chọn 5 món ngẫu nhiên
    }

    private List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();
        foods.add(new Food("Pizza", "120.000đ", R.drawable.sample_food));
        foods.add(new Food("Burger", "75.000đ", R.drawable.sample_food));
        foods.add(new Food("Gà rán", "95.000đ", R.drawable.sample_food));
        foods.add(new Food("Mì Ý", "85.000đ", R.drawable.sample_food));
        foods.add(new Food("Khoai tây chiên", "45.000đ", R.drawable.sample_food));
        return foods;
    }

    private int calculateSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        int minItemWidth = 180; // Chiều rộng tối thiểu mong muốn của mỗi item (dp)

        return Math.max(1, (int) (screenWidth / minItemWidth));
    }
}