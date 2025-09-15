package com.ascentya.AsgriV2.Models;

import java.util.List;

public class Symtoms_Model {
    String title, cover_image, dp_img;
    List<PointerImages_Model> Symtoms;
    String gallery_Images;

    public String getDp_img() {
        return dp_img;
    }

    public void setDp_img(String dp_img) {
        this.dp_img = dp_img;
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

    public List<PointerImages_Model> getSymtoms() {
        return Symtoms;
    }

    public void setSymtoms(List<PointerImages_Model> symtoms) {
        Symtoms = symtoms;
    }

    public String getGallery_Images() {
        return gallery_Images;
    }

    public void setGallery_Images(String gallery_Images) {
        this.gallery_Images = gallery_Images;
    }
}
