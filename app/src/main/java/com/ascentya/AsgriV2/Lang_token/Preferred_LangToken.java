package com.ascentya.AsgriV2.Lang_token;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferred_LangToken {

    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "PicVote_Token";
    public static final String KEY = "aa";

    // Constructor
    public Preferred_LangToken(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setToken(String token) {

        editor.putString("token", token);
        editor.commit();

    }

    public String getToken() {

        return pref.getString("token", "");
    }

    public void clearall() {
        editor.clear();
        editor.commit();
    }

}
