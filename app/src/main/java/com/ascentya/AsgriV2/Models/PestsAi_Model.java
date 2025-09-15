package com.ascentya.AsgriV2.Models;

public class PestsAi_Model {

    String pest, pest_desc, control_measure;
    Integer pest_image;

    public String getPest() {
        return pest;
    }

    public void setPest(String pest) {
        this.pest = pest;
    }

    public String getPest_desc() {
        return pest_desc;
    }

    public void setPest_desc(String pest_desc) {
        this.pest_desc = pest_desc;
    }

    public Integer getPest_image() {
        return pest_image;
    }

    public void setPest_image(Integer pest_image) {
        this.pest_image = pest_image;
    }

    public String getControl_measure() {
        return control_measure;
    }

    public void setControl_measure(String control_measure) {
        this.control_measure = control_measure;
    }
}
