package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class StateModel{

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("state")
	private String state;

	public String getStateId(){
		return stateId;
	}

	public String getState(){
		return state;
	}

	@Override
	public String toString() {
		return state;
	}
}