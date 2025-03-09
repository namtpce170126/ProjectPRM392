package com.example.projectprm392.ProfileControl;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectprm392.DAOs.AccountDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Account;
import com.example.projectprm392.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateAddressFragment extends DialogFragment {

    private static final String TAG = "UpdateAddressFragment";

    private Spinner spinnerCountry, spinnerProvince, spinnerDistrict, spinnerWard;
    private EditText edtDetailAddress;
    private Button btnUpdateProfile, btnCancel;
    private RequestQueue requestQueue;

    private List<String> countryList = new ArrayList<>();
    private List<String> provinceList = new ArrayList<>();
    private List<String> districtList = new ArrayList<>();
    private List<String> wardList = new ArrayList<>();
    private List<JSONObject> provinceData = new ArrayList<>();
    private List<JSONObject> districtData = new ArrayList<>();
    private List<JSONObject> wardData = new ArrayList<>();

    private AccountDAO accountDAO;
    private DatabaseHelper dbHelper;

    // Interface để thông báo khi địa chỉ được cập nhật
    public interface OnAddressUpdatedListener {
        void onAddressUpdated(String newAddress);
    }

    private OnAddressUpdatedListener addressUpdatedListener;

    public void setOnAddressUpdatedListener(OnAddressUpdatedListener listener) {
        this.addressUpdatedListener = listener;
    }

    public static UpdateAddressFragment newInstance(int accountId) {
        UpdateAddressFragment fragment = new UpdateAddressFragment();
        Bundle args = new Bundle();
        args.putInt("acc_id", accountId);
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
            Log.e(TAG, "getContext() is null in onCreate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_address, container, false);

        spinnerCountry = view.findViewById(R.id.spinnerCountry);
        spinnerProvince = view.findViewById(R.id.spinnerProvince);
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict);
        spinnerWard = view.findViewById(R.id.spinnerWard);
        edtDetailAddress = view.findViewById(R.id.edtDetailAddress);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnCancel = view.findViewById(R.id.btnCancel); // Thêm nút Cancel

        requestQueue = Volley.newRequestQueue(getContext());

        Bundle args = getArguments();
        int accountId = args != null ? args.getInt("acc_id", -1) : -1;

        if (accountId == -1) {
            Log.e(TAG, "Invalid accountId: " + accountId);
            Toast.makeText(getContext(), "Invalid account ID", Toast.LENGTH_SHORT).show();
            dismiss();
            return view;
        }

        countryList.add("Việt Nam");
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        loadProvinces();

        spinnerProvince.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                districtList.clear();
                wardList.clear();
                spinnerDistrict.setAdapter(null);
                spinnerWard.setAdapter(null);
                if (position > 0) {
                    try {
                        String provinceId = provinceData.get(position - 1).getString("id");
                        Log.d(TAG, "Selected province id: " + provinceId);
                        loadDistricts(provinceId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error getting province id: " + e.getMessage());
                        Toast.makeText(getContext(), "Error getting province id: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        spinnerDistrict.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                wardList.clear();
                spinnerWard.setAdapter(null);
                if (position > 0) {
                    try {
                        String districtId = districtData.get(position - 1).getString("id");
                        loadWards(districtId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error getting district id: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Xử lý sự kiện cho nút Save
        btnUpdateProfile.setOnClickListener(v -> saveAddress(accountId));

        // Xử lý sự kiện cho nút Cancel
        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accountDAO != null) {
            accountDAO.close();
        }
    }

    private void loadProvinces() {
        String url = "https://esgoo.net/api-tinhthanh/1/0.htm";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    provinceList.clear();
                    provinceData.clear();
                    provinceList.add("Chọn tỉnh/thành");
                    try {
                        if (response.getInt("error") == 0) {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject province = data.getJSONObject(i);
                                provinceList.add(province.getString("name"));
                                provinceData.add(province);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, provinceList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerProvince.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error: " + response.getString("error_text"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON Error: " + e.getMessage());
                        Toast.makeText(getContext(), "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error loading provinces: " + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "VolleyError: " + error.toString());
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void loadDistricts(String provinceId) {
        String url = "https://esgoo.net/api-tinhthanh/2/" + provinceId + ".htm";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    districtList.clear();
                    districtData.clear();
                    districtList.add("Chọn quận/huyện");
                    try {
                        if (response.getInt("error") == 0) {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject district = data.getJSONObject(i);
                                districtList.add(district.getString("name"));
                                districtData.add(district);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districtList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerDistrict.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error: " + response.getString("error_text"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON Error: " + e.getMessage());
                        Toast.makeText(getContext(), "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error loading districts: " + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "VolleyError: " + error.toString());
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void loadWards(String districtId) {
        String url = "https://esgoo.net/api-tinhthanh/3/" + districtId + ".htm";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    wardList.clear();
                    wardData.clear();
                    wardList.add("Chọn phường/xã");
                    try {
                        if (response.getInt("error") == 0) {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject ward = data.getJSONObject(i);
                                wardList.add(ward.getString("name"));
                                wardData.add(ward);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, wardList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerWard.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Error: " + response.getString("error_text"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON Error: " + e.getMessage());
                        Toast.makeText(getContext(), "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error loading wards: " + error.toString(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "VolleyError: " + error.toString());
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void saveAddress(int accountId) {
        if (accountDAO == null) {
            Log.e(TAG, "accountDAO is null");
            Toast.makeText(getContext(), "Database connection failed", Toast.LENGTH_SHORT).show();
            return;
        }

        String country = spinnerCountry.getSelectedItem() != null ? spinnerCountry.getSelectedItem().toString() : "";
        String province = spinnerProvince.getSelectedItem() != null ? spinnerProvince.getSelectedItem().toString() : "";
        String district = spinnerDistrict.getSelectedItem() != null ? spinnerDistrict.getSelectedItem().toString() : "";
        String ward = spinnerWard.getSelectedItem() != null ? spinnerWard.getSelectedItem().toString() : "";
        String detailAddress = edtDetailAddress.getText().toString().trim();

        // Kiểm tra xem các trường có được điền đầy đủ không
        if (province.isEmpty() || district.isEmpty() || ward.isEmpty() || detailAddress.isEmpty()) {
            Toast.makeText(getContext(), "Please enter detail address!", Toast.LENGTH_SHORT).show();
            return; // Không đóng fragment, chỉ hiển thị thông báo
        }

        String fullAddress = detailAddress + "\n" + ward + ", " + district + ", " + province + ", " + country;

        accountDAO.open();
        Account currentAccount = accountDAO.getAccountById(accountId);
        if (currentAccount == null) {
            Log.e(TAG, "Account not found for ID: " + accountId);
            Toast.makeText(getContext(), "Account not found", Toast.LENGTH_SHORT).show();
            accountDAO.close();
            return;
        }

        currentAccount.setAddress(fullAddress);
        try {
            int rowsAffected = accountDAO.updateAccount(currentAccount);
            if (rowsAffected > 0) {
                Toast.makeText(getContext(), "Address updated successfully for account " + accountId, Toast.LENGTH_LONG).show();
                // Gửi địa chỉ mới về AddressFragment
                if (addressUpdatedListener != null) {
                    addressUpdatedListener.onAddressUpdated(fullAddress);
                }
                dismiss(); // Đóng fragment khi cập nhật thành công
            } else {
                Toast.makeText(getContext(), "Failed to update address", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating address: " + e.getMessage());
            Toast.makeText(getContext(), "Error updating address: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            accountDAO.close();
        }
    }
}