package com.ascentya.AsgriV2.my_farm.model;

import com.google.gson.annotations.SerializedName;

public class ReportDetail {

    @SerializedName("recovered")
    private String recovered;

    @SerializedName("zi_id")
    private String ziId;

    @SerializedName("applied")
    private String applied;

    @SerializedName("part_infection_count")
    private String partInfectionCount;

    @SerializedName("remedy_count")
    private String remedyCount;

    @SerializedName("name")
    private String name;

    @SerializedName("created_at")
    private String createdAt;

    public String getRecovered(){
        return recovered;
    }

    public String getZiId(){
        return ziId;
    }

    public String getApplied(){
        return applied;
    }

    public String getPartInfectionCount(){
        return partInfectionCount;
    }

    public String getRemedyCount(){
        return remedyCount;
    }

    public String getName(){
        return name;
    }

    public String getCreatedAt(){
        return createdAt;
    }

}
