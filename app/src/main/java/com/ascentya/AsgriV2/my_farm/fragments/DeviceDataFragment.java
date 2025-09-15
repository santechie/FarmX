package com.ascentya.AsgriV2.my_farm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.DeviceDataChartActivity;
import com.ascentya.AsgriV2.Adapters.DeviceDataAdapter;
import com.ascentya.AsgriV2.Adapters.DeviceDataNewAdapter;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.DeviceData;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.HUMIDITY;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.MOISTURE;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.PH;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.TEMPERATURE;
import static com.ascentya.AsgriV2.Activitys.DeviceDataActivity.VISIBILITY;

import org.json.JSONObject;

public class DeviceDataFragment extends BaseFragment
        implements DeviceDataAdapter.Action, SwipeRefreshLayout.OnRefreshListener,
        BaseActivity.ChangeLand {

    private boolean showToolbar;
    private Toolbar toolbar;

    private String landId, userId;
    private int selectedLandIndex = 0, selectedCropIndex = 0, selectedZoneIndex = 0;

    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<Crops_Main> crops = new ArrayList<>();
    private ArrayList<Zone_Model> zones = new ArrayList<>();
    private ArrayList<DeviceData> deviceDataList = new ArrayList<>();

    private DeviceDataNewAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isFirstLand = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            showToolbar = getArguments().getBoolean("toolbar");
            landId = getArguments().getString("land_id");
        }

        adapter = new DeviceDataNewAdapter(this);

        registerReceiver(Constants.Broadcasts.LAND_UPDATE, new BaseActivity.ReceiverInterface() {
            @Override
            public void onReceive(Intent intent) {
                if (getView() != null) loadLands();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        view.findViewById(R.id.appBarLay).setVisibility(showToolbar? View.VISIBLE : View.GONE);

        view.findViewById(R.id.landSpinner).setOnClickListener(v -> showLands());
        view.findViewById(R.id.cropSpinner).setOnClickListener(v -> showCrops());
        view.findViewById(R.id.zoneSpinner).setOnClickListener(v -> showZones());

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        ((RecyclerView) view.findViewById(R.id.recyclerView))
                .setLayoutManager(new LinearLayoutManager(getContext()));

        ((RecyclerView) view.findViewById(R.id.recyclerView))
                .setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        setToolbar();
        loadLands();
    }

    private void setToolbar(){
        if (showToolbar){
            toolbar.setTitle(getString(R.string.my_devices));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }

    private void showLands(){
        new SelectItemDialog(getString(R.string.lands), (List<Object>)(List<?>) lands, position -> {
            selectedLandIndex = position;
            updateLand();
        }).show(getChildFragmentManager(), "lands");
    }

    private void showCrops(){
        if (!lands.isEmpty() && !checkCrop(lands.get(selectedLandIndex), lands, DeviceDataFragment.this)) return;
        new SelectItemDialog(getString(R.string.crops), (List<Object>)(List<?>) crops, position -> {
            selectedCropIndex = position;
            updateCrop();
        }).show(getChildFragmentManager(), "crops");
    }

    private void showZones(){
        if (!lands.isEmpty() && !checkCrop(lands.get(selectedLandIndex), lands, DeviceDataFragment.this)) return;
        new SelectItemDialog(getString(R.string.zones), (List<Object>)(List<?>) zones, position -> {
            selectedZoneIndex = position;
            updateZones();
        }).show(getChildFragmentManager(), "zones");
    }

    private void loadLands(){
//        if (!checkSubscription(Components.MyFarm.LAND, ModuleManager.ACCESS.VIEW)) return;
        showLoading();
        ApiHelper.loadLands(getSessionManager().getUser().getId(), (response,lands, error) -> {
            hideLoading();

            if (UserHelper.checkResponse(requireContext(), response)){
                return;
            }
            DeviceDataFragment.this.lands.clear();
            DeviceDataFragment.this.lands.addAll(lands);
            updateLand();
        });
    }

    private void updateLand(){
        if (getView() == null || lands.isEmpty()) return;
        if (isFirstLand && landId != null) {
            isFirstLand = false;
            for (int i = 0; i < lands.size(); i++) {
                if (lands.get(i).getId().equals(landId)) {
                    selectedLandIndex = i;
                }
            }
        }
        ((TextView) getView().findViewById(R.id.landSpinner))
                .setText(lands.get(selectedLandIndex).getLand_name());
        selectedCropIndex = 0;
        loadCrops();
    }

    private void loadCrops(){
//        if (!checkSubscription(Components.MyFarm.CROP)) return;
        if (lands.isEmpty()) return;
        if (!checkCrop(lands.get(selectedLandIndex), lands, DeviceDataFragment.this)) return;
        crops.clear();
        crops.addAll(ApiHelper.getAllCrops(lands.get(selectedLandIndex)));
//        System.out.println("Crops:" + GsonUtils.toJson(crops));
        updateCrop();
    }

    private void updateCrop(){
        if (getView() == null) return;
        String cropName = crops.get(selectedCropIndex).getName();
        String varietyName = crops.get(selectedCropIndex).getVarietyName();
        if (varietyName != null) cropName += " (" + varietyName + ")";
        ((TextView) getView().findViewById(R.id.cropSpinner))
                .setText(cropName);
        selectedZoneIndex = 0;
        loadZones();
    }

    private void loadZones(){
//        if (!checkSubscription(Components.MyFarm.ZONE)) return;
        String landId = lands.get(selectedLandIndex).getId();
        String cropId = crops.get(selectedCropIndex).getCrop_id();
        String userId = getSessionManager().getUser().getId();
        showLoading();
        ApiHelper.loadZones(landId,  cropId, crops.get(selectedCropIndex).getLcId(),userId, new ApiHelper.ZoneAction() {
            @Override
            public void onLoadComplete(JSONObject response,ArrayList<Zone_Model> zones, boolean error) {
                hideLoading();

                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                DeviceDataFragment.this.zones.clear();
                DeviceDataFragment.this.zones.addAll(zones);
                updateZones();
            }
        });
    }

    private void updateZones(){
        if (zones.isEmpty()){
            ((TextView) getView().findViewById(R.id.zoneSpinner))
                    .setText("");
            clearDeviceData();
            return;
        }
        if (getView() == null) return;
            ((TextView) getView().findViewById(R.id.zoneSpinner))
                    .setText(zones.get(selectedZoneIndex).getZone_name());
        onChange();
    }

    private void onChange(){
        loadDeviceData();
    }

    private void clearDeviceData(){
        DeviceDataFragment.this.deviceDataList.clear();
        adapter.update();
    }

    private void loadDeviceData(){
//        if (!checkSubscription(Components.MyFarm.REAL_TIME_DATA)) return;
        Zone_Model zoneModel = zones.get(selectedZoneIndex);
        showLoading();
        ApiHelper.loadDeviceData(zoneModel.getZone_id(), new ApiHelper.DeviceDataAction() {
            @Override
            public void onLoadComplete(JSONObject response,ArrayList<DeviceData> deviceDataList, boolean error) {
                hideLoading();
                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                DeviceDataFragment.this.deviceDataList.clear();
                if (error){
                    errorToast("Network Error!");
                }else{
                    DeviceDataFragment.this.deviceDataList.addAll(deviceDataList);
                }
                updateDeviceData();
            }
        });
    }

    private void updateDeviceData(){
        adapter.update();
    }

    public static DeviceDataFragment newInstance(boolean showToolbar, String landId,
            /*String cropId, String zoneId*/ Crops_Main crop){
        DeviceDataFragment deviceDataFragment = new DeviceDataFragment();
        Bundle args = new Bundle();
        args.putBoolean("toolbar", showToolbar);
        args.putString("land_id", landId);
//        args.putString("crop_id", cropId);
//        args.putString("zone_id", zoneId);
        args.putString("crop", GsonUtils.toJson(crop));
        deviceDataFragment.setArguments(args);
        return deviceDataFragment;
    }

    @Override
    public ArrayList<DeviceData> getDataList() {
        return deviceDataList;
    }

    @Override
    public void onChartClicked(int deviceId) {
        DeviceData deviceData = getDeviceData(deviceId, deviceDataList);
        deviceData.setZoneId(zones.get(selectedZoneIndex).getZone_id());
        DeviceDataChartActivity.open(getContext(), deviceData);
    }

    @Override
    public void onViewClicked(int type, int deviceId) {
        adapter.setSelected(type, deviceId);
    }

    @Override
    public String getActualRange(String type) {
        String range = "";
        String cropId = crops.get(selectedCropIndex).getCrop_id();
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

    private DeviceData getDeviceData(int deviceId, ArrayList<DeviceData> deviceDataList){
        for (DeviceData deviceData: deviceDataList){
//            System.out.println(deviceData.getDeviceId() + " = " +deviceId);
            if (deviceData.getDeviceId() == deviceId)
//                System.out.print(" true");
                return deviceData;
        }
        return null;
    }

    @Override
    public void onRefresh() {
        loadDeviceData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!lands.isEmpty()) checkCrop(lands.get(selectedLandIndex), lands, DeviceDataFragment.this);
    }

    @Override
    public void onChangeLand() {
        showLands();
    }
}
