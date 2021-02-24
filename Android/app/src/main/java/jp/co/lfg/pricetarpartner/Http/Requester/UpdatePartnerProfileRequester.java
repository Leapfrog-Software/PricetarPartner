package jp.co.lfg.pricetarpartner.Http.Requester;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import jp.co.lfg.pricetarpartner.Http.ApiRequester;
import jp.co.lfg.pricetarpartner.System.Base64Utility;
import jp.co.lfg.pricetarpartner.System.BitmapUtility;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class UpdatePartnerProfileRequester {

    public static void update(String nickname, String area, String career, String status, String newPrice, String oldPrice, String dangerousPrice, String dangerousMessage, String bigPrice, String bigMessage, String inspectionPrice, String inspectionMessage, String message, Bitmap image, final Callback callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("command", "updatePartnerProfile");
        params.put("userId", SaveData.getInstance().userId);
        params.put("nickname", Base64Utility.encode(nickname));
        params.put("area", Base64Utility.encode(area));
        params.put("career", Base64Utility.encode(career));
        params.put("status", Base64Utility.encode(status));
        params.put("newPrice", Base64Utility.encode(newPrice));
        params.put("oldPrice", Base64Utility.encode(oldPrice));
        params.put("dangerousPrice", Base64Utility.encode(dangerousPrice));
        params.put("dangerousMessage", Base64Utility.encode(dangerousMessage));
        params.put("bigPrice", Base64Utility.encode(bigPrice));
        params.put("bigMessage", Base64Utility.encode(bigMessage));
        params.put("inspectionPrice", Base64Utility.encode(inspectionPrice));
        params.put("inspectionMessage", Base64Utility.encode(inspectionMessage));
        params.put("message", Base64Utility.encode(message));

        if (image != null) {
            params.put("image", BitmapUtility.getBase64EncodedString(image));
        } else {
            params.put("image", "");
        }

        ApiRequester.post(params, new ApiRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, JSONObject json) {
                if (result) {
                    try {
                        String apiResult = json.getString("result");
                        if (apiResult.equals("0")) {
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch (JSONException e) {}
                }
                callback.didReceiveData(false);
            }
        });
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}
