package com.ascentya.AsgriV2.Models;

import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.gson.annotations.SerializedName;

public class LandCropModel{

	@SerializedName("area")
	private String area;

	@SerializedName("zone")
	private String zone;

	@SerializedName("crop_id")
	private String cropId;

	@SerializedName("variety_id")
	private String varietyId;

	@SerializedName("lc_id")
	private String lcId;

	private String yieldDuring, yieldUnit, harvestDuring;

	public String getArea(){
		return area;
	}

	public String getZone(){
		return zone;
	}

	public String getCropId(){
		return cropId;
	}

	public String getYieldDuring() {
		return yieldDuring;
	}

	public void setYieldDuring(String yieldDuring) {
		this.yieldDuring = yieldDuring;
	}

	public String getYieldUnit() {
		return yieldUnit;
	}

	public void setYieldUnit(String yieldUnit) {
		this.yieldUnit = yieldUnit;
	}

	public String getHarvestDuring() {
		return harvestDuring;
	}

	public void setHarvestDuring(String harvestDuring) {
		this.harvestDuring = harvestDuring;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public void setCropId(String cropId) {
		this.cropId = cropId;
	}

	public String getVarietyId() {
		return varietyId;
	}

	public void setVarietyId(String varietyId) {
		this.varietyId = varietyId;
	}

	public String getLcId() {
		return lcId;
	}

	public void setLcId(String lcId) {
		this.lcId = lcId;
	}

	@Override
	public String toString() {
		Crops_Main crops_main = Webservice.getCrop(cropId);
		if (crops_main != null){
			VarietyModel varietyModel = Webservice.getVariety(varietyId);
			if (varietyModel != null){
				return crops_main + " (" + varietyModel.getName() + ")";
			}else{
				return crops_main.getName();
			}
		}
		return null;
	}
}