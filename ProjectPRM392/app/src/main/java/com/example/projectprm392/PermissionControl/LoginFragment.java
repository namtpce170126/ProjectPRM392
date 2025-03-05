package com.example.projectprm392.PermissionControl;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.R;

public class LoginFragment extends Fragment {
    EditText etPhone, etPassword;
    ImageView ivTogglePassword, ivDeleteSign;
    TextView linkRegister;

    public LoginFragment() {
        // Required empty public constructor
    }
    boolean isPasswordVisible = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.login);*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        /*Button btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            // Chuyển sang RegisterFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });*/

        etPassword = view.findViewById(R.id.etPassword);
        ivTogglePassword = view.findViewById(R.id.ivTogglePassword);
        linkRegister = view.findViewById(R.id.tvRegister);
        etPhone = view.findViewById(R.id.etPhone);
        ivDeleteSign = view.findViewById(R.id.ivDeleteSign);

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

        // Chuyển trang đăng ký
        linkRegister.setOnClickListener(v -> {
            // Chuyển sang RegisterFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
