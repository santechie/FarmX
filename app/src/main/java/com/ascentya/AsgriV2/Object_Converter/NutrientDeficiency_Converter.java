package com.ascentya.AsgriV2.Object_Converter;

import com.ascentya.AsgriV2.Models.NutrientDef_Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class NutrientDeficiency_Converter {
    Gson gson = new Gson();

    public List<NutrientDef_Model> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<NutrientDef_Model>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }


    public String someObjectListToString(List<NutrientDef_Model> someObjects) {
        return gson.toJson(someObjects);
    }
}
