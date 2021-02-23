package jp.co.lfg.pricetarpartner.System;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class SaveData {

    static class SharedPreferenceKey {
        public static String Key = "PricetarPartner";
        public static String UserId = "UserId";
    }

    private static SaveData mInstance = null;

    public Context mContext;
    public String userId = "";

    private SaveData(){}

    public static SaveData getInstance() {

        if(mInstance == null){
            mInstance = new SaveData();
        }
        return mInstance;
    }

    public void initialize(Context context) {

        mContext = context;

        SharedPreferences data = context.getSharedPreferences(SharedPreferenceKey.Key, Context.MODE_PRIVATE);

        this.userId = data.getString(SharedPreferenceKey.UserId, "");
    }

    public void save() {

        SharedPreferences data = mContext.getSharedPreferences(SharedPreferenceKey.Key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        editor.putString(SharedPreferenceKey.UserId, this.userId);

        editor.apply();
    }

}
