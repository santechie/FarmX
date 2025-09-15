package com.ascentya.AsgriV2.Models;

public class Zone_Model {

    String crop_icons_id, Basic_info_id, zone_id, crop_icons_images, crop_name, scientific_name, soil_ph, temperature, humidity, soil_moisture, light_visibility, pollution, zone_name, area, lcId;

    public String getCrop_icons_id() {
        return crop_icons_id;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public void setCrop_icons_id(String crop_icons_id) {
        this.crop_icons_id = crop_icons_id;
    }

    public String getBasic_info_id() {
        return Basic_info_id;
    }

    public void setBasic_info_id(String basic_info_id) {
        Basic_info_id = basic_info_id;
    }

    public String getCrop_icons_images() {
        return crop_icons_images;
    }

    public void setCrop_icons_images(String crop_icons_images) {
        this.crop_icons_images = crop_icons_images;
    }

    public String getCrop_name() {
        return crop_name;
    }

    public void setCrop_name(String crop_name) {
        this.crop_name = crop_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getSoil_ph() {
        return soil_ph;
    }

    public void setSoil_ph(String soil_ph) {
        this.soil_ph = soil_ph;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSoil_moisture() {
        return soil_moisture;
    }

    public void setSoil_moisture(String soil_moisture) {
        this.soil_moisture = soil_moisture;
    }

    public String getLight_visibility() {
        return light_visibility;
    }

    public void setLight_visibility(String light_visibility) {
        this.light_visibility = light_visibility;
    }

    public String getPollution() {
        return pollution;
    }

    public void setPollution(String pollution) {
        this.pollution = pollution;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLcId() {
        return lcId;
    }

    public void setLcId(String lcId) {
        this.lcId = lcId;
    }

    @Override
    public String toString() {
        return zone_name;
    }
}
