package com.ascentya.AsgriV2.Models;

import java.util.List;

public class BasicInfo_Model {

    Boolean expand;
    List<Taxanomy_Model> Data;

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }

    public List<Taxanomy_Model> getData() {
        return Data;
    }

    public void setData(List<Taxanomy_Model> data) {
        Data = data;
    }
}
