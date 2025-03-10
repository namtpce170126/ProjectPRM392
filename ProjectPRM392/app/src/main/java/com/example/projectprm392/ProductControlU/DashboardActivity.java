package com.example.projectprm392.ProductControlU;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.projectprm392.Admin.AdminActivity;
import com.example.projectprm392.Admin.Dashboard;
import com.example.projectprm392.DAOs.ProductDetailDAOU;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.R;

public class DashboardActivity extends AppCompatActivity {

    private TextView listOrderbtn;
    private CardView cardManageProduct;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dashboard);

        cardManageProduct = findViewById(R.id.cardManageProduct);
        listOrderbtn = findViewById(R.id.xemThem);

        listOrderbtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AdminActivity.class);
            intent.putExtra("open_order_list_admin", true); // Gửi dữ liệu để MainActivity biết cần mở ClientProfileFragment
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this); // Khởi tạo DatabaseHelper
        ProductDetailDAOU productDetailDAOU = new ProductDetailDAOU(dbHelper); // Truyền dbHelper vào
        int sumProid = productDetailDAOU.getSumProid();
        int sumDoanhThu = productDetailDAOU.getSumDoanhThu();
        int sumChoXacNhan = productDetailDAOU.getChoXacNhan();
        int sumDangGiao = productDetailDAOU.getDangGiao();

        TextView sumProTxt = findViewById(R.id.textView17);
        sumProTxt.setText(String.valueOf(sumProid));


        TextView doanhThuTxt = findViewById(R.id.textView16);
        doanhThuTxt.setText(String.valueOf(sumDoanhThu));

        TextView dangGiaoTXT = findViewById(R.id.dangGiaoTxt);
        dangGiaoTXT.setText(String.valueOf(sumDangGiao));

        TextView choXacNhanTXT = findViewById(R.id.choXacNhanTxt);
        choXacNhanTXT.setText(String.valueOf(sumChoXacNhan));


    }

    public void thongKeBieuDo(View v) {
        Intent intent = new Intent(DashboardActivity.this, ChartUActivity.class);
        startActivity(intent);
    }


}