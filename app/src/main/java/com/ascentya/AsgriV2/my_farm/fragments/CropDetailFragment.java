package com.ascentya.AsgriV2.my_farm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.DeviceDataAdapter;
import com.ascentya.AsgriV2.Adapters.DeviceDataNewAdapter;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.DeviceData;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.HUMIDITY;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.MOISTURE;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.PH;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.TEMPERATURE;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.VISIBILITY;

import org.json.JSONObject;

public class CropDetailFragment extends BaseFragment implements DeviceDataAdapter.Action {

    private String cropId = null, varietyId = null, cropType = null, userId ;
    private Maincrops_Model land;
    private DeviceDataNewAdapter.GridAdapter adapter;
    private ArrayList<Zone_Model> zones = new ArrayList<>();
    private ArrayList<DeviceData> deviceDataList = new ArrayList<>();
    private Crops_Main crop;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DeviceDataNewAdapter.GridAdapter(this);

        if (getArguments() != null){
            land = GsonUtils.fromJson(getArguments().getString("land"),
                    Maincrops_Model.class);
            cropId = getArguments().getString("crop_id");
            varietyId = getArguments().getString("variety_id");
            cropType = getArguments().getString("type");
            userId = getSessionManager().getUser().getId();
            crop = GsonUtils.fromJson(getArguments().getString("crop"), Crops_Main.class);
            if (crop != null){
                cropId = crop.getCrop_id();
                varietyId = crop.getVarietyId();
            }
//            toast("Variety Id: " + varietyId);
            loadZones();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crop_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Crops_Main cropsMain = Webservice.getCrop(cropId);

        ((TextView) view.findViewById(R.id.cropName)).setText(cropsMain.getName());
        VarietyModel varietyModel = Webservice.getVariety(varietyId);
        String varietyName = "";
        if (varietyModel != null)
            varietyName = varietyModel.getName();
        ((TextView) view.findViewById(R.id.cropType)).setText(varietyName);
        ((TextView) view.findViewById(R.id.acreCount)).setText(land.getAcre_count());
        Glide.with(this).load(cropsMain.getIcon()).into((ImageView) view.findViewById(R.id.cropImage));
        ((RecyclerView) view.findViewById(R.id.recyclerView))
                .setLayoutManager(new GridLayoutManager(getContext(), 3));
        ((RecyclerView) view.findViewById(R.id.recyclerView)).setAdapter(adapter);
        loadZones();
        loadExpectedDate();
    }


    public static CropDetailFragment getInstance(Maincrops_Model land, String type, Crops_Main crop){
        CropDetailFragment cropDetailFragment = new CropDetailFragment();
        Bundle args = new Bundle();
        args.putString("land", GsonUtils.toJson(land));
//        args.putString("crop_id", cropId);
//        args.putString("variety_id", varietyId);
        args.putString("type", type);
        args.putString("crop", GsonUtils.toJson(crop));
        cropDetailFragment.setArguments(args);
        return cropDetailFragment;
    }

    @Override
    public ArrayList<DeviceData> getDataList() {
        return deviceDataList;
    }

    @Override
    public void onChartClicked(int deviceId) {

    }

    @Override
    public void onViewClicked(int type, int deviceId) {

    }

    @Override
    public String getActualRange(String type) {
        String range = "";
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

    private void loadZones(){
        if (!zones.isEmpty()){
            updateZones();
            loadDeviceData();
        }

        if (!checkSubscription(Components.MyFarm.ZONE)){
            updateZones();
            updateDeviceData();
            return;
        }

//        toast( userId);

        ApiHelper.loadZones(land.getId() , crop.getCrop_id(), crop.getLcId(), userId, new ApiHelper.ZoneAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Zone_Model> zones, boolean error) {
                System.out.println("Zones Loading...");

                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                if (error){
                    if (getContext() != null) {
                        toast("No Zones");
                        updateZones();
                        updateDummyData();
                    }
                }else {
                    CropDetailFragment.this.zones.clear();
                    CropDetailFragment.this.zones.addAll(zones);
                    updateZones();
                    loadDeviceData();
                }
            }
        });
    }

    private void updateDummyData(){
        deviceDataList.clear();
        DeviceData deviceData = new DeviceData();
        deviceData.setLight(0);
        deviceData.setTemperature(0);
        deviceData.setHumidity(0);
        deviceData.setSoilMoisture(0);
        deviceData.setPh(0);
        deviceDataList.add(deviceData);
        updateDeviceData();
    }

    private void updateZones(){
        if (getView() == null) return;
        ((TextView) getView().findViewById(R.id.zoneCount)).setText(zones.size() + "");
    }

    private void loadDeviceData(){

        if (!checkSubscription(Components.MyFarm.REAL_TIME_DATA)){
            updateDummyData();
            return;
        }

        if (!deviceDataList.isEmpty()){
            updateDeviceData();
            loadExpectedDate();
            return;
        }

        if (zones.isEmpty()) return;

        ApiHelper.loadDeviceData(zones.get(0).getZone_id(), new ApiHelper.DeviceDataAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<DeviceData> deviceDataList, boolean error) {
                System.out.println("Device Data Loading...");
                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                if (error){
                    DeviceData deviceData = new DeviceData();
                    CropDetailFragment.this.deviceDataList.clear();
                    CropDetailFragment.this.deviceDataList.add(deviceData);
                    updateDeviceData();
                    if (getContext() != null)
                        errorToast("Device Data Load Error");
                }else {
                    CropDetailFragment.this.deviceDataList.clear();
                    CropDetailFragment.this.deviceDataList.addAll(deviceDataList);
                    if (CropDetailFragment.this.deviceDataList.isEmpty()){
                        DeviceData deviceData = new DeviceData();
                        CropDetailFragment.this.deviceDataList.add(deviceData);
                    }
                    updateDeviceData();
                }
            }
        });
    }

    private void updateDeviceData(){
        if (getView() == null || deviceDataList.isEmpty()) return;
        adapter.setDeviceData(deviceDataList.get(0), Webservice.getCrop(cropId).getIcon(), 1);
    }

    private void loadExpectedDate(){
        if (getView() == null) return;
        ApiHelper.loadExpectedDate(cropId, new ApiHelper.ExpectedDateAction() {
            @Override
            public void onLoadComplete(JSONObject response,String harvest, String yield, boolean error) {
                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                if (error){
                    if (getContext() != null)
                        errorToast("Expected Date Load Error!");
                }else {
                    ((TextView) getView().findViewById(R.id.harvest)).setText(harvest);
                    ((TextView) getView().findViewById(R.id.yield)).setText(yield);
                }
            }
        });
    }

    public String getCropId(){
        return cropId;
    }

    public String getVarietyId(){ return varietyId; }

    public Crops_Main getCrop(){ return crop; }
}
