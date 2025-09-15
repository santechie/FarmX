package com.ascentya.AsgriV2.my_farm.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import es.dmoral.toasty.Toasty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.FarmX_MyStockActivity;
import com.ascentya.AsgriV2.Activitys.Scheme_Activity;
import com.ascentya.AsgriV2.Adapters.GeneralViewPagerAdapter;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.dialog.CommonDialog;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.adapters.MenuAdapter;
import com.ascentya.AsgriV2.my_farm.fragments.CropDetailFragment;
import com.ascentya.AsgriV2.my_farm.model.Menu;
import com.google.android.material.chip.ChipGroup;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CropActivity extends BaseActivity
    implements GeneralViewPagerAdapter.Action, MenuAdapter.Action {

    private TextView landSpinner;
    private ViewPager viewPager;
    private ChipGroup landGroup;
    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private String cropId, varietyId, selectedLandId;
    private Maincrops_Model land;

    private GeneralViewPagerAdapter viewPagerAdapter;
    private ArrayList<Fragment> cropDetailFragments = new ArrayList<>();

    private MenuAdapter menuAdapter;
    private ArrayList<Menu> menuList = new ArrayList<>();

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropId = getIntent().getStringExtra("crop_id");
        varietyId = getIntent().getStringExtra("variety_id");
        land = getFromIntent("land", Maincrops_Model.class);
        selectedLandId = land.getId();
        
        setToolbarTitle("My Crop", true);

        if (getModuleManager().canDelete(Components.MyFarm.CROP))
        setMenu(R.menu.crop_menu);

        landSpinner = findViewById(R.id.landSpinner);
        viewPager = findViewById(R.id.viewPager);
        landGroup = findViewById(R.id.landGroup);

        landGroup.setSelectionRequired(true);
        landGroup.setSingleLine(true);

        landSpinner.setOnClickListener(v->showLands());

        landGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                onLandSelected(checkedId);
            }
        });

        viewPagerAdapter = new GeneralViewPagerAdapter(getSupportFragmentManager(),
                this::getFragments);

        viewPager.setAdapter(viewPagerAdapter);
        WormDotsIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        loadMenus();

        RecyclerView menuView = findViewById(R.id.recyclerView);
        menuAdapter = new MenuAdapter(this);
        menuView.setLayoutManager(new GridLayoutManager(this, 3));
        menuView.setAdapter(menuAdapter);

        loadLands();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.disable){
            confirmDisable();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDisable(){
        Crops_Main crop = getCrop();
        new CommonDialog(this, null,
                "Do you want to disable '" + crop.getName() + "'", "yes", "no",
                new CommonDialog.Action() {
            @Override
            public void actionOne() {
                updateCrop();
            }

            @Override
            public void actionTwo() {
                //Toasty.success(CropActivity.this, "Action Two").show();
            }

            @Override
            public void onCancelled() {
                Toasty.error(CropActivity.this, "Update Crop Error!").show();
            }
        });
    }

    private void updateCrop(){
        showLoading();
        ApiHelper.updateCrop(getCrop().getLcId(), getSessionManager().getUser().getId() ,new ApiHelper.CropUpdateAction() {
            @Override
            public void onResult(JSONObject response,boolean status, boolean error) {
                hideLoading();

                if (UserHelper.checkResponse(CropActivity.this, response)){
                    return;
                }

                if (error){
                    Toasty.error(CropActivity.this, "Crop Update Error!").show();
                    return;
                }

                if (status){
                    sendBroadcast(Constants.Broadcasts.LAND_UPDATE);
                    loadLands();
                    Toasty.success(CropActivity.this, "Crop Updated!").show();
                }else {
                    Toasty.normal(CropActivity.this, "Crop Update Failed!").show();
                }
            }
        });
    }

    @SuppressLint("SuspiciousIndentation")
    private void loadMenus(){
        menuList.clear();

        if (getModuleManager().canView(Components.MyFarm.ZONE))
        menuList.add(new Menu(R.drawable.ic_zone, "Zones", "my_zone"));
        if (getModuleManager().canView(Components.MyFarm.ACTIVITY))
        menuList.add(new Menu(R.drawable.ic_activities, "Activity", "activities"));
        if (getModuleManager().canView(Components.MyFarm.PEST_DISEASE))
        menuList.add(new Menu(R.drawable.ic_pest_disease, "Pest & Disease", "pest_and_disease"));
        if (getModuleManager().canView(Components.MyFarm.MY_STOCK))
        menuList.add(new Menu(R.drawable.ic_my_stocks, "Stock", "stock"));
        if (getModuleManager().canView(Components.MyFarm.INCOME)
        ||getModuleManager().canView(Components.MyFarm.EXPENSE))
        menuList.add(new Menu(R.drawable.ic_finance, "Finance", "finance"));
        if (getModuleManager().canView(Modules.SCHEME))
        menuList.add(new Menu(R.drawable.ic_schemes, "Schemes", "schemes"));
    }

    private void loadLands(){
        showLoading();
        ApiHelper.loadLands(sessionManager.getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response,ArrayList<Maincrops_Model> lands, boolean error) {
                hideLoading();
                if (UserHelper.checkResponse(CropActivity.this, response)){
                    return;
                }
                if (!error){
                    CropActivity.this.lands.clear();
                    CropActivity.this.lands.addAll(lands);
                    updateLands();
                }
            }
        });
    }

    private void updateLands(){

        /*landGroup.removeAllViews();

        int landIndex = 0;
        for (Maincrops_Model land: lands){
            Chip chip = new Chip(this);
            chip.setId(landIndex++);
            chip.setText(land.getLand_name());
            chip.setCheckable(true);
            chip.setChecked(land.getId().equals(selectedLandId));
            landGroup.addView(chip);
        }*/

        if (selectedLandId != null && !lands.isEmpty()){
            boolean isLandSelected = false;
            for (int i=0; i<lands.size(); i++){
                if (lands.get(i).getId().equals(selectedLandId)){
                    isLandSelected = true;
                    onLandSelected(i);
                }
            }
            if (!isLandSelected) onLandSelected(0);
        }
    }

    private void showLands(){
        if (lands.isEmpty()) return;
        new SelectItemDialog("Lands",(List<Object>)(List<?>) lands, position -> {
            onLandSelected(position);
//            landId = lands.get(position).getId();
//            updateLands();
            //updateCrops();
            //loadZones(landId, lands.get(position).getId());
        }).show(getSupportFragmentManager(), "crops");
    }

    private void onLandSelected(int position){
        selectedLandId = lands.get(position).getId();
        landSpinner.setText(lands.get(position).getLand_name());
        updateCropDetails();
    }

    private boolean isFirstCrop = true;

    private void updateCropDetails(){
        try {

            Maincrops_Model land  = getLand();
            JSONArray mainCrops = new JSONArray(land.getMainCropsString());
            JSONArray interCrops = new JSONArray(land.getInterCropsString());

            cropDetailFragments.clear();
            viewPagerAdapter.notifyDataSetChanged();

            int currentFragment = 0;

            ArrayList<Crops_Main> mainCropModels = ApiHelper.getMainCropModels(getLand());

            int i = 0;
            for (Crops_Main crop: mainCropModels){
                if (isFirstCrop && CropActivity.this.cropId != null && cropId.equals(crop.getCrop_id())){
                    isFirstCrop = false;
                    currentFragment = i;
                }
                cropDetailFragments.add(
                        CropDetailFragment
                            .getInstance(land, "Main Crop", crop));
                i++;
            }

            ArrayList<Crops_Main> interCropModels = ApiHelper.getInterCropModels(getLand());

            for (Crops_Main crop: interCropModels){
                if (isFirstCrop && CropActivity.this.cropId != null && cropId.equals(crop.getCrop_id())){
                    isFirstCrop = false;
                    currentFragment = i;
                }
                cropDetailFragments.add(
                        CropDetailFragment
                                .getInstance(land, "Main Crop", crop));
                i++;
            }

            /*for (int i=0; i<mainCrops.length(); i++){
                JSONObject jsonObject = mainCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                if (isFirstCrop && CropActivity.this.cropId != null
                        && cropId.equals(CropActivity.this.cropId)){
                    isFirstCrop = false;
                    currentFragment = i;
                }
                cropDetailFragments.add(CropDetailFragment
                        .getInstance(land, "Main Crop", ));
            }

            for (int i=0; i<interCrops.length(); i++){
                JSONObject jsonObject = interCrops.getJSONObject(i);
                String cropId = jsonObject.getString("crop_id");
                if (isFirstCrop && CropActivity.this.cropId != null
                        && cropId.equals(CropActivity.this.cropId)){
                    isFirstCrop = false;
                    currentFragment = mainCrops.length() + i;
                }
                cropDetailFragments.add(CropDetailFragment
                        .getInstance(land, cropId, varietyId, "Inter Crop"));
            }*/

            viewPagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(currentFragment);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Maincrops_Model getLand(){
        for (Maincrops_Model land: lands){
            if (selectedLandId.equals(land.getId())) return land;
        }
        return null;
    }

    @Override
    public ArrayList<Fragment> getFragments() {
        return cropDetailFragments;
    }

    @Override
    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    @Override
    public void onClicked(int position) {
        switch (position){
            case 0:

                openActivity(ZonesActivity.class, Pair.create("land_id", getLandId()),
                        Pair.create("crop_id", getCropId()),
                        Pair.create("variety_id", getCropId()),
                        Pair.create("crop", getCrop()));

                /*openWithSubscription(ZonesActivity.class,
                    Components.MyFarm.ZONE,
                    ModuleManager.ACCESS.VIEW,
//                openWithAccess(ZonesActivity.class,
//                        VIEW_ZONES,
                        Pair.create("land_id", getLandId()),
                        Pair.create("crop_id", getCropId()),
                        Pair.create("variety_id", getCropId()),
                        Pair.create("crop", getCrop()));*/
                break;
            case 1:

                openActivity(ActivitiesActivity.class,Pair.create("land_id", getLandId()),
                        Pair.create("crop_id", getCropId()),
                        Pair.create("variety_id", getVarietyId()),
                        Pair.create("crop", getCrop()));

                /*openWithSubscription(ActivitiesActivity.class,
                    Components.MyFarm.ACTIVITY,
                    ModuleManager.ACCESS.VIEW,
//                openWithAccess(ActivitiesActivity.class,
//                    VIEW_LAND_ACTIVITIES,
                    Pair.create("land_id", getLandId()),
                    Pair.create("crop_id", getCropId()),
                    Pair.create("variety_id", getVarietyId()),
                    Pair.create("crop", getCrop()));*/
                break;
            case 2:

                openActivity(PestDiseaseActivity.class,Pair.create("land_id", getLandId()),
                        Pair.create("crop_id", getCropId()),
                        Pair.create("crop", getCrop()));

               /* openWithSubscription(PestDiseaseActivity.class,
                    Components.MyFarm.PEST_DISEASE,
                    ModuleManager.ACCESS.VIEW,
//                openWithAccess(PestDiseaseActivity.class,
//                    VIEW_PEST_HISTORY,
                    Pair.create("land_id", getLandId()),
                    Pair.create("crop_id", getCropId()),
                    Pair.create("crop", getCrop()));*/
                break;
            case 3:

                openActivity(FarmX_MyStockActivity.class);
                /*openWithSubscription(FarmX_MyStockActivity.class,
                        Components.MyFarm.MY_STOCK,
                        ModuleManager.ACCESS.VIEW);*/
//                openWithAccess(FarmX_MyStockActivity.class, VIEW_STOCKS);
                break;
            case 4:

                openActivity(FinancesActivity.class, Pair.create("land_id", getLandId()),
                        Pair.create("crop_id", getCropId()),
                        Pair.create("variety_id", getVarietyId()));

               /* openWithSubscription(FinancesActivity.class,
                        Components.MyFarm.EXPENSE,
                        ModuleManager.ACCESS.VIEW,
//                openWithAccess(FinancesActivity.class,
//                    VIEW_EXPENSE,
                    Pair.create("land_id", getLandId()),
                    Pair.create("crop_id", getCropId()),
                    Pair.create("variety_id", getVarietyId()));*/
                break;
            case 5:

                openActivity(Scheme_Activity.class,Pair.create("land_id", getLandId()),
                        Pair.create("crop_id", getCropId()));
                /*openWithSubscription(Scheme_Activity.class,
                        Modules.SCHEME,
                        ModuleManager.ACCESS.VIEW,
//                openWithAccess(Scheme_Activity.class,
//                    SCHEME,
                    Pair.create("land_id", getLandId()),
                    Pair.create("crop_id", getCropId()));*/
                    break;
        }
    }

    private String getLandId(){
        if (selectedLandId != null) return selectedLandId;
        else if (land != null) return land.getId();
        else if (!lands.isEmpty()) lands.get(0).getId();
        return null;
    }

    private String getCropId(){
        return ((CropDetailFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem())).getCropId();
    }

    private String getVarietyId(){
        return ((CropDetailFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem())).getVarietyId();
    }

    private Crops_Main getCrop(){
        return ((CropDetailFragment) viewPagerAdapter.getItem(viewPager.getCurrentItem())).getCrop();
    }
}