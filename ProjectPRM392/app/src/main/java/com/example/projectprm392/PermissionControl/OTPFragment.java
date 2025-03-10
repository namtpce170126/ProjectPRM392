package com.example.projectprm392.PermissionControl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.Admin.Dashboard;
import com.example.projectprm392.R;

import java.util.Random;

public class OTPFragment extends Fragment {

    private EditText[] otpInputs = new EditText[6];
    private String generatedOTP;
    private TextView tvPhoneNumber, tvResendCode, tvTimer;
    private Button btnConfirmOTP;
    private ImageView btnBack;
    private CountDownTimer countDownTimer;
    private static final long OTP_VALIDITY_DURATION = 2 * 60 * 1000; // 2 phút (đơn vị: milliseconds)

    public OTPFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tải trang OTP
        View view = inflater.inflate(R.layout.activity_otpactivity, container, false);

        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvResendCode = view.findViewById(R.id.tvResendCode);
        tvTimer = view.findViewById(R.id.tvTimer);
        btnConfirmOTP = view.findViewById(R.id.btnConfirmOTP);
        btnBack = view.findViewById(R.id.btnBack);

        // Mảng chứa 6 mã số
        otpInputs[0] = view.findViewById(R.id.otp1);
        otpInputs[1] = view.findViewById(R.id.otp2);
        otpInputs[2] = view.findViewById(R.id.otp3);
        otpInputs[3] = view.findViewById(R.id.otp4);
        otpInputs[4] = view.findViewById(R.id.otp5);
        otpInputs[5] = view.findViewById(R.id.otp6);

        // Xóa nội dung cũ trên các ô OTP nếu có
        /*for (EditText editText : otpInputs) {
            editText.setText("");  // Xóa nội dung nhập trước đó
        }*/

        // Tạo và hiển thị mã OTP
        generatedOTP = generateOTP();
        showNotificationOTP(generatedOTP);
        startOTPTimer();

        // Xử lý nhập OTP tự động chuyển ô
        setupOTPInputs();

        // Xử lý xác nhận OTP
        btnConfirmOTP.setOnClickListener(v -> validateOTP());


        // Xử lý quay về
        btnBack.setOnClickListener(v -> {
            Bundle bundle = getArguments();
            if (bundle == null) {
                Log.d("OTPFragment", "Bundle is NULL"); // Kiểm tra nếu bundle không tồn tại
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                boolean regToOTP = bundle.getBoolean("reg_to_otp", false);
                Log.d("OTPFragment", "check: " + regToOTP);
                if (regToOTP) {
                    Intent intent = new Intent(requireContext(), RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

        });

        // Xử lý gửi lại mã
        tvResendCode.setOnClickListener(v -> resendOTP());

        return view;
    }

    // Tạo mã OTP 6 chữ số ngẫu nhiên
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo số từ 100000 - 999999
        return String.valueOf(otp);
    }

    // Hiển thị OTP trên thanh thông báo
    private void showNotificationOTP(String otp) {
        String channelId = "OTP_CHANNEL";
        String channelName = "OTP Notification";

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo channel cho Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Mã OTP của bạn")
                .setContentText("Mã xác thực: " + otp)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Gửi thông báo
        notificationManager.notify(1, builder.build());
    }

    // Xử lý nhập OTP tự động chuyển sang ô tiếp theo
    private void setupOTPInputs() {
        for (int i = 0; i < otpInputs.length; i++) {
            final int index = i;
            otpInputs[i].addTextChangedListener(new TextWatcher() {

                // Phương thức xử lý trước khi nhập văn bản (chưa sử dụng)
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                // Phương thức xử lý khi nhập văn bản (chưa sử dụng)
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpInputs.length - 1) {
                        otpInputs[index + 1].requestFocus(); // Chuyển focus
                    }
                }

                // Phương thức xử lý sau khi nhập văn bản (chưa sử dụng)
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    // Kiểm tra mã OTP nhập vào có đúng không
    private void validateOTP() {
        String checkForgotPassSession = checkForgotPassSession();
        StringBuilder enteredOTP = new StringBuilder();
        for (EditText editText : otpInputs) {
            enteredOTP.append(editText.getText().toString().trim());
        }

        if (enteredOTP.toString().equals(generatedOTP)) {
            Toast.makeText(getContext(), "Xác thực thành công!", Toast.LENGTH_SHORT).show();
            if (checkForgotPassSession == null){
                registerInfoComfirm();
            } else {
                resetPassword();
            }
        } else {
            Toast.makeText(getContext(), "Mã OTP không đúng. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý gửi lại OTP
    private void resendOTP() {
        generatedOTP = generateOTP();
        showNotificationOTP(generatedOTP);
        startOTPTimer();
        Toast.makeText(getContext(), "Mã OTP mới đã được gửi!", Toast.LENGTH_SHORT).show();
    }

    // Xử lý chuyển sang RegisterInfoFragment
    private void registerInfoComfirm(){
        RegisterInfoFragment registerInfoFragment = new RegisterInfoFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_permission, registerInfoFragment)
                .addToBackStack(null)
                .commit();
    }

    // Kiểm tra session quên mk
    private String checkForgotPassSession() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone_forgot_pass", null);
    }

    // Xử lý chuyển sang ResetPasswordFragment
    private void resetPassword(){
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_permission, resetPasswordFragment)
                .addToBackStack(null)
                .commit();
    }

    // Đếm ngược thời gian hiệu lực
    private void startOTPTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(OTP_VALIDITY_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                tvTimer.setText(String.format("(%02d:%02d)", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("(Hết hiệu lực)");
                generatedOTP = null; // Vô hiệu hóa OTP
                Toast.makeText(getContext(), "Mã OTP đã hết hạn, vui lòng yêu cầu mã mới!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clearOTPInputs();
    }

    private void clearOTPInputs() {
        for (EditText otpInput : otpInputs) {
            otpInput.setText(""); // Xóa nội dung nhập vào
        }
    }
}