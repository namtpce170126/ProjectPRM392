package com.example.projectprm392.PermissionControl;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.R;

public class ForgotPassword extends AppCompatActivity {

    private EditText etPhoneForgot;
    private ImageView ivDeleteSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        etPhoneForgot = findViewById(R.id.etPhoneForgot);
        ivDeleteSign = findViewById(R.id.ivDeleteSign);

        // Bắt sự kiện click để xóa nội dung EditText
        ivDeleteSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPhoneForgot.setText("");
            }
        });
    }
}