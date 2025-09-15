package com.ascentya.AsgriV2.my_farm.model;

import com.ascentya.AsgriV2.Models.ImageModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportItem {

    @SerializedName("symptom")
    private String symptom;

    @SerializedName("images")
    private String images;

    @SerializedName("zi_id")
    private String ziId;

    @SerializedName("remedy_count")
    private String remedyCount;

    @SerializedName("part")
    private String part;

    @SerializedName("name")
    private String name;

    @SerializedName("pi_id")
    private String piId;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("recovered")
    private String recovered;

    @SerializedName("applied")
    private String applied;

    private ArrayList<ImageModel> imageModels = new ArrayList<ImageModel>();


    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setZiId(String ziId) {
        this.ziId = ziId;
    }

    public void setRemedyCount(String remedyCount) {
        this.remedyCount = remedyCount;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPiId(String piId) {
        this.piId = piId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImageModels(ArrayList<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    public String getSymptom(){
        return symptom;
    }

    public String getImages(){
        return images;
    }

    public String getZiId(){
        return ziId;
    }

    public String getRemedyCount(){
        return remedyCount;
    }

    public String getPart(){
        return part;
    }

    public String getName(){
        return name;
    }

    public String getPiId(){
        return piId;
    }

    public String getType(){
        return type;
    }

    public String getStatus(){
        return status;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getApplied() {
        return applied;
    }

    public void setApplied(String applied) {
        this.applied = applied;
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
}
