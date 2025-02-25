package com.example.projectprm392;

import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.HomeControl.Category;
import com.example.projectprm392.HomeControl.CategoryAdapter;
import com.example.projectprm392.HomeControl.DiscountProductAdapter;
import com.example.projectprm392.HomeControl.Food;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView recyclerBestSeller;
    private DiscountProductAdapter foodAdapter;
    private ArrayList<Food> foodList;

    private RecyclerView recyclerCategory;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        //RECYCLERVIEW FOR DISCOUNT
        recyclerView = findViewById(R.id.recyclerDiscounted);

        int spanCount = calculateSpanCount(); // Hoặc tính toán tự động dựa vào màn hình
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        foodList = new ArrayList<>();
        foodList.add(new Food("Pizza", "120.000đ", R.drawable.sample_food));
        foodList.add(new Food("Burger", "75.000đ", R.drawable.sample_food));
        foodList.add(new Food("Gà rán", "95.000đ", R.drawable.sample_food));
        foodList.add(new Food("Mì Ý", "85.000đ", R.drawable.sample_food));
        foodList.add(new Food("Khoai tây chiên", "45.000đ", R.drawable.sample_food));

        foodAdapter = new DiscountProductAdapter(this, foodList);
        recyclerView.setAdapter(foodAdapter);


        //RECYCLER VIEW FOR BEST SELLER
        recyclerBestSeller = findViewById(R.id.recyclerBestSeller);
        recyclerBestSeller.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerBestSeller.setAdapter(new DiscountProductAdapter(this, foodList));



        //RECYCLER VIEW CUA CATEGORY
        recyclerCategory = findViewById(R.id.recyclerCategories);

        categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Fast Food", R.drawable.sample_food));
        categoryList.add(new Category(2, "Drinks", R.drawable.sample_food));
        categoryList.add(new Category(3, "Desserts", R.drawable.sample_food));
        categoryList.add(new Category(4, "Pizza", R.drawable.sample_food));
        categoryList.add(new Category(5, "Burgers", R.drawable.sample_food));
        categoryList.add(new Category(6, "Asian Food", R.drawable.sample_food));

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerCategory.setLayoutManager(layoutManager1);
        categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerCategory.setAdapter(categoryAdapter);

    }

    private int calculateSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        int minItemWidth = 180; // Chiều rộng tối thiểu mong muốn của mỗi item (dp)

        return Math.max(1, (int) (screenWidth / minItemWidth));
    }
}