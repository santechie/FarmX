package com.ascentya.AsgriV2.my_farm.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FileImageItem {

    @SerializedName("url")
    private String url;

    @SerializedName("name")
    private String name;

    private ArrayList<FileImageItem> imageModels = new ArrayList<FileImageItem>();



    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<FileImageItem> getImageModels(){
        if (imageModels.isEmpty() && getUrl() != null){
            String[] images = getUrl().split(",");
            for (String image: images) {
                imageModels.add(new FileImageItem());
            }
        }
        return imageModels;
    }

}
