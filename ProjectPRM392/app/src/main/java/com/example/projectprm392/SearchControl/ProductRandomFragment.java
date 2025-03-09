package com.example.projectprm392.SearchControl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.List;


public class ProductRandomFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductRandomAdapter productAdapter;
    private List<Product> productList;
    private ProductDAO productDAO;

    public ProductRandomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_random, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        productDAO = new ProductDAO(new DatabaseHelper(this.getContext()));

        int spanCount = calculateSpanCount();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        productList = productDAO.getRandomProduct(5);
        productAdapter = new ProductRandomAdapter(this.getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        return view;
    }

    private int calculateSpanCount() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        int minItemWidth = 180; // Chiều rộng tối thiểu mong muốn của mỗi item (dp)

        return Math.max(1, (int) (screenWidth / minItemWidth));
    }
}