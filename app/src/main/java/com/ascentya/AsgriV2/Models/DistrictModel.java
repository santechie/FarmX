package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class DistrictModel{

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("district_id")
	private String districtId;

	@SerializedName("state_id")
	private String stateId;

	public String getDistrictName(){
		return districtName;
	}

	public String getDistrictId(){
		return districtId;
	}

	public String getStateId(){
		return stateId;
	}

	@Override
	public String toString() {
		return districtName;
	}
}