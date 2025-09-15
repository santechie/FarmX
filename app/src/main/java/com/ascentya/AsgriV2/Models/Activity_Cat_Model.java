package com.ascentya.AsgriV2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ascentya.AsgriV2.Utils.GsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;

public class Activity_Cat_Model implements Parcelable {

    String fence_id, sowing_by, user_id, land_id, crop_id, lc_id, service_id, prepare_type, crop_type, fencing_by, equipment_name, equipment_type, vendor_name, contract_type, material_name, member_id, start_date, end_date, days_count, member_count, total_amount, created_date;
    String imagesString, status = "";
    ArrayList<ImageModel> images = new ArrayList<>();

    public Activity_Cat_Model(Parcel in) {
        fence_id = in.readString();
        user_id = in.readString();
        land_id = in.readString();
        crop_id = in.readString();
        lc_id = in.readString();
        service_id = in.readString();
        prepare_type = in.readString();
        crop_type = in.readString();
        fencing_by = in.readString();
        equipment_name = in.readString();
        equipment_type = in.readString();
        vendor_name = in.readString();
        contract_type = in.readString();
        material_name = in.readString();
        member_id = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        days_count = in.readString();
        member_count = in.readString();
        total_amount = in.readString();
        created_date = in.readString();
        sowing_by = in.readString();
        imagesString = in.readString();
        status = in.readString();

        try {
            prepareImages(new JSONArray(imagesString));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public Activity_Cat_Model() {

    }

    public static final Creator<Activity_Cat_Model> CREATOR = new Creator<Activity_Cat_Model>() {
        @Override
        public Activity_Cat_Model createFromParcel(Parcel in) {
            return new Activity_Cat_Model(in);
        }

        @Override
        public Activity_Cat_Model[] newArray(int size) {
            return new Activity_Cat_Model[size];
        }
    };

    public String getSowing_by() {
        return sowing_by;
    }

    public void setSowing_by(String sowing_by) {
        this.sowing_by = sowing_by;
    }


    public String getFence_id() {
        return fence_id;
    }

    public void setFence_id(String fence_id) {
        this.fence_id = fence_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLand_id() {
        return land_id;
    }

    public void setLand_id(String land_id) {
        this.land_id = land_id;
    }

    public String getCrop_id() {
        return crop_id;
    }

    public void setCrop_id(String crop_id) {
        this.crop_id = crop_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getPrepare_type() {
        return prepare_type;
    }

    public void setPrepare_type(String prepare_type) {
        this.prepare_type = prepare_type;
    }

    public String getCrop_type() {
        return crop_type;
    }

    public void setCrop_type(String crop_type) {
        this.crop_type = crop_type;
    }

    public String getFencing_by() {
        return fencing_by;
    }

    public void setFencing_by(String fencing_by) {
        this.fencing_by = fencing_by;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getEquipment_type() {
        return equipment_type;
    }

    public void setEquipment_type(String equipment_type) {
        this.equipment_type = equipment_type;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getContract_type() {
        return contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public String getDays_count() {
        return days_count;
    }

    public void setDays_count(String days_count) {
        this.days_count = days_count;
    }

    public String getMember_count() {
        return member_count;
    }

    public void setMember_count(String member_count) {
        this.member_count = member_count;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLc_id() {
        return lc_id;
    }

    public void setLc_id(String lc_id) {
        this.lc_id = lc_id;
    }

    public String getImagesString() {
        return imagesString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImagesString(String imagesString) {
        this.imagesString = imagesString;
    }

    public void prepareImages(JSONArray images){
        try {
            this.images.clear();
            for (int i=0; i<images.length(); i++){
                ImageModel imageModel = GsonUtils.fromJson(images.getJSONObject(i).toString(), ImageModel.class);
                this.images.add(imageModel);
            }
            setImagesString(images.toString());
        }catch (Exception e){
            //e.printStackTrace();
        }
    }

    public ArrayList<ImageModel> getImages(){ return images; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fence_id);
        parcel.writeString(user_id);
        parcel.writeString(land_id);
        parcel.writeString(crop_id);
        parcel.writeString(lc_id);
        parcel.writeString(service_id);
        parcel.writeString(prepare_type);
        parcel.writeString(crop_type);
        parcel.writeString(fencing_by);
        parcel.writeString(equipment_name);
        parcel.writeString(equipment_type);
        parcel.writeString(vendor_name);
        parcel.writeString(contract_type);
        parcel.writeString(material_name);
        parcel.writeString(member_id);
        parcel.writeString(start_date);
        parcel.writeString(end_date);
        parcel.writeString(days_count);
        parcel.writeString(member_count);
        parcel.writeString(total_amount);
        parcel.writeString(created_date);
        parcel.writeString(sowing_by);
        parcel.writeString(imagesString);
        parcel.writeString(status);
    }
}
