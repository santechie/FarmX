package com.ascentya.AsgriV2.Models;

import java.util.List;

public class Identification_Stages {

    String title, cover_image, stage, dp;
    List<String> desc;
    List<String> Gallery_Images;


    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }

    public List<String> getGallery_Images() {
        return Gallery_Images;
    }

    public void setGallery_Images(List<String> gallery_Images) {
        Gallery_Images = gallery_Images;
    }
}
