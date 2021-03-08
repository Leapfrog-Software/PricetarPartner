package jp.co.lfg.pricetarpartner.System;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapUtility {

    public static Bitmap createUserBitmap(Bitmap bitmap) {

        int width, height;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth > bitmapHeight) {
            width = 800;
            height = 800 * bitmapHeight / bitmapWidth;
        } else {
            width = 800 * bitmapWidth / bitmapHeight;
            height = 800;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static Bitmap createChatBitmap(Bitmap bitmap) {

        int width, height;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth > bitmapHeight) {
            width = 500;
            height = 500 * bitmapHeight / bitmapWidth;
        } else {
            width = 500 * bitmapWidth / bitmapHeight;
            height = 500;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public static String getBase64EncodedString(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
