package jp.co.lfg.pricetarpartner.Http.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;

import jp.co.lfg.pricetarpartner.System.Base64Utility;
import jp.co.lfg.pricetarpartner.System.DateUtility;

public class ChatGroupData {

    public String id;
    public String userId1;
    public String userId2;
    public int unreadCount1;
    public int unreadCount2;

    public static ChatGroupData create(JSONObject json) {

        try {
            ChatGroupData chatGroupData = new ChatGroupData();

            chatGroupData.id = json.getString("id");
            chatGroupData.userId1 = json.getString("userId1");
            chatGroupData.userId2 = json.getString("userId2");
            chatGroupData.unreadCount1 = Integer.parseInt(json.getString("unreadCount1"));
            chatGroupData.unreadCount2 = Integer.parseInt(json.getString("unreadCount2"));

            return chatGroupData;

        } catch (Exception e) {}

        return null;
    }
}
