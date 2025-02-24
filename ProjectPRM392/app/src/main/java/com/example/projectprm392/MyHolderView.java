package com.example.projectprm392;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolderView extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameView, name2View,priceView,price2View, quantityView,quantity2View;
    public MyHolderView(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.hinhHangInCart);
        nameView = itemView.findViewById(R.id.txtTenMonHang);
        name2View = itemView.findViewById(R.id.txtTenMonHang1);
        quantityView = itemView.findViewById(R.id.soluongInCart);
        quantity2View = itemView.findViewById(R.id.soLuong);
        priceView = itemView.findViewById(R.id.priceInCart);
        price2View = itemView.findViewById(R.id.priceInCart);
    }
}
