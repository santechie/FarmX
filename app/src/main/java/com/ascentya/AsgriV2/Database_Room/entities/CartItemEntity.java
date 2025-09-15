package com.ascentya.AsgriV2.Database_Room.entities;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CartItemEntity implements Serializable {

    @NonNull
    @PrimaryKey
    private String prod_id;

    @ColumnInfo(name = "user_id")
    private String user_id;

    @ColumnInfo(name = "cat_id")
    private String cat_id;

    @ColumnInfo(name = "prod_name")
    private String prod_name;

    @ColumnInfo(name = "prod_desc")
    private String prod_desc;

    @ColumnInfo(name = "prod_price")
    private String prod_price;

    @ColumnInfo(name = "created_date")
    private String created_date;

    @ColumnInfo(name = "prod_image")
    private String prod_image;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "quantity")
    private int quantity;

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }

    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
