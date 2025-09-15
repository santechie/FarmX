package com.ascentya.AsgriV2.Models;

import java.util.ArrayList;

public class Crops_Main {
    String name, icon, crop_id, s_name, waterph, pollution, tempreture, humidity, moisture, dissolved_solids;
    ArrayList<VarietyModel> varieties;
    String varietyId, lcId, varietyName;

    public String getWaterph() {
        return waterph;
    }

    public void setWaterph(String waterph) {
        this.waterph = waterph;
    }

    public String getPollution() {
        return pollution;
    }

    public void setPollution(String pollution) {
        this.pollution = pollution;
    }

    public String getTempreture() {
        return tempreture;
    }

    public void setTempreture(String tempreture) {
        this.tempreture = tempreture;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getDissolved_solids() {
        return dissolved_solids;
    }

    public void setDissolved_solids(String dissolved_solids) {
        this.dissolved_solids = dissolved_solids;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getCrop_id() {
        return crop_id;
    }

    public void setCrop_id(String crop_id) {
        this.crop_id = crop_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<VarietyModel> getVarieties() {
        return varieties;
    }

    public void setVarieties(ArrayList<VarietyModel> varieties) {
        this.varieties = varieties;
    }

    public String getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(String varietyId) {
        this.varietyId = varietyId;
    }

    public String getLcId() {
        return lcId;
    }

    public void setLcId(String lcId) {
        this.lcId = lcId;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    @Override
    public String toString() {
        if (getVarietyName() == null) return name;
        return name + " (" + getVarietyName() + ")";
    }
}
