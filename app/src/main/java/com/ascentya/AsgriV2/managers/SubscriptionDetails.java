package com.ascentya.AsgriV2.managers;

import com.google.gson.annotations.SerializedName;

public class SubscriptionDetails {

    @SerializedName("price")
    private String price;

    @SerializedName("id")
    private String id;

    @SerializedName("validity")
    private String validity;

    @SerializedName("plan")
    private String plan;

    @SerializedName("subscription_start_date")
    private String startDate;

    @SerializedName("subscription_end_date")
    private String endDate;

    @SerializedName("status")
    private String status;

    @SerializedName("is_default")
    private String trial;

    public String getPrice(){
        return price;
    }

    public String getId(){
        return id;
    }

    public String getValidity(){
        return validity;
    }

    public String getPlan(){
        return plan;
    }

    public String getStatus(){
        return status;
    }

    public String getTrial() {
        return trial;
    }


    public String getStartDate(){ return startDate;}

    public String getEndDate(){ return endDate;}
}
