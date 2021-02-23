package jp.co.lfg.pricetarpartner.Http.Requester;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import jp.co.lfg.pricetarpartner.Http.ApiRequester;
import jp.co.lfg.pricetarpartner.System.Base64Utility;

public class RegisterUserRequester {

    public static void register(String email, String password, final Callback callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("command", "registerUser");
        params.put("email", Base64Utility.encode(email));
        params.put("password", Base64Utility.encode(password));

        ApiRequester.post(params, new ApiRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, JSONObject json) {
                if (result) {
                    try {
                        String apiResult = json.getString("result");
                        if (apiResult.equals("0")) {
                            String userId = json.getString("userId");
                            callback.didReceiveData(true, userId);
                            return;
                        }
                    } catch (JSONException e) {}
                }
                callback.didReceiveData(false, null);
            }
        });
    }

    public interface Callback {
        void didReceiveData(boolean result, String userId);
    }
}
