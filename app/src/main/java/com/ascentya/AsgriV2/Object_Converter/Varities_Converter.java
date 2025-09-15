package com.ascentya.AsgriV2.Object_Converter;

import com.ascentya.AsgriV2.Models.Varieties_Models;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Varities_Converter {
    Gson gson = new Gson();

    public List<Varieties_Models> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Varieties_Models>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }


    public String someObjectListToString(List<Varieties_Models> someObjects) {
        return gson.toJson(someObjects);
    }
}
