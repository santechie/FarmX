package com.ascentya.AsgriV2.Models;

import org.apache.commons.text.StringEscapeUtils;

public class PointerImages_Model {

    String desc, images;
    String[] imagesArray;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        //Todo Temp
//        System.out.println("Converting...");
//        System.out.println("Received: \n" + images);
//        System.out.println("Formatted: \n" + images.replaceAll("\\r\\n", ""));
//        System.out.println("Formatted 2: \n" + StringEscapeUtils.unescapeJava(images).replaceAll("http:", "URL"));
//        System.out.println("Formatted 3: \n" + images.replaceAll("(\\r\\n|\\n|\\r)", ""));
        this.images = StringEscapeUtils.unescapeJava(images);
    }

    public void setImagesArray(String[] images){
        this.imagesArray = images;
    }
}
