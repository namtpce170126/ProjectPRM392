package com.example.projectprm392;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);

        recyclerView = findViewById(R.id.OrderHistoryRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo dữ liệu mẫu
        orderList = new ArrayList<>();
        orderList.add(new Order("1", "2025-02-26", Arrays.asList(
                new OrderDetail("Gà rán", 100000, 2),
                new OrderDetail("Coca", 20000, 1)
        )));
        orderList.add(new Order("2", "2025-02-26", Arrays.asList(
                new OrderDetail("Pizza", 150000, 1)
        )));
        orderList.add(new Order("3", "2025-02-26", Arrays.asList(
                new OrderDetail("Burger", 80000, 3),
                new OrderDetail("Pepsi", 25000, 2)
        )));
        orderList.add(new Order("4", "2025-02-26", Arrays.asList(
                new OrderDetail("Sushi", 120000, 2)
        )));
        orderList.add(new Order("5", "2025-02-26", Arrays.asList(
                new OrderDetail("Phở", 60000, 4),
                new OrderDetail("Trà đá", 10000, 4)
        )));

        // Thiết lập adapter
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);
    }
}