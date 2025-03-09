package com.example.projectprm392.ProfileControl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.OrderControl.OrderHistoryFragment;
import com.example.projectprm392.R;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ProfileFragment";
    private Button btnUpdateProfile;
    private LinearLayout btnOrderHistory, btnShippingAddress;
    private TextView txtFullName, txtBirthday, txtPhone, txtMail, txtProfileName, textView10, txtLogout;
    private String mParam1;
    private String mParam2;
    private AccountDAO accountDAO;
    private DatabaseHelper dbHelper;
    private Account currentAccount; // Lưu tài khoản hiện tại

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
        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            accountDAO = new AccountDAO(dbHelper);
        } else {
            throw new IllegalStateException("Context is null in ProfileFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Khởi tạo các view
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnOrderHistory = view.findViewById(R.id.btnOrderHistory);
        btnShippingAddress = view.findViewById(R.id.btnShippingAddress);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtBirthday = view.findViewById(R.id.txtBirthday);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtMail = view.findViewById(R.id.txtMail);
        txtProfileName = view.findViewById(R.id.textView2);
        textView10 = view.findViewById(R.id.textView10);
        txtLogout = view.findViewById(R.id.txtLogout);

        // Kiểm tra null cho các view
        if (txtFullName == null || txtBirthday == null || txtPhone == null || txtMail == null || txtProfileName == null ||
                btnUpdateProfile == null || btnOrderHistory == null || btnShippingAddress == null) {
            Log.e(TAG, "One or more views are null. Check layout IDs.");
            return view;
        }

        // Load dữ liệu ban đầu
        loadProfileData();

        // Xử lý sự kiện click nút Update Profile
        btnUpdateProfile.setOnClickListener(v -> {
            int accountId = getLoggedInAccountId();
            UpdateProfileDialogFragment dialogFragment = UpdateProfileDialogFragment.newInstance("", "");
            Bundle args = new Bundle();
            args.putInt("acc_id", currentAccount != null ? currentAccount.getAccId() : accountId); // Truyền acc_id
            dialogFragment.setArguments(args);
            dialogFragment.show(getParentFragmentManager(), "UpdateProfileDialog");
        });

        // Xử lý sự kiện click nút Order History
        btnOrderHistory.setOnClickListener(v -> {
            OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
            if (getParentFragmentManager() != null) {
                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragment_container, orderHistoryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnShippingAddress.setOnClickListener(v -> {
            AddressFragment addressFragment = new AddressFragment();
            if (getParentFragmentManager() != null) {
                getParentFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )
                        .replace(R.id.fragment_container, addressFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Bấm nút đăng xuất
        txtLogout.setOnClickListener(v -> logoutAccount());

        return view;
    }

    // Tải dữ liệu profile
    private void loadProfileData() {
        try {
            accountDAO.open();
            int accountId = getLoggedInAccountId();
            currentAccount = accountDAO.getAccountById(accountId);
            if (currentAccount != null) {
                updateUI(currentAccount);
            } else {
                Log.e(TAG, "No account found with ID 1. Check database data.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Database error: " + e.getMessage());
        } finally {
            accountDAO.close();
        }
    }

    // Load lại dữ liệu mới
    private void updateUI(Account account) {
        txtFullName.setText(account.getFullName());
        txtBirthday.setText(account.getBirthday());
        txtPhone.setText(account.getPhoneNumber());
        txtMail.setText(account.getEmail());
        txtProfileName.setText(account.getFullName());
        textView10.setText(account.getFullName() + "'s Profile");
    }

    public void refreshProfileData(Account updatedAccount) {
        if (updatedAccount != null) {
            currentAccount = updatedAccount;
            // Cập nhật giao diện bằng cách truy vấn lại từ database để đảm bảo dữ liệu mới
            try {
                accountDAO.open();
                Account refreshedAccount = accountDAO.getAccountById(updatedAccount.getAccId());
                if (refreshedAccount != null) {
                    updateUI(refreshedAccount);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error refreshing profile data: " + e.getMessage());
            } finally {
                accountDAO.close();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accountDAO != null) {
            accountDAO.close();
        }
    }

    // Xử lý đăng xuất tài khoản
    private void logoutAccount(){
        // Xóa session đăng nhập
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("logged_in_user_id"); // Xóa ID người dùng
        editor.apply();

        ClientProfileFragment clientProfileFragment = new ClientProfileFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, clientProfileFragment)
                .addToBackStack(null)
                .commit();
    }

    // Lấy account_id từ session
    private int getLoggedInAccountId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }
}