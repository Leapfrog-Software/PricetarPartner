package jp.co.lfg.pricetarpartner.System;

import android.graphics.Bitmap;

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
}
