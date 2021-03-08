package jp.co.lfg.pricetarpartner.System;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;

import jp.co.lfg.pricetarpartner.MainActivity;

import static android.app.Activity.RESULT_OK;

public class GalleryManager {

    public enum SourceType {
        Camera,
        Gallery
    }

    public static int RequestCodeGallery = 1011;
    public static int RequestCodeCamera = 10012;

    private static GalleryManager mManager = new GalleryManager();
    private static MainActivity mActivity;
    private SourceType mSourceType;
    private Callback mCallback;
    private Uri mImageUri;
    private String mImagePath;

    private GalleryManager(){}

    public static void initialize(MainActivity activity) {
        mActivity = activity;
    }

    public static GalleryManager getInstance() {
        return mManager;
    }

    public void open(SourceType sourceType, Callback callback) {

        mCallback = callback;
        mSourceType = sourceType;

        if (!PermissionManager.getInstance().checkPermission(PermissionManager.PermissionType.writeExternalStorage)) {
            PermissionManager.getInstance().requestPermission(PermissionManager.PermissionType.writeExternalStorage);
            return;
        }
        if (!PermissionManager.getInstance().checkPermission(PermissionManager.PermissionType.camera)) {
            PermissionManager.getInstance().requestPermission(PermissionManager.PermissionType.camera);
            return;
        }

        // カメラ
        if (sourceType == SourceType.Camera) {
            String fileName = DateUtility.dateToString(new Date(), "yyyyMMddkkmmss");
            File file = null;
            try {
                File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = File.createTempFile(fileName, ".jpg", storageDir);
                mImagePath = file.getAbsolutePath();
            } catch (IOException e) {
                return;
            }
            if (file == null) {
                return;
            }
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoURI = FileProvider.getUriForFile(mActivity, "jp.co.lfg.pricetarpartner.fileprovider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            mActivity.startActivityForResult(intent, RequestCodeCamera);

            mImageUri = photoURI;
        }
        // ギャラリー
        else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            mActivity.startActivityForResult(intent, GalleryManager.RequestCodeGallery);
        }
    }

    public void didChangePermission() {

        if (PermissionManager.getInstance().checkPermission(PermissionManager.PermissionType.writeExternalStorage)) {
            if (PermissionManager.getInstance().checkPermission(PermissionManager.PermissionType.camera)) {
                if ((mSourceType != null) && (mCallback != null)) {
                    open(mSourceType, mCallback);
                }
            } else {
                PermissionManager.getInstance().requestPermission(PermissionManager.PermissionType.camera);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if ((requestCode == RequestCodeGallery) || (requestCode == RequestCodeCamera)) {
                didSelectImage(data);
            }
        }
    }

    public void didSelectImage(Intent data) {

        Uri uri = null;
        Bitmap bitmap = null;

        // カメラ
        if (mSourceType == SourceType.Camera) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inJustDecodeBounds = false;
            options.inSampleSize = 1;
            options.inPurgeable = true;

            uri = mImageUri;
            bitmap = BitmapFactory.decodeFile(mImagePath, options);

            // Xperia2.1
            if (bitmap == null) {
                uri = data.getData();
                if (uri != null) {
                    bitmap = BitmapFactory.decodeFile(uri.toString(), options);
                }
            }
        }
        // ギャラリー
        else {
            uri = data.getData();
            if (uri != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);
                } catch (IOException e) {}
            }
        }

        if ((uri != null) && (bitmap != null)) {
            Bitmap rotatedBitmap = rotateIfNeeded(uri, bitmap);
            if (rotatedBitmap != null) {
                mCallback.didSelectImage(rotatedBitmap);
            }
        }

        mSourceType = null;
        mCallback = null;
    }

    private Bitmap rotateIfNeeded(Uri uri, Bitmap bitmap) {

        if (Build.VERSION.SDK_INT < 24) {
            return bitmap;
        }

        try {
            ParcelFileDescriptor parcelFileDescriptor = mActivity.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            ExifInterface ei = new ExifInterface(fileDescriptor);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            parcelFileDescriptor.close();

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateBitmap(bitmap, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateBitmap(bitmap, 190);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateBitmap(bitmap, 270);
                default:
                    return bitmap;
            }
        } catch (IOException e) {
            return bitmap;
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degree) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotatedImg;
    }

    public interface Callback {
        void didSelectImage(Bitmap bitmap);
    }
}

