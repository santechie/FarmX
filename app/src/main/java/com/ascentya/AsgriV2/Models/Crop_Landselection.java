package com.ascentya.AsgriV2.Models;

public class Crop_Landselection {
    String name, area, zone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
