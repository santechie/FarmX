package com.ascentya.AsgriV2.Utils;

public class DeviceDataUtils {

    public static String formatLightValue(String value, String symbol){
        try {
            float valueFloat = Float.parseFloat(value) / 1000f;
            int valueInt = (int) valueFloat;
            if (valueFloat <= 1)
                return value;
            if (valueInt == valueFloat){
                return valueInt + symbol;
            }
            return String.format("%.1f", valueFloat) + symbol;
        }catch (Exception e){}
        return value;
    }
}
