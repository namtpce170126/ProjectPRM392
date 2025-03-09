package com.example.projectprm392.PermissionControl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.ProfileControl.ClientProfileFragment;
import com.example.projectprm392.R;

public class ResetPasswordFragment extends Fragment {

    private ImageView btnBack, ivTogglePassword1, ivTogglePassword2;
    private EditText etNewPass, etConfirmPass;
    private Button btnResetPass;
    boolean isPasswordVisible1 = false;
    boolean isPasswordVisible2 = false;

    public ResetPasswordFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Tải trang reset password
        View view =  inflater.inflate(R.layout.reset_password, container, false);

        btnBack = view.findViewById(R.id.btnBack);
        etNewPass = view.findViewById(R.id.etNewPass);
        etConfirmPass = view.findViewById(R.id.etConfirmPass);
        btnResetPass = view.findViewById(R.id.btnResetPass);
        ivTogglePassword1 = view.findViewById(R.id.ivTogglePassword1);
        ivTogglePassword2 = view.findViewById(R.id.ivTogglePassword2);

        // Hiển/ẩn mật khẩu mới
        ivTogglePassword1.setOnClickListener(v -> {
            if (isPasswordVisible1) {
                // Chuyển về kiểu nhập mật khẩu (ẩn)
                etNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword1.setImageResource(R.drawable.closed_eyes);
                isPasswordVisible1 = false;
            } else {
                // Chuyển về kiểu nhập văn bản (hiện)
                etNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword1.setImageResource(R.drawable.ic_eye_open);
                isPasswordVisible1 = true;
            }
            // Giữ con trỏ ở cuối văn bản khi thay đổi kiểu nhập
            etNewPass.setSelection(etNewPass.getText().length());
        });

        // Hiển/ẩn mật khẩu xác nận
        ivTogglePassword2.setOnClickListener(v -> {
            if (isPasswordVisible2) {
                // Chuyển về kiểu nhập mật khẩu (ẩn)
                etConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword2.setImageResource(R.drawable.closed_eyes);
                isPasswordVisible2 = false;
            } else {
                // Chuyển về kiểu nhập văn bản (hiện)
                etConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword2.setImageResource(R.drawable.ic_eye_open);
                isPasswordVisible2 = true;
            }
            // Giữ con trỏ ở cuối văn bản khi thay đổi kiểu nhập
            etConfirmPass.setSelection(etConfirmPass.getText().length());
        });

        // Xử lý nút reset mật khẩu
        btnResetPass.setOnClickListener(v -> resetPassword());

        // Nút đóng fragment
        btnBack.setOnClickListener(v -> backForgotPass());

        return view;
    }

    // Xử lý nút back
    private void backForgotPass(){
        removeForgotPassSession();
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_permission, forgotPasswordFragment)
                .addToBackStack(null)
                .commit();
    }

    // Xóa session quên mật khẩu
    private void removeForgotPassSession(){
        // Xóa session quên mk
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("phone_forgot_pass"); // Xóa số người dùng
        editor.apply();
    }

    // Đặt lại mật khẩu
    private void resetPassword() {
        String newPassword = etNewPass.getText().toString().trim();
        String confirmPassword = etConfirmPass.getText().toString().trim();

        if (!validatePassword(newPassword, confirmPassword)) {
            return;
        }

        // Lấy số điện thoại từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("phone_forgot_pass", null);

        if (phoneNumber == null) {
            Toast.makeText(requireContext(), "Lỗi: Không tìm thấy số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        AccountDAO accountDAO = new AccountDAO(dbHelper);

        // Cập nhật mật khẩu
        boolean isUpdated = accountDAO.updatePassword(phoneNumber, newPassword);
        if (isUpdated) {
            Toast.makeText(requireContext(), "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            backToLogin();
        } else {
            Toast.makeText(requireContext(), "Cập nhật mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePassword(String newPassword, String confirmPassword) {

        // Validate dữ liệu
        if (newPassword.isEmpty()) {
            etNewPass.setError("Vui lòng nhập số điện thoại");
            etNewPass.requestFocus();
            return false;
        }

        if (newPassword.length() < 6) {
            etNewPass.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etNewPass.requestFocus();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPass.setError("Mật khẩu xác nhận không khớp!");
            etConfirmPass.requestFocus();
            return false;
        }

        return true;
    }

    // Quay về trang đăng nhập
    private void backToLogin() {
        removeForgotPassSession();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}