package com.ascentya.AsgriV2.Object_Converter;

import com.ascentya.AsgriV2.Models.Symtoms_Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PSymtoms_Converter {
    Gson gson = new Gson();

    public List<Symtoms_Model> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Symtoms_Model>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }


    public String someObjectListToString(List<Symtoms_Model> someObjects) {
        return gson.toJson(someObjects);
    }
}
