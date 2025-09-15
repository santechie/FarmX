package com.ascentya.AsgriV2.e_market.data;

import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.repositeries.DataRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DataFilterManager {

    // CategoryId, ItemIdArrayList
    private HashMap<String, ArrayList<String>> selectedItems = new HashMap<>();

    // CategoryId, <ItemId, <ItemTypeId>>
    private HashMap<String, HashMap<String, ArrayList<String>>> selectedItemTypes = new HashMap<>();

    private HashMap<String, List<String>> filters = new HashMap<>();

    public void selectAll(DataRepository dataRepository){
        for (Category category: dataRepository.getAllCategories()){
            List<Item> items = dataRepository.getItems(category.getId());
            for (Item item: items){
                List<ItemType> itemTypes = dataRepository.getItemTypes(category.getId(), item.getId());
                if (itemTypes == null || itemTypes.isEmpty()){
                    setItemSelected(category.getId(), item.getId(), true);
                }else {
                    for (ItemType itemType: itemTypes){
                        setItemTypeSelected(category.getId(), item.getId(), itemType.getId(), true);
                    }
                }
            }
        }
    }

    public void setItemSelected(String categoryId, String itemId, boolean selected){
        ArrayList<String> itemsList = selectedItems.get(categoryId);
        if (selected){
            if (itemsList == null){
                itemsList = new ArrayList<>();
            }
            if (!itemsList.contains(itemId))
                itemsList.add(itemId);
            selectedItems.put(categoryId, itemsList);
//            System.out.println("Selected Items: "+ GsonUtils.getGson().toJson(selectedItems));
        }else {
            if (itemsList != null){
                itemsList.remove(itemId);
            }
        }
    }

    public boolean isItemSelected(String categoryId, String itemId) {
//        System.out.println("Get Selected Items: "+ GsonUtils.getGson().toJson(selectedItems));
        if (selectedItems.containsKey(categoryId)){
            ArrayList<String> itemList = selectedItems.get(categoryId);
            if (itemList != null){
                return itemList.contains(itemId);
            }
        }
        return false;
    }

    public void setItemTypeSelected(String categoryId, String itemId, String itemTypeId, boolean selected){
        HashMap<String, ArrayList<String>> itemList = selectedItemTypes.get(categoryId);
        if (selected){
            if (itemList == null){
                itemList = new HashMap<>();
            }
            ArrayList<String> itemTypeList = itemList.get(itemId);
            if (itemTypeList == null){
                itemTypeList = new ArrayList<>();
            }
            if (!itemTypeList.contains(itemTypeId)){
                itemTypeList.add(itemTypeId);
            }
            itemList.put(itemId, itemTypeList);
            selectedItemTypes.put(categoryId, itemList);
        }else {
            if (itemList != null){
                ArrayList<String> itemTypeList = itemList.get(itemId);
                itemTypeList.remove(itemTypeId);
            }
        }
//        System.out.println("Item Type: "+GsonUtils.getGson().toJson(selectedItemTypes));
    }

    public boolean isItemTypeSelected(String categoryId, String itemId, String itemTypeId) {
//        System.out.println("Get Item Type: "+GsonUtils.getGson().toJson(selectedItemTypes));
        HashMap<String, ArrayList<String>> itemList = selectedItemTypes.get(categoryId);
        if (itemList != null){
            ArrayList<String> itemTypeList = itemList.get(itemId);
            if (itemTypeList != null)
                return itemTypeList.contains(itemTypeId);
        }
        return false;
    }

    private static DataFilterManager instance;

    public static DataFilterManager getInstance() {
        if (instance == null)
            instance = new DataFilterManager();
        return instance;
    }

    public void setFilter(String groupId, String ...values){
        filters.put(groupId, Arrays.asList(values));
    }

    public List<String> getFilter(String groupId){
        if (hasFilters(groupId)){
            return filters.get(groupId);
        }
        return Collections.emptyList();
    }

    public boolean hasFilters(String groupId){
        return filters.containsKey(groupId);
    }

    public boolean hasAnyFilters(){
        boolean isEmpty = true;

        for (int i=0; i<selectedItems.size(); i++){
            String categoryId = (String) selectedItems.keySet().toArray()[i];
            for (int j=0; j<selectedItems.get(categoryId).size(); j++){
                isEmpty = selectedItems.get(selectedItems.keySet().toArray()[i]).get(j).isEmpty() && isEmpty;
            }
        }

        for (int i=0; i<selectedItemTypes.size(); i++){
            String categoryId = (String) selectedItemTypes.keySet().toArray()[i];
            HashMap<String, ArrayList<String>> itemList = selectedItemTypes.get(categoryId);
            if (itemList == null)
                continue;
            for (int j=0; j<itemList.size(); j++){
                String itemId = (String) itemList.keySet().toArray()[j];
                ArrayList<String> itemTypesId = itemList.get(itemId);
                isEmpty = isEmpty && (itemTypesId == null || itemTypesId.isEmpty());
            }
        }

        System.out.println("hasAnyFilters: "+!isEmpty);
        return !isEmpty;
    }

    public HashMap<String, ArrayList<String>> getCatItem(){
        return selectedItems;
    }

    public HashMap<String, HashMap<String, ArrayList<String>>> getItemItemType(){
        return selectedItemTypes;
    }
}
