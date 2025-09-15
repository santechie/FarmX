package com.ascentya.AsgriV2.Models;

import java.util.List;

public class PostCultivation_Model {
    String Title;
    List<String> Data;


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public List<String> getData() {
        return Data;
    }

    public void setData(List<String> data) {
        Data = data;
    }
}
