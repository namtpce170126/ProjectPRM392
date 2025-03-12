package com.example.projectprm392.OrderControlX;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.MainActivity;
import com.example.projectprm392.R;

public class AfterOrderActivity extends AppCompatActivity {
    ImageButton btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_after_order);
        btnback = findViewById(R.id.imageButtonBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterOrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}