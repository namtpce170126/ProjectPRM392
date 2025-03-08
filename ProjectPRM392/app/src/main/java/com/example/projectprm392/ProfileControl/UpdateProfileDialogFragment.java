package com.example.projectprm392.ProfileControl;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateProfileDialogFragment extends DialogFragment {

    private EditText edtFullName, edtBirthday, edtPhone, edtEmail;
    private Button btnSave;
    private AccountDAO accountDAO;
    private DatabaseHelper dbHelper;
    private Account currentAccount;
    private static final String TAG = "UpdateProfileDialog";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public UpdateProfileDialogFragment() {
        // Required empty public constructor
    }

    public static UpdateProfileDialogFragment newInstance(String param1, String param2) {
        UpdateProfileDialogFragment fragment = new UpdateProfileDialogFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile_dialog, container, false);

        // Khởi tạo các view
        edtFullName = view.findViewById(R.id.edtFullName);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        edtPhone = view.findViewById(R.id.edtPhoneNumber);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnSave = view.findViewById(R.id.btnUpdateProfile);

        // Lấy tài khoản hiện tại từ arguments
        Bundle args = getArguments();
        if (args != null) {
            int accId = args.getInt("acc_id", -1); // Mặc định acc_id = -1 nếu không có
            accountDAO.open();
            currentAccount = accountDAO.getAccountById(accId);
            if (currentAccount != null) {
                // Đổ dữ liệu hiện tại vào các EditText
                edtFullName.setText(currentAccount.getFullName() != null ? currentAccount.getFullName() : "");
                edtBirthday.setText(currentAccount.getBirthday() != null ? currentAccount.getBirthday() : "");
                edtPhone.setText(currentAccount.getPhoneNumber() != null ? currentAccount.getPhoneNumber() : "");
                edtEmail.setText(currentAccount.getEmail() != null ? currentAccount.getEmail() : "");
            }
            accountDAO.close();
        }

        // Thêm DatePicker cho txtBirthday
        edtBirthday.setOnClickListener(v -> showDatePickerDialog());

        // Xử lý sự kiện click nút Save
        btnSave.setOnClickListener(v -> {
            if (currentAccount != null) {
                saveUpdatedProfile();
            } else {
                Toast.makeText(getContext(), "Không tìm thấy tài khoản để cập nhật", Toast.LENGTH_SHORT).show();
            }
        });

        // Tùy chỉnh dialog
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(yearSelected, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String selectedDate = sdf.format(selectedCalendar.getTime());
                    edtBirthday.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveUpdatedProfile() {
        if (currentAccount != null) {
            accountDAO.open();
            currentAccount.setFullName(edtFullName.getText().toString().trim());
            currentAccount.setBirthday(edtBirthday.getText().toString().trim());
            currentAccount.setPhoneNumber(edtPhone.getText().toString().trim());
            currentAccount.setEmail(edtEmail.getText().toString().trim());

            int rowsAffected = accountDAO.updateAccount(currentAccount);
            if (rowsAffected > 0) {
                Toast.makeText(getContext(), "Information updated successfully!", Toast.LENGTH_SHORT).show();
                // Cập nhật ProfileFragment
                refreshParentFragment();
            } else {
                Toast.makeText(getContext(), "Update information failed!", Toast.LENGTH_SHORT).show();
            }
            accountDAO.close();
            dismiss();
        }
    }

    private void refreshParentFragment() {
        // Lấy ProfileFragment từ FragmentManager
        Fragment parentFragment = getParentFragmentManager().findFragmentById(R.id.fragment_container);
        if (parentFragment instanceof ProfileFragment) {
            ((ProfileFragment) parentFragment).refreshProfileData(currentAccount);
        } else {
            Log.e(TAG, "ProfileFragment not found in fragment container");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accountDAO != null) {
            accountDAO.close();
        }
    }
}