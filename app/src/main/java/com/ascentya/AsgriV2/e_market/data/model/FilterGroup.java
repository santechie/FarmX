package com.ascentya.AsgriV2.e_market.data.model;

import java.util.ArrayList;

public class FilterGroup extends FilterData{

    private String type;
    private ArrayList<FilterValue> filterValues;

    public FilterGroup(String id, String name, String type){
        setId(id);
        setName(name);
        setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<FilterValue> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(ArrayList<FilterValue> filterValues) {
        this.filterValues = filterValues;
    }
}
