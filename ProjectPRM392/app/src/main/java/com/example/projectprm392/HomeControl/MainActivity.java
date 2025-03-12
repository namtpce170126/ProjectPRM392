package com.example.projectprm392.HomeControl;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.ProfileControl.ClientProfileFragment;
import com.example.projectprm392.ProfileControl.ProfileFragment;
import com.example.projectprm392.R;
import com.example.projectprm392.SearchControl.SearchProduct;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        // Load fragment Home mặc định
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        boolean openProfileClient = getIntent().getBooleanExtra("open_client_profile", false);
        if (savedInstanceState == null && openProfileClient) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ClientProfileFragment())
                    .commit();
        }

        // Sử dụng setOnItemSelectedListener() thay cho setOnNavigationItemSelectedListener()
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_search) {
                selectedFragment = new SearchProduct();
            } else if (item.getItemId() == R.id.nav_account) {

                // Kiểm tra trạng thái đăng nhập
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                int userId = sharedPreferences.getInt("logged_in_user_id", -1);

                if (userId == -1) {
                    selectedFragment = new ClientProfileFragment(); // Chưa đăng nhập
                } else {
                    selectedFragment = new ProfileFragment(); // Đã đăng nhập
                }
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}