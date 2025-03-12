package com.example.projectprm392.OrderControlX;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.OrderDAOX;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.ProductControlU.DashboardActivity;
import com.example.projectprm392.R;

import java.util.List;

public class Confirm_edit_Order_Activity extends AppCompatActivity {
    ImageButton btnback;
    private Button btnListConfirmOrder, btnListUpdateSatus;

    private OrderDAOX orderDAOX;
    private List<Order> orderList;
    private AcceptOrderAdapter acceptOrderAdapter;
    private UpdateStatusOrderAdapter updateStatusOrderAdapter;
    RecyclerView recyclerViewListOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_edit_order);

        btnback = findViewById(R.id.btnBackDashboard);

        recyclerViewListOrder = findViewById(R.id.recyclerViewOrderNeedAccept);
        btnListConfirmOrder = findViewById(R.id.buttonConfirmOrder);
        btnListUpdateSatus = findViewById(R.id.buttonUpdateStatus);

        // Khởi tạo database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        orderDAOX = new OrderDAOX(dbHelper);

        orderList = orderDAOX.getAllOrdersByStatus("Ordered");

        btnListConfirmOrder.setOnClickListener(view -> {
            // Cấu hình RecyclerView cho danh mục
            acceptOrderAdapter = new AcceptOrderAdapter(this,orderList);
            recyclerViewListOrder.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewListOrder.setAdapter(acceptOrderAdapter);
        });

        btnListUpdateSatus.setOnClickListener(view -> {
            orderDAOX = new OrderDAOX(dbHelper);
            orderList = orderDAOX.getAllOrdersExcludingOrdered();
            // Cấu hình RecyclerView cho danh mục
            updateStatusOrderAdapter = new UpdateStatusOrderAdapter(this,orderList);
            recyclerViewListOrder.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewListOrder.setAdapter(updateStatusOrderAdapter);
        });

        btnback.setOnClickListener(view -> {
            Intent intent = new Intent(Confirm_edit_Order_Activity.this, DashboardActivity.class);
            startActivity(intent);
        });

    }
}