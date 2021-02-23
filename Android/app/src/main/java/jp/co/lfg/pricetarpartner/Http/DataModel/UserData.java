package jp.co.lfg.pricetarpartner.Http.DataModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import jp.co.lfg.pricetarpartner.System.Base64Utility;
import jp.co.lfg.pricetarpartner.System.DateUtility;

public class UserData {

    public enum ProfileType {
        none,
        client,
        partner;

        public static ProfileType create(String value) {

            if (value.equals("0")) {
                return none;
            } else if (value.equals("1")) {
                return client;
            } else if (value.equals("2")) {
                return partner;
            } else {
                return null;
            }
        }

        public String toValue() {

            if (this == none) {
                return "0";
            } else if (this == client) {
                return "1";
            } else {
                return "2";
            }
        }
    }

    public String id;
    public String nickname;
    public String area;
    public ProfileType profileType;
    public String clientUseFrequency;
    public String clientCondition;
    public ArrayList<String> clientGenres;
    public String clientMessage;
    public String partnerCareer;
    public String partnerStatus;
    public String partnerNewPrice;
    public String partnerOldPrice;
    public String partnerDangerousPrice;
    public String partnerDangerousMessage;
    public String partnerBigPrice;
    public String partnerBigMessage;
    public String partnerInspectionPrice;
    public String partnerInspectionMessage;
    public String partnerMessage;
    public Date loginDatetime;

    public static UserData create(JSONObject json) {

        try {
            UserData userData = new UserData();

            userData.id = json.getString("id");
            userData.nickname = Base64Utility.decode(json.getString("nickname"));
            userData.area = Base64Utility.decode(json.getString("area"));

            userData.profileType = ProfileType.create(json.getString("profileType"));
            if (userData.profileType == null) {
                return null;
            }

            userData.clientUseFrequency = Base64Utility.decode(json.getString("clientUseFrequency"));
            userData.clientCondition = Base64Utility.decode(json.getString("clientCondition"));

            userData.clientGenres = new ArrayList<>();
            String[] clientGenres = json.getString("clientGenres").split(",");
            for (int i = 0; i < clientGenres.length; i++) {
                userData.clientGenres.add(Base64Utility.decode(clientGenres[i]));
            }

            userData.clientMessage = Base64Utility.decode(json.getString("clientMessage"));
            userData.partnerCareer = Base64Utility.decode(json.getString("partnerCareer"));
            userData.partnerStatus = Base64Utility.decode(json.getString("partnerStatus"));
            userData.partnerNewPrice = Base64Utility.decode(json.getString("partnerNewPrice"));
            userData.partnerOldPrice = Base64Utility.decode(json.getString("partnerOldPrice"));
            userData.partnerDangerousPrice = Base64Utility.decode(json.getString("partnerDangerousPrice"));
            userData.partnerDangerousMessage = Base64Utility.decode(json.getString("partnerDangerousMessage"));
            userData.partnerBigPrice = Base64Utility.decode(json.getString("partnerBigPrice"));
            userData.partnerBigMessage = Base64Utility.decode(json.getString("partnerBigMessage"));
            userData.partnerInspectionPrice = Base64Utility.decode(json.getString("partnerInspectionPrice"));
            userData.partnerInspectionMessage = Base64Utility.decode(json.getString("partnerInspectionMessage"));
            userData.partnerMessage = Base64Utility.decode(json.getString("partnerMessage"));

            userData.loginDatetime = DateUtility.stringToDate(json.getString("loginDatetime"), "yyyyMMddkkmmss");
            if (userData.loginDatetime == null) {
                return null;
            }

            return userData;

        } catch (Exception e) {}

        return null;
    }
}
