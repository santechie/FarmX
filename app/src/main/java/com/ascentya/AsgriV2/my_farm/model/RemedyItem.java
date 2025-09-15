package com.ascentya.AsgriV2.my_farm.model;

import com.ascentya.AsgriV2.Models.ImageModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RemedyItem{

	@SerializedName("quantity_unit")
	private String quantityUnit;

	@SerializedName("applied_at")
	private String appliedAt;

	@SerializedName("images")
	private String images;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("cost")
	private String cost;

	@SerializedName("is_recovered")
	private String isRecovered;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("pi_id")
	private String piId;

	@SerializedName("remedy")
	private String remedy;

	@SerializedName("ir_id")
	private String irId;

	@SerializedName("is_admin")
	private String isAdmin;

	@SerializedName("is_applied")
	private String isApplied;

	@SerializedName("description")
	private String description;

	@SerializedName("vote")
	private String vote;


	@SerializedName("status")
	private String status;

	@SerializedName("url")
	private String url;

	@SerializedName("name")
	private String name;

	private ArrayList<ImageModel> imageModels = new ArrayList<ImageModel>();
	private ArrayList<FileImageItem> fileModels = new ArrayList<FileImageItem>();

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public void setAppliedAt(String appliedAt) {
		this.appliedAt = appliedAt;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public void setIsRecovered(String isRecovered) {
		this.isRecovered = isRecovered;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setPiId(String piId) {
		this.piId = piId;
	}

	public void setRemedy(String remedy) {
		this.remedy = remedy;
	}

	public void setIrId(String irId) {
		this.irId = irId;
	}

	public void setIsApplied(String isApplied) {
		this.isApplied = isApplied;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setImageModels(ArrayList<ImageModel> imageModels) {
		this.imageModels = imageModels;
	}

	public String getQuantityUnit(){
		return quantityUnit;
	}

	public String getAppliedAt(){
		return appliedAt;
	}


	public String getImages(){
		return images;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuantity(){
		return quantity;
	}

	public String getCost(){
		return cost;
	}

	public String getIsRecovered(){
		return isRecovered;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getPiId(){
		return piId;
	}

	public String getRemedy(){
		return remedy;
	}

	public String getIrId(){
		return irId;
	}

	public String getIsApplied(){
		return isApplied;
	}

	public String getStatus(){
		return status;
	}

	public String getDescription() {
		return description;
	}

	public String getVote() {
		return vote;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public boolean isAdmin(){
		return getIsAdmin() != null && getIsAdmin().equals("1");
	}

	public ArrayList<ImageModel> getImageModels(){
		if (imageModels.isEmpty() && getImages() != null){
			String[] images = getImages().split(", ");
			for (String image: images) {
				imageModels.add(new ImageModel(image));
			}
		}
		return imageModels;
	}

	public ArrayList<FileImageItem> getFileModels(){
		if (fileModels.isEmpty() && getUrl() != null){
			String[] images = getUrl().split(", ");
			for (String image: images) {
				fileModels.add(new FileImageItem());
			}
		}
		return fileModels;
	}

}