package com.example.projectprm392.PermissionControl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.projectprm392.R;

public class RegisterFragment extends Fragment {

    TextView linkLogin;
    EditText etPhoneNumber;
    ImageView btnClose;
    Button btnContinue;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tải trang register
        View view = inflater.inflate(R.layout.register, container, false);

        linkLogin = view.findViewById(R.id.tvLogin);
        etPhoneNumber = view.findViewById(R.id.etPhone);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnClose = view.findViewById(R.id.btnClose);

        // Bắt lỗi nhập số điện thoại
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhoneNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Nút "Tiếp tục"
        btnContinue.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString();
            if (validatePhoneNumber(phoneNumber)) {
                // Thực thi chuyển dữ liệu số điện thoại sang fragment khác
                /*sendPhoneToNextFragment(phoneNumber);*/
                savePhoneNumber(phoneNumber); // Lưu số điện thoại vào SharedPreferences
                Toast.makeText(getContext(), "Số điện thoại đã lưu!", Toast.LENGTH_SHORT).show();

                // Chuyển sang RegisterFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new OTPFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Nút đóng fragment
        btnClose.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Chuyển sang Login
        linkLogin.setOnClickListener(v -> {
            // Quay lại LoginFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    // Hàm kiểm tra số điện thoại
    private boolean validatePhoneNumber(String phone) {
        if (phone.length() != 10 || !phone.matches("^0[0-9]{9}$")) {
            btnContinue.setEnabled(false);
            btnContinue.setBackgroundResource(R.drawable.bg_button_disable); // Cập nhật background
            return false;
        } else {
            btnContinue.setEnabled(true);
            btnContinue.setBackgroundResource(R.drawable.bg_button_enable);
            return true;
        }
    }

    // Hàm truyền dữ liệu sang fragment khác
    /*private void sendPhoneToNextFragment(String phoneNumber) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("phone_number", phoneNumber);
        loginFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .addToBackStack(null)
                .commit();
    }*/

    // Hàm lưu số điện thoại vào SharedPreferences
    private void savePhoneNumber(String phoneNumber) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone_number", phoneNumber);
        editor.apply();
    }
}