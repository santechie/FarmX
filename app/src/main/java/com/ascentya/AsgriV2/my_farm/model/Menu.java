package com.ascentya.AsgriV2.my_farm.model;

public class Menu {

    private int image;
    private String title, key;

    public Menu(int image, String title, String key) {
        this.image = image;
        this.title = title;
        this.key = key;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }

}
