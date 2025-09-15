package com.ascentya.AsgriV2.Object_Converter;

import com.ascentya.AsgriV2.Models.Identification_Stages;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PIdentification_Converter {
    Gson gson = new Gson();

    public List<Identification_Stages> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Identification_Stages>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }


    public String someObjectListToString(List<Identification_Stages> someObjects) {
        return gson.toJson(someObjects);
    }
}
