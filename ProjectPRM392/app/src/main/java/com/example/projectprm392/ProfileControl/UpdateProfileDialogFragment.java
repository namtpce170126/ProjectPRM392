package com.example.projectprm392.ProfileControl;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectprm392.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfileDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileDialogFragment extends DialogFragment {

    private EditText txtFullName, txtBirthday, txtPhone, txtMail;
    private Button btnSave;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateProfileDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfileDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout của form update profile
        View view = inflater.inflate(R.layout.fragment_update_profile_dialog, container, false);

        // Khởi tạo các view
        txtFullName = view.findViewById(R.id.txtFullName);
        txtBirthday = view.findViewById(R.id.txtBirthday);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtMail = view.findViewById(R.id.txtMail);
        btnSave = view.findViewById(R.id.btnUpdateProfile);

        // Xử lý sự kiện click nút Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý logic lưu thông tin ở đây
                // Sau khi lưu xong thì đóng dialog
                dismiss();
            }
        });

        // Tùy chỉnh dialog
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}