package jp.co.lfg.pricetarpartner.Http.Requester;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import jp.co.lfg.pricetarpartner.Http.ApiRequester;
import jp.co.lfg.pricetarpartner.System.Base64Utility;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class PostChatMessageRequester {

    public static void post(String targetId, String message, final Callback callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("command", "postChatMessage");
        params.put("senderId", SaveData.getInstance().userId);
        params.put("targetId", targetId);
        params.put("message", Base64Utility.encode(message));

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
