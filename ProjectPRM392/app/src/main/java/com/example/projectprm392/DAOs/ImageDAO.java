package com.example.projectprm392.DAOs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.projectprm392.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDAO {
    public static String uploadImage(Context context, Bitmap bitmap, String fileName) {
        try {
            // Lấy thư mục lưu file trong bộ nhớ nội bộ
            File directory = context.getFilesDir();
            File imageFile = new File(directory, fileName);

            // Ghi dữ liệu ảnh vào file
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Nén ảnh PNG chất lượng cao
            fos.flush();
            fos.close();

            // Trả về tên file để lưu vào database
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Lưu thất bại
        }
    }

    public static Bitmap getImageFromDatabase(Context context, String fileName) {
        try {
            // Lấy thư mục lưu file trong bộ nhớ nội bộ
            File directory = context.getFilesDir();
            File imageFile = new File(directory, fileName);

            // Kiểm tra file có tồn tại không
            if (!imageFile.exists()) {
                return null;
            }
            // Đọc file và chuyển thành Bitmap
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu lỗi
        }
    }

    // Hàm upload ảnh mặc định vào bộ nhớ nếu chưa có
    public static void uploadDefaultImage(Context context) {
        File defaultImageFile = new File(context.getFilesDir(), "default_food.png");
        if (!defaultImageFile.exists()) { // Nếu chưa có, thì lưu vào bộ nhớ
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_food);
                FileOutputStream fos = new FileOutputStream(defaultImageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
