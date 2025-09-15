package com.ascentya.AsgriV2.managers;

import com.google.gson.annotations.SerializedName;

public class SubscriptionAccess {

    @SerializedName("module_id")
    private String moduleId;

    @SerializedName("module_name")
    private String moduleName;

    @SerializedName("module_value")
    private String moduleValue;

    @SerializedName("module_status")
    private String moduleStatus;

    @SerializedName("component_id")
    private String componentId;

    @SerializedName("component_name")
    private String componentName;

    @SerializedName("component_value")
    private String componentValue;

    @SerializedName("component_status")
    private String componentStatus;

    @SerializedName("view_access")
    private String viewAccess;

    @SerializedName("insert_access")
    private String insertAccess;

    @SerializedName("update_access")
    private String updateAccess;

    @SerializedName("delete_access")
    private String deleteAccess;

}
