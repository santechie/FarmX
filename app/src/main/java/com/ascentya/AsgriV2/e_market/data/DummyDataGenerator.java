package com.ascentya.AsgriV2.e_market.data;

import com.ascentya.AsgriV2.e_market.data.model.Category;
import com.ascentya.AsgriV2.e_market.data.model.FilterGroup;
import com.ascentya.AsgriV2.e_market.data.model.FilterValue;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.model.Stock;

import java.util.ArrayList;
import java.util.List;

public class DummyDataGenerator {

    private static List<Stock> stocks;

    public static List<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("2", "Fruits"));
        categories.add(new Category("3", "Vegetables"));
        categories.add(new Category("4", "Flowers"));
        categories.add(new Category("5", "Grains"));
        return categories;
    }

    public static List<Item> getItems(String categoryId) {
        ArrayList<Item> items = new ArrayList<>();
        switch (categoryId) {
            case "1":
                items.addAll(getItems("2"));
                items.addAll(getItems("3"));
                items.addAll(getItems("4"));
                items.addAll(getItems("5"));
                break;
            case "2":
                items.add(new Item("1", categoryId, "Apple"));
                items.add(new Item("2", categoryId, "Orange"));
                items.add(new Item("3", categoryId, "Grapes"));
                break;
            case "3":
                items.add(new Item("1", categoryId, "Tomato"));
                items.add(new Item("2", categoryId, "Potato"));
                items.add(new Item("3", categoryId, "Onion"));
                break;
            case "4":
                items.add(new Item("1", categoryId, "Rose"));
                items.add(new Item("2", categoryId, "Lotus"));
                items.add(new Item("3", categoryId, "Jasmine"));
                break;
            case "5":
                items.add(new Item("1", categoryId, "Rice"));
                items.add(new Item("2", categoryId, "Wheat"));
                items.add(new Item("3", categoryId, "Oats"));
                break;
        }
        return items;
    }

    public static List<Item> getAllItems() {
        return getItems("1");
    }

    public static List<ItemType> getItemTypes(String categoryId, String itemId) {
        ArrayList<ItemType> itemTypes = new ArrayList<>();
        switch (categoryId) {

            case "2":
                switch (itemId) {
                    case "1":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Kashmir Apple"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Green Apple"));
                        break;

                    case "2":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Blood Orange"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Mandarin Orange"));
                        break;

                    case "3":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Red Globe"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Black Muscat"));
                        break;
                }
                break;

            case "3":
                switch (itemId) {
                    case "1":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Green tomato"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Roma tomato"));
                        break;

                    case "2":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Potato A"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Potato B"));
                        break;

                    case "3":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Onion A"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Onion B"));
                        break;
                }
                break;

            case "4":
                switch (itemId) {
                    case "1":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Rose A"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Rose B"));
                        break;

                    case "2":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Lotus A"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Lotus B"));
                        break;

                    case "3":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Jasmine A"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Jasmine B"));
                        break;
                }
                break;

            case "5":
                switch (itemId) {
                    case "1":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Normal"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Basmathi"));
                        break;

                    case "2":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Wheat A"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Wheat B"));
                        break;

                    case "3":
                        itemTypes.add(new ItemType("1", itemId, categoryId, "Oats A"));
                        itemTypes.add(new ItemType("2", itemId, categoryId, "Oats B"));
                        break;
                }
                break;
        }
        return itemTypes;
    }

    public static List<ItemType> getAllItemTypes(){

        ArrayList<ItemType> itemTypes = new ArrayList<>();

        itemTypes.addAll(getItemTypes("2", "1"));
        itemTypes.addAll(getItemTypes("2", "2"));
        itemTypes.addAll(getItemTypes("2", "3"));

        itemTypes.addAll(getItemTypes("3", "1"));
        itemTypes.addAll(getItemTypes("3", "2"));
        itemTypes.addAll(getItemTypes("3", "3"));

        itemTypes.addAll(getItemTypes("4", "1"));
        itemTypes.addAll(getItemTypes("4", "2"));
        itemTypes.addAll(getItemTypes("4", "3"));

        itemTypes.addAll(getItemTypes("5", "1"));
        itemTypes.addAll(getItemTypes("5", "2"));
        itemTypes.addAll(getItemTypes("5", "3"));

        return itemTypes;
    }

    public static List<Stock> getStocks() {

        final Places places = new Places();

        if (stocks != null)
            return stocks;

        stocks = new ArrayList<>();

        stocks.add(new Stock("1", "1", "1", "2", 1, Units.TON, 50000f, "10/11/2020", "12/11/2020", places.MADURAI.id, places.MADURAI.name));
        stocks.add(new Stock("2", "2", "1", "2", 2, Units.TON, 100000f, "11/11/2020", "13/11/2020", places.COIMBATORE.id, places.COIMBATORE.name));
        stocks.add(new Stock("3", "1", "1", "2", 4, Units.TON, 200000f, "12/11/2020", "14/11/2020", places.TRICHY.id, places.TRICHY.name));
        stocks.add(new Stock("4", "1", "2", "2", 500, Units.KG, 10000f, "11/11/2020", "13/11/2020", places.TRICHY.id, places.TRICHY.name));
        stocks.add(new Stock("5", "2", "2", "2", 1, Units.TON, 20000f, "12/11/2020", "14/11/2020", places.OOTY.id, places.OOTY.name));
        stocks.add(new Stock("6", "1", "2", "2", 2, Units.TON, 40000f, "13/11/2020", "15/11/2020", places.THANJAVUR.id, places.THANJAVUR.name));
        stocks.add(new Stock("7", "1", "3", "2", 1200, Units.KG, 12000f, "12/11/2020", "14/11/2020", places.OOTY.id, places.OOTY.name));
        stocks.add(new Stock("8", "2", "3", "2", 2.2f, Units.TON, 24000f, "13/11/2020", "15/11/2020", places.THANJAVUR.id, places.THANJAVUR.name));
        stocks.add(new Stock("9", "1", "3", "2", 4.4f, Units.TON, 48000f, "14/11/2020", "16/11/2020", places.COIMBATORE.id, places.COIMBATORE.name));

        stocks.add(new Stock("10", "1", "1", "3", 2, Units.TON, 20000f, "10/11/2020", "11/11/2020", places.MADURAI.id, places.MADURAI.name));
        stocks.add(new Stock("11", "2", "1", "3", 4, Units.TON, 40000f, "11/11/2020", "12/11/2020", places.THANJAVUR.id, places.THANJAVUR.name));
        stocks.add(new Stock("12", "1", "2", "3", 5, Units.TON, 50000f, "11/11/2020", "12/11/2020", places.TRICHY.id, places.TRICHY.name));
        stocks.add(new Stock("13", "2", "2", "3", 10, Units.TON, 100000f, "12/11/2020", "13/11/2020", places.OOTY.id, places.OOTY.name));
        stocks.add(new Stock("14", "1", "3", "3", 10, Units.TON, 50000f, "12/11/2020", "13/11/2020", places.OOTY.id, places.OOTY.name));
        stocks.add(new Stock("15", "2", "3", "3", 20, Units.TON, 100000f, "13/11/2020", "14/11/2020", places.COIMBATORE.id, places.COIMBATORE.name));

        stocks.add(new Stock("16", "1", "1", "4", 5, Units.TON, 3000f, "10/11/2020", "15/11/2020", places.MADURAI.id, places.MADURAI.name));
        stocks.add(new Stock("17", "2", "1", "4", 10, Units.TON, 6000f, "11/11/2020", "16/11/2020", places.COIMBATORE.id, places.COIMBATORE.name));
        stocks.add(new Stock("18", "1", "2", "4", 10, Units.TON, 5000f, "15/11/2020", "20/11/2020", places.TRICHY.id, places.TRICHY.name));
        stocks.add(new Stock("19", "2", "2", "4", 20, Units.TON, 10000f, "16/11/2020", "21/11/2020", places.MADURAI.id, places.MADURAI.name));
        stocks.add(new Stock("20", "1", "3", "4", 15, Units.TON, 1000f, "20/11/2020", "25/11/2020", places.OOTY.id, places.OOTY.name));
        stocks.add(new Stock("21", "2", "3", "4", 30, Units.TON, 2000f, "21/11/2020", "26/11/2020", places.TRICHY.id, places.TRICHY.name));

        stocks.add(new Stock("22", "1", "1", "5", 200, Units.TON, 200000f, "15/11/2020", "18/11/2020", places.MADURAI.id, places.MADURAI.name));
        stocks.add(new Stock("23", "2", "2", "5", 500, Units.TON, 500000f, "18/11/2020", "21/11/2020", places.TRICHY.id, places.TRICHY.name));
        stocks.add(new Stock("24", "1", "3", "5", 1000, Units.TON, 100000f, "21/11/2020", "24/11/2020", places.OOTY.id, places.OOTY.name));

        return stocks;
    }

    public static Stock getStock(String stockId){
        for (Stock stock: getStocks()){
            if (stock.getId().equals(stockId)){
                return stock;
            }
        }
        return null;
    }

    public static List<FilterGroup> getFilterGroups(){

        final Places places = new Places();
        ArrayList<FilterGroup> filterGroups = new ArrayList<>();

        //

        FilterValue organicFv = new FilterValue("1", "Organic", "");
        FilterValue nonOrganicFv = new FilterValue("2", "Non Organic", "");

        ArrayList<FilterValue> organicList = new ArrayList<>();
        organicList.add(organicFv);
        organicList.add(nonOrganicFv);

        FilterGroup organicFilterGroup = new FilterGroup("1", "Type", Constants.FilterTypes.SINGLE);
        organicFilterGroup.setFilterValues(organicList);
        filterGroups.add(organicFilterGroup);

        //

        FilterValue maduraiFv = new FilterValue(places.MADURAI.id, places.MADURAI.name, "");
        FilterValue trichyFv = new FilterValue(places.TRICHY.id, places.TRICHY.name, "");
        FilterValue kovai = new FilterValue(places.THANJAVUR.id, places.THANJAVUR.name, "");
        FilterValue thanjavur = new FilterValue(places.COIMBATORE.id, places.COIMBATORE.name, "");
        FilterValue ramnad = new FilterValue(places.OOTY.id, places.OOTY.name, "");

        ArrayList<FilterValue> placeList = new ArrayList<>();
        placeList.add(maduraiFv);
        placeList.add(trichyFv);
        placeList.add(kovai);
        placeList.add(thanjavur);
        placeList.add(ramnad);

        FilterGroup placeFilterGroup = new FilterGroup("2", "Place", Constants.FilterTypes.MULTI);
        placeFilterGroup.setFilterValues(placeList);
        filterGroups.add(placeFilterGroup);

        //

        FilterValue priceStartFv = new FilterValue("1", "1000", Constants.FilterValueTypes.NUMBER);
        FilterValue priceEndFv = new FilterValue("2", "100000", Constants.FilterValueTypes.NUMBER);

        ArrayList<FilterValue> priceList = new ArrayList<>();
        priceList.add(priceStartFv);
        priceList.add(priceEndFv);

        FilterGroup priceFilterGroup = new FilterGroup("3", "Price", Constants.FilterTypes.RANGE);
        priceFilterGroup.setFilterValues(priceList);
        filterGroups.add(priceFilterGroup);

        //

        FilterValue dateStartFv = new FilterValue("1", "10/11/2020", Constants.FilterValueTypes.DATE);
        FilterValue dateEntFv = new FilterValue("2", "24/11/2020", Constants.FilterValueTypes.DATE);

        ArrayList<FilterValue> dateList = new ArrayList<>();
        dateList.add(dateStartFv);
        dateList.add(dateEntFv);

        FilterGroup dateFilterGroup = new FilterGroup("4", "Date", Constants.FilterTypes.RANGE);
        dateFilterGroup.setFilterValues(dateList);
        filterGroups.add(dateFilterGroup);

        return filterGroups;
    }

    public class Units {
        public static final String KG = "KG";
        public static final String TON = "TON";
    }

    public static class Place {
        String id;
        String name;

        public Place(String id, String name) {
            this.id = id;
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
    }

    public static class Places {
        public final Place MADURAI = new Place("1", "Madurai");
        public final Place TRICHY = new Place("2", "Trichy");
        public final Place COIMBATORE = new Place("3", "Coimbatore");
        public final Place OOTY = new Place("3", "Ooty");
        public final Place THANJAVUR = new Place("4", "Thanjavur");
    }
}
