package com.ascentya.AsgriV2.e_market.dialog.filter;

import com.ascentya.AsgriV2.e_market.data.model.Category;

import java.util.ArrayList;
import java.util.List;

class SelectableCategoryGroup extends FilterGroup{

    private String title;
    private List<Category> categories;
    private ArrayList<String> selectedCategories = new ArrayList<>();
    private boolean selectAll = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean isCategorySelected(String categoryId){
        return selectedCategories.contains(categoryId);
    }

    public void setCategorySelected(String categoryId){
        selectedCategories.add(categoryId);
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
}
