package com.ascentya.AsgriV2.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.VarietyModel;

import java.util.ArrayList;
import java.util.List;


public class Webservice {

    static Context context;

    public Webservice(Context context) {
        this.context = context;
    }

    //public static String website_path = "https://demos.ascentya.in/asgri_v2/";
   // public static String website_path = "https://vrjaitraders.com/ard_farmx/";
    public static String website_path = "https://vrjaitraders.com/ard_farmx/";
    //public static String path = "http://demos.ascentya.in/api/user_login_updated/api/Authentication/";
    public static String path = "https://vrjaitraders.com/ard_farmx/api/user_login_updated/api/Authentication/";
   // public static String auth_path = "https://demos.ascentya.in/asgri_v2/api/Authentication/";
    public static String auth_path = "https://vrjaitraders.com/ard_farmx/api/Authentication/";
    //public static String api_path = "https://demos.ascentya.in/asgri_v2/api/";
    public static String api_path = "https://vrjaitraders.com/ard_farmx/api/";
    public static String access_path = api_path + "Access/";
    public static String control_measures_path = api_path + "controlMeasure/";
 //   public static String base_path = "https://demos.ascentya.in/asgri_v2/api/Agripedia/";
    public static String base_path = "https://vrjaitraders.com/ard_farmx/api/Agripedia/";

    public static String land_path = api_path + "land/";
    public static String crop_path = api_path + "crop/";
    public static String user_path = api_path + "user/";
    public static String satellite_image_service_path = api_path + "satellite_Image_Service/";
    public static final String utility_path = api_path + "Utility/";
    public static final String infection_path = api_path + "infection/";
    public static final String user_privilege = api_path + "user_Privilege/";

    public static String forumbase_path = "https://vrjaitraders.com/ard_farmx/api/Forum/";
    public static String buysellbase_path = "https://vrjaitraders.com/ard_farmx/api/Buy_sell/";
    public static String forumimagebase_path = "https://vrjaitraders.com/ard_farmx/uploads/forum_attachment/";
  //  public static String buysellimagebase_path = "https://demos.ascentya.in/asgri_v2/uploads/Products/";
    public static String buysellimagebase_path = "https://vrjaitraders.com/ard_farmx/uploads/Products/";
    public static String imgpath = "https://innowity.com/bubbles/";
    public static final List<Crops_Main> Data_crops = new ArrayList<>();
    public static final List<String> crops = new ArrayList<>();
    public static String Searchvalue = "none";
    public static String Searchicon = "none";
    public static String Search_id = "none";
    public static Integer lang_id = 100;
    public static String state_id = "0";
    public static String Login = auth_path + "login";
    public static String register = path + "registration";
    public static String register_f = auth_path + "registration_all";
    public static String edit_farmer = auth_path + "editfarmer";
    public static String get_farmer = auth_path + "getfarmerinfo";
    public static String getname_icon = base_path + "ciup";
    public static String getcrops = base_path + "bi";
    public static String getpassword = path + "forgotpassword";
    public static String getuserauthenticate = auth_path + "userauthenticate";
    public static String getrandomgen = auth_path + "/inactive_user";
    public static String getverifiy = auth_path + "/useractivation";
    public static String getverification_code = auth_path + "/getactivation_code";
    public static String gettxny = base_path + "tmy";
    public static String getvarities = base_path + "vars";
    public static String getcultivation = base_path + "cultivate";
    public static String getpostcultivation = base_path + "pc";
    public static String getfavcondition = base_path + "fc";
    public static String getnjutrientdeficiency = base_path + "nd";
    public static String getnjutrient = base_path + "nutrition";
   // public static String getpestssymtoms = "http://demos.ascentya.in/asgri_v2/api/PD/pd_ps";
    public static String getpestssymtoms = "https://vrjaitraders.com/ard_farmx/api/PD/pd_ps";
    public static String getphdssymtoms = "https://vrjaitraders.com/ard_farmx/api/PD/phd_sym";
    public static String getdiseasssymtoms = "https://vrjaitraders.com/ard_farmx/api/PD/pd_ds";
    public static String getpestscontrolmeasure = "https://vrjaitraders.com/ard_farmx/api/PD/pd_pcm";
    public static String getphdscontrolmeasure = "https://vrjaitraders.com/ard_farmx/api/PD/phd_cm";
    public static String getdiseasscontrolmeasure = "https://vrjaitraders.com/ard_farmx/api/PD/pd_dcm";
    public static String getpestsidentification = "https://vrjaitraders.com/ard_farmx/api/PD/pd_pid";
    public static String getphdsidentification = "https://vrjaitraders.com/ard_farmx/api/PD/phd_id";
    public static String getdideasidentification = "https://vrjaitraders.com/ard_farmx/api/PD/pd_did";
    public static String addland = base_path + "add_land";
    public static String addlandfarmx = base_path + "add_farmx_land";
    public static String addlandlist = base_path + "landcroplist";
    public static String getregisteredland = base_path + "landlist/";
    public static String deleteland = base_path + "deleteland";
    public static String addanimal = base_path + "addanimal";
    public static String animallist = base_path + "animallist/";
    public static String apply = base_path + "applypower";
    public static String add_activity = base_path + "add_activity";
    public static String update_activity = base_path + "update_activity";
    public static String add_activitylist = base_path + "activitylist/";
    public static String add_farmxcrops = base_path + "farmxcrops/";
    public static String add_completedactivitylist = base_path + "activitycompletedlist/";
    public static String add_lmalist = base_path + "getlma/";
    public static String get_cropscycle = base_path + "getcropscycle/";

    public static String getmemberlist = base_path + "memberlist/";
    public static String getincomelist = base_path + "incomelist/";
    public static String getexpenselist = base_path + "expenselist/";
    public static String update_payment = base_path + "updatepayment/";
    public static String getcroplist = base_path + "croplist/";
    public static String delete_activity = base_path + "deleteactivity";
    public static String complete_activity = base_path + "completeactivity";
    public static String delete_animal = base_path + "deleteanimal";

    // real time data


    public static String update_humidity = "https://api.thingspeak.com/channels/1139258/fields/2.json?api_key=S15EYR17BCXSS473&results=2";
    public static String update_pollution = "https://api.thingspeak.com/channels/1139258/fields/3.json?api_key=S15EYR17BCXSS473&results=2";
    public static String update_moisture = "https://api.thingspeak.com/channels/1139258/fields/4.json?api_key=S15EYR17BCXSS473&results=2";
    public static String update_light = "https://api.thingspeak.com/channels/1139258/fields/5.json?api_key=S15EYR17BCXSS473&results=2";
    public static String update_temperature = "https://api.thingspeak.com/channels/1139258/fields/1.json?api_key=S15EYR17BCXSS473&results=2";
    public static String update_ph = "https://api.thingspeak.com/channels/1139258/fields/6.json?api_key=S15EYR17BCXSS473&results=2";
    public static String update_crop = "https://api.thingspeak.com/channels/1139258/fields/7.json?api_key=S15EYR17BCXSS473&results=2";

    public static String updateland = base_path + "landupdate";
    public static String addmember = base_path + "addmember";
    public static String addcrop = base_path + "addcrop";
    public static String deletemember = base_path + "deletemember";
    public static String deleteincome = base_path + "deleteincome";
    public static String deleteexpense = base_path + "deleteexpense";
    public static String updatemember = base_path + "updatemember";
    public static String updateincome = base_path + "updateincome";
    public static String updateexpense = base_path + "updateexpense";
    public static String updatecrop = base_path + "updatecrop";
    public static String activity_list = base_path + "sowinglist";
    public static String finance_getall = base_path + "totalcost";
    public static String income_farmer = base_path + "income_farmer";


    public static String updateanimal = base_path + "updateanimal";
    public static String addincome = base_path + "addincome";
    public static String addexpense = base_path + "addexpense";
    public static String getcrop_realtimeupdate = base_path + "cropenvironmanelist/";
//    public static String Login = path + "/actions.php?action=user_login";

    // forum
    public static String getcat = forumbase_path + "category";
    public static String post_forum = forumbase_path + "addforum";
    public static String get_forum = forumbase_path + "getform";
    public static String get_addcommends = forumbase_path + "addcomment";
    public static String get_commends = forumbase_path + "/getcomment";
    public static String add_report = forumbase_path + "/addreport";
    public static String delete_comment = forumbase_path + "/deletecomment";
    public static String delete_forum = forumbase_path + "/deleteforum";
    public static String postfarmxactivity = base_path + "addactivityfarmx";
    public static String postfarmxsowing = base_path + "/addsoeingfarmx";
    public static String postfarmxirrigation = base_path + "/addirrigationfarmx";
    public static String postfarmxfertilizer = base_path + "/addfertilizerfarmx";
    public static String postfarmxintercultural = base_path + "/addinterculturalfarmx";
    public static String postfarmxharvest = base_path + "/addharvestfarmx";
    public static String getcrop_byid = base_path + "/getactivitycropbyid/";
    public static String getexpected_bycropid = base_path + "/getexpecteddate/";
    public static String getwarehouse_byplace = base_path + "/getnearestwarehouse/";
    public static String getveterinary_byplace = base_path + "/getveterinary/";
    public static String getpostharvest_byplace = base_path + "/getpostharvest/";
    public static String getactivity_title = base_path + "/land_preparation";
    public static String post_activity = base_path + "insertsowing";
    public static String up_activity = base_path + "updatesowing";
    public static String update_activity_status = base_path + "update_activity_status";
    public static String landservicecat = base_path + "land_services";
    public static String supervisor_list = base_path + "supervisorlist";
    public static String addzone_report = base_path + "/add_report";
    public static String addzone_remedy = base_path + "add_remedy";
    public static String reports_get = base_path + "landreportlist";
    public static String remedy_get = base_path + "landremedylist";
    public static String landzone_get = base_path + "landzone";
    public static String landzonecrop_get = base_path + "landzonecrop";
    public static String incomecat_get = base_path + "/incomecat";
    public static String farmerincome = base_path + "farmerincome";
    public static String schemes = base_path + "/schemes";
    public static String schemes_cat = base_path + "schemes_cat";
    public static String masterfield = base_path + "/masterfield";
    public static String addenvironment = base_path + "addenvironment";
    public static String mastersfromid = base_path + "mastersfromid";
    public static String valuesfrommaster = base_path + "/valuesfrommaster";
    public static String userprivileges = base_path + "userprivileges";


    //buysell
    public static String getbuysell_cat = buysellbase_path + "category";
    public static String add_buysellproduct = buysellbase_path + "addproduct";
    public static String add_sellproduct = buysellbase_path + "sellproduct";
    public static String delete_sellproduct = buysellbase_path + "deleteproduct";
    public static String update_sellproduct = buysellbase_path + "updateproduct";
    public static String get_buyproduct = buysellbase_path + "product";
    public static String add_cart = buysellbase_path + "addtocart";
    public static String cart_quantityupdate = buysellbase_path + "updatecart";
    public static String get_cart = buysellbase_path + "cartlist";
    public static String delete_cart = buysellbase_path + "deletecart";
    public static String clear_cart = buysellbase_path + "clearcheckout";
    public static String get_blocks = buysellbase_path + "getblocks";
    public static String get_village = buysellbase_path + "getvillages";
    public static String get_villagetn = buysellbase_path + "getvillagetn";
    public static String get_district = buysellbase_path + "getdistrict";
    public static String get_districttn = buysellbase_path + "getdistricttn";
    public static String get_searchedforum = buysellbase_path + "forumsearch";
    public static String get_fpolist = buysellbase_path + "getfpolist";
    public static String add_address = buysellbase_path + "address";
    public static String get_address = buysellbase_path + "getaddress";
    public static String delete_address = buysellbase_path + "deleteaddress";
    public static String orderlist = buysellbase_path + "orderlist";
    public static String valuesFromMasterLand = base_path + "valuesfrommasterland";

    public static final String getDeviceData = base_path + "get_device_data";
    public static final String getLandDeviceData = base_path + "get_land_device_data";
    public static final String getSoilTestRequests = utility_path + "get_soil_test_requests";
    public static final String getSoilTestLabs = utility_path + "get_soil_test_labs";
    public static final String getSoilTestTicket = utility_path + "get_soil_test_ticket";
    public static final String soilTestRequest = utility_path + "soil_test_request";
    public static final String getSoilTestResult = utility_path + "get_soil_test_result";

    public static final String getStates = base_path + "get_states";
    public static final String getDistrict = base_path + "get_districts";
    public static final String getVillages = base_path + "get_villages";
    public static final String getSoilType = land_path + "get_soil_types";
    public static final String getInfectionType = infection_path + "get_infection_types";
    public static final String getCropType =  crop_path + "get_crop_parts";

    public static final String postBugReport = base_path + "post_bug_report";

    public static final String guestLogin = base_path + "guest_login";

   // public static final String subscriptionPath = "https://demos.ascentya.in/asgri_v2/api/subscription/";
    public static final String subscriptionPath = "https://vrjaitraders.com/ard_farmx/api/subscription/";
    public static final String getSubscriptions = base_path + "get_subscriptions";
    public static final String getExpiredDetails = subscriptionPath + "get_subscription_detail";

    public static final String checkAccess = access_path + "check_access";

    public static final String adminPath = "https://vrjaitraders.com/ard_farmx/admin/index.php/";
    public static final String adminMain = adminPath + "/main";
    public static final String iotRequest = adminMain + "/iot_request";
    public static final String getUserPrivileges = access_path + "get_privileges";

    public static final String cropHistory = base_path + "crop_history"; // land_id
    public static final String updateCrop = base_path + "update_crop"; // lc_id
    public static final String addCrop = base_path + "add_crop";

    public static final String pests = control_measures_path + "pests";
    public static final String diseases = control_measures_path + "diseases";
    public static final String nutritionDeficiencies = control_measures_path + "nutrition_deficiencies";

    public static final String getLandDetail = land_path + "get_land_detail";

    public static final String sendOtp = user_path + "send_otp";
    public static final String validateOtp = user_path + "validate_otp";
    public static final String resetPassword = user_path + "reset_password";

    public static final String getWeatherForecast = satellite_image_service_path + "get_weather_forecast";

    public static final String submitInfection = infection_path + "submit";
    public static final String getInfections = infection_path + "get_infections";
    public static final String getPartInfections = infection_path + "get_part_infections";
    public static final String getInfectionRemedies = infection_path + "get_infection_remedies";
    public static final String submitInfectionRemedy = infection_path + "submit_remedy";
    public static final String updateInfectionRemedy = infection_path + "update_remedy";
    public static final String updateVoteRemedy = infection_path + "update_remedy_vote";
    public static final String findReportSuggestion = infection_path + "find_suggestion";

    public static final String getModulePrivileges = user_privilege + "get_privileges";

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        }
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setCrops(List<Crops_Main> crops){
        Data_crops.clear();
        Data_crops.addAll(crops);
    }

    public static List<Crops_Main> getCrops(){
        return Data_crops;
    }

    public static List<VarietyModel> getVarieties(String cropId){
        for (Crops_Main crop: getCrops()){
            if (crop.getCrop_id().equals(cropId)){
                return crop.getVarieties();
            }
        }
        return null;
    }

    public static VarietyModel getVariety(String varietyId){
        for (Crops_Main crop: getCrops()){
            if (crop.getVarieties() != null) {
                for (VarietyModel varietyModel : crop.getVarieties()) {
                    if (varietyModel.getId().equals(varietyId)) {
                        return varietyModel;
                    }
                }
            }
        }
        return null;
    }

    public static Crops_Main getCrop(String cropId){
        for (Crops_Main cropMain: Data_crops){
            if (cropMain.getCrop_id().equals(cropId)){
                return cropMain;
            }
        }
        return new Crops_Main();
    }

}
