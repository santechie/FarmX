package com.ascentya.AsgriV2.my_farm.model;

import com.google.gson.annotations.SerializedName;

public class SoilType {



    @SerializedName("soil_id")
    private String soilId;

    @SerializedName("soil_name")
    private String soilName;

    public void setSoilId(String id) {
        this.soilId = id;
    }

    public String getSoilId(){
        return soilId;
    }

    public void setSoilName(String soilName) {
        this.soilName = soilName;
    }

    public String getSoilName(){
        return soilName;
    }

    @Override
    public String toString() {
        return soilName;
    }
}
