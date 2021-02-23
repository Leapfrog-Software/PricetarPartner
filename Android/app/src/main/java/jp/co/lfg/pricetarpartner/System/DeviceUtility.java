package jp.co.lfg.pricetarpartner.System;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import jp.co.lfg.pricetarpartner.MainActivity;

import static android.content.Context.WINDOW_SERVICE;

public class DeviceUtility {

    private static MainActivity mActivity;
    private static Point mWindowSize = null;
    private static float mDensity = -1;

    public static void initialize(MainActivity activity) {

        mActivity = activity;

        WindowManager wm = (WindowManager)activity.getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        mWindowSize = size;

        mDensity = activity.getResources().getDisplayMetrics().density;
    }

    public static Point getWindowSize() {
        return mWindowSize;
    }

    public static float getDeviceDensity(){
        return mDensity;
    }

    public static int getStatusBarHeight(Activity activity) {

        Window window = activity.getWindow();
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
