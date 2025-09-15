package com.ascentya.AsgriV2.my_farm.model;

import com.google.gson.annotations.SerializedName;

public class InfectionType {

    @SerializedName("type_id")
    private String typeId;

    @SerializedName("name")
    private String typeName;

    @SerializedName("value")
    private String typeValue;

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId(){
        return typeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName(){
        return typeName;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue(){
        return typeValue;
    }

    @Override
    public String toString() {
        return  getTypeName();
    }
}
