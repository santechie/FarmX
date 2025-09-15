package com.ascentya.AsgriV2.data;

public class Modules {

    public static class Module{
        public String value;
        public void setValue(String value) {
            this.value = value;
        }
    }

    public static final Module AGRIPEDIA = getModule("agripedia");
    public static final Module MY_FARM = getModule("my_farm");
    public static final Module CHATS = getModule("chats");
    public static final Module SCHEME = getModule("scheme");
    public static final Module COMMUNITY = getModule("community");
    public static final Module MARKET_NEWS = getModule("market_news");
    public static final Module BUY_AND_SELL = getModule("buy_and_sell");
    public static final Module HISTORY = getModule("history");
    public static final Module EMARKET = getModule("emarket");
    public static final Module UTILITY = getModule("utility");

    private static Module getModule(String value){
        Module module = new Module();
        module.value = value;
        return module;
    }
}
