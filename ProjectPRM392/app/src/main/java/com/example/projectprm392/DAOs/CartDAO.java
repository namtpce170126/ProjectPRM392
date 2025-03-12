package com.example.projectprm392.DAOs;

import com.example.projectprm392.Database.DatabaseHelper;

public class CartDAO extends SingletonBaseDAO{
    public CartDAO(DatabaseHelper dbHelper){
        super(dbHelper);
    }
    public int deleteCartById(int cartId) {
        open();
        int result = db.delete("cart",
                "cart_id = ?",
                new String[]{String.valueOf(cartId)});
        close();
        return result;
    }
}
