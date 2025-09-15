package com.ascentya.AsgriV2.e_market.data.repositeries;

import android.content.Context;

import com.ascentya.AsgriV2.e_market.data.DummyDataGenerator;
import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import java.util.ArrayList;
import java.util.List;

public class DataRepository extends BaseRepository{

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<ItemType> itemTypes = new ArrayList<>();

    public DataRepository(Context context) {
        super(context);
    }

    private void checkCategoryData(){
        if (categories.isEmpty())
            categories.addAll(DummyDataGenerator.getCategories());
    }

    private void checkItemData(){
        if (items.isEmpty())
            items.addAll(DummyDataGenerator.getAllItems());
    }

    private void checkItemTypeData(){
        if (itemTypes.isEmpty())
            itemTypes.addAll(DummyDataGenerator.getAllItemTypes());
    }

    public List<Category> getAllCategories(){
        checkCategoryData();
        return categories;
    }

    public List<Item> getAllItems(){
        checkItemData();
        return items;
    }

    public List<ItemType> getAllItemTypes(){
        checkItemTypeData();
        return itemTypes;
    }

    public List<Item> getItems(String categoryId){
        checkItemData();
        ArrayList<Item> items = new ArrayList<>();
        for (Item item: getAllItems()){
            if (item.getCategoryId().equals(categoryId)){
                items.add(item);
            }
        }
        return items;
    }

    public Item getItem(String categoryId, String itemId){
        for (Item item: getItems(categoryId)){
            if (item.getId().equals(itemId))
                return item;
        }
        return null;
    }

    public Category getCategory(String categoryId){
        for (Category category: getAllCategories()){
            if (category.getId().equals(categoryId)){
                return category;
            }
        }
        return null;
    }

    public ItemType getItemType(String categoryId, String itemId, String itemTypeId){
        List<ItemType> itemTypes = getItemTypes(categoryId, itemId);
        for (ItemType itemType: itemTypes){
            if (itemType.getCategoryId().equals(categoryId)
                    && itemType.getItemId().equals(itemId)
                        && itemType.getId().equals(itemTypeId)){
                return itemType;
            }
        }
        return null;
    }

    public List<ItemType> getItemTypes(String categoryId, String itemId){
        checkItemTypeData();
        ArrayList<ItemType> itemTypes = new ArrayList<>();
        for (ItemType itemType: getAllItemTypes()){
            if (itemType.getItemId().equals(itemId)
            && itemType.getCategoryId().equals(categoryId)){
                itemTypes.add(itemType);
            }
        }
        return itemTypes;
    }

    private static DataRepository instance;

    public static DataRepository getInstance(Context context){
        if (instance == null)
            instance = new DataRepository(context);
        return instance;
    }

}
