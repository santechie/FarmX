package com.ascentya.AsgriV2.Database_Room;

import com.ascentya.AsgriV2.Models.Fav_Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

public class FavdataConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Fav_Model> stringToList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Fav_Model>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ListToString(List<Fav_Model> someObjects) {
        return gson.toJson(someObjects);
    }
}
