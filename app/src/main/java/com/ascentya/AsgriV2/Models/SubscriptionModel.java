package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class SubscriptionModel{

	@SerializedName("subscription_id")
	private String subscriptionId;

	@SerializedName("date")
	private String date;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("payment_id")
	private String paymentId;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("validity")
	private String validity;

	@SerializedName("status")
	private String status;

	public String getSubscriptionId(){
		return subscriptionId;
	}

	public String getDate(){
		return date;
	}

	public String getUserId(){
		return userId;
	}

	public String getPaymentId(){
		return paymentId;
	}

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}

	public String getValidity(){
		return validity;
	}

	public String getStatus(){
		return status;
	}
}