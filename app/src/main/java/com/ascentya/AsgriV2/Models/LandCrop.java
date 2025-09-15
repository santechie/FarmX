package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class LandCrop {

   @SerializedName("id")
   private String id;

   @SerializedName("land_id")
   private String landId;

   @SerializedName("crop_id")
   private String cropId;

   @SerializedName("variety_id")
   private String varietyId;

   @SerializedName("type")
   private String type;

   @SerializedName("status")
   private int status;

   @SerializedName("created_at")
   private String createAt;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getLandId() {
      return landId;
   }

   public void setLandId(String landId) {
      this.landId = landId;
   }

   public String getCropId() {
      return cropId;
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

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public int getStatus() {
      return status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public String getCreateAt() {
      return createAt;
   }

   public void setCreateAt(String createAt) {
      this.createAt = createAt;
   }
}
