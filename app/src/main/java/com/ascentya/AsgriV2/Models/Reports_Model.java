package com.ascentya.AsgriV2.Models;

import java.util.List;

public class Reports_Model {

    String id, content, affacted_disease, affected_cause, recovery_process, affected_img, recovery_img;
    List<Remedy_Model> remedy_data;

    public List<Remedy_Model> getRemedy_data() {
        return remedy_data;
    }

    public void setRemedy_data(List<Remedy_Model> remedy_data) {
        this.remedy_data = remedy_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAffacted_disease() {
        return affacted_disease;
    }

    public void setAffacted_disease(String affacted_disease) {
        this.affacted_disease = affacted_disease;
    }

    public String getAffected_cause() {
        return affected_cause;
    }

    public void setAffected_cause(String affected_cause) {
        this.affected_cause = affected_cause;
    }

    public String getRecovery_process() {
        return recovery_process;
    }

    public void setRecovery_process(String recovery_process) {
        this.recovery_process = recovery_process;
    }

    public String getAffected_img() {
        return affected_img;
    }

    public void setAffected_img(String affected_img) {
        this.affected_img = affected_img;
    }

    public String getRecovery_img() {
        return recovery_img;
    }

    public void setRecovery_img(String recovery_img) {
        this.recovery_img = recovery_img;
    }
}
