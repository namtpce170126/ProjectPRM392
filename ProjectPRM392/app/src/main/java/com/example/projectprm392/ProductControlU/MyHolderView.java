package com.example.projectprm392.ProductControlU;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;

public class MyHolderView extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameView,price1View, priceTotalView,quantityView,quantityUpDownView,TongCartCheck;
    CheckBox checkBox;

    ImageButton imageButtonIncrease, imageButtonDecrease;
    public MyHolderView(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.hinhHangInCart);
        nameView = itemView.findViewById(R.id.txtTenMonHang);

        quantityView = itemView.findViewById(R.id.soluongInCart);
        quantityUpDownView = itemView.findViewById(R.id.soLuong);
        priceTotalView = itemView.findViewById(R.id.totalPriceInCart);
        price1View = itemView.findViewById(R.id.txtPrice1);

        checkBox = itemView.findViewById(R.id.checkBox);
        imageButtonIncrease = itemView.findViewById(R.id.imageButton);
        imageButtonDecrease = itemView.findViewById(R.id.imageButton3);
    }
}
