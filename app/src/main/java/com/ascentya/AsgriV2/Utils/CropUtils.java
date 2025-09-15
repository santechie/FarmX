package com.ascentya.AsgriV2.Utils;

import android.util.Log;

import com.ascentya.AsgriV2.Models.LandCropModel;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CropUtils {

    public static Type typeToken = new TypeToken<List<LandCropModel>>(){}.getType();

    public static List<LandCropModel> getLandCropModelList(String cropString){
        Log.i("Crop Utils", cropString);
        if (cropString == null || cropString.equals("")) new ArrayList<LandCropModel>();
        return GsonUtils.getGson().fromJson(cropString, typeToken);
    }
}
