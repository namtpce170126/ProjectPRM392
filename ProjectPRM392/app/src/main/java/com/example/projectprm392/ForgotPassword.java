package com.example.projectprm392;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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