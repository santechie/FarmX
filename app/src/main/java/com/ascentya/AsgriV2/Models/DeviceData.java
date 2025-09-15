package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class DeviceData {

    @SerializedName("id")
    int id;
    @SerializedName("device_id")
    int deviceId;
    @SerializedName("master_id")
    int masterId;
    @SerializedName("zone_id")
    String zoneId;
    @SerializedName("device_type")
    String deviceType;
    @SerializedName("temperature")
    float temperature;
    @SerializedName("humidity")
    float humidity;
    @SerializedName("soil_moisture")
    float soilMoisture;
    @SerializedName("ph")
    float ph;
    @SerializedName("light")
    float light;
    @SerializedName("created_at")
    String createdAt;

    String image = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(float soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public boolean hasValidData(){
        return getTemperature() != 0 && getHumidity() != 0 && getPh() != 0 && getSoilMoisture() != 0;
    }

    public boolean isImage(){
        return image != null;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
