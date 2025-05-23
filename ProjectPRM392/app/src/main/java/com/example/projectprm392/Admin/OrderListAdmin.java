package com.example.projectprm392.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.OrderDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.OrderControl.OrderAdapter;
import com.example.projectprm392.OrderControl.OrderHistoryFragment;
import com.example.projectprm392.PermissionControl.RegisterActivity;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderListAdmin extends Fragment {
    private RecyclerView recyclerView;
    private ImageView btnBackToProfile;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private LinearLayout lastSelectedButton = null, linearLayoutNoOrder = null;
    private TextView textView11;
    private OrderDAO orderDAO;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OrderListAdmin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFragment newInstance(String param1, String param2) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_list_admin, container, false);

        // Khởi tạo các view
        recyclerView = view.findViewById(R.id.OrderHistoryRecycleView);
        linearLayoutNoOrder = view.findViewById(R.id.linearLayoutNoOrder);
        btnBackToProfile = view.findViewById(R.id.btnBackToProfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo DatabaseHelper và OrderDAO
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        orderDAO = new OrderDAO(dbHelper);

        btnBackToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack trước đó
            startActivity(intent);

        });

        // Lấy danh sách đơn hàng cho account_id
        int accountId = getLoggedInAccountId();
        orderList = getOrdersForAccount(accountId); // Lấy đơn hàng cho account_id
        Collections.reverse(orderList); // Đảo ngược danh sách nếu muốn hiển thị mới nhất trước

        // Kiểm tra nếu orderList rỗng thì hiển thị txtNoOrder, ngược lại hiển thị RecyclerView
        if (orderList == null || orderList.isEmpty()) {
            recyclerView.setVisibility(View.GONE); // Ẩn RecyclerView
            linearLayoutNoOrder.setVisibility(View.VISIBLE); // Hiển thị TextView
        } else {
            recyclerView.setVisibility(View.VISIBLE); // Hiển thị RecyclerView
            linearLayoutNoOrder.setVisibility(View.GONE); // Ẩn TextView

            // Thiết lập adapter
            orderAdapter = new OrderAdapter(orderList);
            recyclerView.setAdapter(orderAdapter);
        }

        // Lấy tham chiếu đến các nút trạng thái
        LinearLayout btnOrdered = view.findViewById(R.id.btnOrdered);
        LinearLayout btnPreparing = view.findViewById(R.id.btnPreparing);
        LinearLayout btnDelivering = view.findViewById(R.id.btnDelivering);
        LinearLayout btnDelivered = view.findViewById(R.id.btnDelivered);
        LinearLayout btnCancelled = view.findViewById(R.id.btnCancelled);
        textView11 = view.findViewById(R.id.textView11);

        // Thiết lập sự kiện click cho từng nút
        btnOrdered.setOnClickListener(v -> setButtonSelected(btnOrdered, "Ordered"));
        btnPreparing.setOnClickListener(v -> setButtonSelected(btnPreparing, "Preparing"));
        btnDelivering.setOnClickListener(v -> setButtonSelected(btnDelivering, "Delivering"));
        btnDelivered.setOnClickListener(v -> setButtonSelected(btnDelivered, "Delivered"));
        btnCancelled.setOnClickListener(v -> setButtonSelected(btnCancelled, "Cancelled"));

        return view;
    }

    // Phương thức lấy danh sách đơn hàng cho một account_id cụ thể
    private List<Order> getOrdersForAccount(int accountId) {
        List<Order> allOrders = orderDAO.getAllOrders();
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getAccountId() == accountId) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    private void filterOrders(String status) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getStatus().equals(status)) {
                filteredList.add(order);
            }
        }
        linearLayoutNoOrder = getView().findViewById(R.id.linearLayoutNoOrder);
        if (filteredList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            linearLayoutNoOrder.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            linearLayoutNoOrder.setVisibility(View.GONE);
            orderAdapter.updateList(filteredList); // Cập nhật adapter với danh sách đã lọc
        }
    }

    // Phương thức lọc danh sách và thay đổi giao diện nút
    private void setButtonSelected(LinearLayout button, String status) {
        if (lastSelectedButton == button) {
            button.getChildAt(0).setBackgroundResource(R.drawable.bg_rounded_square);
            lastSelectedButton = null;
            orderAdapter.updateList(orderList); // Hiển thị toàn bộ danh sách
            textView11.setText("Order");
        } else {
            if (lastSelectedButton != null) {
                lastSelectedButton.getChildAt(0).setBackgroundResource(R.drawable.bg_rounded_square);
            }
            button.getChildAt(0).setBackgroundResource(R.drawable.bg_rounded_square_selected);
            lastSelectedButton = button;
            filterOrders(status); // Lọc danh sách theo trạng thái
            textView11.setText(status);
        }
    }

    // Lấy account_id từ session
    private int getLoggedInAccountId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }
}
