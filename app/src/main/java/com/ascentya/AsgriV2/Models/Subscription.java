package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class Subscription{

	@SerializedName("price")
	private String price;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("validity")
	private String validity;

	@SerializedName("type")
	private String type;

	@SerializedName("status")
	private String status;

	public String getPrice(){
		return price;
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

	public String getType(){
		return type;
	}

	public String getStatus(){
		return status;
	}
}