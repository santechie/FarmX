package com.ascentya.AsgriV2.Object_Converter;

import com.ascentya.AsgriV2.Models.ControlMeasure_Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PControl_Converter {
    Gson gson = new Gson();

    public List<ControlMeasure_Model> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ControlMeasure_Model>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }


    public String someObjectListToString(List<ControlMeasure_Model> someObjects) {
        return gson.toJson(someObjects);
    }
}
