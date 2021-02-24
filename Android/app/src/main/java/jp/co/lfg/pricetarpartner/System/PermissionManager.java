package jp.co.lfg.pricetarpartner.System;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import jp.co.lfg.pricetarpartner.MainActivity;

public class PermissionManager {

    public enum PermissionType {
        writeExternalStorage,
        camera;

        public int getRequestCode() {

            switch (this) {
                case writeExternalStorage:
                    return 1001;
                case camera:
                    return 1002;
            }
            return 0;
        }

        public String getPermissionCode() {

            switch (this) {
                case writeExternalStorage:
                    return Manifest.permission.WRITE_EXTERNAL_STORAGE;
                case camera:
                    return Manifest.permission.CAMERA;
            }
            return "";
        }
    }

    private static PermissionManager mInstance = null;
    private static MainActivity mActivity = null;

    private PermissionManager() {}

    public static PermissionManager getInstance() {

        if (mInstance == null) {
            mInstance = new PermissionManager();
        }
        return mInstance;
    }

    public static void initialize(MainActivity activity) {

        mActivity = activity;

        if (!getInstance().checkPermission(PermissionManager.PermissionType.writeExternalStorage)) {
            getInstance().requestPermission(PermissionManager.PermissionType.writeExternalStorage);
        }
    }

    public boolean checkPermission(PermissionType type) {
        return ActivityCompat.checkSelfPermission(mActivity, type.getPermissionCode()) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(PermissionType type) {
        ActivityCompat.requestPermissions(mActivity, new String[]{ type.getPermissionCode() }, type.getRequestCode());
    }

    public void onRequestPermissionsResult(int requestCode) {

        if (requestCode == PermissionType.writeExternalStorage.getRequestCode()) {
            GalleryManager.getInstance().didChangePermission();
        }
    }
}
