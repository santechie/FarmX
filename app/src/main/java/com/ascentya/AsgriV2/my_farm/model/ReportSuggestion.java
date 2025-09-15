package com.ascentya.AsgriV2.my_farm.model;

import android.text.TextUtils;

import com.ascentya.AsgriV2.Models.ImageModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportSuggestion{

	@SerializedName("pest_name")
	private String pestName;

	@SerializedName("disease_images")
	private String diseaseImages;

	@SerializedName("zi_id")
	private String ziId;

	@SerializedName("name")
	private String name;

	@SerializedName("pest_images")
	private String pestImages;

	@SerializedName("crop_id")
	private String cropId;

	@SerializedName("disease_name")
	private String diseaseName;

	@SerializedName("type")
	private String type;

	@SerializedName("pest_symptoms")
	private String pestSymptoms;

	@SerializedName("pest_remedies")
	private String pestRemedies;

	@SerializedName("disease_symptoms")
	private String diseaseSymptoms;

	@SerializedName("disease_remedies")
	private String diseaseRemedies;

	private ArrayList<ImageModel> imageModels = new ArrayList<>();

	public String getPestName(){
		return pestName;
	}

	public String getDiseaseImages(){
		return diseaseImages;
	}

	public String getZiId(){
		return ziId;
	}

	public String getName(){
		return name;
	}

	public String getPestImages(){
		return pestImages;
	}

	public String getCropId(){
		return cropId;
	}

	public String getDiseaseName(){
		return diseaseName;
	}

	public String getType(){
		return type;
	}

	public String getPestSymptoms() {
		return pestSymptoms;
	}

	public void setPestSymptoms(String pestSymptoms) {
		this.pestSymptoms = pestSymptoms;
	}

	public String getPestRemedies() {
		return pestRemedies;
	}

	public void setPestRemedies(String pestRemedies) {
		this.pestRemedies = pestRemedies;
	}

	public String getDiseaseSymptoms() {
		return diseaseSymptoms;
	}

	public void setDiseaseSymptoms(String diseaseSymptoms) {
		this.diseaseSymptoms = diseaseSymptoms;
	}

	public String getDiseaseRemedies() {
		return diseaseRemedies;
	}

	public void setDiseaseRemedies(String diseaseRemedies) {
		this.diseaseRemedies = diseaseRemedies;
	}

	public ArrayList<ImageModel> getImageModels(){
		if (imageModels.isEmpty() && !TextUtils.isEmpty(getValidImages())){
			String[] images = getValidImages().split(", ");
			for (String image: images) {
				imageModels.add(new ImageModel(image));
			}
		}
		return imageModels;
	}

	public String getValidName() {
		if (pestName != null) return pestName;
		else if (diseaseName != null) return diseaseName;
		else return "";
	}

	public String getValidImages(){
		if (pestImages != null) return pestImages;
		else if (diseaseImages != null) return diseaseImages;
		else return "";
	}

	private final String POINTER = "- ";

	public String getValidSymptoms(){
		if (pestSymptoms != null) return POINTER + pestSymptoms.replace("<NEW_LINE>", "\n" + POINTER);
		else if (diseaseSymptoms != null) return POINTER + diseaseSymptoms.replace("<NEW_LINE>", "\n" + POINTER);
		else return "";
	}

	public String getValidRemedies(){
		if (pestRemedies != null) return POINTER + pestRemedies.replace("<NEW_LINE", "\n" + POINTER);
		else if (diseaseRemedies != null) return POINTER + diseaseRemedies.replace("<NEW_LINE>", "\n" + POINTER);
		else return "";
	}
}