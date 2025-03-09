package com.example.projectprm392.SearchControl;

import android.os.Bundle;

import androidx.annotation.Nullable;
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


public class SearchProductResultFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductRandomAdapter productAdapter;
    private String query;
    private ProductDAO productDAO;

    public SearchProductResultFragment() {
        // Required empty public constructor
    }

    public static SearchProductResultFragment newInstance(String query) {
        SearchProductResultFragment fragment = new SearchProductResultFragment();
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
        productDAO = new ProductDAO(new DatabaseHelper(this.getContext()));

        int spanCount = calculateSpanCount();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        List<Product> searchResults = productDAO.getProductsByKeyword(query);

        productAdapter = new ProductRandomAdapter(this.getContext(), searchResults);
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