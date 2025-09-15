package com.ascentya.AsgriV2.Models;

import java.util.List;

public class NutrientDef_Model {
    String name, firstleter, nd_video;
    List<PointerImages_Model> symptoms;
    List<String> corrective_measure;

    public String getNd_video() {
        return nd_video;
    }

    public void setNd_video(String nd_video) {
        this.nd_video = nd_video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstleter() {
        return firstleter;
    }

    public void setFirstleter(String firstleter) {
        this.firstleter = firstleter;
    }

    public List<PointerImages_Model> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<PointerImages_Model> symptoms) {
        this.symptoms = symptoms;
    }

    public List<String> getCorrective_measure() {
        return corrective_measure;
    }

    public void setCorrective_measure(List<String> corrective_measure) {
        this.corrective_measure = corrective_measure;
    }


}
