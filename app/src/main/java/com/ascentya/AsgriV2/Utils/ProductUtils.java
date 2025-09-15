package com.ascentya.AsgriV2.Utils;

public class ProductUtils {

    public static String getPrice(String actualPrice){
        try {
            if (!actualPrice.equals("0"))
                return "Rs. " + actualPrice;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "NA";
    }

    public static Boolean isZero(String actualPrice){
        try {
            return actualPrice.equals("0");
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
