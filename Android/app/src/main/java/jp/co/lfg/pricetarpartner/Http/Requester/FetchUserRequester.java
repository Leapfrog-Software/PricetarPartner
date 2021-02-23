package jp.co.lfg.pricetarpartner.Http.Requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import jp.co.lfg.pricetarpartner.Http.ApiRequester;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class FetchUserRequester {

    private static FetchUserRequester mRequester = new FetchUserRequester();

    private FetchUserRequester() {}

    public static FetchUserRequester getInstance() {
        return mRequester;
    }

    public ArrayList<UserData> dataList = new ArrayList<>();

    public void fetch(final Callback callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("command", "getUser");
        params.put("userId", SaveData.getInstance().userId);

        ApiRequester.post(params, new ApiRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, JSONObject json) {
                if (result) {
                    try {
                        String apiResult = json.getString("result");
                        if (apiResult.equals("0")) {
                            ArrayList<UserData> users = new ArrayList<>();
                            JSONArray userArray = json.getJSONArray("users");
                            for (int i = 0; i < userArray.length(); i++) {
                                UserData userData = UserData.create(userArray.getJSONObject(i));
                                if (userData != null) {
                                    users.add(userData);
                                }
                            }
                            dataList = users;
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch (JSONException e) {}
                }
                callback.didReceiveData(false);
            }
        });
    }

    public UserData query(String userId) {

        for (UserData userData : dataList) {
            if (userData.id.equals(userId)) {
                return userData;
            }
        }
        return null;
    }

    public interface Callback {
        void didReceiveData(boolean result);
    }
}
