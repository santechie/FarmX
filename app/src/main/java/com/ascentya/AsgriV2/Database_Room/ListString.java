package com.ascentya.AsgriV2.Database_Room;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListString {

    Gson gson = new Gson();

    public List<String> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<String>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }


    public String someObjectListToString(List<String> someObjects) {
        return gson.toJson(someObjects);
    }
}
