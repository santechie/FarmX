package com.ascentya.AsgriV2.managers;

import android.content.Context;

public class PrivilegeManager {

    private static PrivilegeManager instance = null;

    private PrivilegeManager(Context context){

    }

    public static PrivilegeManager getInstance(Context context){
        if (instance == null)
            instance = new PrivilegeManager(context);
        return instance;
    }

    public static class Modules{

    }

    public static class Operations{

    }
}
