package com.ascentya.AsgriV2.Utils;

import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.DeviceData;
import com.ascentya.AsgriV2.Models.LandDeviceData;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.my_farm.model.CropHistory;
import com.ascentya.AsgriV2.my_farm.model.LandDetail;
import com.ascentya.AsgriV2.my_farm.model.RemedyItem;
import com.ascentya.AsgriV2.my_farm.model.ReportDetail;
import com.ascentya.AsgriV2.my_farm.model.ReportItem;
import com.ascentya.AsgriV2.my_farm.model.ReportSuggestion;
import com.ascentya.AsgriV2.my_farm.model.WeatherForecast;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class ApiHelper {

    public static final Type weatherForecastListType = new TypeToken<List<WeatherForecast>>(){}.getType();
    public static final Type infectionDetailListType = new TypeToken<List<ReportDetail>>(){}.getType();
    public static final Type infectionDetailItemListType = new TypeToken<List<ReportItem>>(){}.getType();
    public static final Type infectionRemedyItemListType = new TypeToken<List<RemedyItem>>(){}.getType();
    public static final Type reportSuggestionListType = new TypeToken<List<ReportSuggestion>>(){}.getType();

    public static void loadLands(String userId, LandAction action){


        AndroidNetworking.get(Webservice.add_farmxcrops + userId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("dsfkdsjfldsjlfjs"+jsonObject);
                LogUtils.log(getClass().getSimpleName(), "Load Lands: " + jsonObject);
                ArrayList<Maincrops_Model> Data = new ArrayList<>();
                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONArray mainCropJsonArray = jsonArray.getJSONObject(i).optJSONArray("maincrop");
                            String mainCropString = mainCropJsonArray == null ? "" : mainCropJsonArray.toString();

                            JSONArray interCropJsonArray = jsonArray.getJSONObject(i).optJSONArray("intercrop");
                            String interCropString = interCropJsonArray == null ? "" : interCropJsonArray.toString();

                            Maincrops_Model obj = new Maincrops_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("id"));
                            obj.setLand_name(jsonArray.getJSONObject(i).optString("land_name"));
                            obj.setDistrict(jsonArray.getJSONObject(i).optString("district"));
                            obj.setTaluk(jsonArray.getJSONObject(i).optString("taluk"));
                            obj.setAcre_count(jsonArray.getJSONObject(i).optString("acre_count"));
                            obj.setSoiltype(jsonArray.getJSONObject(i).optString("soiltype"));
                            obj.setMaincrop(mainCropString);
                            obj.setMainCropsString(mainCropString);
                            obj.setIntercrop(interCropString);
                            obj.setInterCropsString(interCropString);
                            obj.setAnual_revenue(jsonArray.getJSONObject(i).optString("anual_revenue"));
                            obj.setIrrigation_type(jsonArray.getJSONObject(i).optString("irrigation_type"));
                            obj.setGovt_scheme(jsonArray.getJSONObject(i).optString("govt_scheme"));
                            obj.setLive_stocks(jsonArray.getJSONObject(i).optString("live_stocks"));
                            obj.setSoilhealth_card(jsonArray.getJSONObject(i).optString("soilhealth_card"));
                            obj.setOrganic_farmer(jsonArray.getJSONObject(i).optString("organic_farmer"));
                            obj.setExport(jsonArray.getJSONObject(i).optString("export"));
                            obj.setFieldId(jsonArray.getJSONObject(i).optString("field_id"));

                            if (mainCropJsonArray != null && mainCropJsonArray.length() != 0){
                                JSONObject mainCropObj = mainCropJsonArray.getJSONObject(0);
                                Maincrops_Model mainCropModel = obj.Clone();
                                mainCropModel.setMaincrop(mainCropObj.optString("crop_id"));
                                mainCropModel.setVarietyId(mainCropObj.optString("variety_id"));
                                Data.add(mainCropModel);
                            }else {
                                Data.add(obj);
                            }

                            /*if (interCropArray != null){
                                for (int m=0; m<interCropArray.length(); m++){
                                    JSONObject interCropObj = interCropArray.getJSONObject(m);
                                    Maincrops_Model interCropModel = obj.Clone();
                                    interCropModel.setIntercrop(interCropObj.optString("crop_id"));
                                    System.out.println("Inter Crop: " + interCropModel.getIntercrop());
                                    Data.add(interCropModel);
                                }
                            }*/

                            //Data.add(obj);
                        }

                    }

                    action.onLoadComplete(jsonObject, Data, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        action.onLoadComplete(jsonObject, Data, true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(ANError anError) {
                try {
                    action.onLoadComplete(null, new ArrayList<>(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void landDetails(String landId, LandDetailAction action){
        AndroidNetworking.post(Webservice.getLandDetail)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status")){
                        action.onResult(response,true, GsonUtils.fromJson(response.toString(), LandDetail.class));
                    }else {
                        action.onResult(response,false, null);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    action.onResult(response,false, null);
                }
            }

            @Override
            public void onError(ANError anError) {
                action.onResult(null,false, null);
            }
        });
    }

    public static void loadZones(String landId, String cropId, String lcId, String user_id, ZoneAction action){
        ArrayList<Zone_Model> zones = new ArrayList<>();

        AndroidNetworking.post(Webservice.addlandlist)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .addUrlEncodeFormBodyParameter("crop_id", cropId)
                .addUrlEncodeFormBodyParameter("lc_id", lcId)
                .addUrlEncodeFormBodyParameter("user_id", user_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("Zones Response: " + jsonObject);

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = new JSONArray();

                        try {
                            jsonArray = jsonObject.getJSONArray("data");
                        }catch (Exception e){
                            System.out.println("Json Data: " + jsonObject.get("data"));
                            e.printStackTrace();
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Zone_Model obj = new Zone_Model();
                            obj.setZone_id(jsonArray.getJSONObject(i).optString("zone_id"));
                            obj.setZone_name(jsonArray.getJSONObject(i).optString("zone_name"));
                            obj.setCrop_name(jsonArray.getJSONObject(i).optString("crop_id"));
                            obj.setLcId(jsonArray.getJSONObject(i).optString("lc_id"));
                            obj.setCrop_icons_images(jsonArray.getJSONObject(i).optString("crop_icons_images"));
                            obj.setScientific_name(jsonArray.getJSONObject(i).optString("scientific_name"));
                            obj.setSoil_ph(jsonArray.getJSONObject(i).optString("soil_ph"));
                            obj.setTemperature(jsonArray.getJSONObject(i).optString("temperature"));
                            obj.setHumidity(jsonArray.getJSONObject(i).optString("humidity"));
                            obj.setSoil_moisture(jsonArray.getJSONObject(i).optString("soil_moisture"));
                            obj.setPollution(jsonArray.getJSONObject(i).optString("pollution"));
                            obj.setLight_visibility(jsonArray.getJSONObject(i).optString("light_visibility"));
                            zones.add(obj);
                        }
                    }
                    action.onLoadComplete(jsonObject,zones, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        action.onLoadComplete(jsonObject,zones, true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                try {
                    action.onLoadComplete(null, zones, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadSupervisor(String landId, SupervisorAction action){
        AndroidNetworking.post(Webservice.supervisor_list)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.optBoolean("status")){
                        JSONObject jsonArray = response.getJSONObject("data");
                        String name = jsonArray.getString("user_name");
                        String mobileNumber = jsonArray.getString("user_phone");
                        action.onLoadComplete(jsonArray ,name, mobileNumber, true);
                    }else {
                        action.onLoadComplete(response,null, null, true);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    try {
                        action.onLoadComplete(response,null, null, true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                try {
                    action.onLoadComplete(null,null, null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadLandDeviceData(String landId, LandDeviceDataAction action){
        AndroidNetworking.post(Webservice.getLandDeviceData)
                .addUrlEncodeFormBodyParameter("land_id", landId)
//                .addUrlEncodeFormBodyParameter("land_id", "51")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Land Device Data: \n" + response);
                try {
                    if (response.optBoolean("status")){
                        LandDeviceData landDeviceData = GsonUtils.getGson()
                                .fromJson(response.optString("data"),
                                        LandDeviceData.class);
                        action.onLoadComplete(response,landDeviceData, false);
                    }else {
                        action.onLoadComplete(response,null, false);
                    }
                }catch (Exception e){
                    try {
                        action.onLoadComplete(response,null, true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                try {
                    action.onLoadComplete(null,null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadDeviceData(String zoneId, DeviceDataAction action){
        ArrayList<DeviceData> deviceDataList = new ArrayList<>();

        AndroidNetworking
                .post(Webservice.valuesFromMasterLand)
                .addUrlEncodeFormBodyParameter("land_id", zoneId)
                //.addUrlEncodeFormBodyParameter("land_id", "185979")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Device Data: " + response);
                try {
                    if (response.getBoolean("status")){
                        JSONArray jsonArray = response.optJSONArray("data");
                        List<DeviceData> deviceDatas = GsonUtils.getGson()
                                .fromJson(jsonArray.toString(), EMarketStorage.deviceDataToken);

                        System.out.println("API Data: " + deviceDatas.size());

                        ArrayList<DeviceData> deviceDataOptimized = new ArrayList<>();

                        for (int i=0; i<deviceDatas.size(); i++){
                            DeviceData deviceData = deviceDatas.get(i);
//                            System.out.println(GsonUtils.getGson().toJson(deviceData));
//                            System.out.println("Device Data: Processing " + deviceData.getDeviceId());
                            if (!hasDeviceData(deviceData.getDeviceId(), deviceDataOptimized)){
                                deviceDataOptimized.add(deviceData);
//                                System.out.println("Device Data: Added " + deviceData.getDeviceId());
                            }else{
                                if (isUpdatedData(deviceData,
                                        getDeviceData(deviceData.getDeviceId(), deviceDataOptimized))){
                                    replaceDeviceData(deviceData, deviceDataOptimized);
//                                    System.out.println("Device Data: Replaced " + deviceData.getDeviceId());
                                }else {
//                                    System.out.println("Device Data: Rejected " + deviceData.getDeviceId());
                                }
                            }
                        }

                        //System.out.println("Optimized Data: " + deviceDataOptimized.size());
                       /* DeviceData deviceData = GsonUtils.getGson().fromJson(
                                GsonUtils.getGson().toJson(deviceDataOptimized.get(0)), DeviceData.class);

                        deviceData.*/

                        System.out.println("Optimized Device Data: " + deviceDataOptimized.size());

                        deviceDataList.addAll(deviceDataOptimized);
                    }
                    action.onLoadComplete(response, deviceDataList, false);
                }catch (Exception e){
                    e.printStackTrace();
                    try {
                        action.onLoadComplete(response,deviceDataList, true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                try {
                    action.onLoadComplete(null,deviceDataList, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadExpectedDate(String cropId, ExpectedDateAction action){
        AndroidNetworking.get(Webservice.getexpected_bycropid + cropId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        String yieldDuring = jsonObject.getJSONObject("data").optString("yield_during");
                        String yieldUnit = jsonObject.getJSONObject("data").optString("yield_unit");
                        String harvestDuring = jsonObject.getJSONObject("data").optString("harvesting_during");
                        String yield = yieldDuring + " " + yieldUnit;

                        action.onLoadComplete(jsonObject,harvestDuring, yield, false);

                        //if (model == null){

                            //profit_yield.setText("");

                            //yield_unit = jsonObject.getJSONObject("data").optString("yield_unit");
                            //yield.setText(yieldDuring + " " + yieldUnit);
                            //harvest_date.setText(harvestDuring);

                        //}else {

                           /* model.setYieldDuring(yieldDuring);
                            model.setYieldUnit(yieldUnit);
                            model.setHarvestDuring(harvestDuring);

                            if(mainCrops.contains(model)){
                                mainCropAdapter.notifyItemChanged(mainCrops.indexOf(model));
                            }else {
                                interCropAdapter.notifyItemChanged(interCrops.indexOf(model));
                            }

                        }

                        yield_val = Double.parseDouble(jsonObject.getJSONObject("data").optString("yield_during"));*/
//                        if (yield_val != 0.0) {
//                            profit_yield.setText(String.valueOf(yield_val - Double.parseDouble(jsonObject.getJSONObject("data").optString("yield_during"))));
//                        } else {
//                            profit_yield.setText("");
//                        }
                    }

//                    getmembers();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        action.onLoadComplete(jsonObject,"", "", true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                try {
                    action.onLoadComplete(null,"", "", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadPestDisease(String userId, String landId, String zoneId, PestAction action){
        ArrayList<ZoneReport> zoneReports = new ArrayList<>();
        AndroidNetworking.post(Webservice.reports_get)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .addUrlEncodeFormBodyParameter("User_id", userId)
                .addUrlEncodeFormBodyParameter("zone_name", zoneId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    LogUtils.log(getClass().getSimpleName(), "Pest Disease Response: " + response);
                    if (response.optBoolean("status")){
                        JSONArray jsonArray = response.optJSONArray("data");
                        List<ZoneReport> zoneReportList = GsonUtils.getGson().fromJson(jsonArray.toString(),
                                EMarketStorage.ZoneReportListType);
                        zoneReports.addAll(zoneReportList);
                        action.onLoadComplete(response,zoneReports, false);
                    }else {
                        action.onLoadComplete(response,zoneReports, true);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    try {
                        action.onLoadComplete(response,zoneReports, true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                try {
                    action.onLoadComplete(null,zoneReports, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadPestDiseaseNew(String zoneId, String userId, PestActionNew action){
        ArrayList<ReportDetail> zoneReports = new ArrayList<>();

        AndroidNetworking.post(Webservice.getInfections)
                .addUrlEncodeFormBodyParameter("zone_id", zoneId)
                .addUrlEncodeFormBodyParameter("user_id",userId )
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    LogUtils.log(getClass().getSimpleName(), "Pest Disease Response: " + response);
                    if (response.optBoolean("status")){
                        JSONArray jsonArray = response.optJSONArray("data");
                        List<ReportDetail> zoneReportList = GsonUtils.getGson().fromJson(jsonArray.toString(),
                                infectionDetailListType);
                        zoneReports.addAll(zoneReportList);
                        action.onLoadComplete(response,zoneReports, false);
                    }else {
                        action.onLoadComplete(response,zoneReports, true);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    try {
                        action.onLoadComplete(response,zoneReports, true);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                try {
                    action.onLoadComplete(null,zoneReports, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadCropHistory(String landId, CropHistoryAction action){
        AndroidNetworking.post(Webservice.cropHistory)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.log(ApiHelper.class.getSimpleName(), "Crop History: \n" + response);
                if (response.optBoolean("status")){
                    JSONArray cropHistoryArray = response.optJSONArray("data");
                    action.onResult(response, GsonUtils.fromJson(cropHistoryArray.toString(),
                            EMarketStorage.cropHistoryListType), false);
                }else {
                    action.onResult(response,new ArrayList<>(), false);
                }
            }

            @Override
            public void onError(ANError anError) {
                action.onResult(null,null, true);
            }
        });
    }

    public static void updateCrop(String lcId,String userId, CropUpdateAction action){

        LogUtils.log(ApiHelper.class.getSimpleName(), "Update Crop: Lc Id - "+lcId);

        AndroidNetworking.post(Webservice.updateCrop)
                .addUrlEncodeFormBodyParameter("lc_id", lcId)
                .addUrlEncodeFormBodyParameter("user_id", userId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.log(ApiHelper.class.getSimpleName(), "Update Crop: "+lcId+" \n" + response);
                try {
                    action.onResult(response,response.optBoolean("status"), false);
                }catch (Exception e){
                    action.onResult(response,false, true);
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                LogUtils.log(ApiHelper.class.getSimpleName(), "Update Crop: (Error)\n" + GsonUtils.toJson(anError));
                action.onResult(null,false, true);
            }
        });
    }

    public static void loadActivities(String userId, String landId,
                                      String cropId, String cropType,
                                      String lcId, String type,
                                      ActivitiesAction action){

        ANRequest.PostRequestBuilder builder = AndroidNetworking.post(Webservice.activity_list);

        builder.addUrlEncodeFormBodyParameter("user_id", userId);
        builder.addUrlEncodeFormBodyParameter("land_id", landId);
        if (cropId != null)
            builder.addUrlEncodeFormBodyParameter("crop_id", cropId);
        builder.addUrlEncodeFormBodyParameter("crop_type", cropType);
        if (lcId != null)
            builder.addUrlEncodeFormBodyParameter("lc_id", lcId);
        builder.addUrlEncodeFormBodyParameter("type", type);
        builder.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Activity_Cat_Model> activityList = new ArrayList<>();
                LogUtils.log(this.getClass().getSimpleName(), "Activity List: " + response.toString());
                try {
                    if (response.optBoolean("status")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Activity_Cat_Model activityModel = new Activity_Cat_Model();
                            activityModel.setFence_id(jsonArray.getJSONObject(i).optString("id"));
                            activityModel.setSowing_by(jsonArray.getJSONObject(i).optString("sowing_by"));
                            activityModel.setCrop_id(jsonArray.getJSONObject(i).optString("crop_id"));
                            activityModel.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                            activityModel.setLc_id(jsonArray.getJSONObject(i).optString("lc_id"));
                            activityModel.setService_id(jsonArray.getJSONObject(i).optString("service_id").trim());
                            activityModel.setPrepare_type(jsonArray.getJSONObject(i).optString("prepare_type"));
                            activityModel.setCrop_type(jsonArray.getJSONObject(i).optString("crop_type"));
                            activityModel.setFencing_by(jsonArray.getJSONObject(i).optString("fencing_by"));
                            activityModel.setEquipment_name(jsonArray.getJSONObject(i).optString("equipment_name"));
                            activityModel.setEquipment_type(jsonArray.getJSONObject(i).optString("equipment_type"));
                            activityModel.setVendor_name(jsonArray.getJSONObject(i).optString("vendor_name"));
                            activityModel.setContract_type(jsonArray.getJSONObject(i).optString("contract_type"));
                            activityModel.setMaterial_name(jsonArray.getJSONObject(i).optString("material_name"));
                            activityModel.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                            activityModel.setStart_date(jsonArray.getJSONObject(i).optString("start_date"));
                            activityModel.setEnd_date(jsonArray.getJSONObject(i).optString("end_date"));
                            activityModel.setDays_count(jsonArray.getJSONObject(i).optString("days_count"));
                            activityModel.setMember_count(jsonArray.getJSONObject(i).optString("member_count"));
                            activityModel.setTotal_amount(jsonArray.getJSONObject(i).optString("total_amount"));
                            activityModel.setCreated_date(jsonArray.getJSONObject(i).optString("created_date"));
                            activityModel.prepareImages(jsonArray.getJSONObject(i).optJSONArray("images"));
                            activityModel.setStatus(jsonArray.getJSONObject(i).optString("status", ""));
                            activityList.add(activityModel);
                        }
                    }
                    action.onResult(response,activityList, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    action.onResult(response,activityList, true);
                }
            }

            @Override
            public void onError(ANError anError) {
                LogUtils.log(this.getClass().getSimpleName(), "Activity List Error");
                anError.printStackTrace();
                action.onResult(null,null, true);
            }
        });

    }

    public static void updateActivityStatus(String activityId, String serviceId, String status, CropUpdateAction action){
        AndroidNetworking.post(Webservice.update_activity_status)
                .addUrlEncodeFormBodyParameter("activity_id", activityId)
                .addUrlEncodeFormBodyParameter("service_id", serviceId)
                .addUrlEncodeFormBodyParameter("status", status)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                action.onResult(response,response.optBoolean("status", false), false);
            }

            @Override
            public void onError(ANError anError) {
                action.onResult(null,false, true);
                anError.printStackTrace();
                LogUtils.log(getClass().getSimpleName(), "Error: " + anError.getErrorBody());
            }
        });
    }

    public static void loadWeatherForecast(String landId, WeatherForecastAction action){
        AndroidNetworking.post(Webservice.getWeatherForecast)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.log(this.getClass().getSimpleName(), response.toString());
                if (response.optBoolean("status", false)){
                    action.onResult(response,true, false,
                            response.optString("message", "Weather Forecast Loaded!"),
                            GsonUtils.fromJson(response.opt("data").toString(), weatherForecastListType));
                }else {
                    action.onResult(response,false, false,
                            response.optString("message", "No Weather Forecast!"), new ArrayList<>());
                }
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                action.onResult(null ,false, true, "Weather Forecast Load Error!", null);
            }
        });
    }

    public static ArrayList<String> getMainCrops(Maincrops_Model land){
        ArrayList<String> cropIds = new ArrayList<>();
        try {

            JSONArray mainCrops = new JSONArray(land.getMainCropsString());

            for (int i=0; i<mainCrops.length(); i++){
                JSONObject jsonObject = mainCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                cropIds.add(cropId);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return cropIds;
    }

    public static ArrayList<String> getInterCrops(Maincrops_Model land){
        ArrayList<String> cropIds = new ArrayList<>();
        try {

            JSONArray interCrops = new JSONArray(land.getInterCropsString());

            for (int i=0; i<interCrops.length(); i++){
                JSONObject jsonObject = interCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                cropIds.add(cropId);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return cropIds;
    }

    public static LinkedHashMap<String, ArrayList<String>> getCropIds(Maincrops_Model land, boolean isMain){
        LinkedHashMap<String, ArrayList<String>> cropIds = new LinkedHashMap<>();
        try {
            JSONArray cropArray = null;

            if (isMain){
                cropArray = new JSONArray(land.getMainCropsString());
            }else {
                cropArray = new JSONArray(land.getInterCropsString());
            }

            if (cropArray != null){
                for (int i=0; i<cropArray.length(); i++){
                    JSONObject jsonObject = cropArray.getJSONObject(i);
                    String cropId = jsonObject.getString("crop_id");
                    String varietyId = jsonObject.optString("variety_id");
                    if (!cropIds.containsKey(cropId))
                        cropIds.put(cropId, new ArrayList<>());
                    if (varietyId != null && !TextUtils.isEmpty(varietyId))
                        cropIds.get(cropId).add(varietyId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return cropIds;
    }

    public static String getVarietyId(Maincrops_Model land, JSONObject cropJson){
        String varietyId = "";
        try {
            varietyId = cropJson.optString("variety_id");
        }catch (Exception e){
            e.printStackTrace();
        }
        return varietyId;
    }

    public static ArrayList<Crops_Main> getMainCropModels(Maincrops_Model land){

        ArrayList<Crops_Main> crops = new ArrayList<>();

        try {

            JSONArray mainCrops = new JSONArray(land.getMainCropsString());
            //JSONArray interCrops = new JSONArray(land.getInterCropsString());

            for (int i=0; i<mainCrops.length(); i++){
                JSONObject jsonObject = mainCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                String varietyId = jsonObject.optString("variety_id");
                String lcId = jsonObject.optString("lc_id");
                Crops_Main crop = new Crops_Main();
                crop.setCrop_id(cropId);
                crop.setName(Webservice.getCrop(cropId).getName());
                crop.setVarietyId(varietyId);
                crop.setLcId(lcId);
                VarietyModel varietyModel = Webservice.getVariety(varietyId);
                crop.setVarietyName(varietyModel != null ? varietyModel.getName() : null);
                crops.add(crop);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return crops;
    }

    public static ArrayList<Crops_Main> getInterCropModels(Maincrops_Model land){

        ArrayList<Crops_Main> crops = new ArrayList<>();

        try {

            //JSONArray mainCrops = new JSONArray(land.getMainCropsString());
            JSONArray interCrops = new JSONArray(land.getInterCropsString());

            for (int i=0; i<interCrops.length(); i++){
                JSONObject jsonObject = interCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                String varietyId = jsonObject.optString("variety_id");
                String lcId = jsonObject.optString("lc_id");
                Crops_Main crop = new Crops_Main();
                crop.setCrop_id(cropId);
                crop.setName(Webservice.getCrop(cropId).getName());
                crop.setVarietyId(varietyId);
                crop.setLcId(lcId);
                VarietyModel varietyModel = Webservice.getVariety(varietyId);
                crop.setVarietyName(varietyModel != null ? varietyModel.getName() : null);
                crops.add(crop);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return crops;

    }

    public static ArrayList<Crops_Main> getAllCrops(Maincrops_Model land){
        ArrayList<Crops_Main> crops = new ArrayList<>();
        try {

            JSONArray mainCrops = new JSONArray(land.getMainCropsString());
            JSONArray interCrops = new JSONArray(land.getInterCropsString());

            for (int i=0; i<mainCrops.length(); i++){
                JSONObject jsonObject = mainCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                String varietyId = jsonObject.optString("variety_id");
                String lcId = jsonObject.optString("lc_id");
                Crops_Main crop = new Crops_Main();
                crop.setCrop_id(cropId);
                crop.setName(Webservice.getCrop(cropId).getName());
                crop.setVarietyId(varietyId);
                crop.setLcId(lcId);
                VarietyModel varietyModel = Webservice.getVariety(varietyId);
                crop.setVarietyName(varietyModel != null ? varietyModel.getName() : null);
                crops.add(crop);
            }

            for (int i=0; i<interCrops.length(); i++){
                JSONObject jsonObject = interCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                String varietyId = jsonObject.optString("variety_id");
                String lcId = jsonObject.optString("lc_id");
                Crops_Main crop = new Crops_Main();
                crop.setCrop_id(cropId);
                crop.setName(Webservice.getCrop(cropId).getName());
                crop.setVarietyId(varietyId);
                crop.setLcId(lcId);
                VarietyModel varietyModel = Webservice.getVariety(varietyId);
                crop.setVarietyName(varietyModel != null ? varietyModel.getName() : null);
                crops.add(crop);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return crops;
    }

    private static boolean hasDeviceData(int deviceId, ArrayList<DeviceData> deviceDataList){
        return getDeviceData(deviceId, deviceDataList) != null;
    }

    private static DeviceData getDeviceData(int deviceId, ArrayList<DeviceData> deviceDataList){
        for (DeviceData deviceData: deviceDataList){
//            System.out.println(deviceData.getDeviceId() + " = " +deviceId);
            if (deviceData.getDeviceId() == deviceId)
//                System.out.print(" true");
                return deviceData;
        }
        return null;
    }

    private static boolean isUpdatedData(DeviceData deviceData, DeviceData deviceDataOld){
        Date date = new Date(deviceData.getCreatedAt());
        Date oldDate = new Date(deviceDataOld.getCreatedAt());
        return date.after(oldDate);
//        return true;
    }

    private static void replaceDeviceData(DeviceData deviceData, ArrayList<DeviceData> deviceDataList){
        deviceDataList.remove(getDeviceData(deviceData.getDeviceId(), deviceDataList));
        deviceDataList.add(deviceData);
    }

    public interface LandAction{
        void onLoadComplete(JSONObject response, ArrayList<Maincrops_Model> lands, boolean error);
    }

    public interface ZoneAction{
        void onLoadComplete(JSONObject response, ArrayList<Zone_Model> zones, boolean error);
    }

    public interface DeviceDataAction{
        void onLoadComplete(JSONObject response,ArrayList<DeviceData> deviceDataList, boolean error);
    }

    public interface LandDeviceDataAction{
        void onLoadComplete(JSONObject response,LandDeviceData landDeviceData, boolean error);
    }

    public interface ExpectedDateAction{
        void onLoadComplete(JSONObject response,String harvest, String yield, boolean error);
    }

    public interface PestAction{
        void onLoadComplete(JSONObject response,ArrayList<ZoneReport> reports, boolean error);
    }

    public interface PestActionNew{
        void onLoadComplete(JSONObject response, ArrayList<ReportDetail> reports, boolean error);
    }

    public interface SupervisorAction{
        void onLoadComplete(JSONObject response,String name, String mobileNumber, boolean error);
    }

    public interface CropUpdateAction{
        void onResult(JSONObject response, boolean status, boolean error);
    }

    public interface CropHistoryAction{
        void onResult(JSONObject response,ArrayList<CropHistory> cropHistoryList, boolean error);
    }

    public interface ActivitiesAction{
        void onResult(JSONObject response, ArrayList<Activity_Cat_Model> activityList, boolean error);
    }

    public interface LandDetailAction{
        void onResult(JSONObject response, boolean status, LandDetail landDetail);
    }

    public interface WeatherForecastAction{
        void onResult(JSONObject response,boolean status, boolean error, String message, ArrayList<WeatherForecast> weatherForecastList);
    }
}
