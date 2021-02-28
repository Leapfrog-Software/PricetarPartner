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

public class UpdateClientProfileRequester {

    public static void update(String nickanme, String area, String useFrequency, String newCondition, String oldCondition, ArrayList<String> genres, ArrayList<String> options, String message, Bitmap image, final Callback callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("command", "updateClientProfile");
        params.put("userId", SaveData.getInstance().userId);
        params.put("nickname", Base64Utility.encode(nickanme));
        params.put("area", Base64Utility.encode(area));
        params.put("useFrequency", Base64Utility.encode(useFrequency));
        params.put("newCondition", Base64Utility.encode(newCondition));
        params.put("oldCondition", Base64Utility.encode(oldCondition));

        StringJoiner genreJoiner = new StringJoiner(",");
        for (String genre : genres) {
            genreJoiner.add(Base64Utility.encode(genre));
        }
        params.put("genres", genreJoiner.toString());

        StringJoiner optionJoiner = new StringJoiner(",");
        for (String option : options) {
            optionJoiner.add(Base64Utility.encode(option));
        }
        params.put("options", optionJoiner.toString());

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
