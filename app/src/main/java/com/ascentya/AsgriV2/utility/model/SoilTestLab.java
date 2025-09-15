package com.ascentya.AsgriV2.utility.model;

import com.google.gson.annotations.SerializedName;

public class SoilTestLab{

	@SerializedName("id")
	private int id;

	@SerializedName("area")
	private String area;

	@SerializedName("village_id")
	private String villageId;

	@SerializedName("distance")
	private String distance;

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("street")
	private String street;

	@SerializedName("taluk_id")
	private String talukId;

	@SerializedName("door_no")
	private String doorNo;

	@SerializedName("name")
	private String name;

	@SerializedName("state_id")
	private String stateId;

	@SerializedName("district_id")
	private String districtId;

	@SerializedName("taluk_name")
	private String villageName;

	@SerializedName("state")
	private String state;

	@SerializedName("landmark")
	private Object landmark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArea(){
		return area;
	}

	public String getVillageId(){
		return villageId;
	}

	public String getDistance(){
		return distance;
	}

	public String getDistrictName(){
		return districtName;
	}

	public String getStreet(){
		return street;
	}

	public String getTalukId(){
		return talukId;
	}

	public String getDoorNo(){
		return doorNo;
	}

	public String getName(){
		return name;
	}

	public String getStateId(){
		return stateId;
	}

	public String getDistrictId(){
		return districtId;
	}

	public String getVillageName(){
		return villageName;
	}

	public String getState(){
		return state;
	}

	public Object getLandmark(){
		return landmark;
	}
}