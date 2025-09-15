package com.ascentya.AsgriV2.utility.model;

import com.google.gson.annotations.SerializedName;

public class SoilTestRequest{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("name")
	private String name;

	@SerializedName("lab_name")
	private String labName;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("soiltype")
	private String soiltype;

	@SerializedName("id")
	private String id;

	@SerializedName("land_id")
	private String landId;

	@SerializedName("ticket_number")
	private String ticketNumber;

	@SerializedName("mobile_number")
	private String mobileNumber;

	@SerializedName("lab_id")
	private String labId;

	@SerializedName("land_name")
	private String landName;

	@SerializedName("status")
	private String status;

	@SerializedName("result_id")
	private String resultId;

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getUserId(){
		return userId;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getSoiltype(){
		return soiltype;
	}

	public String getId(){
		return id;
	}

	public String getLandId(){
		return landId;
	}

	public String getTicketNumber(){
		return ticketNumber;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public String getLabId(){
		return labId;
	}

	public String getLandName(){
		return landName;
	}

	public String getStatus(){
		return status;
	}

	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}
}