package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class ImageModel {

    @SerializedName("image_id")
    private String imageId;

    @SerializedName("image")
    private String image;

    private boolean isNew = false;

    public ImageModel(){}

    public ImageModel(String image){
        this.image = image;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImage() {
        return image;
    }

    public void setPath(String image) {
        this.image = image;
    }

    public boolean getNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
