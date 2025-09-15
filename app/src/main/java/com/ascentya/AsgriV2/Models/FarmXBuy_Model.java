package com.ascentya.AsgriV2.Models;

import com.google.gson.annotations.SerializedName;

public class FarmXBuy_Model {

    @SerializedName("prod_name")
    String product_name;

    @SerializedName("prod_image")
    String product_image;

    @SerializedName("prod_price")
    String product_price;

    @SerializedName("status")
    String product_status;

    @SerializedName("cat_id")
    String cat_id;

    @SerializedName("user_id")
    String productuser_id;

    @SerializedName("prod_desc")
    String product_desc;

    @SerializedName("prod_id")
    String product_id;

    @SerializedName("created_date")
    String product_date;

    @SerializedName("quantity")
    String product_quantity;

    @SerializedName("cart_id")
    String cart_id;

    public String getProduct_name() {
        return product_name;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getProductuser_id() {
        return productuser_id;
    }

    public void setProductuser_id(String productuser_id) {
        this.productuser_id = productuser_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}
