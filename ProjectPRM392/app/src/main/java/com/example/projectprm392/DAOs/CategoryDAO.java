package com.example.projectprm392.DAOs;

import android.database.Cursor;

import com.example.projectprm392.Database.DatabaseHelper;
import com.example.projectprm392.Models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends SingletonBaseDAO{
    public CategoryDAO(DatabaseHelper dbHelper) {
        super(dbHelper);
    }

    public List<Category> getAll() {
        open();
        String query = "SELECT * FROM category WHERE is_deleted = 0";
        List<Category> listCategory = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                Category category = new Category(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                );
                listCategory.add(category);
            } while(cursor.moveToNext());
        }
        cursor.close();
        close();
        return listCategory;
    }

    public Category getOne(int categoryId, int isActived) {
        open();
        Category category = null;
        String query = "SELECT cat_id, cat_name, cat_description, is_deleted FROM category WHERE cat_id = ? AND is_deleted = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId), String.valueOf(isActived)});

        if(cursor.moveToFirst()) {
            category = new Category(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3)
            );
        }
        cursor.close();
        close();
        return category;
    }
}
