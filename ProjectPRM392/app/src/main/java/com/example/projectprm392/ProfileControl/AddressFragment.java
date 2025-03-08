package com.example.projectprm392.ProfileControl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.R;

public class AddressFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AddressFragment";

    private Button btnUpdateAddress;
    private ImageView btnBack;
    private TextView txtDetailAddress, txtFullName, txtPhone;
    private AccountDAO accountDAO;
    private DatabaseHelper dbHelper;
    private Account currentAccount;

    private String mParam1;
    private String mParam2;

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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
            throw new IllegalStateException("Context is null in AddressFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        btnUpdateAddress = view.findViewById(R.id.btnUpdateAddress);
        btnBack = view.findViewById(R.id.btnBack);
        txtDetailAddress = view.findViewById(R.id.txtDetailAddress);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtPhone = view.findViewById(R.id.txtPhone);

        if (txtDetailAddress == null || btnUpdateAddress == null || btnBack == null) {
            Log.e(TAG, "One or more views are null. Check layout IDs.");
            return view;
        }

        loadAddressData();

        btnUpdateAddress.setOnClickListener(v -> {
            int accountId = getLoggedInAccountId();
            if (accountId == -1) {
                Toast.makeText(getContext(), "Please log in to update address", Toast.LENGTH_SHORT).show();
                return;
            }
            UpdateAddressFragment dialogFragment = UpdateAddressFragment.newInstance(accountId);
            dialogFragment.setOnAddressUpdatedListener(newAddress -> {
                // Làm mới giao diện với địa chỉ mới
                txtDetailAddress.setText(newAddress);
            });
            dialogFragment.show(getParentFragmentManager(), "UpdateAddressDialog");
        });

        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void loadAddressData() {
        try {
            accountDAO.open();
            int accountId = getLoggedInAccountId();
            currentAccount = accountDAO.getAccountById(accountId);
            if (currentAccount != null) {
                updateUI(currentAccount);
            } else {
                Log.e(TAG, "No account found with ID: " + accountId);
                txtDetailAddress.setText("You have not updated your address.\nPlease update your shipping address!");
                txtDetailAddress.setTypeface(null, Typeface.ITALIC);
                txtDetailAddress.setTextSize(16);
            }
        } catch (Exception e) {
            Log.e(TAG, "Database error: " + e.getMessage());
            txtDetailAddress.setText("Error loading address");
        } finally {
            accountDAO.close();
        }
    }

    private void updateUI(Account account) {
        txtFullName.setText(account.getFullName());
        txtPhone.setText(account.getPhoneNumber());

        String address = account.getAddress();
        if (address == null || address.isEmpty()) {
            txtDetailAddress.setText("You have not updated your address.\nPlease update your shipping address!");
            txtDetailAddress.setTypeface(null, Typeface.ITALIC);
            txtDetailAddress.setTextSize(16);
        } else {
            txtDetailAddress.setText(address);
            txtDetailAddress.setTypeface(null, Typeface.NORMAL);
            txtDetailAddress.setTextSize(16);
        }
    }

    public void refreshAddressData(Account updatedAccount) {
        if (updatedAccount != null) {
            currentAccount = updatedAccount;
            try {
                accountDAO.open();
                Account refreshedAccount = accountDAO.getAccountById(updatedAccount.getAccId());
                if (refreshedAccount != null) {
                    updateUI(refreshedAccount);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error refreshing address data: " + e.getMessage());
            } finally {
                accountDAO.close();
            }
        }
    }

    private int getLoggedInAccountId() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("logged_in_user_id", -1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accountDAO != null) {
            accountDAO.close();
        }
    }
}