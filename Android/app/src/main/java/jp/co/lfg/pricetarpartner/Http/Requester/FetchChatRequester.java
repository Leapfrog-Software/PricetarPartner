package jp.co.lfg.pricetarpartner.Http.Requester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.lfg.pricetarpartner.Http.ApiRequester;
import jp.co.lfg.pricetarpartner.Http.DataModel.ChatData;
import jp.co.lfg.pricetarpartner.System.SaveData;

public class FetchChatRequester {

    public static void fetch(String targetId, final Callback callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("command", "getChat");
        params.put("userId1", SaveData.getInstance().userId);
        params.put("userId2", targetId);

        ApiRequester.post(params, new ApiRequester.Callback() {
            @Override
            public void didReceiveData(boolean result, JSONObject json) {
                if (result) {
                    try {
                        String apiResult = json.getString("result");
                        if (apiResult.equals("0")) {
                            ArrayList<ChatData> chats = new ArrayList<>();
                            JSONArray chatsArray = json.getJSONArray("chats");
                            for (int i = 0; i < chatsArray.length(); i++) {
                                ChatData chatData = ChatData.create(chatsArray.getJSONObject(i));
                                if (chatData != null) {
                                    chats.add(chatData);
                                }
                            }
                            callback.didReceiveData(true, chats);
                            return;
                        }
                    } catch (JSONException e) {}
                }
                callback.didReceiveData(false, null);
            }
        });
    }

    public interface Callback {
        void didReceiveData(boolean result, ArrayList<ChatData> chats);
    }
}
