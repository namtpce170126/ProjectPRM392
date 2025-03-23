package com.example.projectprm392.HomeControl;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectprm392.ProductControlU.CartActivity;

import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private Toolbar toolbar;
    private ImageButton btnCart,btn_cart,btnAddToCart;
    private RecyclerView recyclerView;
    private RecyclerView recyclerBestSeller;
    private DiscountProductAdapter foodAdapter;
    private ArrayList<Product> foodList;
    private ProductDAO productDAO;

    private RecyclerView recyclerCategory;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);





        // Ánh xạ Toolbar
        toolbar = view.findViewById(R.id.toolbar);
        btnCart = view.findViewById(R.id.btn_cart);


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCartHome(v);
            }
        });



        // Thiết lập Toolbar làm ActionBar của Fragment
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("FFood");
        }

        // RECYCLERVIEW FOR DISCOUNT
        recyclerView = view.findViewById(R.id.recyclerDiscounted);

        int spanCount = calculateSpanCount();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        productDAO = new ProductDAO(new DatabaseHelper(getContext()));
        foodList = new ArrayList<>(productDAO.getAll(0));

        foodAdapter = new DiscountProductAdapter(getContext(), foodList);
        recyclerView.setAdapter(foodAdapter);



//        foodList = new ArrayList<>();
//        foodList.add(new Pro("Pizza", "120.000đ", R.drawable.sample_food));
//        foodList.add(new Food("Burger", "75.000đ", R.drawable.sample_food));
//        foodList.add(new Food("Gà rán", "95.000đ", R.drawable.sample_food));
//        foodList.add(new Food("Mì Ý", "85.000đ", R.drawable.sample_food));
//        foodList.add(new Food("Khoai tây chiên", "45.000đ", R.drawable.sample_food));
//
//        foodAdapter = new DiscountProductAdapter(getContext(), foodList);
//        recyclerView.setAdapter(foodAdapter);
//
//        // RECYCLERVIEW FOR BEST SELLER
//        recyclerBestSeller = view.findViewById(R.id.recyclerBestSeller);
//        recyclerBestSeller.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
//        recyclerBestSeller.setAdapter(new DiscountProductAdapter(getContext(), foodList));

        // RECYCLER VIEW FOR CATEGORY
        recyclerCategory = view.findViewById(R.id.recyclerCategories);

        categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Fast Food", R.drawable.sample_food));
        categoryList.add(new Category(2, "Drinks", R.drawable.sample_food));
        categoryList.add(new Category(3, "Desserts", R.drawable.sample_food));
        categoryList.add(new Category(4, "Pizza", R.drawable.sample_food));
        categoryList.add(new Category(5, "Burgers", R.drawable.sample_food));
        categoryList.add(new Category(6, "Asian Food", R.drawable.sample_food));



        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerCategory.setLayoutManager(layoutManager1);
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        recyclerCategory.setAdapter(categoryAdapter);




        return view;
    }

    private int calculateSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        int minItemWidth = 180; // Chiều rộng tối thiểu mong muốn của mỗi item (dp)

        return Math.max(1, (int) (screenWidth / minItemWidth));
    }


    public void goToCartHome(View view) {
        if (isAdded() && getContext() != null) {
            Intent intent = new Intent(requireContext(), CartActivity.class);
            startActivity(intent);
        }
    }







}