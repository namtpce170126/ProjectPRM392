package com.example.projectprm392.SearchControl;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.projectprm392.HomeControl.HomeFragment;
import com.example.projectprm392.R;

public class SearchProduct extends Fragment {
    private SearchView searchView;
    private ImageButton btnBackSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);

        searchView = view.findViewById(R.id.searchView);
        btnBackSearchView = view.findViewById(R.id.btnBackSearchView);

        // Load danh sách món ăn ngẫu nhiên khi mở fragment
        loadFragment(new ProductRandomFragment());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchView", "Người dùng tìm kiếm: " + query);

                if (!query.isEmpty() && query != null) {
                    // Thay đổi fragment khi tìm kiếm
                    SearchProductResultFragment searchResultFragment = com.example.projectprm392.SearchControl.SearchProductResultFragment.newInstance(query);
                    loadFragment(searchResultFragment);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //XỬ LÝ BUTTON TRỞ VỀ TRANG FRAGMENT TRUOCD ĐÓ
        btnBackSearchView.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(); // Trở về Fragment trước đó
            } else {
                // Nếu không có Fragment trước, trở về Home hoặc Profile
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new HomeFragment()); // Thay HomeFragment bằng Fragment chính của bạn
                transaction.commit();
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.productSearchView, fragment);
        transaction.commit();
    }
}