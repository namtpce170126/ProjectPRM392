package com.example.projectprm392.PermissionControl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.R;

import java.util.Random;

public class OTPFragment extends Fragment {

    private EditText[] otpInputs = new EditText[6];
    private String generatedOTP;
    private TextView tvPhoneNumber, tvResendCode, tvTimer;
    private Button btnConfirmOTP;
    private ImageView btnClose;

    public OTPFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_otpactivity, container, false);

        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);
        tvResendCode = view.findViewById(R.id.tvResendCode);
        tvTimer = view.findViewById(R.id.tvTimer);
        btnConfirmOTP = view.findViewById(R.id.btnConfirmOTP);
        btnClose = view.findViewById(R.id.btnClose);

        // Mảng chứa 6 mã số
        otpInputs[0] = view.findViewById(R.id.otp1);
        otpInputs[1] = view.findViewById(R.id.otp2);
        otpInputs[2] = view.findViewById(R.id.otp3);
        otpInputs[3] = view.findViewById(R.id.otp4);
        otpInputs[4] = view.findViewById(R.id.otp5);
        otpInputs[5] = view.findViewById(R.id.otp6);

        // Tạo và hiển thị mã OTP
        generatedOTP = generateOTP();
        showNotificationOTP(generatedOTP);

        // Xử lý nhập OTP tự động chuyển ô
        setupOTPInputs();

        // Xử lý xác nhận OTP
        btnConfirmOTP.setOnClickListener(v -> validateOTP());

        // Xử lý nút đóng
        btnClose.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

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
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpInputs.length - 1) {
                        otpInputs[index + 1].requestFocus(); // Chuyển focus
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    // Kiểm tra mã OTP nhập vào có đúng không
    private void validateOTP() {
        StringBuilder enteredOTP = new StringBuilder();
        for (EditText editText : otpInputs) {
            enteredOTP.append(editText.getText().toString().trim());
        }

        if (enteredOTP.toString().equals(generatedOTP)) {
            Toast.makeText(getContext(), "Xác thực thành công!", Toast.LENGTH_SHORT).show();
            registerInfoComfirm();
        } else {
            Toast.makeText(getContext(), "Mã OTP không đúng. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý gửi lại OTP
    private void resendOTP() {
        generatedOTP = generateOTP();
        showNotificationOTP(generatedOTP);
        Toast.makeText(getContext(), "Mã OTP mới đã được gửi!", Toast.LENGTH_SHORT).show();
    }

    private void registerInfoComfirm(){
        RegisterInfoFragment registerInfoFragment = new RegisterInfoFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, registerInfoFragment)
                .addToBackStack(null)
                .commit();
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }*/
}