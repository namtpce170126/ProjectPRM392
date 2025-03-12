package com.example.projectprm392.PermissionControl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.HomeControl.MainActivity;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.R;

public class RegisterActivity extends AppCompatActivity {

    ImageView btnClose;
    TextView linkLogin;
    EditText etPhoneNumber;
    Button btnRegister;
    CheckBox chkTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        btnClose = findViewById(R.id.btnClose);
        linkLogin = findViewById(R.id.tvLogin);
        etPhoneNumber = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnContinue);
        chkTerms = findViewById(R.id.chkTerms);

        btnClose.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("open_client_profile", true); // Gửi dữ liệu để MainActivity biết cần mở ClientProfileFragment
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Bắt lỗi nhập số điện thoại
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhoneNumber();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Kiểm tra checkbox khi thay đổi trạng thái
        chkTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            validatePhoneNumber();
        });

        // Nút "Tiếp tục"
        btnRegister.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString();
            if (validatePhoneNumber()) {
                // Kiểm tra số điện thoại đã tồn tại chưa
                if (isPhoneNumberExists(phoneNumber)) {
                    Toast.makeText(RegisterActivity.this, "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Thực thi chuyển dữ liệu số điện thoại sang fragment khác
                savePhoneNumber(phoneNumber); // Lưu số điện thoại vào SharedPreferences

                // Chuyển sang Permission OTPFragment
                Intent intent = new Intent(RegisterActivity.this, Permisson.class);
                intent.putExtra("register_step", true); // Gửi dữ liệu để MainActivity biết cần mở ClientProfileFragment
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Chuyển trang đăng nhập
        linkLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Hàm kiểm tra số điện thoại
    private boolean validatePhoneNumber() {
        String phone = etPhoneNumber.getText().toString();
        boolean isPhoneValid = phone.length() == 10 && phone.matches("^0[0-9]{9}$");
        boolean isTermsChecked = chkTerms.isChecked();

        if (isPhoneValid && isTermsChecked) {
            btnRegister.setEnabled(true);
            btnRegister.setBackgroundResource(R.drawable.bg_button_enable);
            return true;
        } else {
            btnRegister.setEnabled(false);
            btnRegister.setBackgroundResource(R.drawable.bg_button_disable); // Cập nhật background
            return false;
        }
    }

    // Hàm lưu số điện thoại vào SharedPreferences
    private void savePhoneNumber(String phoneNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone_number", phoneNumber);
        editor.apply();
    }

    // Kiểm tra sđt đã có chưa
    private boolean isPhoneNumberExists(String phoneNumber) {
        DatabaseHelper dbHelper = new DatabaseHelper(RegisterActivity.this);
        AccountDAO accountDAO = new AccountDAO(dbHelper);
        Account account = accountDAO.getAccountByPhone(phoneNumber);
        return account != null; // Nếu tài khoản tồn tại, trả về true
    }
}