package com.ascentya.AsgriV2.Models;

public class Masterfield_Model {

    String id, master_name, master_status, created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaster_name() {
        return master_name;
    }

    public void setMaster_name(String master_name) {
        this.master_name = master_name;
    }

    public String getMaster_status() {
        return master_status;
    }

    public void setMaster_status(String master_status) {
        this.master_status = master_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return master_name;
    }
}
