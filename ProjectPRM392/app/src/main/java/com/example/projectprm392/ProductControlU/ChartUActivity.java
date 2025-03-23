package com.example.projectprm392.ProductControlU;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartUActivity extends AppCompatActivity {
    private BarChart barChart,barChartOrder;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chart_uactivity);
        barChart = findViewById(R.id.barChart);
        barChartOrder = findViewById(R.id.barChartOrder);
        databaseHelper = new DatabaseHelper(this);

        //dữ liệu từ SQLite và hiển thị lên biểu đồ
        loadChartData();
        loadChartDataOrder();
    }


    private void loadChartData() {
        Cursor cursor = databaseHelper.getProductSales();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String proName = cursor.getString(0); // tên sản phẩm
                int totalQuantity = cursor.getInt(1); // tổng số lượng đã bán
                entries.add(new BarEntry(index, totalQuantity));
                labels.add(proName); // set tên sản phẩm làm nhãn
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Vẽ
        drawChart(entries, labels);
    }
    private void loadChartDataOrder() {
        Cursor cursor = databaseHelper.getMonthlyOrderCount(); // Lấy dữ liệu theo tháng
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String month = cursor.getString(0); // tháng
                int totalOrders = cursor.getInt(1); // tổng số đơn hàng
                entries.add(new BarEntry(index, totalOrders));
                labels.add(month); // set tháng làm nhãn
                index++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Vẽ biểu đồ
        drawChartOrder(entries, labels);
    }

    private void drawChartOrder(ArrayList<BarEntry> entries, ArrayList<String> labels) {
        BarDataSet dataSet = new BarDataSet(entries, "Số lượng đơn hàng");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData data = new BarData(dataSet);
        barChartOrder.setData(data);

        // Trục X
        XAxis xAxis = barChartOrder.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // set tháng làm nhãn

        // Trục Y
        YAxis leftAxis = barChartOrder.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        barChartOrder.getAxisRight().setEnabled(false); // Ẩn trục Y bên phải
        barChartOrder.invalidate(); // Refresh biểu đồ
    }

    private void drawChart(ArrayList<BarEntry> entries, ArrayList<String> labels) {
        BarDataSet dataSet = new BarDataSet(entries, "Số lượng đã bán");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData data = new BarData(dataSet);
        barChart.setData(data);

        // trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // set tên sản phẩm làm nhãn

        //  trục Y
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        barChart.getAxisRight().setEnabled(false); //  trục Y bên phải
        barChart.invalidate(); // refresh biểu đồ
    }


    public void backDashboard(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }




}