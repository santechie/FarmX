package com.ascentya.AsgriV2.my_farm.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.LandCropAdapter;
import com.ascentya.AsgriV2.Models.LandCropModel;
import com.ascentya.AsgriV2.Models.LandDeviceData;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.CropUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.activities.CropActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class LandFragment extends BaseFragment {

    private String landId;
    //private ChipGroup landGroup;
    private ArrayList<Maincrops_Model> lands = new ArrayList<>();

    private TextView temp_val, humidity_val, moisture_val, ph_val;
    private TextView supervisor_name, supervisor_district;

    private List<LandCropModel> mainCrops = new ArrayList<>(), interCrops = new ArrayList<>();
    private LinearLayout mainCropLay, interCropLay;
    private RecyclerView mainCropRv, interCropRv;
    private LandCropAdapter mainCropAdapter, interCropAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            landId = getArguments().getString("land_id");
            System.out.println("Land Id: " + landId);
        }
        mainCropAdapter = new LandCropAdapter(mainAction);
        interCropAdapter = new LandCropAdapter(interAction);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_land, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.back).setOnClickListener(v -> requireActivity().onBackPressed());
        ((TextView) view.findViewById(R.id.name)).setText(getSessionManager().getUser().getFirstname());
        ((TextView) view.findViewById(R.id.place)).setText(getSessionManager().getUser().getDistrict());
        ((TextView) view.findViewById(R.id.date)).setText(DateUtils.displayDate(new Date().toString()));

        //landGroup = view.findViewById(R.id.landGroup);

        //landGroup.setSelectionRequired(true);
        //landGroup.setSingleLine(true);

       /* landGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                onLandSelected(checkedId);
            }
        });*/

        temp_val = view.findViewById(R.id.temp);
        humidity_val = view.findViewById(R.id.humidity);
        moisture_val = view.findViewById(R.id.moisture);
        ph_val = view.findViewById(R.id.ph);

        supervisor_name = view.findViewById(R.id.supervisor_name);
        supervisor_district = view.findViewById(R.id.supervisor_district);

        mainCropLay = view.findViewById(R.id.mainCropLay);
        interCropLay = view.findViewById(R.id.interCropLay);

        mainCropRv = view.findViewById(R.id.mainCropRv);
        interCropRv = view.findViewById(R.id.interCropRv);

        mainCropRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        interCropRv.setLayoutManager(new LinearLayoutManager(requireContext()));

        mainCropRv.setAdapter(mainCropAdapter);
        interCropRv.setAdapter(interCropAdapter);

        loadLands();
    }

    private void loadLands(){

        if (!checkSubscription(Components.MyFarm.LAND)){
            return;
        }

        ApiHelper.loadLands(getSessionManager().getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Maincrops_Model> lands, boolean error) {

                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                if (!error){
                    LandFragment.this.lands.clear();
                    LandFragment.this.lands.addAll(lands);
                    if (landId == null && !lands.isEmpty()) landId = lands.get(0).getId();
                    updateLands();
                }else {
                    errorToast("Network error!");
                }
            }
        });
    }

    private void updateLands(){

        if (lands.isEmpty() || landId == null) return;

        /*landGroup.removeAllViews();

        int landIndex = 0;
        for (Maincrops_Model land: lands){
            Chip chip = new Chip(getContext());
            chip.setId(landIndex++);
            chip.setText(land.getLand_name());
            chip.setCheckable(true);
            chip.setChecked(land.getId().equals(landId));
            landGroup.addView(chip);
        }*/

        onLandSelected();
    }

    private void onLandSelected(){
        //landId = lands.get(position).getId();
        updateLandDetails();
        //updateCropDetails();
    }

    private void updateLandDetails(){
        loadDeviceData();
        loadSupervisor();
        loadCrops();
    }

    private void loadDeviceData(){
        ApiHelper.loadLandDeviceData(landId, new ApiHelper.LandDeviceDataAction() {
            @Override
            public void onLoadComplete(JSONObject response,LandDeviceData landDeviceData, boolean error) {
                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                if (error){
                    errorToast("Network error!");
                    return;
                }
                if (landDeviceData != null){
                    temp_val.setText(checkValue(landDeviceData.getTemperature()));
                    humidity_val.setText(checkValue(landDeviceData.getHumidity()));
                    moisture_val.setText(checkValue(landDeviceData.getSoilMoisture()));
                    ph_val.setText(checkValue(landDeviceData.getPh()));
                }else {
                    temp_val.setText(checkValue(null));
                    humidity_val.setText(checkValue(null));
                    moisture_val.setText(checkValue(null));
                    ph_val.setText(checkValue(null));
                }
            }
        });
    }

    private void loadSupervisor(){
        ApiHelper.loadSupervisor(landId, new ApiHelper.SupervisorAction() {
            @Override
            public void onLoadComplete(JSONObject response,String name, String mobileNumber, boolean error) {
                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                supervisor_name.setText(name);
                supervisor_district.setText(mobileNumber);
            }
        });
    }

    private void loadCrops(){
        if (!checkSubscription(Components.MyFarm.CROP)){
            updateCrops();
            return;
        }

        Maincrops_Model land = getSelectedLand();
        mainCrops.clear();
        mainCrops.addAll(CropUtils.getLandCropModelList(land.getMainCropsString()));
        interCrops.clear();
        interCrops.addAll(CropUtils.getLandCropModelList(land.getInterCropsString()));
        updateCrops();
    }

    private void updateCrops(){
        mainCropLay.setVisibility(mainCrops.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        interCropLay.setVisibility(interCrops.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        mainCropAdapter.notifyDataSetChanged();
        interCropAdapter.notifyDataSetChanged();
//        toast("Crop: " + checkCrop(getSelectedLand()));
        if (checkCrop(getSelectedLand(), null, null))
            updateExpectedDate();
    }

    private void updateExpectedDate(){
        for (LandCropModel landCropModel: mainCrops){
            ApiHelper.loadExpectedDate(landCropModel.getCropId(), new ApiHelper.ExpectedDateAction() {
                @Override
                public void onLoadComplete(JSONObject response,String harvest, String yield, boolean error) {
                    if (UserHelper.checkResponse(requireContext(), response)){
                        return;
                    }
                    landCropModel.setYieldDuring(yield);
                    landCropModel.setHarvestDuring(harvest);
                    mainCropAdapter.notifyItemChanged(mainCrops.indexOf(landCropModel));
                }
            });
        }

        for (LandCropModel landCropModel: interCrops){
            ApiHelper.loadExpectedDate(landCropModel.getCropId(), new ApiHelper.ExpectedDateAction() {
                @Override
                public void onLoadComplete(JSONObject response,String harvest, String yield, boolean error) {
                    if (UserHelper.checkResponse(requireContext(), response)){
                        return;
                    }
                    landCropModel.setYieldDuring(yield);
                    landCropModel.setHarvestDuring(harvest);
                    interCropAdapter.notifyItemChanged(interCrops.indexOf(landCropModel));
                }
            });
        }
    }

    private Maincrops_Model getSelectedLand(){
        for (Maincrops_Model land: lands){
            if (land.getId().equals(landId)) return land;
        }
        return null;
    }

    private String checkValue(String value){
        return value == null ? "0" : value;
    }

    public static LandFragment getInstance(String landId){
        LandFragment landFragment = new LandFragment();
        Bundle args = new Bundle();
        args.putString("land_id", landId);
        landFragment.setArguments(args);
        return landFragment;
    }

    private LandCropAdapter.Action mainAction = new LandCropAdapter.Action() {
        @Override
        public List<LandCropModel> getLandCrops() {
            return mainCrops;
        }

        @Override
        public void onClicked(int position) {
            toast("hello");
            openCropActivity(mainCrops.get(position).getCropId(),
                    mainCrops.get(position).getVarietyId());
            //Activity(CropActivity.class/*, Pair.create("crop_id", this.getLandCrops().get(position).getCropId())*/);
        }
    };

    private LandCropAdapter.Action interAction = new LandCropAdapter.Action() {
        @Override
        public List<LandCropModel> getLandCrops() {
            return interCrops;
        }

        @Override
        public void onClicked(int position) {
            openCropActivity(interCrops.get(position).getCropId(),
                    interCrops.get(position).getVarietyId());
            //openActivity(CropActivity.class/*, Pair.create("crop_id", getLandCrops().get(position).getCropId())*/);
        }
    };

    public void openCropActivity(String cropId, String varietyId){
        openActivity(CropActivity.class,
                Pair.create("crop_id", cropId),
                Pair.create("variety_id", varietyId),
                Pair.create("land", getSelectedLand()));
    }
}
