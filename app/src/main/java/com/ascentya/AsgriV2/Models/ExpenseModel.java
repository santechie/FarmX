package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class ExpenseModel{

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("service_id")
	private String serviceId;

	@SerializedName("prepare_type")
	private String prepareType;

	public String getTotalAmount(){
		return totalAmount;
	}

	public String getServiceId(){
		return serviceId;
	}

	public String getPrepareType(){
		return prepareType;
	}
}