package com.ascentya.AsgriV2.e_market.data.model;

public class Stock {

    String id;
    String itemId;
    String itemTypeId;
    String categoryId;
    float quantity;
    String unit;
    float price;
    String dateStart;
    String dateEnd;
    String placeId;
    String placeName;

    public Stock(String id, String itemTypeId, String itemId, String categoryId, float quantity, String unit, float price, String dateStart, String dateEnd, String placeId, String placeName) {
        this.id = id;
        this.itemTypeId = itemTypeId;
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.placeId = placeId;
        this.placeName = placeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }
}
