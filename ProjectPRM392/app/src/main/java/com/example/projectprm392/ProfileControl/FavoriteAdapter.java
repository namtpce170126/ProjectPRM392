package com.example.projectprm392.ProfileControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.DAOs.CartDAOU;
import com.example.projectprm392.DAOs.CategoryDAO;
import com.example.projectprm392.DAOs.FavoriteDAO;
import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Cart;
import com.example.projectprm392.Models.Category;
import com.example.projectprm392.Models.Product;
import com.example.projectprm392.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<Product> favoriteProducts;
    private CategoryDAO categoryDAO;
    private CartDAOU cartDAO;
    private FavoriteDAO favoriteDAO;
    private Map<Integer, Category> categoryCache = new HashMap<>();
    private Context context;
    private OnFavoriteRemovedListener onFavoriteRemovedListener; // Listener để thông báo khi xóa favorite

    // Constructor cập nhật với listener
    public FavoriteAdapter(Context context, List<Product> favoriteProducts, DatabaseHelper dbHelper, OnFavoriteRemovedListener listener) {
        this.context = context;
        this.favoriteProducts = favoriteProducts;
        this.categoryDAO = new CategoryDAO(dbHelper);
        this.cartDAO = new CartDAOU(dbHelper);
        this.favoriteDAO = new FavoriteDAO(dbHelper);
        this.onFavoriteRemovedListener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Product product = favoriteProducts.get(position);
        holder.txtProductName.setText(product.getProName());
        holder.txtProductPrice.setText(String.format("%.2f VND", product.getProPrice()));

        // Kiểm tra cache trước khi truy vấn danh mục
        Category category = categoryCache.get(product.getCatId());
        if (category == null) {
            category = categoryDAO.getOne(product.getCatId(), 0);
            if (category != null) {
                categoryCache.put(product.getCatId(), category);
            }
        }
        holder.txtProductCategory.setText(category != null ? category.getCatName() : "Unknown Category");

        // Xử lý nút Add to Cart
        holder.btnAddToCart.setOnClickListener(v -> addToCart(product));

        // Xử lý nút Hủy Favorite
        holder.btnAddToFavorite.setOnClickListener(v -> removeFromFavorite(product, position));
    }

    private void addToCart(Product product) {
        int customerId = 1; // Giả sử customerId = 1, thay đổi theo logic thực tế
        int stockQuantity = product.getProQuantity();

        Cart existingCartItem = cartDAO.getCartItemByProductId(customerId, product.getProId());

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getProQuantity() + 1;
            if (newQuantity > stockQuantity) {
                Toast.makeText(context, "Không đủ hàng trong kho! Chỉ còn " + stockQuantity + " sản phẩm.", Toast.LENGTH_SHORT).show();
                return;
            }
            existingCartItem.setProQuantity(newQuantity);
            existingCartItem.setCartPrice(newQuantity * product.getProPrice());
            boolean success = cartDAO.updateCart(existingCartItem);
            Toast.makeText(context, success ? "Cập nhật giỏ hàng thành công!" : "Lỗi khi cập nhật giỏ hàng!", Toast.LENGTH_SHORT).show();
        } else {
            if (stockQuantity < 1) {
                Toast.makeText(context, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
                return;
            }
            Cart cartItem = new Cart(0, customerId, product.getProId(), 1, product.getProPrice());
            boolean success = cartDAO.addToCart(cartItem);
            Toast.makeText(context, success ? "Đã thêm vào giỏ hàng!" : "Lỗi khi thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromFavorite(Product product, int position) {
        int accountId = 1; // Giả sử accountId = 1, thay đổi theo logic thực tế
        int result = favoriteDAO.deleteFavorite(accountId, product.getProId());
        if (result > 0) {
            favoriteProducts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteProducts.size());
            Toast.makeText(context, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
            if (onFavoriteRemovedListener != null) {
                onFavoriteRemovedListener.onFavoriteRemoved();
            }
        } else {
            Toast.makeText(context, "Lỗi khi xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return favoriteProducts.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtProductCategory;
        ImageView imgProduct;
        ImageButton btnAddToCart;
        ImageView btnAddToFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductCategory = itemView.findViewById(R.id.txtProductCategory);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnAddToFavorite = itemView.findViewById(R.id.btnAddToFavorite);
        }
    }

    // Interface để thông báo khi xóa favorite
    public interface OnFavoriteRemovedListener {
        void onFavoriteRemoved();
    }
}