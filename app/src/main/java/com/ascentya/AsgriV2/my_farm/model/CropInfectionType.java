package com.ascentya.AsgriV2.my_farm.model;

import com.google.gson.annotations.SerializedName;


public class CropInfectionType {

    @SerializedName("part_id")
    private String partId;

    @SerializedName("crop_id")
    private String cropId;

    @SerializedName("name")
    private String cropName;

    @SerializedName("value")
    private String typeValue;

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartId(){
        return partId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getCropId(){
        return cropId;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropName(){
        return cropName;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue(){
        return typeValue;
    }

    @Override
    public String toString() {
        return cropName;
    }

}
