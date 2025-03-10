package com.example.projectprm392.PermissionControl;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
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
import com.example.projectprm392.MainActivity;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterInfoFragment extends Fragment {
    private EditText etFullname, etEmail, etPass, etBirth;
    private ImageView ivDeleteSign1, ivDeleteSign2, ivTogglePassword, btnDatePicker, btnBack;
    private Button btnConfirmRegister;
    boolean isPasswordVisible = false;
    private final Calendar calendar = Calendar.getInstance();

    public RegisterInfoFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tải trang register info
        View view = inflater.inflate(R.layout.register_info, container, false);

        etFullname = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPass = view.findViewById(R.id.etPassword);
        etBirth = view.findViewById(R.id.etBirth);
        ivDeleteSign1 = view.findViewById(R.id.ivDeleteSign1);
        ivDeleteSign2 = view.findViewById(R.id.ivDeleteSign2);
        ivTogglePassword = view.findViewById(R.id.ivTogglePassword);
        btnDatePicker = view.findViewById(R.id.ivCalender);
        btnConfirmRegister = view.findViewById(R.id.btnComplete);
        btnBack = view.findViewById(R.id.btnBack);

        // Xóa nội dung trong ô Fullname
        ivDeleteSign1.setOnClickListener(v -> {
            etFullname.setText("");
        });

        // Xóa nội dung trong ô Email
        ivDeleteSign2.setOnClickListener(v -> {
            etEmail.setText("");
        });

        // Ẩn/hiện mật khẩu
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

        // Chọn ngày trong lịch(chọn ngày quá khứ)
        btnDatePicker.setOnClickListener(v -> showDatePicker());

        btnConfirmRegister.setOnClickListener(v -> registerAccount());

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    // Hiển thị dialog lịch để chọn ngaày
    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Hiện thị lịch cùng với ngày hiện tại
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
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

    // Lấy số điện thoại từ SharedPreferences
    private String getSavedPhoneNumber() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone_number", "Số không xác định");
    }

    // Đăng ký, tạo tài khoản
    private void registerAccount() {
        String fullName = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String birthday = etBirth.getText().toString().trim();
        String phoneNumber = getSavedPhoneNumber();

        // Kiểm tra validate
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            /*Toast.makeText(requireContext(), "Email không hợp lệ!", Toast.LENGTH_SHORT).show();*/
            etEmail.setError("Email không hợp lệ!");
            etEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            /*Toast.makeText(requireContext(), "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();*/
            etPass.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPass.requestFocus();
            return;
        }

        // Khởi tạo AccountDAO
        AccountDAO accountDAO = new AccountDAO(new DatabaseHelper(requireContext()));

        // Thêm tài khoản vào database
        Account newAccount = new Account(0, 2, fullName, password, fullName, phoneNumber, email, birthday, "", 0);
        Account createdAccount = accountDAO.insertAccountByClient(newAccount);

        // Kiểm tra đã tạo tài khoản chưa
        if (createdAccount != null) {
            saveSessionLogin(createdAccount.getAccId());
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        } else {
            Toast.makeText(requireContext(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    // Lưu session đăng nhập(bằng accId)
    private void saveSessionLogin(int accID) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("logged_in_user_id", accID);
        editor.apply();
    }
}