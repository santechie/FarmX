package com.ascentya.AsgriV2.e_market.data.model;

public class ItemType {

    private String id, itemId, categoryId, name;

    public ItemType(String id, String itemId, String categoryId, String name) {
        this.id = id;
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
