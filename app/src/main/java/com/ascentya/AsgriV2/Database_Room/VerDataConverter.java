package com.ascentya.AsgriV2.Database_Room;

import com.ascentya.AsgriV2.Models.Varieties_Models;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

public class VerDataConverter {

    Gson gson = new Gson();

    @TypeConverter
    public List<Varieties_Models> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Varieties_Models>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public String someObjectListToString(List<Varieties_Models> someObjects) {
        return gson.toJson(someObjects);
    }
}
