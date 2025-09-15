package com.ascentya.AsgriV2.Models;

public class Members_Model {

    String member_id, member_name, created_at, member_age, member_gender, relation, farming_exp, member_payment, member_bilingtype;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMember_payment() {
        return member_payment;
    }

    public void setMember_payment(String member_payment) {
        this.member_payment = member_payment;
    }

    public String getMember_bilingtype() {
        return member_bilingtype;
    }

    public void setMember_bilingtype(String member_bilingtype) {
        this.member_bilingtype = member_bilingtype;
    }

    public String getMember_gender() {
        return member_gender;
    }

    public void setMember_gender(String member_gender) {
        this.member_gender = member_gender;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_age() {
        return member_age;
    }

    public void setMember_age(String member_age) {
        this.member_age = member_age;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getFarming_exp() {
        return farming_exp;
    }

    public void setFarming_exp(String farming_exp) {
        this.farming_exp = farming_exp;
    }

    @Override
    public String toString() {
        return member_name;
    }

}
