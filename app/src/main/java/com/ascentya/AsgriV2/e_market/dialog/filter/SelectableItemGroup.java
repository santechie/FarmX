package com.ascentya.AsgriV2.e_market.dialog.filter;

import com.ascentya.AsgriV2.e_market.data.model.Item;

import java.util.ArrayList;
import java.util.List;

class SelectableItemGroup extends FilterGroup {

    private String title;
    private List<Item> items;
    private ArrayList<String> selectedItems = new ArrayList<>();
    private boolean selectAll = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean isItemSelected(String itemId){
        return selectedItems.contains(itemId);
    }

    public void setItemSelected(String itemId){
        selectedItems.add(itemId);
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
}
