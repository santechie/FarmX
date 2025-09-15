package com.ascentya.AsgriV2.Utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtils {

    private static Gson gson;

    public static Gson getGson(){
        if (gson == null)
            gson = new Gson();
        return gson;
    }

    public static  <T> T fromJson(String json, Type type){
        return getGson().fromJson(json, type);
    }

    public static String toJson(Object object){
        return getGson().toJson(object);
    }
}
