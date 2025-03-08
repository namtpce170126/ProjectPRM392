package com.example.projectprm392.PermissionControl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.R;

public class ForgotPasswordFragment extends Fragment {

    private EditText etPhoneForgot;
    private ImageView ivDeleteSign, btnClose;
    private Button btnFotgotPass;

    public ForgotPasswordFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgot_password, container, false);

        etPhoneForgot = view.findViewById(R.id.etPhoneForgot);
        ivDeleteSign = view.findViewById(R.id.ivDeleteSign);
        btnFotgotPass = view.findViewById(R.id.btnContinue);
        btnClose = view.findViewById(R.id.btnClose);

        // Bắt sự kiện click để xóa nội dung EditText
        ivDeleteSign.setOnClickListener(v -> {
            etPhoneForgot.setText("");
        });

        // Xác thực số điện thoại quên mk
        btnFotgotPass.setOnClickListener(v -> {
            handleResetPass();
        });

        // Nút đóng fragment
        btnClose.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void handleResetPass(){
        String phone = etPhoneForgot.getText().toString().trim();

        // Validate dữ liệu
        if (phone.isEmpty()) {
            etPhoneForgot.setError("Vui lòng nhập số điện thoại");
            etPhoneForgot.requestFocus();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            etPhoneForgot.setError("Số điện thoại không hợp lệ (10 chữ số, bắt đầu bằng 0)");
            etPhoneForgot.requestFocus();
            return;
        }

        // Kiểm tra tài khoản trong database
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        AccountDAO accountDAO = new AccountDAO(dbHelper);
        Account account = accountDAO.getAccountByPhone(phone);

        if (account == null) {
            Toast.makeText(requireContext(), "Số điện thoại không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm session quên mk
        forgotPassSession(account.getPhoneNumber());

        // Chuyển sang RegisterFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new OTPFragment())
                .addToBackStack(null)
                .commit();
    }

    // Tạo session quên mật khẩu(bằng sđt)
    private void forgotPassSession(String phoneNum) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone_forgot_pass", phoneNum);
        editor.apply();
    }
}