package com.example.projectprm392.OrderControl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectprm392.DAOs.OrderDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Order;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {

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

    public OrderHistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        // Khởi tạo các view
        recyclerView = view.findViewById(R.id.OrderHistoryRecycleView);
        linearLayoutNoOrder = view.findViewById(R.id.linearLayoutNoOrder);
        btnBackToProfile = view.findViewById(R.id.btnBackToProfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo DatabaseHelper và OrderDAO
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        orderDAO = new OrderDAO(dbHelper);

        btnBackToProfile.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
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

            // Thiết lập listener cho nút Cancel Order
            orderAdapter.setOnCancelOrderListener(orderId -> showCancelConfirmationDialog(orderId));
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

    // Phương thức hiển thị dialog xác nhận hủy đơn hàng
    private void showCancelConfirmationDialog(int orderId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Cancellation")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Xác nhận hủy, cập nhật trạng thái
                    updateOrderStatus(orderId, "Cancelled");
                    dialog.dismiss();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss(); // Đóng dialog nếu chọn "No"
                })
                .setCancelable(true) // Cho phép đóng dialog bằng nút Back
                .show();
    }

    // Phương thức cập nhật trạng thái đơn hàng
    private void updateOrderStatus(int orderId, String newStatus) {
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            order.setStatus(newStatus);
            int rowsAffected = orderDAO.updateOrder(order);
            if (rowsAffected > 0) {
                Toast.makeText(getContext(), "Order cancelled successfully!", Toast.LENGTH_SHORT).show();
                // Làm mới danh sách đơn hàng
                int accountId = getLoggedInAccountId();
                orderList = getOrdersForAccount(accountId);
                Collections.reverse(orderList);
                orderAdapter.updateList(orderList);
            } else {
                Toast.makeText(getContext(), "Failed to cancel order!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Order not found!", Toast.LENGTH_SHORT).show();
        }
    }

    // Lấy account_id từ session
    private int getLoggedInAccountId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }
}