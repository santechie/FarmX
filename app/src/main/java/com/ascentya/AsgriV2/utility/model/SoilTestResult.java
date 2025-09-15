package com.ascentya.AsgriV2.utility.model;

import com.google.gson.annotations.SerializedName;

public class SoilTestResult{

	@SerializedName("salinity")
	private String salinity;

	@SerializedName("potassium")
	private String potassium;

	@SerializedName("calcium")
	private String calcium;

	@SerializedName("sulphur")
	private String sulphur;

	@SerializedName("nitrogen")
	private String nitrogen;

	@SerializedName("magnesium")
	private String magnesium;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("ph_acidity")
	private String phAcidity;

	@SerializedName("manganese")
	private String manganese;

	@SerializedName("sodium")
	private String sodium;

	@SerializedName("iron")
	private String iron;

	@SerializedName("copper")
	private String copper;

	@SerializedName("id")
	private String id;

	@SerializedName("request_id")
	private String requestId;

	@SerializedName("zinc")
	private String zinc;

	@SerializedName("phosphorus")
	private String phosphorus;

	@SerializedName("status")
	private String status;

	public String getSalinity(){
		return salinity;
	}

	public String getPotassium(){
		return potassium;
	}

	public String getCalcium(){
		return calcium;
	}

	public String getSulphur(){
		return sulphur;
	}

	public String getNitrogen(){
		return nitrogen;
	}

	public String getMagnesium(){
		return magnesium;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getPhAcidity(){
		return phAcidity;
	}

	public String getManganese(){
		return manganese;
	}

	public String getSodium(){
		return sodium;
	}

	public String getIron(){
		return iron;
	}

	public String getCopper(){
		return copper;
	}

	public String getId(){
		return id;
	}

	public String getRequestId(){
		return requestId;
	}

	public String getZinc(){
		return zinc;
	}

	public String getPhosphorus(){
		return phosphorus;
	}

	public String getStatus(){
		return status;
	}
}