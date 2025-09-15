package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class ZoneRemedy{

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("fertilizer_used")
	private String fertilizerUsed;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("date_action")
	private String dateAction;

	@SerializedName("report_id")
	private String reportId;

	@SerializedName("activity_type")
	private String activityType;

	@SerializedName("zone_name")
	private String zoneName;

	@SerializedName("id")
	private String id;

	@SerializedName("land_id")
	private String landId;

	public String getCost() {
		return cost;
	}

	@SerializedName("cost")
	private String cost;

	public String getQuantity(){
		return quantity;
	}

	public String getFertilizerUsed(){
		return fertilizerUsed;
	}

	public String getUserId(){
		return userId;
	}

	public String getDateAction(){
		return dateAction;
	}

	public String getReportId(){
		return reportId;
	}

	public String getActivityType(){
		return activityType;
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
}