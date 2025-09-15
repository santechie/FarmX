package com.ascentya.AsgriV2.managers;

import com.google.gson.annotations.SerializedName;

public class NewSubscription {

    @SerializedName("subscription_id")
    private String subscriptionId;

    @SerializedName("name")
    private String planName;

    @SerializedName("description")
    private String PlanDesc;

    @SerializedName("validity")
    private String validity;

    @SerializedName("price")
    private String price;

    @SerializedName("status")
    private String status;

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanDesc(String planDesc) {
        PlanDesc = planDesc;
    }

    public String getPlanDesc() {
        return PlanDesc;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getValidity() {
        return validity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
