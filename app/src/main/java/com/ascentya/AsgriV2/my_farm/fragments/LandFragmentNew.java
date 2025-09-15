package com.ascentya.AsgriV2.my_farm.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.LandCropAdapter;
import com.ascentya.AsgriV2.Models.LandCropModel;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.CropUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.views.DynamicViewPager;
import com.ascentya.AsgriV2.my_farm.activities.CropActivity;
import com.ascentya.AsgriV2.my_farm.adapters.WeatherForecastViewPagerAdapter;
import com.ascentya.AsgriV2.my_farm.model.WeatherForecast;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import org.json.JSONObject;

public class LandFragmentNew extends BaseFragment implements WeatherForecastViewPagerAdapter.Action {

    private String landId;
    //private ChipGroup landGroup;
    private ArrayList<Maincrops_Model> lands = new ArrayList<>();

    private TextView landNameTv, temp_val, humidity_val, moisture_val, ph_val;
    private TextView supervisor_name, supervisor_district;

    private List<LandCropModel> mainCrops = new ArrayList<>(), interCrops = new ArrayList<>();
    private LinearLayout mainCropLay, interCropLay;
    private RecyclerView mainCropRv, interCropRv;
    private LandCropAdapter mainCropAdapter, interCropAdapter;

    private DynamicViewPager weatherForecastViewPager;
    private WormDotsIndicator indicator;
    private WeatherForecastViewPagerAdapter weatherForecastAdapter;
    private ArrayList<WeatherForecast> weatherForecastList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            landId = getArguments().getString("land_id");
            System.out.println("Land Id: " + landId);
        }
        mainCropAdapter = new LandCropAdapter(mainAction);
        interCropAdapter = new LandCropAdapter(interAction);
        weatherForecastAdapter = new WeatherForecastViewPagerAdapter(
                getChildFragmentManager(), this);

        registerReceiver(Constants.Broadcasts.LAND_UPDATE, new BaseActivity.ReceiverInterface() {
            @Override
            public void onReceive(Intent intent) {
                if (getView() != null)
                    loadLands();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_land_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        landNameTv = view.findViewById(R.id.landName);

        view.findViewById(R.id.back).setOnClickListener(v -> requireActivity().onBackPressed());
        //((TextView) view.findViewById(R.id.name)).setText(getSessionManager().getUser().getFirstname());
        //((TextView) view.findViewById(R.id.place)).setText(getSessionManager().getUser().getDistrict());
        //((TextView) view.findViewById(R.id.date)).setText(DateUtils.displayDate(new Date().toString()));

        indicator = view.findViewById(R.id.indicator);

        weatherForecastViewPager = view.findViewById(R.id.viewPager);
        weatherForecastViewPager.setAdapter(weatherForecastAdapter);
        //weatherForecastViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());

        indicator.setViewPager(weatherForecastViewPager);

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

        /*if (!checkSubscription(Components.MyFarm.LAND)){
            return;
        }*/

        ApiHelper.loadLands(getSessionManager().getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Maincrops_Model> lands, boolean error) {

               /* if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }*/

                if (!error){
                    LandFragmentNew.this.lands.clear();
                    LandFragmentNew.this.lands.addAll(lands);
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

        landNameTv.setText(getSelectedLand().getLand_name());

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
        checkWeatherForecast();
        loadSupervisor();
        if (checkCrop(getSelectedLand(), null, null))
            loadCrops();
    }

    private void checkWeatherForecast(){
        boolean hasData = Objects.requireNonNull(getSelectedLand()).getFieldId() != null;
        if (hasData){
            loadWeatherForecastData();
        }else {
            setUpWeatherForecastUi(false);
        }
    }

    private void setUpWeatherForecastUi(boolean visible){
        if (visible){
            indicator.setVisibility(View.VISIBLE);
            weatherForecastViewPager.setVisibility(View.VISIBLE);
        }else {
            indicator.setVisibility(View.INVISIBLE);
            weatherForecastViewPager.setVisibility(View.GONE);
        }
    }

    private void loadWeatherForecastData(){
        showLoading();
        ApiHelper.loadWeatherForecast(Objects.requireNonNull(getSelectedLand()).getId(),
                new ApiHelper.WeatherForecastAction() {
            @Override
            public void onResult(JSONObject response,boolean status, boolean error, String message,
                                 ArrayList<WeatherForecast> weatherForecastList) {
                hideLoading();

                if (UserHelper.checkResponse(getContext(), response)){
                    return;
                }
                if (!error){
                    if (status){
                        setUpWeatherForecastUi(true);
                        LandFragmentNew.this.weatherForecastList.clear();
                        LandFragmentNew.this.weatherForecastList.addAll(weatherForecastList);
                        //Toasty.normal(requireContext(), weatherForecastList.size() + " ws").show();
                        updateWeatherForecastData();
                    }else {
                        setUpWeatherForecastUi(false);
                        if (message != null){
                            Toasty.normal(requireContext(), message).show();
                        }else {
                            Toasty.normal(requireContext(), "No Weather Forecast Data").show();
                        }
                    }
                }else {
                    Toasty.error(requireContext(), "Weather Forecast Load Error!").show();
                }
            }
        });
    }

    private void updateWeatherForecastData(){
        weatherForecastAdapter.notifyDataSetChanged();
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
        /*if (!checkSubscription(Components.MyFarm.CROP)){

            return;
        }*/
        updateCrops();
        Maincrops_Model land = getSelectedLand();
        mainCrops.clear();
//        if (getModuleManager().canView(ModuleManager.Components.MyFarm.CROP))
        mainCrops.addAll(CropUtils.getLandCropModelList(land.getMainCropsString()));
        interCrops.clear();
//        if (getModuleManager().canView(ModuleManager.Components.MyFarm.CROP))
        interCrops.addAll(CropUtils.getLandCropModelList(land.getInterCropsString()));
        System.out.println();
        updateCrops();
    }

    private void updateCrops(){
        mainCropLay.setVisibility(mainCrops.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        interCropLay.setVisibility(interCrops.isEmpty() ? View.INVISIBLE : View.VISIBLE);
        mainCropAdapter.notifyDataSetChanged();
        interCropAdapter.notifyDataSetChanged();
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

    public static LandFragmentNew getInstance(String landId){
        LandFragmentNew landFragment = new LandFragmentNew();
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

        @SuppressLint("SuspiciousIndentation")
        @Override
        public void onClicked(int position) {
            if (getModuleManager().canView(Components.MyFarm.CROP))
            openCropActivity(mainCrops.get(position).getCropId(),
                    mainCrops.get(position).getVarietyId());
//            toast("hello");
            //Activity(CropActivity.class/*, Pair.create("crop_id", this.getLandCrops().get(position).getCropId())*/);
        }
    };

    private LandCropAdapter.Action interAction = new LandCropAdapter.Action() {
        @Override
        public List<LandCropModel> getLandCrops() {
            return interCrops;
        }

        @SuppressLint("SuspiciousIndentation")
        @Override
        public void onClicked(int position) {
            if (getModuleManager().canView(Components.MyFarm.CROP))
            openCropActivity(interCrops.get(position).getCropId(),
                    interCrops.get(position).getVarietyId());
//            toast("hello");
            //openActivity(CropActivity.class/*, Pair.create("crop_id", getLandCrops().get(position).getCropId())*/);
        }
    };

    public void openCropActivity(String cropId, String varietyId){

        openActivity(CropActivity.class,
                Pair.create("crop_id", cropId),
                Pair.create("variety_id", varietyId),
                Pair.create("land", getSelectedLand()));
    }

    @Override
    public ArrayList<WeatherForecast> getWeatherForecastList() {
        return weatherForecastList;
    }
}
