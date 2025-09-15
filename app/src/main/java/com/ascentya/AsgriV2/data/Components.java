package com.ascentya.AsgriV2.data;

public class Components{

    public static class Component{
        public Modules.Module module;
        public String value;

        public Modules.Module getModule() {
            return module;
        }

        public void setModule(Modules.Module module) {
            this.module = module;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Agripedia{
        public static final Component INFO = getComponent("info", Modules.AGRIPEDIA);
        public static final Component CULTIVATION = getComponent("cultivation", Modules.AGRIPEDIA);
        public static final Component PEST = getComponent("pest", Modules.AGRIPEDIA);
        public static final Component DISEASE = getComponent("disease", Modules.AGRIPEDIA);
        public static final Component PHD = getComponent("phd", Modules.AGRIPEDIA);
    }

    public static class MyFarm{

        public static final Component LAND = getComponent("my_lands", Modules.MY_FARM);
        public static final Component CROP = getComponent("my_crops", Modules.MY_FARM);
        public static final Component ZONE = getComponent("my_zones", Modules.MY_FARM);
        public static final Component REAL_TIME_DATA = getComponent("real_time_data", Modules.MY_FARM);
        public static final Component PEST_DISEASE = getComponent("pest_and_disease", Modules.MY_FARM);
        public static final Component PEST_DISEASE_DETECTION = getComponent("pest_disease_detection", Modules.MY_FARM);
        public static final Component REMEDY = getComponent("remedy", Modules.MY_FARM);
        public static final Component MEMBERS = getComponent("members", Modules.MY_FARM);
        public static final Component MY_STOCK = getComponent("my_stocks", Modules.MY_FARM);
        public static final Component ACTIVITY = getComponent("activities", Modules.MY_FARM);
        public static final Component FINANCE = getComponent("finance", Modules.MY_FARM);
        public static final Component INCOME = getComponent("income", Modules.MY_FARM);
        public static final Component EXPENSE = getComponent("expense", Modules.MY_FARM);
    }

    public static class Chat{
        public static final Component CHAT = getComponent("chats", Modules.CHATS);
    }

    public static class Scheme{
        public static final Component FARM_INPUT = getComponent("farm_inputs", Modules.SCHEME);
        public static final Component EQUIPMENT_LOAN = getComponent("equipment_loan", Modules.SCHEME);
        public static final Component COLD_STORAGE_WAREHOUSE = getComponent("cold_storage_warehouse", Modules.SCHEME);
        public static final Component INFRASTRUCTURE = getComponent("infrastructure", Modules.SCHEME);
        public static final Component CROP_LOAN = getComponent("crop_loan", Modules.SCHEME);
        public static final Component ANIMAL_HUSBANDARY = getComponent("animals_husbandary", Modules.SCHEME);
        public static final Component GOLD_LOAN = getComponent("gold_loan", Modules.SCHEME);
        public static final Component OTHER_LOAN = getComponent("other_loans", Modules.SCHEME);
    }

    public static class Community{
        public static final Component MY_FORUM = getComponent("my_forum", Modules.COMMUNITY);
        public static final Component ALL = getComponent("all", Modules.COMMUNITY);
        public static final Component AGRICULTURE = getComponent("agriculture", Modules.COMMUNITY);
        public static final Component BUSINESS = getComponent("business", Modules.COMMUNITY);
        public static final Component SALES = getComponent("sales", Modules.COMMUNITY);
        public static final Component BIOTECH = getComponent("biotech", Modules.COMMUNITY);
        public static final Component PESTS = getComponent("pests", Modules.COMMUNITY);
        public static final Component DISEASE = getComponent("disease", Modules.COMMUNITY);
        public static final Component YIELD = getComponent("yield", Modules.COMMUNITY);
        public static final Component HARVEST = getComponent("harvest", Modules.COMMUNITY);
        public static final Component OTHER = getComponent("other", Modules.COMMUNITY);
    }

    public static class MarketNews{
        public static final Component MARKET_NEWS = getComponent("market_news", Modules.MARKET_NEWS);
    }

    public static class BuyAndSell{
        public static final Component BUY = getComponent("buy", Modules.BUY_AND_SELL);
        public static final Component SELL = getComponent("sell", Modules.BUY_AND_SELL);
        public static final Component ORDERS = getComponent("orders", Modules.BUY_AND_SELL);
    }

    public static class History{
        public static final Component HISTORY = getComponent("history", Modules.HISTORY);
    }

    public static class Utility{
        public static final Component FERTILIZER_CALCULATOR = getComponent("fertilizer_calculator", Modules.UTILITY);
        public static final Component SOIL_TEST = getComponent("soil_test", Modules.UTILITY);
        public static final Component WATER_TEST = getComponent("water_test", Modules.UTILITY);
        public static final Component FARMX_FACILITY_CENTER = getComponent("farmx_facility_center", Modules.UTILITY);
    }

    private static Component getComponent(String value, Modules.Module module){
        Component component = new Component();
        component.module = module;
        component.value = value;
        return component;
    }

    public static Component findComponent(String value, Modules.Module module){
        return getComponent(value, module);
    }
}
