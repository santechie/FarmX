package com.ascentya.AsgriV2.Database_Room.entities;

import com.ascentya.AsgriV2.Database_Room.DataTypeConverter;
import com.ascentya.AsgriV2.Database_Room.FavdataConverter;
import com.ascentya.AsgriV2.Models.Fav_Model;

import java.io.Serializable;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Info_Model implements Serializable {
    @PrimaryKey()
    private int basic_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "s_name")
    private String s_name;

    @ColumnInfo(name = "family")
    private String family;

    @ColumnInfo(name = "pests_symtoms")
    private String pests_symtoms;

    @ColumnInfo(name = "pests_identification")
    private String pests_identification;

    @ColumnInfo(name = "pests_controlmeasure")
    private String pests_controlmeasure;

    @ColumnInfo(name = "diseas_symtoms")
    private String diseas_symtoms;

    @ColumnInfo(name = "diseas_identification")
    private String diseas_identification;

    @ColumnInfo(name = "diseas_controlmeasure")
    private String diseas_controlmeasure;

    @ColumnInfo(name = "phd_symtoms")
    private String phd_symtoms;

    @ColumnInfo(name = "phd_identification")
    private String phd_identification;

    @ColumnInfo(name = "phd_controlmeasure")
    private String phd_controlmeasure;

    @ColumnInfo(name = "nutrient_dificiency")
    private String nutrient_dificiency;
    @ColumnInfo(name = "fav_condition")
    private String fav_condition;

    @ColumnInfo(name = "cultivation")
    private String cultivation;
    @ColumnInfo(name = "post_cultivation")
    private String post_cultivation;

    @ColumnInfo(name = "verieties")
    private String verieties;
    @ColumnInfo(name = "nutrient_values")
    private String nutrient_values;

    public String getFav_condition() {
        return fav_condition;
    }

    public void setFav_condition(String fav_condition) {
        this.fav_condition = fav_condition;
    }

    public String getCultivation() {
        return cultivation;
    }

    public void setCultivation(String cultivation) {
        this.cultivation = cultivation;
    }

    public String getPost_cultivation() {
        return post_cultivation;
    }

    public void setPost_cultivation(String post_cultivation) {
        this.post_cultivation = post_cultivation;
    }

    public String getVerieties() {
        return verieties;
    }

    public void setVerieties(String verieties) {
        this.verieties = verieties;
    }

    public String getNutrient_values() {
        return nutrient_values;
    }

    public void setNutrient_values(String nutrient_values) {
        this.nutrient_values = nutrient_values;
    }

    public String getDiseas_symtoms() {
        return diseas_symtoms;
    }

    public void setDiseas_symtoms(String diseas_symtoms) {
        this.diseas_symtoms = diseas_symtoms;
    }

    public String getDiseas_identification() {
        return diseas_identification;
    }

    public void setDiseas_identification(String diseas_identification) {
        this.diseas_identification = diseas_identification;
    }

    public String getDiseas_controlmeasure() {
        return diseas_controlmeasure;
    }

    public void setDiseas_controlmeasure(String diseas_controlmeasure) {
        this.diseas_controlmeasure = diseas_controlmeasure;
    }

    public String getPhd_symtoms() {
        return phd_symtoms;
    }

    public void setPhd_symtoms(String phd_symtoms) {
        this.phd_symtoms = phd_symtoms;
    }

    public String getPhd_identification() {
        return phd_identification;
    }

    public void setPhd_identification(String phd_identification) {
        this.phd_identification = phd_identification;
    }

    public String getPhd_controlmeasure() {
        return phd_controlmeasure;
    }

    public void setPhd_controlmeasure(String phd_controlmeasure) {
        this.phd_controlmeasure = phd_controlmeasure;
    }

    public String getNutrient_dificiency() {
        return nutrient_dificiency;
    }

    public void setNutrient_dificiency(String nutrient_dificiency) {
        this.nutrient_dificiency = nutrient_dificiency;
    }

    public String getPests_symtoms() {
        return pests_symtoms;
    }

    public void setPests_symtoms(String pests_symtoms) {
        this.pests_symtoms = pests_symtoms;
    }

    public String getPests_identification() {
        return pests_identification;
    }

    public void setPests_identification(String pests_identification) {
        this.pests_identification = pests_identification;
    }

    public String getPests_controlmeasure() {
        return pests_controlmeasure;
    }

    public void setPests_controlmeasure(String pests_controlmeasure) {
        this.pests_controlmeasure = pests_controlmeasure;
    }


    @ColumnInfo(name = "test")
    @TypeConverters(DataTypeConverter.class)
    private List<String> desc;


    @ColumnInfo(name = "taxonomy")

    private String taxonomy;

    @ColumnInfo(name = "fav_model")
    @TypeConverters(FavdataConverter.class)
    private List<Fav_Model> fav_model;

    @ColumnInfo(name = "varieties")

    private String varieties;

    public int getBasic_id() {
        return basic_id;
    }

    public void setBasic_id(int basic_id) {
        this.basic_id = basic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }


    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public List<Fav_Model> getFav_model() {
        return fav_model;
    }

    public void setFav_model(List<Fav_Model> fav_model) {
        this.fav_model = fav_model;
    }

    public String getVarieties() {
        return varieties;
    }

    public void setVarieties(String varieties) {
        this.varieties = varieties;
    }
}
