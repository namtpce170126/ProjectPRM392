package com.example.projectprm392.Admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.PermissionControl.OTPFragment;
import com.example.projectprm392.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        // Load order list của người bán
        boolean openOrderList = getIntent().getBooleanExtra("open_order_list_admin", false);
        if (savedInstanceState == null && openOrderList) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_admin, new OrderListAdmin())
                    .commit();
        }
    }
}