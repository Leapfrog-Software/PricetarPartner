package jp.co.lfg.pricetarpartner.Http.DataModel;

import org.json.JSONObject;

import java.util.Date;
import java.util.StringJoiner;

import jp.co.lfg.pricetarpartner.System.Base64Utility;
import jp.co.lfg.pricetarpartner.System.Constants;
import jp.co.lfg.pricetarpartner.System.DateUtility;

public class ChatData {

    public enum ChatType {
        message,
        image;

        public static ChatType create(String value) {

            if (value.equals("0")) {
                return message;
            } else if (value.equals("1")) {
                return image;
            }
            return null;
        }
    }

    public String id;
    public String groupId;
    public String senderId;
    public String targetId;
    public Date datetime;
    public ChatType type;
    public String message;

    public static ChatData create(JSONObject json) {

        try {
            ChatData chatData = new ChatData();

            chatData.id = json.getString("id");
            chatData.groupId = json.getString("groupId");
            chatData.senderId = json.getString("senderId");
            chatData.targetId = json.getString("targetId");

            chatData.datetime = DateUtility.stringToDate(json.getString("datetime"), "yyyyMMddkkmmss");
            if (chatData.datetime == null) {
                return null;
            }

            chatData.type = ChatType.create(json.getString("type"));
            if (chatData.type == null) {
                return null;
            }

            chatData.message = Base64Utility.decode(json.getString("message"));

            return chatData;

        } catch (Exception e) {}

        return null;
    }

    public String getImageUrl() {
        return Constants.ServerRootUrl + "data/chat/" + this.groupId + "/" + this.id;
    }

    public String getDisplayDatetime() {

        String dateStr = DateUtility.dateToString(this.datetime, "yyyy/M/d");
        int hour = Integer.parseInt(DateUtility.dateToString(this.datetime, "kk"));
        String minute = DateUtility.dateToString(this.datetime, "mm");
        String timeStr = (hour < 12) ? (String.valueOf(hour) + ":" + minute + " AM") : (String.valueOf(hour - 12) + ":" + minute + " PM");
        return dateStr + ", " + timeStr;
    }
}
