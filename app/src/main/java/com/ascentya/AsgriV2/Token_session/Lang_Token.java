package com.ascentya.AsgriV2.Token_session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Lang_Token {

    private SharedPreferences prefs;

    public Lang_Token(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
    }

    public String getusename() {
        String usename = prefs.getString("usename", "");
        return usename;
    }
}

