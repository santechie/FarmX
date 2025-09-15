package com.ascentya.AsgriV2.data;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.DrawableRes;

public class Constants {

    public static final String BUY_PRODUCT = "buy_product";

    public static final String SAMPLE_NUMBER = "+917305934168";

    public static final String MOBILE_NUMBER = "mobile_number";

    public static final String TEMPERATURE = "Temperature";
    public static final String TEMPERATURE_SYMBOL = String.valueOf('\u00B0') + "C";
    public static final String HUMIDITY = "Humidity";
    public static final String HUMIDITY_SYMBOL = String.valueOf('%');
    public static final String PH = "pH";
    public static final String PH_SYMBOL = "";
    public static final String MOISTURE = "Moisture";
    public static final String MOISTURE_SYMBOL = String.valueOf('%');
    public static final String VISIBILITY = "Visibility";
    public static final String VISIBILITY_SYMBOL = "K";
    public static final char DEGREE = '\u00B0';

    public static class SoilTestRequestStatus {
        public static final String OPEN = "open";
        public static final String CLOSED = "closed";
        public static final String PICKED = "picked";
    }

    public static class TestType {
        public String name, type;

        public TestType(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static class TestTypes {
        public static final TestType fertilizerCalculator = new TestType("Fertilizer Calculator", "fertilizer_calculator");
        public static final TestType SoilTest = new TestType("Soil Test", "soil_test");
        public static final TestType WaterTest = new TestType("Water Test", "water_test");
        public static final TestType facilityCenter = new TestType("FarmX Facility Center", "facility_center");
    }

    public static class SoilTestResultFields {

        public static class Base {
            String name;
            int icon;

            public Base(@DrawableRes int iconId, String name) {
                icon = iconId;
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIcon() {
                return icon;
            }

            public void setIcon(int icon) {
                this.icon = icon;
            }
        }

        public static final Base PH = new Base(R.drawable.ic_ph, "PH");
        public static final Base PHOSPHORUS = new Base(R.drawable.ic_phosphorus, "Phosphorus");
        public static final Base SALINITY = new Base(R.drawable.ic_salinity, "Salinity");
        public static final Base SODIUM = new Base(R.drawable.ic_sodium, "Sodium");
        public static final Base SULPHUR = new Base(R.drawable.ic_sulphur, "Sulphur");
        public static final Base NITROGEN = new Base(R.drawable.ic_nitrogen, "Nitrogen");
        public static final Base CALCIUM = new Base(R.drawable.ic_calcium, "Calcium");
        public static final Base MANGANESE = new Base(R.drawable.ic_manganese__1_, "Manganese");
        public static final Base COPPER = new Base(R.drawable.ic_copper, "Copper");
        public static final Base POTASSIUM = new Base(R.drawable.ic_potassium, "Potassium");
        public static final Base MAGNESIUM = new Base(R.drawable.ic_magnesium, "Magnesium");
        public static final Base ZINC = new Base(R.drawable.ic_zinc, "Zinc");
        public static final Base IRON = new Base(R.drawable.ic_iron, "Iron");
        public static final Base BORON = new Base(R.drawable.ic_boron, "Boron");
        public static final Base CARBONATE = new Base(R.drawable.ic_carbonate, "Carbonate");
        public static final Base BICARBONATE = new Base(R.drawable.ic_bicarbonate, "bicarbonate");
        public static final Base SULFATE = new Base(R.drawable.ic_sulphur, "Sulfate");
        public static final Base CHLORIDE = new Base(R.drawable.ic_chloride, "Chloride");
        public static final Base NITRATE = new Base(R.drawable.ic_nitrogen, "Nitrate");
        public static final Base CONDUCTIVITY = new Base(R.drawable.ic_conductivity, "Conductivity");
        public static final Base HARDNESS = new Base(R.drawable.ic_hardness, "Hardness");
        public static final Base ALKALINITY = new Base(R.drawable.ic_alkalinity, "Alkalinity");
        public static final Base DISSOLVED_SALTS = new Base(R.drawable.ic_salts, "Dissolved Salts");
        public static final Base SAR = new Base(R.drawable.ic_sar, "SAR");

    }

    public static enum UserType {
        GUEST, REGISTERED, PAID
    }

    public static class WaterTestResultFields {

        //public static final SoilTestResultFields.Base CALCIUM = new SoilTestResultFields.Base(R.drawable.ic_calcium, "Calcium (Ca)")
    }

    public static class Broadcasts {

        public static final String ACTIVITY_UPDATE = "activity_update";
        public static final String LAND_UPDATE = "land_update";
        public static final String ZONE_UPDATE = "zone_update";
        public static final String PEST_UPDATE = "pest_update";
    }

    public static class Activities{

        public static int getActivityImages(String serviceId){
            switch (serviceId){
                case "1": return R.drawable.ic_soil_preparation;
                case "2": return R.drawable.land;
                case "3": return R.drawable.ic_water_analysis;
                case "4": return R.drawable.ic_cultivation;
                case "5": return R.drawable.ic_post_harvest;
            }
            return 0;
        }

        public static String getActivityName(String serviceId){
            switch (serviceId){
                case "1": return "Soil Preparation";
                case "2": return "Land Preparation";
                case "3": return "Water Analysis";
                case "4": return "Cultivation";
                case "5": return "Post Harvest";
            }
            return "";
        }
    }

    public static class ActivityStatus{
        public static final String NOT_COMPLETED = "1";
        public static final String COMPLETED = "2";
    }

    public static class ActivityTypes {

        public static class ActivityType {

            String id, name, type;

            public static ActivityType get(String id, String name, String type) {
                ActivityType activityType = new ActivityType();
                activityType.id = id;
                activityType.name = name;
                activityType.type = type;
                return activityType;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getType() {
                return type;
            }
        }

        public static ActivityType getActivityType(String serviceId) {
            switch (serviceId) {
                default:
                    return controlMeasures;
                case "1":
                    return SoilPreparation;
                case "2":
                    return LandPreparation;
                case "3":
                    return WaterAnalysis;
                case "4":
                    return Cultivation;
                case "5":
                    return PostHarvest;
            }
        }

        public static final ActivityType controlMeasures =
                ActivityType.get("0", "Control Measures", "control_measures");

        public static final ActivityType SoilPreparation =
                ActivityType.get("1", "Soil Preparation", "soil_preparation");

        public static final ActivityType LandPreparation =
                ActivityType.get("2", "Land Preparation", "land_preparation");

        public static final ActivityType WaterAnalysis =
                ActivityType.get("3", "Water Analysis", "water_analysis");

        public static final ActivityType Cultivation =
                ActivityType.get("4", "Cultivation", "cultivation");

        public static final ActivityType PostHarvest =
                ActivityType.get("5", "Post Harvest", "post_harvest");

    }

    //crop report type
    public static final class ReportType{
        private String ctname, ctvalue;

        public ReportType(String ctname, String ctvalue){
            this.ctname = ctname;
            this.ctvalue = ctvalue;
        }

        public String getName() {
            return ctname;
        }

        public void setName(String ctname) {
            this.ctname = ctname;
        }

        public String getValue() {
            return ctvalue;
        }

        public void setValue(String value) {
            this.ctvalue = ctvalue;
        }

        @Override
        public String toString() {
            return ctname;
        }

        public static ReportType get(String ctname, String ctvalue){
            return new ReportType(ctname, ctvalue);
        }
    }

    public static final class ReportTypes{

        public static final ReportType PEST = ReportType.get("Pest", "pest");
        public static final ReportType DISEASE = ReportType.get("Disease", "disease");
        public static final ReportType NUTRITION_DEFICIENCY = ReportType.get("Nutrition Deficiency", "nutrition_deficiency");

        public static final List<ReportType> ALL = Arrays.asList(PEST, DISEASE, NUTRITION_DEFICIENCY);

        public static ReportType get(String ctvalue){
            if (ctvalue != null) {
                for (ReportType reportType : ALL) {
                    if (reportType.ctvalue.equals(ctvalue)){
                        return reportType;
                    }
                }
            }
            return null;
        }

    }
/////////////////////////////////////////////////////////
    public static final class CropPart{
        private String cpname, cpvalue;

        public CropPart(String cpname, String cpvalue){
            this.cpname = cpname;
            this.cpvalue = cpvalue;
        }

        public String getName() {
            return cpname;
        }

        public void setName(String cpname) {
            this.cpname = cpname;
        }

        public String getValue() {
            return cpvalue;
        }

        public void setValue(String cpvalue) {
            this.cpvalue = cpvalue;
        }

        @Override
        public String toString() {
            return cpname;
        }

        public static CropPart get(String cpname, String cpvalue){
            return new CropPart(cpname, cpvalue);
        }


    }

    public static final class CropParts{
        public static final CropPart FLOWER = CropPart.get("Flower", "flower");
        public static final CropPart BUD = CropPart.get("Bud", "bud");
        public static final CropPart LEAF = CropPart.get("Leaf", "leaf");
        public static final CropPart STEM = CropPart.get("Stem", "stem");
        public static final CropPart ROOT = CropPart.get("Root", "root");
        public static final CropPart OTHER = CropPart.get("Other", "other");

        public static final List<CropPart> ALL = Arrays.asList(FLOWER, BUD, LEAF, STEM, ROOT, OTHER);

        public static CropPart get(String cpvalue){
            if (cpvalue != null) {
                for (CropPart cropPart : ALL) {
                    if (cropPart.cpvalue.equals(cpvalue)){
                        return cropPart;
                    }
                }
            }
            return null;
        }
    }

    public static String getControlMeasureLinks(String report){
        switch (report){
            case "pest": return Webservice.pests;
            case "disease": return Webservice.diseases;
            case "nutrition_deficiency": return Webservice.nutritionDeficiencies;
        }
        return null;
    }

    public static class RemedyRate{
        public static final int NONE = 0;
        public static final int USEFUL = 1;
        public static final int NOT_USEFUL = -1;

        public static boolean isNone(String rate){
            return rate == null || rate.equals(String.valueOf(NONE));
        }
        public static boolean isUseful(String rate){
            return rate == null || rate.equals(String.valueOf(USEFUL));
        }
        public static boolean isNotUseful(String rate){
            return rate == null || rate.equals(String.valueOf(NOT_USEFUL));
        }


    }
}
