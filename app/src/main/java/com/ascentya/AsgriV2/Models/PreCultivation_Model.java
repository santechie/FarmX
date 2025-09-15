package com.ascentya.AsgriV2.Models;

import java.util.List;

public class PreCultivation_Model {

    String Title;
    List<Cultivation_Disc_Model> Data;
    Boolean check;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public List<Cultivation_Disc_Model> getData() {
        return Data;
    }

    public void setData(List<Cultivation_Disc_Model> data) {
        Data = data;
    }
}
