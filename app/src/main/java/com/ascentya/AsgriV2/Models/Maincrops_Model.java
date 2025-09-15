package com.ascentya.AsgriV2.Models;

public class Maincrops_Model implements Cloneable {
    String id, district, land_name, taluk, acre_count, soiltype, maincrop, intercrop, anual_revenue, irrigation_type, govt_scheme, live_stocks, soilhealth_card, organic_farmer, export, created_at, cropId;
    String mainCropsString, interCropsString;
    String varietyId, lcId;
    String fieldId;


    public String getLand_name() {
        return land_name;
    }

    public void setLand_name(String land_name) {
        this.land_name = land_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getAcre_count() {
        return acre_count;
    }

    public void setAcre_count(String acre_count) {
        this.acre_count = acre_count;
    }

    public String getSoiltype() {
        return soiltype;
    }

    public void setSoiltype(String soiltype) {
        this.soiltype = soiltype;
    }

    public String getMaincrop() {
        return maincrop;
    }

    public void setMaincrop(String maincrop) {
        this.maincrop = maincrop;
    }

    public String getIntercrop() {
        return intercrop;
    }

    public void setIntercrop(String intercrop) {
        this.intercrop = intercrop;
    }

    public String getAnual_revenue() {
        return anual_revenue;
    }

    public void setAnual_revenue(String anual_revenue) {
        this.anual_revenue = anual_revenue;
    }

    public String getIrrigation_type() {
        return irrigation_type;
    }

    public void setIrrigation_type(String irrigation_type) {
        this.irrigation_type = irrigation_type;
    }

    public String getGovt_scheme() {
        return govt_scheme;
    }

    public void setGovt_scheme(String govt_scheme) {
        this.govt_scheme = govt_scheme;
    }

    public String getLive_stocks() {
        return live_stocks;
    }

    public void setLive_stocks(String live_stocks) {
        this.live_stocks = live_stocks;
    }

    public String getSoilhealth_card() {
        return soilhealth_card;
    }

    public void setSoilhealth_card(String soilhealth_card) {
        this.soilhealth_card = soilhealth_card;
    }

    public String getOrganic_farmer() {
        return organic_farmer;
    }

    public void setOrganic_farmer(String organic_farmer) {
        this.organic_farmer = organic_farmer;
    }

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    @Override
    public String toString() {
        return getLand_name();
    }

    public Maincrops_Model Clone() throws CloneNotSupportedException {return (Maincrops_Model) clone();}

    public String getMainCropsString() {
        return mainCropsString;
    }

    public void setMainCropsString(String mainCropsString) {
        this.mainCropsString = mainCropsString;
    }

    public String getInterCropsString() {
        return interCropsString;
    }

    public void setInterCropsString(String interCropsString) {
        this.interCropsString = interCropsString;
    }

    public String getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(String varietyId) {
        this.varietyId = varietyId;
    }

    public String getLcId() {
        return lcId;
    }

    public void setLcId(String lcId) {
        this.lcId = lcId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        return fieldId;
    }

}
