package com.example.projectprm392;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterInfo extends AppCompatActivity {
    private EditText etFullname, etEmail, etPass, etBirth;
    private ImageView ivDeleteSign1, ivDeleteSign2, ivTogglePassword, btnDatePicker;
    boolean isPasswordVisible = false;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_info);

        etFullname = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPassword);
        etBirth = findViewById(R.id.etBirth);
        ivDeleteSign1 = findViewById(R.id.ivDeleteSign1);
        ivDeleteSign2 = findViewById(R.id.ivDeleteSign2);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        btnDatePicker = findViewById(R.id.ivCalender);

        ivDeleteSign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etFullname.setText("");
            }
        });

        ivDeleteSign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail.setText("");
            }
        });

        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Chuyển về kiểu nhập mật khẩu (ẩn)
                etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.closed_eyes);
                isPasswordVisible = false;
            } else {
                // Chuyển về kiểu nhập văn bản (hiện)
                etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
                isPasswordVisible = true;
            }
            // Giữ con trỏ ở cuối văn bản khi thay đổi kiểu nhập
            etPass.setSelection(etPass.getText().length());
        });

        btnDatePicker.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    etBirth.setText(sdf.format(calendar.getTime()));
                },
                year, month, day
        );

        // Chỉ cho phép chọn ngày trong quá khứ
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }
}