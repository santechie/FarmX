package com.ascentya.AsgriV2.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ZoneReport{

	@SerializedName("type")
	private String type;

	@SerializedName("affected_cause")
	private String affectedCause;

	@SerializedName("recovery_process")
	private String recoveryProcess;

	@SerializedName("affected_image")
	private ObjectItem affectedImage;

	@SerializedName("affected_disease")
	private String affectedDisease;

	@SerializedName("zone_name")
	private String zoneName;

	@SerializedName("id")
	private String id;

	@SerializedName("land_id")
	private String landId;

	@SerializedName("recovery_image")
	private ObjectItem recoveryImage;

	@SerializedName("cost")
	private String cost;

	@SerializedName("User_id")
	private String userId;

	@SerializedName("content")
	private String content;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("remedy_list")
	private List<ZoneRemedy> remedyList;

	@SerializedName("images")
	private List<ImageModel> images;

	public String getAffectedCause(){
		return affectedCause;
	}

	public String getRecoveryProcess(){
		return recoveryProcess;
	}

	public ObjectItem getAffectedImage(){
		return affectedImage;
	}

	public String getAffectedDisease(){
		return affectedDisease;
	}

	public String getZoneName(){
		return zoneName;
	}

	public String getId(){
		return id;
	}

	public String getLandId(){
		return landId;
	}

	public ObjectItem getRecoveryImage(){
		return recoveryImage;
	}

	public String getUserId(){
		return userId;
	}

	public String getContent(){
		return content;
	}

	public List<ZoneRemedy> getRemedyList(){
		return remedyList;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getCost() {
		return cost;
	}

	public String getType() {
		return type;
	}

	public List<ImageModel> getImages() {
		return images;
	}

	public void setImages(List<ImageModel> images) {
		this.images = images;
	}
}