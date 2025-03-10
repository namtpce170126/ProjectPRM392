package com.example.projectprm392.ProductControlU;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.DAOs.ProductDetailDAOU;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard);

        DatabaseHelper dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper
        ProductDetailDAOU productDetailDAOU = new ProductDetailDAOU(dbHelper); // Truyền dbHelper vào
        int sumProid = productDetailDAOU.getSumProid();
        int sumDoanhThu = productDetailDAOU.getSumDoanhThu();

        TextView sumProTxt = findViewById(R.id.textView17);
        sumProTxt.setText(String.valueOf(sumProid));


        TextView doanhThuTxt = findViewById(R.id.textView16);
        doanhThuTxt.setText(String.valueOf(sumDoanhThu));


    }
}