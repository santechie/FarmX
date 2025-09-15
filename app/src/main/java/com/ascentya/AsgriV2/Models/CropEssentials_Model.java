package com.ascentya.AsgriV2.Models;

public class CropEssentials_Model {

    String name, status, disc, title, values, actual_value;
    Integer icon, statusicon;
    String success, date;

    public String getActual_value() {
        return actual_value;
    }

    public void setActual_value(String actual_value) {
        this.actual_value = actual_value;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Integer getIcon() {
        return icon;
    }

    public Integer getStatusicon() {
        return statusicon;
    }

    public void setStatusicon(Integer statusicon) {
        this.statusicon = statusicon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
