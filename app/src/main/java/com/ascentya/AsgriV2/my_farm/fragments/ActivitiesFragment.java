package com.ascentya.AsgriV2.my_farm.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.GeneralViewPagerAdapter;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.activities.ActivitiesCalendarActivity;
import com.ascentya.AsgriV2.my_farm.adapters.ActivityTabAdapter;
import com.ascentya.AsgriV2.my_farm.model.Menu;
import com.ascentya.AsgriV2.my_land.fragments.ActivityListFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class ActivitiesFragment extends BaseFragment
        implements ActivityTabAdapter.Action, GeneralViewPagerAdapter.Action, BaseActivity.ChangeLand {

    private String landId, cropId, varietyId;

    private Toolbar toolbar;
    private boolean showToolbar = false;

    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<Crops_Main> crops = new ArrayList<>();
    private int selectedLandIndex = 0, selectedCropIndex = 0, selectedMenu = 0;

    private ImageView cropImageIv;
    private TextView cropNameTv, acreCountTv;
    private RecyclerView menuRv;

    private ArrayList<Menu> menuList = new ArrayList<>();
    private ActivityTabAdapter menuAdapter;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private GeneralViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            showToolbar = getArguments().getBoolean("toolbar");
            landId = getArguments().getString("land_id");
            cropId = getArguments().getString("crop_id");
            varietyId = getArguments().getString("variety_id");
            System.out.println("Show Toolbar: " + showToolbar);
            System.out.println("A Land Id: " + landId);
            System.out.println("A Crop Id: " + cropId);
        }

        menuAdapter = new ActivityTabAdapter(this);
        viewPagerAdapter = new GeneralViewPagerAdapter(getChildFragmentManager(), this);
        loadMenu();

        registerReceiver(Constants.Broadcasts.LAND_UPDATE, new BaseActivity.ReceiverInterface() {
            @Override
            public void onReceive(Intent intent) {
                if (getView() != null) loadLands();
            }
        });
    }

    private void loadMenu(){
        menuList.add(new Menu(R.drawable.ic_soil_preparation, getString(R.string.soil_preparation), ""));
        menuList.add(new Menu(R.drawable.land, getString(R.string.land_preparation), ""));
        menuList.add(new Menu(R.drawable.ic_water_analysis, getString(R.string.water_analysis), ""));
        menuList.add(new Menu(R.drawable.ic_cultivation, getString(R.string.cultivation), ""));
        menuList.add(new Menu(R.drawable.ic_post_harvest, getString(R.string.post_harvest), ""));
    }

    private void loadFragments(){
        fragments.clear();

        Maincrops_Model landModel = lands.get(selectedLandIndex);
        Crops_Main cropModel = crops.get(selectedCropIndex);

        System.out.println("Crop: " + GsonUtils.toJson(cropModel));

        fragments.add(ActivityListFragment.newInstance(getString(R.string.soil_preparation), "soil_preparation", landModel, cropModel));
        fragments.add(ActivityListFragment.newInstance(getString(R.string.land_preparation), "land_preparation", landModel, cropModel));
        fragments.add(ActivityListFragment.newInstance(getString(R.string.water_analysis), "water_analysis", landModel, cropModel));
        fragments.add(ActivityListFragment.newInstance(getString(R.string.cultivation), "cultivation", landModel, cropModel));
        fragments.add(ActivityListFragment.newInstance(getString(R.string.post_harvest), "post_harvest", landModel, cropModel));

        viewPagerAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        view.findViewById(R.id.appBarLay).setVisibility(showToolbar? View.VISIBLE : View.GONE);

        setToolbar();

        view.findViewById(R.id.landContainer).setOnClickListener(v -> showLands());
        view.findViewById(R.id.cropContainer).setOnClickListener(v -> showCrops());

        cropImageIv = view.findViewById(R.id.cropImage);
        cropNameTv = view.findViewById(R.id.cropName);
        acreCountTv = view.findViewById(R.id.acreCount);

        menuRv = view.findViewById(R.id.recyclerView);
        menuRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        menuRv.setAdapter(menuAdapter);

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        setListener();
        loadLands();
    }

    private void setToolbar(){
        if (showToolbar){
            toolbar.setTitle(getString(R.string.activities));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
            toolbar.inflateMenu(R.menu.activities_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.activities){
                        if (!lands.isEmpty() && checkCrop(lands.get(selectedLandIndex),
                                lands, ActivitiesFragment.this)) {

                            openActivity(ActivitiesCalendarActivity.class,
                                    Pair.create("land_id", landId),
                                    Pair.create("crop_id", cropId),
                                    Pair.create("variety_id", varietyId),
                                    Pair.create("crop", GsonUtils.toJson(crops.get(selectedCropIndex))));
                        }
                        return true;
                    }
                    return false;
                }
            });
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }

    private void setListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateMenu(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void showLands(){
        new SelectItemDialog(getString(R.string.lands), (List<Object>)(List<?>) lands, position -> {
            selectedLandIndex = position;
            updateLand();
        }).show(getChildFragmentManager(), "lands");
    }

    public String getSelectedLandId(){
        if (lands != null && !lands.isEmpty()) return lands.get(selectedLandIndex).getId();
        return null;
    }

    private void showCrops(){
        if (!lands.isEmpty() && !checkCrop(lands.get(selectedLandIndex), lands, ActivitiesFragment.this)) return;
        new SelectItemDialog(getString(R.string.crops), (List<Object>)(List<?>) crops, position -> {
            selectedCropIndex = position;
            updateCrop();
        }).show(getChildFragmentManager(), "lands");
    }

    public Crops_Main getSelectedCrop(){
        if (crops != null && !crops.isEmpty()) return crops.get(selectedCropIndex);
        return null;
    }

    public String getSelectedCropId(){
        if (crops != null && !crops.isEmpty()) return crops.get(selectedCropIndex).getCrop_id();
        return null;
    }

    public String getSelectedVarietyId(){
        if (crops != null && !crops.isEmpty()) return crops.get(selectedCropIndex).getVarietyId();
        return null;
    }

    public String getSelectedLcId(){
        if (crops != null && !crops.isEmpty()) return crops.get(selectedCropIndex).getLcId();
        return null;
    }

    private void loadLands(){
        showLoading();
        ApiHelper.loadLands(getSessionManager().getUser().getId(), (response, lands, error) -> {
            hideLoading();
            /*if (UserHelper.checkResponse(requireContext(), response)){
                return;
            }*/
            ActivitiesFragment.this.lands.clear();
            ActivitiesFragment.this.lands.addAll(lands);
            updateLand();
        });
    }

    private boolean isFirstLand = true;
    private boolean isFirstCrop = true;

    private void updateLand(){
        if (getView() == null || lands.isEmpty()) return;
        if (isFirstLand && landId != null){
            isFirstLand = false;
            for (int i=0; i<lands.size(); i++){
                if (lands.get(i).getId().equals(landId)){
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
        if (lands.isEmpty()) return;
        if (!checkCrop(lands.get(selectedLandIndex), lands, ActivitiesFragment.this)) return;
        crops.clear();
        crops.addAll(ApiHelper.getAllCrops(lands.get(selectedLandIndex)));
        if (isFirstCrop && cropId != null){
            isFirstCrop = false;
            for (int i=0; i<crops.size(); i++){
                if (crops.get(i).getCrop_id().equals(cropId)){
                    selectedCropIndex = i;
                }
            }
        }
        updateCrop();
    }

    private void updateCrop(){
        if (getView() == null) return;
        ((TextView) getView().findViewById(R.id.cropSpinner))
                .setText(crops.get(selectedCropIndex).getName());
        VarietyModel varietyModel = Webservice.getVariety(crops.get(selectedCropIndex).getVarietyId());
        if (varietyModel == null) ((TextView) getView().findViewById(R.id.varietyName)).setText("");
        else ((TextView) getView().findViewById(R.id.varietyName)).setText(varietyModel.getName());
        onChange();
    }

    private void onChange(){
        Maincrops_Model land = lands.get(selectedLandIndex);
        Crops_Main cropsMain = crops.get(selectedCropIndex);
        Glide.with(this)
                .load(Webservice.getCrop(cropsMain.getCrop_id())
                        .getIcon()).into(cropImageIv);
        cropNameTv.setText(cropsMain.getName());
        acreCountTv.setText(land.getAcre_count());
        loadFragments();
    }

    public static ActivitiesFragment newInstance(boolean showToolbar, String landId, String cropId, String varietyId){
        ActivitiesFragment activitiesFragment = new ActivitiesFragment();
        Bundle args = new Bundle();
        args.putBoolean("toolbar", showToolbar);
        args.putString("land_id", landId);
        args.putString("crop_id", cropId);
        args.putString("variety_id", varietyId);
        activitiesFragment.setArguments(args);
        return activitiesFragment;
    }

    public static ActivitiesFragment newInstance(String landId, String cropId, String varietyId){
        return newInstance(false, landId, cropId, varietyId);
    }

    @Override
    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    @Override
    public int getSelectedPosition() {
        return selectedMenu;
    }

    @Override
    public void onSelected(int position) {
        updateMenu(position);
        viewPager.setCurrentItem(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateMenu(int position){
        menuRv.scrollToPosition(position);
        selectedMenu = position;
        menuAdapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!lands.isEmpty()) checkCrop(lands.get(selectedLandIndex), lands, ActivitiesFragment.this);
    }

    @Override
    public void onChangeLand() {
        showLands();
    }
}
