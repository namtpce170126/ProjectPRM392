package com.example.projectprm392.PermissionControl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.Admin.Dashboard;
import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.MainActivity;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.ProfileControl.ProfileFragment;
import com.example.projectprm392.R;

public class LoginActivity extends AppCompatActivity {

    EditText etPhone, etPassword;
    ImageView ivTogglePassword, ivDeleteSign, btnClose;
    TextView linkRegister, txtForgotPass;
    Button btnLogin;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etPassword = findViewById(R.id.etPassword);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        linkRegister = findViewById(R.id.tvRegister);
        etPhone = findViewById(R.id.etPhone);
        ivDeleteSign = findViewById(R.id.ivDeleteSign);
        btnClose = findViewById(R.id.btnClose);
        btnLogin = findViewById(R.id.btnLogin);
        txtForgotPass = findViewById(R.id.tvForgot);

        // Chuyển sang ForgotPasswordFragment
        txtForgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Permisson.class);
            intent.putExtra("login_step", true); // Gửi dữ liệu để MainActivity biết cần mở ClientProfileFragment
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        btnClose.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("open_client_profile", true); // Gửi dữ liệu để MainActivity biết cần mở ClientProfileFragment
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Xóa ký tự ô nhập
        ivDeleteSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPhone.setText("");
            }
        });

        // Hiển/ẩn mật khẩu
        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Chuyển về kiểu nhập mật khẩu (ẩn)
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.closed_eyes);
                isPasswordVisible = false;
            } else {
                // Chuyển về kiểu nhập văn bản (hiện)
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
                isPasswordVisible = true;
            }
            // Giữ con trỏ ở cuối văn bản khi thay đổi kiểu nhập
            etPassword.setSelection(etPassword.getText().length());
        });

        btnLogin.setOnClickListener(v -> handleLogin());

        // Chuyển trang đăng ký
        linkRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Xử lý đăng nhập
    private void handleLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate dữ liệu
        if (phone.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            etPhone.setError("Số điện thoại không hợp lệ (10 chữ số, bắt đầu bằng 0)");
            etPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        // Kiểm tra tài khoản trong database
        DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
        AccountDAO accountDAO = new AccountDAO(dbHelper);
        Account account = accountDAO.getAccountByPhoneAndPass(phone, password);

        // Kiểm tra tk
        if (account == null) {
            Toast.makeText(LoginActivity.this, "Số điện thoại hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu tài khoản đăng nhập
        saveSessionLogin(account.getAccId());

        // Kiểm tra role tài khoản
        if (account.getRoleId() == 1){
            // Chuyển sang DashboardActivity
            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack trước đó
            startActivity(intent);
        } else {
            // Chuyển sang HomeFragment
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack trước đó
            startActivity(intent);
        }
    }

    // Lưu session đăng nhập(bằng accId)
    private void saveSessionLogin(int accID) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("logged_in_user_id", accID);
        editor.apply();
    }
}