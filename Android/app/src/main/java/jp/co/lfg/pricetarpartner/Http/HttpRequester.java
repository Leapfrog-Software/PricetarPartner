package jp.co.lfg.pricetarpartner.Http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.SyncStateContract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.co.lfg.pricetarpartner.System.Constants;

public class HttpRequester extends AsyncTask<String, Integer, Object> {

    public enum RequestType {
        string,
        image
    }

    private RequestType mRequestType;
    private Callback mCallback = null;

    public HttpRequester(RequestType requestType, Callback callback) {
        mRequestType = requestType;
        mCallback = callback;
    }

    @Override
    protected Object doInBackground(String... params) {

        HttpURLConnection conn = null;
        Object object = null;

        try {
            URL url = new URL(params[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Constants.HttpReadTimeout);
            conn.setConnectTimeout(Constants.HttpConnectTimeout);
            conn.setUseCaches(false);
            conn.setRequestMethod(params[1]);

            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (params.length >= 3) {
                String paramStr = params[2];
                PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
                printWriter.print(paramStr);
                printWriter.close();
            }

            conn.connect();
            int resp = conn.getResponseCode();
            if ((int) (resp / 100) == 2) {
                InputStream stream = conn.getInputStream();

                if (mRequestType == RequestType.string) {
                    object = streamToString(stream);
                } else {
                    object = streamToBitmap(stream);
                }
                stream.close();
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return object;
    }

    @Override
    protected void onPostExecute(Object data) {
        super.onPostExecute(data);

        if (data != null) {
            mCallback.didReceiveData(true, data);
        } else {
            mCallback.didReceiveData(false, null);
        }
    }

    private String streamToString(InputStream stream) {

        try {
            StringBuffer buffer = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader bufReder = new BufferedReader(reader);
            String line = "";
            while ((line = bufReder.readLine()) != null) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }
                buffer.append(line);
            }
            return buffer.toString();

        } catch (Exception e) {
            return null;
        }
    }

    private Bitmap streamToBitmap(InputStream stream) {

        try {
            return BitmapFactory.decodeStream(stream);
        } catch (Exception e) {
            return null;
        }
    }

    public interface Callback {
        void didReceiveData(boolean result, Object data);
    }
}