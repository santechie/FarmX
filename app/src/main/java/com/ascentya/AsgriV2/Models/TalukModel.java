package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class TalukModel{

	@SerializedName("taluk_id")
	private String talukId;

	@SerializedName("taluk_name")
	private String talukName;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("district_id")
	private String districtId;

	public String getTalukId(){
		return talukId;
	}

	public String getTalukName(){
		return talukName;
	}

	public String getStateId(){
		return stateId;
	}

	public String getDistrictId(){
		return districtId;
	}

	@Override
	public String toString() {
		return talukName;
	}
}