package jp.co.lfg.pricetarpartner.Http;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;

import jp.co.lfg.pricetarpartner.MainActivity;
import jp.co.lfg.pricetarpartner.System.Base64Utility;
import jp.co.lfg.pricetarpartner.System.DateUtility;

public class ImageStorage {

    private static class RequestData {
        public String url;
        public ImageView imageView;
        public int errorImageId;

        public static RequestData create(String url, ImageView imageView, int errorImageId) {

            RequestData requestData = new RequestData();
            requestData.url = url;
            requestData.imageView = imageView;
            requestData.errorImageId = errorImageId;
            return requestData;
        }
    }

    private static class CacheData {
        public String url;
        public Bitmap bitmap;
        public Date datetime;

        public static CacheData create(String url, Bitmap bitmap, Date datetime) {

            CacheData cacheData = new CacheData();
            cacheData.url = url;
            cacheData.bitmap = bitmap;
            cacheData.datetime = datetime;
            return cacheData;
        }
    }

    public static int RequestCodeImageStorage = 1005;
    private static int expirationInterval = 5 * 60;     // 5分

    private static MainActivity mActivity;
    private static ImageStorage mInstance;

    private ArrayList<RequestData> mRequests = new ArrayList<>();
    private ArrayList<CacheData> mCaches = new ArrayList<>();

    public static void initialize(MainActivity mainActivity) {

        mActivity = mainActivity;
        getInstance().checkPermission();
    }

    public boolean checkPermission() {

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCodeImageStorage);
            return false;
        }
        return true;
    }

    public static ImageStorage getInstance() {

        if (mInstance == null) {
            mInstance = new ImageStorage();
        }
        return mInstance;
    }

    public void fetch(final String url, final ImageView imageView, int errorImageId) {
        fetch(url, imageView, errorImageId, null);
    }

    public void fetch(final String url, final ImageView imageView, int errorImageId, final Callback callback) {

        cancelRequest(imageView);

        for (int i = 0; i < mCaches.size(); i++) {
            CacheData cacheData = mCaches.get(i);
            if (cacheData.url.equals(url)) {
                if (DateUtility.addSecond(new Date(), -1 * expirationInterval).before(cacheData.datetime)) {
                    imageView.setImageBitmap(cacheData.bitmap);
                    if (callback != null) {
                        callback.didReceiveData(true, cacheData.bitmap);
                    }
                    return;
                }
            }
        }

        mRequests.add(RequestData.create(url, imageView, errorImageId));

        readLocalFile(url, new DidEndProcedureCallback() {
            @Override
            public void didEnd(Bitmap bitmap) {
                if (bitmap != null) {
                    applyResult(url, imageView, bitmap, callback);
                } else {
                    fetchRemoteFile(url, new DidEndProcedureCallback() {
                        @Override
                        public void didEnd(Bitmap bitmap) {
                            applyResult(url, imageView, bitmap, callback);
                        }
                    });
                }
            }
        });
    }

    public void query(String url, final Callback callback) {

        readLocalFile(url, new DidEndProcedureCallback() {
            @Override
            public void didEnd(Bitmap bitmap) {
                callback.didReceiveData((bitmap != null), bitmap);
            }
        });
    }

    public void deleteLocalFile(String url) {

        File file = createLocalPath(url);
        file.delete();
    }

    private void applyResult(final String url, final ImageView imageView, final Bitmap bitmap, final Callback callback) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (RequestData requestData : mRequests) {
                    if (requestData.url.equals(url)) {
                        if (bitmap != null) {
                            requestData.imageView.setImageBitmap(bitmap);
                        } else {
                            requestData.imageView.setImageResource(requestData.errorImageId);
                        }
                    }
                }

                cancelRequest(imageView);

                if (callback != null) {
                    callback.didReceiveData((bitmap != null), bitmap);
                }

                if (bitmap != null) {
                    int cacheCount = mCaches.size();
                    for (int i = cacheCount - 1; i >= 0; i--) {
                        CacheData cacheData = mCaches.get(i);
                        if (DateUtility.addSecond(new Date(), -1 * expirationInterval).after(cacheData.datetime)) {
                            mCaches.remove(i);
                        }
                    }
                    if (mCaches.size() > 50) {
                        mCaches.remove(0);
                    }
                    mCaches.add(CacheData.create(url, bitmap, new Date()));
                }
            }
        });
    }

    private File getRootDirectory() {
        return mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    private File createLocalPath(String url) {
        return new File(getRootDirectory(), Base64Utility.encode(url));
    }

    private void readLocalFile(final String url, final DidEndProcedureCallback callback) {

        if (!checkPermission()) {
            callback.didEnd(null);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = null;
                try {
                    File localPath = createLocalPath(url);
                    BasicFileAttributes attrs = Files.readAttributes(localPath.toPath(), BasicFileAttributes.class);
                    long creationTime = attrs.creationTime().toMillis();
                    long diff = (new Date().getTime() - creationTime) / 1000;
                    if (diff > 60 * 60) {
                        deleteLocalFile(url);
                        callback.didEnd(null);
                        return;
                    }

                    InputStream inputStream = new FileInputStream(localPath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {}

                callback.didEnd(bitmap);
            }
        }).start();
    }

    private void saveLocalFile(final String url, final Bitmap bitmap, final DidEndProcedureCallback callback) {

        if (!checkPermission()) {
            callback.didEnd(null);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream outputStream = new FileOutputStream(createLocalPath(url));
                    boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    if (result) {
                        callback.didEnd(bitmap);
                    } else {
                        callback.didEnd(null);
                    }
                } catch (Exception e) {
                    callback.didEnd(null);
                }
            }
        }).start();
    }

    private void fetchRemoteFile(final String url, final DidEndProcedureCallback callback) {

        HttpRequester http = new HttpRequester(HttpRequester.RequestType.image, new HttpRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, Object data) {
                if (result && (data != null) && (data instanceof Bitmap)) {
                    saveLocalFile(url, (Bitmap)data, callback);
                } else {
                    callback.didEnd(null);
                }
            }
        });
        http.execute(url, "GET");
    }

    public void cancelRequest(ImageView imageView) {

        for (int i = mRequests.size() - 1; i >= 0; i--) {
            if (mRequests.get(i).imageView == imageView) {
                mRequests.remove(i);
            }
        }
    }

    public void removeAll() {

        File rootDir = getRootDirectory();
        for (File file : rootDir.listFiles()) {
            file.delete();
        }

        mCaches.clear();
    }

    private interface DidEndProcedureCallback {
        void didEnd(Bitmap bitmap);
    }

    public interface Callback {
        void didReceiveData(boolean result, Bitmap bitmap);
    }
}
