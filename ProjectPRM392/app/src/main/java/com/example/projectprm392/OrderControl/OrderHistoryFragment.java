package com.example.projectprm392.OrderControl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectprm392.Order;
import com.example.projectprm392.OrderDetail;
import com.example.projectprm392.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
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

        // Thiết lập LinearLayoutManager, sử dụng getContext() hoặc requireContext()
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        return view;
    }
}