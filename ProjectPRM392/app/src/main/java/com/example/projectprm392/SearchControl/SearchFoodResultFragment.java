package com.example.projectprm392.SearchControl;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectprm392.HomeControl.Food;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;


public class SearchFoodResultFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodRandomAdapter foodAdapter;
    private List<Food> allFoods;
    private String query;

    public SearchFoodResultFragment() {
        // Required empty public constructor
    }

    public static SearchFoodResultFragment newInstance(String query) {
        SearchFoodResultFragment fragment = new SearchFoodResultFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString("query");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_food_result_frragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        int spanCount = calculateSpanCount();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        allFoods = getAllFoods();  // Lấy danh sách toàn bộ món ăn
        List<Food> searchResults = filterFoods(query);

        foodAdapter = new FoodRandomAdapter(this.getContext(), searchResults);
        recyclerView.setAdapter(foodAdapter);
        return view;
    }
    private List<Food> filterFoods(String query) {
        List<Food> filteredList = new ArrayList<>();
        for (Food food : allFoods) {
            if (food.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(food);
            }
        }
        return filteredList;
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