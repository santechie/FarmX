package com.ascentya.AsgriV2.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.DeviceDataAdapter;
import com.ascentya.AsgriV2.Adapters.DeviceDataNewAdapter;
import com.ascentya.AsgriV2.Models.Crop_Realfield;
import com.ascentya.AsgriV2.Models.DeviceData;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class DeviceDataActivity extends BaseActivity implements DeviceDataAdapter.Action {

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

    public static final String MASTER = "master";
    public static final String SLAVE = "slave";

    private ViewDialog viewDialog;

    private RecyclerView recyclerView;
    private TextView cropSpinner, zoneSpinner;

    private ArrayList<Crop_Realfield> crops = new ArrayList<>();
    private ArrayList<Zone_Model> zones = new ArrayList<>();
//    private ArrayAdapter<Crop_Realfield> cropAdapter;
//    private ArrayAdapter<Zone_Model> zoneAdapter;

    private ArrayList<DeviceData> deviceDataList = new ArrayList<>();
    private DeviceDataNewAdapter adapter;

    private int selectedCrop, selectedZone;
    private String landId, userId, lcId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_data);

        setToolbarTitle("My Devices", true);
        viewDialog = new ViewDialog(this);

        landId = getIntent().getStringExtra("land_id");

        cropSpinner = findViewById(R.id.cropSpinner);
        zoneSpinner = findViewById(R.id.zoneSpinner);

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new DeviceDataNewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        cropSpinner.setOnClickListener(view -> showCropSelectDialog());
        zoneSpinner.setOnClickListener(view -> showZoneSelectDialog());

//        setUpSpinner();
        loadCrops();
//        loadDeviceData();
//        updateDeviceData();
    }

    private void loadCrops() {
        crops.clear();
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.landzonecrop_get)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String cropId = jsonArray.getJSONObject(i).optString("crop_id");
                            Crop_Realfield obj = new Crop_Realfield();
                            obj.setId(cropId);
                            obj.setName(Webservice.getCrop(cropId).getName());
                            crops.add(obj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                viewDialog.hideDialog();
                updateCrops();
                if (!crops.isEmpty())
                    loadZones(landId,crops.get(selectedCrop).getId(), userId, lcId);
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
            }
        });

    }

    private void clearDeviceData(){
        deviceDataList.clear();
        updateDeviceData();
    }

    private void loadZones(String landId, String cropId, String userId, String lcId) {
        zones.clear();
        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.addlandlist)
                .addUrlEncodeFormBodyParameter("land_id", landId)
                .addUrlEncodeFormBodyParameter("crop_id", cropId)
                .addUrlEncodeFormBodyParameter("user_id", userId)
                .addUrlEncodeFormBodyParameter("lc_id", lcId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                viewDialog.hideDialog();
                if (UserHelper.checkResponse(DeviceDataActivity.this, jsonObject)){
                    return;
                }

                try {

                    System.out.println();

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Zone_Model obj = new Zone_Model();
                            obj.setZone_id(jsonArray.getJSONObject(i).optString("zone_id"));
                            obj.setZone_name(jsonArray.getJSONObject(i).optString("zone_name"));
                            obj.setCrop_name(jsonArray.getJSONObject(i).optString("crop_id"));
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

                    } else {

                    }

                    selectedZone = 0;
//                    updateZones();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                updateZones();
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();


            }
        });
    }

    private void updateCrops() {
//        cropAdapter.notifyDataSetChanged();
        if (!crops.isEmpty())
            cropSpinner.setText(crops.get(selectedCrop).toString());
        else
            cropSpinner.setText("No Crops");
        clearDeviceData();
    }

    private void updateZones() {
//        zoneAdapter.notifyDataSetChanged();
        if (!zones.isEmpty()){
            zoneSpinner.setText(zones.get(selectedZone).toString());
            loadDeviceData();
        } else
            zoneSpinner.setText("No Zones");
    }

    private void showCropSelectDialog(){
        if (crops.isEmpty())
            return;
        new SelectItemDialog("Crops",(List<Object>)(List<?>) crops, position -> {
            selectedCrop = position;
            updateCrops();
            loadZones(landId, userId, lcId,crops.get(position).getId());
        }).show(getSupportFragmentManager(), "crops");
    }

    private void showZoneSelectDialog(){
        if (zones.isEmpty())
            return;
        new SelectItemDialog("Zones", (List<Object>)(List<?>) zones, position -> {
            selectedZone = position;
            updateZones();
//            loadZones("44", crops.get(position).getId());
        }).show(getSupportFragmentManager(), "crops");
    }

    private void loadDeviceData() {
        if (zones.isEmpty())
            return;

        deviceDataList.clear();
        viewDialog.showDialog();

        AndroidNetworking
                .post(Webservice.valuesFromMasterLand)
                .addUrlEncodeFormBodyParameter("land_id", zones.get(selectedZone).getZone_id())
//                .addUrlEncodeFormBodyParameter("land_id", "16")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                viewDialog.hideDialog();

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

                        System.out.println("Optimized Data: " + deviceDataOptimized.size());
                       /* DeviceData deviceData = GsonUtils.getGson().fromJson(
                                GsonUtils.getGson().toJson(deviceDataOptimized.get(0)), DeviceData.class);

                        deviceData.*/

                        deviceDataList.addAll(deviceDataOptimized);
                    }
                }catch (Exception e){
                    Toasty.error(DeviceDataActivity.this, "Device Data Parse Error!").show();
                    e.printStackTrace();
                }
                updateDeviceData();
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                viewDialog.hideDialog();
                Toasty.error(DeviceDataActivity.this, "Device Data Load Error!").show();
            }
        });
    }

    private boolean hasDeviceData(int deviceId, ArrayList<DeviceData> deviceDataList){
        return getDeviceData(deviceId, deviceDataList) != null;
    }

    private DeviceData getDeviceData(int deviceId, ArrayList<DeviceData> deviceDataList){
        for (DeviceData deviceData: deviceDataList){
//            System.out.println(deviceData.getDeviceId() + " = " +deviceId);
            if (deviceData.getDeviceId() == deviceId)
//                System.out.print(" true");
                return deviceData;
        }
        return null;
    }

    private boolean isUpdatedData(DeviceData deviceData, DeviceData deviceDataOld){
        Date date = new Date(deviceData.getCreatedAt());
        Date oldDate = new Date(deviceDataOld.getCreatedAt());
        return date.after(oldDate);
//        return true;
    }

    private void replaceDeviceData(DeviceData deviceData, ArrayList<DeviceData> deviceDataList){
        deviceDataList.remove(getDeviceData(deviceData.getDeviceId(), deviceDataList));
        deviceDataList.add(deviceData);
    }

    private void updateDeviceData() {
        findViewById(R.id.noDataCont).setVisibility(deviceDataList.isEmpty() ? View.VISIBLE : View.GONE);
//        adapter.notifyDataSetChanged();
        adapter.update();
    }

    @Override
    public ArrayList<DeviceData> getDataList() {
        return deviceDataList;
    }

    @Override
    public void onChartClicked(int deviceId) {
        DeviceData deviceData = getDeviceData(deviceId, deviceDataList);
        deviceData.setZoneId(zones.get(selectedZone).getZone_id());
        DeviceDataChartActivity.open(this, deviceData);
    }

    @Override
    public void onViewClicked(int type, int deviceId) {
        adapter.setSelected(type, deviceId);
    }

    @Override
    public String getActualRange(String type) {
        String range = "";
        String cropId = crops.get(selectedCrop).getId();
        switch (type){
            case TEMPERATURE: range = Webservice.getCrop(cropId).getTempreture();
            break;
            case HUMIDITY: range = Webservice.getCrop(cropId).getHumidity();
            break;
            case PH: range = Webservice.getCrop(cropId).getWaterph();
            break;
            case MOISTURE: range = Webservice.getCrop(cropId).getMoisture();
            break;
            case VISIBILITY: range = "1-22000";
            break;
        }
        if (range == null || range.isEmpty()){
            range = "0-0";
        }
        return range;
    }

    public static void open(Context context, String landId){
        Intent intent = new Intent(context, DeviceDataActivity.class);
        intent.putExtra("land_id", landId);
        context.startActivity(intent);
    }

}