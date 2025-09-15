package com.ascentya.AsgriV2.Models;

import java.util.List;

public class Subactivity_Model {
    String name, id;
    List<String> Data;

    public List<String> getData() {
        return Data;
    }

    public void setData(List<String> data) {
        Data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
