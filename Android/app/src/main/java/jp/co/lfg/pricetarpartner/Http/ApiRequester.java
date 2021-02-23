package jp.co.lfg.pricetarpartner.Http;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.StringJoiner;

import jp.co.lfg.pricetarpartner.System.Constants;

public class ApiRequester {

    public static void post(HashMap<String, String> params, final Callback callback) {

        HttpRequester http = new HttpRequester(HttpRequester.RequestType.string, new HttpRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, Object data) {
                if (result) {
                    try {
                        JSONObject json = new JSONObject((String)data);
                        callback.didReceiveData(true, json);
                        return;
                    } catch (JSONException e) { }
                }
                callback.didReceiveData(false, null);
            }
        });

        StringJoiner paramJoiner = new StringJoiner("&");
        for (String key : params.keySet()) {
            paramJoiner.add(key + "=" + params.get(key));
        }
        http.execute(Constants.ServerApiUrl, "POST", paramJoiner.toString());
    }

    public interface Callback {
        void didReceiveData(boolean result, JSONObject json);
    }
}
