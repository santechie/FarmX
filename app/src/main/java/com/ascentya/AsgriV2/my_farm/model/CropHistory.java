package com.ascentya.AsgriV2.my_farm.model;

import com.google.gson.annotations.SerializedName;

public class CropHistory{

	@SerializedName("variety_id")
	private String varietyId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("land_id")
	private String landId;

	@SerializedName("crop_id")
	private String cropId;

	@SerializedName("type")
	private String type;

	@SerializedName("status")
	private String status;

	public String getVarietyId(){
		return varietyId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getLandId(){
		return landId;
	}

	public String getCropId(){
		return cropId;
	}

	public String getType(){
		return type;
	}

	public String getStatus(){
		return status;
	}

	public void setVarietyId(String varietyId) {
		this.varietyId = varietyId;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLandId(String landId) {
		this.landId = landId;
	}

	public void setCropId(String cropId) {
		this.cropId = cropId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
