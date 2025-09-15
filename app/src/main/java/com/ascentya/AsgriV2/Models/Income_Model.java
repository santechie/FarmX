package com.ascentya.AsgriV2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Income_Model implements Parcelable {

    String activity_id, activity_land, activity_crop, activity, activity_resources, activity_start, activity_end, members, machines, animals, others;

    public Income_Model(Parcel in) {
        activity_id = in.readString();
        activity_land = in.readString();
        activity_crop = in.readString();
        activity = in.readString();
        activity_resources = in.readString();
        activity_start = in.readString();
        activity_end = in.readString();
        members = in.readString();
        machines = in.readString();
        animals = in.readString();
        others = in.readString();
    }

    public static final Creator<Income_Model> CREATOR = new Creator<Income_Model>() {
        @Override
        public Income_Model createFromParcel(Parcel in) {
            return new Income_Model(in);
        }

        @Override
        public Income_Model[] newArray(int size) {
            return new Income_Model[size];
        }
    };

    public Income_Model(String activity_id, String activity_land, String activity_crop, String activity, String activity_resources, String activity_start, String activity_end, String members, String machines, String animals, String others) {
        this.activity_id = activity_id;
        this.activity_land = activity_land;
        this.activity_crop = activity_crop;
        this.activity = activity;
        this.activity_resources = activity_resources;
        this.activity_start = activity_start;
        this.activity_end = activity_end;
        this.members = members;
        this.machines = machines;
        this.animals = animals;
        this.others = others;


    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_land() {
        return activity_land;
    }

    public void setActivity_land(String activity_land) {
        this.activity_land = activity_land;
    }

    public String getActivity_crop() {
        return activity_crop;
    }

    public void setActivity_crop(String activity_crop) {
        this.activity_crop = activity_crop;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivity_resources() {
        return activity_resources;
    }

    public void setActivity_resources(String activity_resources) {
        this.activity_resources = activity_resources;
    }

    public String getActivity_start() {
        return activity_start;
    }

    public void setActivity_start(String activity_start) {
        this.activity_start = activity_start;
    }

    public String getActivity_end() {
        return activity_end;
    }

    public void setActivity_end(String activity_end) {
        this.activity_end = activity_end;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getMachines() {
        return machines;
    }

    public void setMachines(String machines) {
        this.machines = machines;
    }

    public String getAnimals() {
        return animals;
    }

    public void setAnimals(String animals) {
        this.animals = animals;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(activity_id);
        parcel.writeString(activity_land);
        parcel.writeString(activity_crop);
        parcel.writeString(activity);
        parcel.writeString(activity_resources);
        parcel.writeString(activity_start);
        parcel.writeString(activity_end);
        parcel.writeString(members);
        parcel.writeString(machines);
        parcel.writeString(animals);
        parcel.writeString(others);
    }
}
