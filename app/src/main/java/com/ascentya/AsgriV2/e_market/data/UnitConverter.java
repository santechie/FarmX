package com.ascentya.AsgriV2.e_market.data;

public class UnitConverter {

    public static double convert(double value, String fromUnit, String toUnit){
        if (fromUnit.equals(DummyDataGenerator.Units.TON)
                && toUnit.equals(DummyDataGenerator.Units.KG)){
            return value * 1000d;
        }else if (fromUnit.equals(DummyDataGenerator.Units.KG)
                && toUnit.equals(DummyDataGenerator.Units.TON)){
            return value / 1000d;
        }else {
            return value;
        }
    }
}
