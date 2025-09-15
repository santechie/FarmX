package com.ascentya.AsgriV2.Utils;

import com.ascentya.AsgriV2.Shared_Preference.Userobject;
import com.ascentya.AsgriV2.utility.model.SoilTestLab;

public class MyTextUtils {

    public static String getFormattedAddress(SoilTestLab soilTestLab) {
        return soilTestLab == null ? null :
                soilTestLab.getDoorNo() + ", " + (soilTestLab.getLandmark() == null ? "\n" : soilTestLab.getLandmark() + "\n")
                        + soilTestLab.getStreet() + ", " + soilTestLab.getArea() + "\n"
                        + soilTestLab.getVillageName() + ", " + soilTestLab.getDistrictName() + "\n"
                        + soilTestLab.getState();
    }

    public static String getFormattedAddress(Userobject userobject) {
        return userobject == null ? null :
                userobject.getStreet_name() + ",\n"
                        + userobject.getCity() + ",\n"
                        + userobject.getVillage() + " - "
                + userobject.getPincode();
    }
}
