package com.example.projectprm392.ProfileControl;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.projectprm392.OrderControl.OrderHistoryFragment;
import com.example.projectprm392.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btnUpdateProfile;
    LinearLayout btnOrderHistory;
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Khởi tạo nút Update Profile
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnOrderHistory = view.findViewById(R.id.btnOrderHistory);

        // Xử lý sự kiện click nút Update Profile
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và hiển thị UpdateProfileDialogFragment
                UpdateProfileDialogFragment dialogFragment = new UpdateProfileDialogFragment();
                dialogFragment.show(getParentFragmentManager(), "UpdateProfileDialog");
            }
        });

        // Xử lý sự kiện click nút Order History
        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo instance của OrderHistoryFragment
                OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();

                // Sử dụng FragmentManager để thêm hoặc thay thế fragment
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, orderHistoryFragment) // Thay R.id.fragment_container bằng ID của container trong activity layout của bạn
                        .addToBackStack(null) // Thêm vào back stack để người dùng có thể quay lại
                        .commit();
            }
        });

        return view;
    }
}