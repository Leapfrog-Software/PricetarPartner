package jp.co.lfg.pricetarpartner.Http.Requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.lfg.pricetarpartner.Http.ApiRequester;
import jp.co.lfg.pricetarpartner.Http.DataModel.ChatGroupData;
import jp.co.lfg.pricetarpartner.Http.DataModel.UserData;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class FetchChatGroupRequester {

    private static FetchChatGroupRequester mRequester = new FetchChatGroupRequester();

    private FetchChatGroupRequester() {}

    public static FetchChatGroupRequester getInstance() {
        return mRequester;
    }

    public ArrayList<ChatGroupData> dataList = new ArrayList<>();

    public void fetch(final Callback callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("command", "getChatGroup");
        params.put("userId", SaveData.getInstance().userId);

        ApiRequester.post(params, new ApiRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, JSONObject json) {
                if (result) {
                    try {
                        String apiResult = json.getString("result");
                        if (apiResult.equals("0")) {
                            ArrayList<ChatGroupData> chatGroups = new ArrayList<>();
                            JSONArray chatGroupArray = json.getJSONArray("chatGroups");
                            for (int i = 0; i < chatGroupArray.length(); i++) {
                                ChatGroupData chatGroupData = ChatGroupData.create(chatGroupArray.getJSONObject(i));
                                if (chatGroupData != null) {
                                    chatGroups.add(chatGroupData);
                                }
                            }
                            dataList = chatGroups;
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
