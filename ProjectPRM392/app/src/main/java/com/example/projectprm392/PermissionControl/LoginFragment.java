package com.example.projectprm392.PermissionControl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.ProfileControl.ProfileFragment;
import com.example.projectprm392.R;

public class LoginFragment extends Fragment {
    EditText etPhone, etPassword;
    ImageView ivTogglePassword, ivDeleteSign, btnClose;
    TextView linkRegister, txtForgotPass;
    Button btnLogin;
    boolean isPasswordVisible = false;

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tải trang login
        View view = inflater.inflate(R.layout.login, container, false);

        etPassword = view.findViewById(R.id.etPassword);
        ivTogglePassword = view.findViewById(R.id.ivTogglePassword);
        linkRegister = view.findViewById(R.id.tvRegister);
        etPhone = view.findViewById(R.id.etPhone);
        ivDeleteSign = view.findViewById(R.id.ivDeleteSign);
        btnClose = view.findViewById(R.id.btnClose);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtForgotPass = view.findViewById(R.id.tvForgot);

        // Chuyển sang ForgotPasswordFragment
        txtForgotPass.setOnClickListener(v -> {
            ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, forgotPasswordFragment)
                    .addToBackStack(null)
                    .commit();
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
            // Chuyển sang RegisterFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Nút đóng fragment
        btnClose.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
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
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        AccountDAO accountDAO = new AccountDAO(dbHelper);
        Account account = accountDAO.getAccountByPhoneAndPass(phone, password);

        if (account == null) {
            Toast.makeText(requireContext(), "Số điện thoại hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu tài khoản đăng nhập
        saveSessionLogin(account.getAccId());

        // Chuyển sang HomeFragment
        Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .commit();
    }

    // Lưu session đăng nhập(bằng accId)
    private void saveSessionLogin(int accID) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("logged_in_user_id", accID);
        editor.apply();
    }
}
