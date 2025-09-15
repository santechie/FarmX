package com.ascentya.AsgriV2.my_farm.model;

import com.ascentya.AsgriV2.Models.ImageModel;

import java.util.ArrayList;

public class NewReportItem  {

    private InfectionType infectionType;
    private CropInfectionType cropinfectionPart;

    private String title, symptom;
    private String typeId, typeName, typeValue;
    private String partId, cropId, cropName, croptypeValue;
    private ArrayList<ImageModel> imageModels;
    private int id;



    //id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //title and symptoms
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    //images
    public ArrayList<ImageModel> getImageModels() {
        return imageModels;
    }

    public void setImageModels(ArrayList<ImageModel> imageModels) {
        this.imageModels = imageModels;
    }

    //infection type
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId(){
        return typeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName(){
        return typeName;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue(){
        return typeValue;
    }

    //crop infection type
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getPartId(){
        return partId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getCropId(){
        return cropId;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropName(){
        return cropName;
    }

    public String getCroptypeValue() {
        return croptypeValue;
    }

    public void setCroptypeValue(String croptypeValue) {
        this.croptypeValue = croptypeValue;
    }

    //infection type report
    public InfectionType getReportType() {
        return infectionType;
    }

    public void setReportType(InfectionType infectionType) {
        this.infectionType = infectionType;
    }


    //crop type report
    public CropInfectionType getCropPart() {
        return cropinfectionPart;
    }

    public void setCropPart(CropInfectionType cropinfectionPart) {
        this.cropinfectionPart = cropinfectionPart;
    }


/////////////////////////////////////////////




}


