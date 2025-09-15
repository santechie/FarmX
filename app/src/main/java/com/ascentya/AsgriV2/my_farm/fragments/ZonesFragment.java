package com.ascentya.AsgriV2.my_farm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Cat_Model;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Add_ZonesCrop;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.my_farm.activities.PestDiseaseActivity;
import com.ascentya.AsgriV2.my_farm.activities.ReportPestDiseaseActivity;
import com.ascentya.AsgriV2.my_farm.adapters.ExpandableZoneAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

public class ZonesFragment extends BaseFragment implements ExpandableZoneAdapter.Action, BaseActivity.ChangeLand {

    private int selectedLand = 0;
    private String landId = null, userId ,  cropId = null, varietyId = null;
    private Crops_Main crop;
    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<String> cropIds = new ArrayList<>();
    private HashMap<String, String> varietyIds = new HashMap<>();

    private TextView landSpinner;
    LinkedHashMap<Cat_Model, ArrayList<Zone_Model>> zones = new LinkedHashMap<>();
    ArrayList<Cat_Model> crops = new ArrayList<>();
    ArrayList<Crops_Main> cropModels = new ArrayList<>();

    private ExpandableZoneAdapter zoneAdapter;

    private BaseActivity.ReceiverInterface zoneUpdateReceiver = new BaseActivity.ReceiverInterface() {
        @Override
        public void onReceive(Intent intent) {
            if (getView() != null) loadLands();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            landId = getArguments().getString("land_id");
            cropId = getArguments().getString("crop_id");
            userId = getSessionManager().getUser().getId();
            crop = GsonUtils.fromJson(getArguments().getString("crop"), Crops_Main.class);

            if (crop != null){
                cropId = crop.getCrop_id();
                varietyId = crop.getVarietyId();
            }

            System.out.println("Land Id: " + landId);
            System.out.println("Crop Id: " + cropId);
            System.out.println("Crop: " + GsonUtils.toJson(crop));
        }

        zoneAdapter = new ExpandableZoneAdapter(this);

        registerReceiver(Constants.Broadcasts.ZONE_UPDATE, zoneUpdateReceiver);
        //loadLands();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        landSpinner = view.findViewById(R.id.landSpinner);
        landSpinner.setOnClickListener(v -> showSelectLandDialog());

        ((ExpandableListView) view.findViewById(R.id.zonesList))
                .setAdapter(zoneAdapter);

        if (lands.isEmpty()) loadLands();
        else updateLands();
    }

    private void showSelectLandDialog(){
        if (lands.isEmpty())
            return;
        new SelectItemDialog(getString(R.string.lands), (List<Object>)(List<?>) lands, position -> {
            selectedLand = position;
            landId = lands.get(position).getId();
            updateLands();
            //updateCrops();
            //loadZones(landId, lands.get(position).getId());
        }).show(getChildFragmentManager(), "lands");
    }

    private void loadLands(){
        showLoading();
        ApiHelper.loadLands(getSessionManager().getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Maincrops_Model> lands, boolean error) {
                hideLoading();

                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                ZonesFragment.this.lands.clear();
                ZonesFragment.this.lands.addAll(lands);
                updateLands();
            }
        });
    }

    private void updateLands(){
        int i = 0;
        for (Maincrops_Model land: lands){
            if (landId != null && land.getId().equals(landId)){
                selectedLand = i;
            }
            i++;
        }
        if (landId == null && !lands.isEmpty()) landId = lands.get(selectedLand).getId();
        updateLandText();
    }

    private void updateLandText(){
        if (getView() == null || lands.isEmpty()) return;
        landSpinner.setText(lands.get(selectedLand).getLand_name());
        loadCrops();
    }


    private void loadCrops(){

        if (!lands.isEmpty() && !checkCrop(lands.get(selectedLand), lands, ZonesFragment.this)) return;

        cropIds.clear();
        cropIds.addAll(ApiHelper.getMainCrops(lands.get(selectedLand)));
        cropIds.addAll(ApiHelper.getInterCrops(lands.get(selectedLand)));

        cropModels.clear();
        cropModels.addAll(ApiHelper.getAllCrops(lands.get(selectedLand)));

        //crops.clear();
        //zones.clear();

//        LinkedHashMap<String, ArrayList<String>> mainCropIds = ApiHelper.getCropIds(lands.get(selectedLand), true);
//        LinkedHashMap<String, ArrayList<String>> interCropIds = ApiHelper.getCropIds(lands.get(selectedLand), false);

//        System.out.println("Main Crop Ids: \n" + mainCropIds);
//        System.out.println("Inter Crop Ids: \n" + interCropIds);

        ArrayList<Cat_Model> tempCrops = new ArrayList<>();
        HashMap<String, HashMap<String, ArrayList<Zone_Model>>> tempZones = new HashMap<>();

//        final List<Integer> total = Arrays.asList(0);

        final ArrayList<Crops_Main> zoneLoadedCrops = new ArrayList<>();

        HashMap<Cat_Model, ArrayList<Zone_Model>> cropZones = new HashMap<>();

        if (!cropModels.isEmpty()) showLoading();

//        toast( userId);

        for(Crops_Main crop: cropModels){

            ApiHelper.loadZones(landId, crop.getCrop_id(), crop.getLcId(),userId, new ApiHelper.ZoneAction() {
                @Override
                public void onLoadComplete(JSONObject response, ArrayList<Zone_Model> zones, boolean error) {

                    if (UserHelper.checkResponse(requireContext(), response)){
                        return;
                    }

                    Cat_Model cropModel = new Cat_Model();
                    cropModel.setId(crop.getCrop_id());
                    cropModel.setName(Webservice.getCrop(crop.getCrop_id()).getName());

                    VarietyModel varietyModel = Webservice.getVariety(crop.getVarietyId());
                    if (varietyModel != null){
                        cropModel.setVarietyId(varietyModel.getId());
                        cropModel.setVarietyName(varietyModel.getName());
                    }

                    cropModel.setLcId(crop.getLcId());

//                    System.out.println("Crop: " + GsonUtils.toJson(cropModel));
//                    System.out.println("Zones: " + GsonUtils.toJson(zones));

                    cropZones.put(cropModel, zones);

                    tempCrops.add(cropModel);
                    zoneLoadedCrops.add(crop);

                    if(cropModels.size() == zoneLoadedCrops.size()){
                        hideLoading();
                        updateZones(tempCrops, cropZones);
                    }
                }
            });
        }

//        if (true) return;

        /*for (int m=0; m< mainCropIds.size(); m++){

            String cropId = (String) mainCropIds.keySet().toArray()[m];
            ArrayList<String> varietyIds = mainCropIds.get(cropId);

            total.set(0, total.get(0) + (varietyIds.isEmpty() ? 1 : varietyIds.size()));

            if (varietyIds.isEmpty()){

                ApiHelper.loadZones(landId, cropId, null, new ApiHelper.ZoneAction() {
                    @Override
                    public void onLoadComplete(ArrayList<Zone_Model> zones, boolean error) {

                        System.out.println("Loading Zones: "
                                + "Land Id: " + landId
                                + " Crop Id: " + cropId
                                + " Zones: " + zones.size());

                        Cat_Model cropModel = new Cat_Model();
                        cropModel.setId(cropId);
                        cropModel.setName(Webservice.getCrop(cropId).getName());
                        tempCrops.add(cropModel);
                        HashMap<String, ArrayList<Zone_Model>> zoneMap = new HashMap<>();
                        zoneMap.put(cropId, zones);
                        tempZones.put(cropId, zoneMap);
                        if (tempCrops.size() == total.get(0)){
                            updateZones(tempCrops, tempZones);
                        }
                    }
                });

            }else {

//                System.out.println("Crop: " + cropId);
//                System.out.println("Varieties: " + varietyIds);

                for (String varietyId: varietyIds) {

                    ApiHelper.loadZones(landId, cropId, null, new ApiHelper.ZoneAction() {
                        @Override
                        public void onLoadComplete(ArrayList<Zone_Model> zones, boolean error) {

                            System.out.println("Loading Zones: "
                                    + "Land Id: " + landId
                                    + " Crop Id: " + cropId
                                    + " Variety Id: " + varietyId
                                    + " Zones: " + zones.size());

                            Cat_Model cropModel = new Cat_Model();
                            cropModel.setId(cropId);
                            cropModel.setName(Webservice.getCrop(cropId).getName());
                            cropModel.setVarietyId(varietyId);
                            cropModel.setVarietyName(Webservice.getVariety(varietyId).getName());
                            tempCrops.add(cropModel);
                            if (!tempZones.containsKey(cropId)){
                                tempZones.put(cropId, new HashMap<>());
                            }
                            tempZones.get(cropId).put(varietyId, zones);
                            if (tempCrops.size() == total.get(0)) {
                                updateZones(tempCrops, tempZones);
                            }
                        }
                    });
                }
            }

        }

        for (int i=0; i < interCropIds.size(); i++){

            String cropId = (String) interCropIds.keySet().toArray()[i];
            ArrayList<String> varietyIds = interCropIds.get(cropId);

            total.set(0, total.get(0) + (varietyIds.isEmpty() ? 1 : varietyIds.size()));

            if (varietyIds.isEmpty()){

                ApiHelper.loadZones(landId, cropId, null, new ApiHelper.ZoneAction() {
                    @Override
                    public void onLoadComplete(ArrayList<Zone_Model> zones, boolean error) {

                        System.out.println("Loading Zones: "
                                + "Land Id: " + landId
                                + " Crop Id: " + cropId
                                + " Zones: " + zones.size());

                        Cat_Model cropModel = new Cat_Model();
                        cropModel.setId(cropId);
                        cropModel.setName(Webservice.getCrop(cropId).getName());
                        tempCrops.add(cropModel);
                        HashMap<String, ArrayList<Zone_Model>> zoneMap = new HashMap<>();
                        zoneMap.put(cropId, zones);
                        tempZones.put(cropId, zoneMap);
                        if (tempCrops.size() == total.get(0)){
                            updateZones(tempCrops, tempZones);
                        }
                    }
                });

            }else {

//                System.out.println("Crop: " + cropId);
//                System.out.println("Varieties: " + varietyIds);

                for (String varietyId: varietyIds) {

                    ApiHelper.loadZones(landId, cropId, null,  new ApiHelper.ZoneAction() {
                        @Override
                        public void onLoadComplete(ArrayList<Zone_Model> zones, boolean error) {

                            System.out.println("Loading Zones: "
                                    + "Land Id: " + landId
                                    + " Crop Id: " + cropId
                                    + " Variety Id: " + varietyId
                                    + " Zones: " + zones.size());

                            Cat_Model cropModel = new Cat_Model();
                            cropModel.setId(cropId);
                            cropModel.setName(Webservice.getCrop(cropId).getName());
                            cropModel.setVarietyId(varietyId);
                            cropModel.setVarietyName(Webservice.getVariety(varietyId).getName());
                            tempCrops.add(cropModel);
                            if (!tempZones.containsKey(cropId)){
                                tempZones.put(cropId, new HashMap<>());
                            }
                            tempZones.get(cropId).put(varietyId, zones);
                            if (tempCrops.size() == total.get(0)) {
                                updateZones(tempCrops, tempZones);
                            }
                        }
                    });
                }
            }

        }*/

        /*for (int c=0; c<cropIds.size(); c++){
            final int i = c;
            String cropId = cropIds.get(c);

            ApiHelper.loadZones(landId, cropId, new ApiHelper.ZoneAction() {
                @Override
                public void onLoadComplete(ArrayList<Zone_Model> zones, boolean error) {

                    System.out.println("Loading Zones: "
                            + "Land Id: " + landId
                            + " Crop Id: " + cropId
                            + " Zones: " + zones.size());

                    Cat_Model cropModel = new Cat_Model();
                    cropModel.setId(cropId);
                    cropModel.setName(Webservice.getCrop(cropId).getName());
                    tempCrops.add(cropModel);
                    tempZones.put(cropId, zones);
                    if (tempCrops.size() == cropIds.size()){
                        updateZones(tempCrops, tempZones);
                    }
                }
            });
        }*/
    }


    private void updateZones(ArrayList<Cat_Model> crops,
                             HashMap<Cat_Model, ArrayList<Zone_Model>> zones){

        this.crops.clear();
        this.crops.addAll(crops);
        this.zones.clear();
        this.zones.putAll(zones);

        if (getView() != null && zoneAdapter != null)
            getView().post(() -> zoneAdapter.notifyDataSetChanged());
    }

    public static ZonesFragment newInstance(String landId, Crops_Main crop){
        ZonesFragment zonesFragment = new ZonesFragment();
        Bundle args = new Bundle();
        args.putString("land_id", landId);
        args.putString("crop", GsonUtils.toJson(crop));
//        args.putString("crop_id", cropId);
//        args.putString("variety_id", varietyId);
        zonesFragment.setArguments(args);
        return zonesFragment;
    }

    @Override
    public boolean canViewPest() {
        return getModuleManager().canView(Components.MyFarm.PEST_DISEASE);
    }

    @Override
    public ArrayList<Cat_Model> getCrops() {
        return crops;
    }

    @Override
    public ArrayList<Zone_Model> getZones(Cat_Model catModel) {
        return zones.get(catModel);
    }

    @Override
    public void report(int groupPosition, int childPosition) {
        Zone_Model zoneModel = getZones(getCrops().get(groupPosition)).get(childPosition);
        //System.out.println("Report Zone: " + GsonUtils.toJson(zoneModel));
        if (checkSubscription(Components.MyFarm.PEST_DISEASE, ModuleManager.ACCESS.INSERT)) {
            /*Reports_Dialog obj = new Reports_Dialog();
            //obj.setAction(() -> loadPestDiseases());
            obj.dialog(getContext(), getString(R.string.report), lands.get(selectedLand).getId(), null,
                    getZones(getCrops().get(groupPosition)).get(childPosition).getZone_id(),
                    getSessionManager().getUser().getId());*/

            openActivity(ReportPestDiseaseActivity.class,
                    Pair.create("land", lands.get(selectedLand)),
                    Pair.create("crop", getCrops().get(groupPosition)),
                    Pair.create("zone", getZones(getCrops().get(groupPosition)).get(childPosition)));
        }
    }

    @Override
    public void remedy(int groupPosition, int childPosition) {
        if (checkSubscription(Components.MyFarm.REMEDY, ModuleManager.ACCESS.INSERT)) {
            openWithSubscription(PestDiseaseActivity.class,
                    Components.MyFarm.PEST_DISEASE, ModuleManager.ACCESS.VIEW,
                    Pair.create("land_id", lands.get(selectedLand).getId()),
                    Pair.create("crop_id", getCrops().get(groupPosition).getId()),
                    Pair.create("zone_id", getZones(
                            getCrops().get(groupPosition))
                            .get(childPosition).getZone_id()));
        }
    }

    @Override
    public void history(int groupPosition, int childPosition) {
        openWithSubscription(PestDiseaseActivity.class,
                Components.MyFarm.PEST_DISEASE, ModuleManager.ACCESS.VIEW,
                Pair.create("land_id", lands.get(selectedLand).getId()),
                Pair.create("crop_id", getCrops().get(groupPosition).getId()),
                Pair.create("zone_id", getZones(
                        getCrops().get(groupPosition))
                        .get(childPosition).getZone_id()));
    }

    public void addZones(){
        if (crops.isEmpty()) return;
        showAddZoneDialog();
    }

    public void showAddZoneDialog(){
        Maincrops_Model land = lands.get(selectedLand);
        Add_ZonesCrop obj = new Add_ZonesCrop();
        obj.dialog(requireContext(), getString(R.string.add_zone), land.getLand_name(), land.getId(),
                land.getMaincrop(), getSessionManager().getUser().getId(),
                getViewDialog(), "");
    }

    @Override
    public void onChangeLand() {
        showSelectLandDialog();
    }
}
