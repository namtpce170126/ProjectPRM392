package com.example.projectprm392.PermissionControl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.HomeFragment;
import com.example.projectprm392.MainActivity;
import com.example.projectprm392.R;

public class Permisson extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_permisson);

        // Load OTP của register
        boolean openOTPFromReg = getIntent().getBooleanExtra("register_step", false);
        if (savedInstanceState == null && openOTPFromReg) {
            OTPFragment otpFragment = new OTPFragment();

            // Gửi dữ liệu qua Fragment bằng Bundle
            Bundle bundle = new Bundle();
            bundle.putBoolean("reg_to_otp", true);
            otpFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_permission, otpFragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_permission, new ForgotPasswordFragment())
                    .commit();
        }
    }

    private String getPermissionType() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("permisson_type", null);
    }
}