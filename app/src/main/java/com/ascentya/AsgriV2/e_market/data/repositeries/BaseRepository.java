package com.ascentya.AsgriV2.e_market.data.repositeries;

import android.content.Context;

public class BaseRepository {

    protected Context context;
    private static BaseRepository instance;

    public BaseRepository(Context context){
        this.context = context;
    }

    public static BaseRepository getInstance(Context context){
        if (instance == null)
            instance = new BaseRepository(context);
        return instance;
    }
}
