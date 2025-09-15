package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class LandDeviceData {

	@SerializedName("master_id")
	private String masterId;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("zone_name")
	private String zoneName;

	@SerializedName("land_id")
	private String landId;

	@SerializedName("crop_id")
	private String cropId;

	@SerializedName("zone_id")
	private String zoneId;

	@SerializedName("light")
	private String light;

	@SerializedName("temperature")
	private String temperature;

	@SerializedName("ph")
	private String ph;

	@SerializedName("humidity")
	private String humidity;

	@SerializedName("id")
	private String id;

	@SerializedName("created_date")
	private String createdDate;

	@SerializedName("soil_moisture")
	private String soilMoisture;

	@SerializedName("status")
	private String status;

	public String getMasterId(){
		return masterId;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public String getZoneName(){
		return zoneName;
	}

	public String getLandId(){
		return landId;
	}

	public String getCropId(){
		return cropId;
	}

	public String getZoneId(){
		return zoneId;
	}

	public String getLight(){
		return light;
	}

	public String getTemperature(){
		return temperature;
	}

	public String getPh(){
		return ph;
	}

	public String getHumidity(){
		return humidity;
	}

	public String getId(){
		return id;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public String getSoilMoisture(){
		return soilMoisture;
	}

	public String getStatus(){
		return status;
	}
}