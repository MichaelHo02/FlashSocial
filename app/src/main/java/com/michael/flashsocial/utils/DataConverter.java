package com.michael.flashsocial.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DataConverter {
    public static byte[] convertImageToByteArr(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap convertByteArrToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] compressImage(byte[] imageToBeCompressed) {
        byte[] compressImage = imageToBeCompressed;
        while (compressImage.length > 500000) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(compressImage, 0, compressImage.length);
            Bitmap resized = Bitmap.createScaledBitmap(
                    bitmap,
                    (int) (bitmap.getWidth() * 0.8),
                    (int) (bitmap.getHeight() * 0.8), false
            );
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            compressImage = stream.toByteArray();
        }
        return compressImage;
    }
}
