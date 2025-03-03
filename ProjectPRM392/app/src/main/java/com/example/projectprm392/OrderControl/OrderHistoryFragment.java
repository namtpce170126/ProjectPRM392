package com.example.projectprm392.OrderControl;

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

import com.example.projectprm392.Order;
import com.example.projectprm392.OrderDetail;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.Arrays;
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
    private LinearLayout lastSelectedButton = null;
    private TextView textView11;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        // Khởi tạo RecyclerView từ view đã inflated
        recyclerView = view.findViewById(R.id.OrderHistoryRecycleView);
        btnBackToProfile = view.findViewById(R.id.btnBackToProfile);
        // Thiết lập LinearLayoutManager, sử dụng getContext() hoặc requireContext()
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBackToProfile.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_left,   // Fragment trước vào từ trái
                                R.anim.slide_out_right  // Fragment hiện tại ra bên phải
                        )
                        .commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Tạo dữ liệu mẫu
        orderList = new ArrayList<>();
        orderList.add(new Order("1", "2025-02-21", "Ordered", Arrays.asList(
                new OrderDetail("Gà rán", 100000, 2),
                new OrderDetail("Coca", 20000, 1)
        )));
        orderList.add(new Order("2", "2025-02-26", "Preparing", Arrays.asList(
                new OrderDetail("Pizza", 150000, 1)
        )));
        orderList.add(new Order("3", "2025-02-26", "Delivering", Arrays.asList(
                new OrderDetail("Burger", 80000, 3),
                new OrderDetail("Pepsi", 25000, 2)
        )));
        orderList.add(new Order("4", "2025-02-27", "Delivered", Arrays.asList(
                new OrderDetail("Sushi", 120000, 2)
        )));
        orderList.add(new Order("5", "2025-02-26", "Cancelled", Arrays.asList(
                new OrderDetail("Phở", 60000, 4),
                new OrderDetail("Trà đá", 10000, 4)
        )));
        orderList.add(new Order("6", "2025-02-26", "Delivered", Arrays.asList(
                new OrderDetail("Phở", 60000, 4),
                new OrderDetail("Trà đá", 10000, 4)
        )));
        orderList.add(new Order("7", "2025-02-26", "Preparing", Arrays.asList(
                new OrderDetail("Phở", 60000, 4),
                new OrderDetail("Trà đá", 10000, 4)
        )));

        Collections.reverse(orderList);
        // Thiết lập adapter
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

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

    private void filterOrders(String status) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getOrderStatus().equals(status)) {
                filteredList.add(order);
            }
        }
        orderAdapter.updateList(filteredList); // Cập nhật adapter với danh sách đã lọc
    }

    // Phương thức lọc danh sách và thay đổi giao diện nút
    private void setButtonSelected(LinearLayout button, String status) {
        if (lastSelectedButton == button) {
            // Nếu nút đã được chọn, bỏ chọn nó
            button.getChildAt(0).setBackgroundResource(R.drawable.bg_rounded_square);
            lastSelectedButton = null;
            // Hiển thị toàn bộ danh sách
            orderAdapter.updateList(new ArrayList<>(orderList));
            textView11.setText("Order");
        } else {
            // Nếu chọn nút mới
            if (lastSelectedButton != null) {
                // Trả lại background mặc định cho nút trước đó
                lastSelectedButton.getChildAt(0).setBackgroundResource(R.drawable.bg_rounded_square);
            }
            // Đặt background bg_rounded_square_selected cho nút được chọn
            button.getChildAt(0).setBackgroundResource(R.drawable.bg_rounded_square_selected);
            lastSelectedButton = button;
            // Lọc danh sách theo trạng thái
            filterOrders(status);
            textView11.setText(status);
        }
    }
}