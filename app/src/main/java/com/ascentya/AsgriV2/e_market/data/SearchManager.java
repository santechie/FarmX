package com.ascentya.AsgriV2.e_market.data;

import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.repositeries.DataRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchManager{

    private DataRepository dataRepository;
    private Action action;
    private String query = "";

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<ItemType> itemTypes = new ArrayList<>();

    public SearchManager(DataRepository dataRepository, Action action){
        this.dataRepository = dataRepository;
        this.action = action;
    }

    public void setQuery(String query){
        this.query = query.trim();

        if (query.isEmpty()){
            clearQuery();
        }else {
            loadCategoriesFromQuery();
            loadItemsFromQuery();
            loadItemTypesFromQuery();
        }

        action.onListRefresh();
    }

    public void clearQuery(){
        this.query = "";
        clearSearchedData();
        action.onListRefresh();
    }

    public boolean isQueryFound(){
        return query != null && !query.trim().isEmpty();
    }

    public List<Category> getSearchedCategories(){
        return categories;
    }

    public List<Item> getSearchedItems(String categoryId){
        ArrayList<Item> catItems = new ArrayList<>();
        for (Item item: items){
            if (item.getCategoryId().equals(categoryId)){
                catItems.add(item);
            }
        }
        return catItems;
    }

    public List<Item> getSearchedItems(){
        return items;
    }

    public List<ItemType> getSearchedItemTypes(String categoryId, String itemId){
        ArrayList<ItemType> tempItemTypes = new ArrayList<>();
        for (ItemType itemType: itemTypes){
            if (itemType.getCategoryId().equals(categoryId) && itemType.getItemId().equals(itemId)){
                tempItemTypes.add(itemType);
            }
        }
        return tempItemTypes;
    }

    public List<ItemType> getSearchedItemTypes(){
        return itemTypes;
    }

    private void loadCategoriesFromQuery(){
        categories.clear();
        for (Category category: dataRepository.getAllCategories()){
            if (containsOrEquals(category.getName(), query)){
                categories.add(category);
            }
        }
    }

    private void loadItemsFromQuery() {
        items.clear();
        for (Item item: dataRepository.getAllItems()){
            if (containsOrEquals(item.getName(), query) || isCategoryAdded(item.getCategoryId())){
                items.add(item);
            }
        }

        for (Item item: items){
            if (!isCategoryAdded(item.getCategoryId())){
                categories.add(getCategory(item.getCategoryId()));
            }
        }
    }

    private void loadItemTypesFromQuery() {
        itemTypes.clear();
        for (ItemType itemType: dataRepository.getAllItemTypes()){
            if ((isCategoryAdded(itemType.getCategoryId())
                    && isItemAdded(itemType.getCategoryId(), itemType.getItemId())) ||
            containsOrEquals(itemType.getName(), query)){
                itemTypes.add(itemType);
            }
        }

        for (ItemType itemType: itemTypes){
            if (!isCategoryAdded(itemType.getCategoryId())){
                categories.add(getCategory(itemType.getCategoryId()));
            }

            if (!isItemAdded(itemType.getCategoryId(), itemType.getItemId())){
                items.add(getItem(itemType.getCategoryId(), itemType.getItemId()));
            }
        }

        System.out.println("\nSearch\n");
        System.out.println("\nCategories:\n"+ GsonUtils.getGson().toJson(categories)+"\n");
        System.out.println("\nItems:\n"+ GsonUtils.getGson().toJson(items)+"\n");
        System.out.println("\nItem Types:\n"+ GsonUtils.getGson().toJson(itemTypes)+"\n");

    }

    private Category getCategory(String categoryId){
        for (Category category: dataRepository.getAllCategories()){
            if (category.getId().equals(categoryId)){
                return category;
            }
        }
        return null;
    }

    private Item getItem(String categoryId, String itemId){
        for (Item item: dataRepository.getAllItems()){
            if (item.getCategoryId().equals(categoryId) && item.getId().equals(itemId)){
                return item;
            }
        }
        return null;
    }

    private boolean isCategoryAdded(String categoryId){
        for (Category category: categories){
            if (category.getId().equals(categoryId)){
                return true;
            }
        }
        return false;
    }

    private boolean isItemAdded(String categoryId, String itemId){
        for (Item item: items){
            if (item.getCategoryId().equals(categoryId) && item.getId().equals(itemId)){
                return true;
            }
        }
        return false;
    }

    private void clearSearchedData(){
        categories.clear();
        items.clear();
        itemTypes.clear();
    }

    public interface Action{
        void onListRefresh();
    }

    private boolean containsOrEquals(String source, String text){
        return source.toLowerCase().contains(text.toLowerCase()) || source.toLowerCase().equals(text.toLowerCase());
    }
}
