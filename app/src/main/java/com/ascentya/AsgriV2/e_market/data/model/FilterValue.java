package com.ascentya.AsgriV2.e_market.data.model;

public class FilterValue extends FilterData{

    private String valueType;

    public FilterValue(String id, String name, String valueType){
        setId(id);
        setName(name);
        setValueType(valueType);
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
