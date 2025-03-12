package com.example.projectprm392.ProfileControl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectprm392.DAOs.FavoriteDAO;
import com.example.projectprm392.DAOs.ProductDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Favorite;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.OrderControl.OrderAdapter;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteAdapter.OnFavoriteRemovedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewFavorites;
    private LinearLayout linearLayoutNoFavorite;
    private ImageView btnBack;
    private FavoriteAdapter favoriteAdapter;
    private List<Product> favoriteProducts;
    private FavoriteDAO favoriteDAO;
    private ProductDAO productDAO;
    private DatabaseHelper dbHelper;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbHelper = new DatabaseHelper(getContext());
        favoriteDAO = new FavoriteDAO(dbHelper);
        productDAO = new ProductDAO(dbHelper);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites);
        linearLayoutNoFavorite = view.findViewById(R.id.linearLayoutNoFavorite);
        btnBack = view.findViewById(R.id.btnBack);

        // Xử lý sự kiện click cho btnBack
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        // Lấy ID người dùng đăng nhập
        int accountId = getLoggedInAccountId();

        // Lấy danh sách sản phẩm yêu thích
        loadFavoriteProducts(accountId);

        // Thiết lập RecyclerView
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        favoriteAdapter = new FavoriteAdapter(getContext(), favoriteProducts, dbHelper, this); // Truyền listener
        recyclerViewFavorites.setAdapter(favoriteAdapter);

        return view;
    }

    private void loadFavoriteProducts(int accountId) {
        List<Favorite> favorites = favoriteDAO.getFavoritesByAccountId(accountId);
        favoriteProducts = new ArrayList<>();
        if (favorites != null && !favorites.isEmpty()) {
            recyclerViewFavorites.setVisibility(View.VISIBLE);
            linearLayoutNoFavorite.setVisibility(View.GONE);
            for (Favorite favorite : favorites) {
                Product product = productDAO.getProductById(favorite.getProId());
                if (product != null) {
                    favoriteProducts.add(product);
                }
            }
        } else {
            Toast.makeText(getContext(), "No favorite foods yet!", Toast.LENGTH_SHORT).show();
            recyclerViewFavorites.setVisibility(View.GONE); // Ẩn RecyclerView
            linearLayoutNoFavorite.setVisibility(View.VISIBLE); // Hiển thị TextView
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // Lấy account_id từ SharedPreferences
    private int getLoggedInAccountId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", 1); // Mặc định là 1 nếu không tìm thấy
    }

    // Khi xóa favorite, cập nhật lại giao diện
    @Override
    public void onFavoriteRemoved() {
        if (favoriteProducts.isEmpty()) {
            Toast.makeText(getContext(), "No favorite foods yet!", Toast.LENGTH_SHORT).show();
        }
    }
}