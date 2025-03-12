package com.example.projectprm392.OrderControlX;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.ImageDAO;
import com.example.projectprm392.DAOs.OrderDAOX;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.List;

public class OrderMyAdapter extends RecyclerView.Adapter<OrderMyViewHolder> {

    private Context context;
    private List<Product> productList;
    private  List<Cart> cartList;
    private OrderDAOX orderDAO;
    private double updatedPrice;
    // Khởi tạo database

    public OrderMyAdapter(Context context, List<Product> productList, List<Cart> cartList) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.context = context;
        this.productList = productList;
        this.cartList = cartList;
        this.orderDAO = new OrderDAOX(dbHelper);
    }

    @NonNull
    @Override
    public OrderMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderMyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_order, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull OrderMyViewHolder holder, int position) {
        Product product = productList.get(position);

        // Tìm số lượng sản phẩm trong giỏ hàng
        int quantity = 0;

        for (Cart cart : cartList) {
            if (cart.getProId() == product.getProId()) {
                quantity = cart.getProQuantity();

                break;
            }
        }
        String quantityCart = quantity +"";
        holder.txtProductName.setText(product.getProName());
//      quantity thiếu
        holder.txtQuantity.setText(quantityCart);
        holder.txtPrice.setText(String.format("%.2f", product.getProPrice()*quantity));


        // Kiểm tra và hiển thị ảnh sản phẩm
        String imageName = product.getProImage();
        if (imageName != null && !imageName.isEmpty()) {
            if (imageName.equals("default_food.png")) {
                // Nếu là ảnh mặc định, hiển thị từ drawable
                holder.imageProduct.setImageResource(R.drawable.default_food);
            } else {
                // Nếu là ảnh thật, load từ bộ nhớ trong
                Bitmap productImage = ImageDAO.getImageFromDatabase(holder.itemView.getContext(), imageName);
                if (productImage != null) {
                    holder.imageProduct.setImageBitmap(productImage);
                } else {
                    // Nếu file bị mất hoặc không tồn tại, hiển thị ảnh mặc định
                    holder.imageProduct.setImageResource(R.drawable.default_food);
                }
            }
        } else {
            // Nếu database NULL hoặc trống, hiển thị ảnh mặc định
            holder.imageProduct.setImageResource(R.drawable.default_food);
        }


        // Xử lý khi bấm nút "+"
        holder.btnAdd.setOnClickListener(v -> {
            updateCart(product, 1, holder.txtQuantity, holder.txtPrice);
        });

        // Xử lý khi bấm nút "-"
        holder.btnDelete.setOnClickListener(v -> {
            updateCart(product, -1, holder.txtQuantity, holder.txtPrice);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }



    private void updateCart(Product product, int change, TextView txtQuantity, TextView txtPrice) {
        int customerId = 1; // ID người dùng đang đăng nhập

        // Cập nhật số lượng sản phẩm trong database
        orderDAO.updateCartQuantity(customerId, product.getProId(), change);

        // Lấy lại danh sách giỏ hàng mới từ database
        cartList = orderDAO.getCartsByAccountId(customerId);

        // Tìm giỏ hàng của sản phẩm này để cập nhật giao diện
        int updatedQuantity = 0;
        double updatedPrice = 0.0;

        for (Cart cart : cartList) {
            if (cart.getProId() == product.getProId()) {
                updatedQuantity = cart.getProQuantity();
                updatedPrice = cart.getCartPrice(); // Lấy giá đã cập nhật từ database
                break;
            }
        }

        // Cập nhật lại giao diện
        txtQuantity.setText(String.valueOf(updatedQuantity));
        txtPrice.setText(String.format("%.2f", updatedPrice)); // Cập nhật giá tiền đúng
        // ✅ Gọi trực tiếp hàm cập nhật trong MainActivity2
        OrderActivity.updateTotalPrice();
    }

}
