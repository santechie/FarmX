package com.ascentya.AsgriV2.Models;

public class Activity_Model {

    String id, type, created_at, start_date, end_date, days_number, total_cost, Machine_type, vendor_nname, contract_type, member_array, vendor_array;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDays_number() {
        return days_number;
    }

    public void setDays_number(String days_number) {
        this.days_number = days_number;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getMachine_type() {
        return Machine_type;
    }

    public void setMachine_type(String machine_type) {
        Machine_type = machine_type;
    }

    public String getVendor_nname() {
        return vendor_nname;
    }

    public void setVendor_nname(String vendor_nname) {
        this.vendor_nname = vendor_nname;
    }

    public String getContract_type() {
        return contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

    public String getMember_array() {
        return member_array;
    }

    public void setMember_array(String member_array) {
        this.member_array = member_array;
    }

    public String getVendor_array() {
        return vendor_array;
    }

    public void setVendor_array(String vendor_array) {
        this.vendor_array = vendor_array;
    }
}
