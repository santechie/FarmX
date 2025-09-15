package com.ascentya.AsgriV2.my_farm.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import es.dmoral.toasty.Toasty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ascentya.AsgriV2.Adapters.GeneralViewPagerAdapter;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.fragments.CropListFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyCropsActivity extends BaseActivity implements GeneralViewPagerAdapter.Action {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GeneralViewPagerAdapter adapter;
    private CropListFragment mainCropFragment, interCropFragment;
    private ArrayList<Fragment> cropListFragments = new ArrayList<>();
    private ArrayList<Maincrops_Model> mainCrops = new ArrayList<>();
    private ArrayList<Maincrops_Model> interCrops = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crops);

        setToolbarTitle(getString(R.string.my_crops), true);

//        if(getModuleManager().canInsert(Components.MyFarm.CROP))
        setMenu(R.menu.my_crops_menu);

        loadFragments();
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        adapter = new GeneralViewPagerAdapter(getSupportFragmentManager(),
                this::getFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        registerReceiver(Constants.Broadcasts.LAND_UPDATE, new ReceiverInterface() {
            @Override
            public void onReceive(Intent intent) {
                loadCrops();
            }
        });
        loadCrops();
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.crop_history) {


            Intent ij = new Intent(getApplicationContext(), CropsHistoryActivity.class);
            ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(ij);

              /*  if (checkSubscription(Components.MyFarm.CROP, ModuleManager.ACCESS.VIEW))
                openActivity(CropsHistoryActivity.class);*/
            return false;
        }else if (item.getItemId() == R.id.add_crop) {

            Intent ij = new Intent(getApplicationContext(), AddCropActivity.class);
            ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(ij);

               /* if (checkSubscription(Components.MyFarm.CROP, ModuleManager.ACCESS.INSERT)) {
                    openActivity(AddCropActivity.class);
                }*/
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        if (!getModuleManager().canInsert(ModuleManager.Components.MyFarm.CROP))
//            menu.findItem(R.id.add_crop).setVisible(true);
//        return false;
//    }

    private void loadFragments() {
        mainCropFragment = CropListFragment.getInstance(getString(R.string.main_crops));
        interCropFragment = CropListFragment.getInstance(getString(R.string.inter_crops));
        cropListFragments.add(mainCropFragment);
        cropListFragments.add(interCropFragment);
    }

    private void loadCrops() {
        showLoading();

        ApiHelper.loadLands(sessionManager.getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Maincrops_Model> lands, boolean error) {
                hideLoading();

               /* if (UserHelper.checkResponse(MyCropsActivity.this, response)) {
                    return;
                }*/

                MyCropsActivity.this.mainCrops.clear();
                MyCropsActivity.this.interCrops.clear();

                if (!error) {
                    try {
                        for (Maincrops_Model land : lands) {

                            ArrayList<Crops_Main> mainCrops = ApiHelper.getMainCropModels(land);
                            ArrayList<Crops_Main> interCrops = ApiHelper.getInterCropModels(land);

                            for (Crops_Main crop : mainCrops) {
                                Maincrops_Model cropLand = land.Clone();
                                cropLand.setMaincrop(crop.getCrop_id());
                                cropLand.setVarietyId(crop.getVarietyId());
                                cropLand.setLcId(crop.getLcId());
                                MyCropsActivity.this.mainCrops.add(cropLand);

                            }

                            for (Crops_Main crop : interCrops) {
                                Maincrops_Model cropLand = land.Clone();
                                cropLand.setIntercrop(crop.getCrop_id());
                                cropLand.setVarietyId(crop.getVarietyId());
                                cropLand.setLcId(crop.getLcId());
                                MyCropsActivity.this.interCrops.add(cropLand);
                            }
                        }

                        if (getModuleManager().canView(Components.MyFarm.CROP))
                            updateCrops();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toasty.error(MyCropsActivity.this, "Load Crop Error!").show();
                }
            }
        });

        /*AndroidNetworking.get(Webservice.add_farmxcrops + sessionManager.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    if (jsonObject.getBoolean("status")){
                        JSONArray landsJson = jsonObject.optJSONArray("data");
                        if (landsJson != null) {
                            for (int i = 0; i < landsJson.length(); i++) {
                                Maincrops_Model obj = new Maincrops_Model();
                                obj.setId(landsJson.getJSONObject(i).optString("id"));
                                obj.setLand_name(landsJson.getJSONObject(i).optString("land_name"));
                                obj.setDistrict(landsJson.getJSONObject(i).optString("district"));
                                obj.setTaluk(landsJson.getJSONObject(i).optString("taluk"));
                                obj.setAcre_count(landsJson.getJSONObject(i).optString("acre_count"));
                                obj.setSoiltype(landsJson.getJSONObject(i).optString("soiltype"));
                                obj.setMaincrop(landsJson.getJSONObject(i).optString("maincrop"));
                                obj.setMainCropsString(landsJson.getJSONObject(i).optString("maincrop"));
                                obj.setIntercrop(landsJson.getJSONObject(i).optString("intercrop"));
                                obj.setInterCropsString(landsJson.getJSONObject(i).optString("intercrop"));
                                obj.setAnual_revenue(landsJson.getJSONObject(i).optString("anual_revenue"));
                                obj.setIrrigation_type(landsJson.getJSONObject(i).optString("irrigation_type"));
                                obj.setGovt_scheme(landsJson.getJSONObject(i).optString("govt_scheme"));
                                obj.setLive_stocks(landsJson.getJSONObject(i).optString("live_stocks"));
                                obj.setSoilhealth_card(landsJson.getJSONObject(i).optString("soilhealth_card"));
                                obj.setOrganic_farmer(landsJson.getJSONObject(i).optString("organic_farmer"));
                                obj.setExport(landsJson.getJSONObject(i).optString("export"));
                                obj.setCropId(landsJson.getJSONObject(i).optString("crop_id"));

                                JSONArray mainCropArray = new JSONArray(landsJson.getJSONObject(i).optString("maincrop"));
                                JSONArray interCropArray = new JSONArray(landsJson.getJSONObject(i).optString("intercrop"));

                                //System.out.println("Main Crop Array: " + mainCropArray);
                                //System.out.println("Inter Crop Array: " + interCropArray);

                                if (mainCropArray != null) {
                                    for (int m = 0; m < mainCropArray.length(); m++) {
                                        JSONObject mainCropObj = mainCropArray.getJSONObject(m);
                                        Maincrops_Model mainCropModel = obj.Clone();
                                        mainCropModel.setMaincrop(mainCropObj.optString("crop_id"));
                                        mainCropModel.setVarietyId(mainCropObj.optString("variety_id"));
//                                        System.out.println("Main Crop: " + mainCropModel.getMaincrop());
                                        mainCrops.add(mainCropModel);
                                    }
                                }

                                if (interCropArray != null) {
                                    for (int m = 0; m < interCropArray.length(); m++) {
                                        JSONObject interCropObj = interCropArray.getJSONObject(m);
                                        Maincrops_Model interCropModel = obj.Clone();
                                        interCropModel.setIntercrop(interCropObj.optString("crop_id"));
                                        interCropModel.setVarietyId(interCropObj.optString("variety_id"));
//                                        System.out.println("Inter Crop: " + interCropModel.getIntercrop());
                                        interCrops.add(interCropModel);
                                    }
                                }
                            }
                        }
                    }

                    updateCrops();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
            }
        });*/
    }


    private void updateCrops() {
        if (mainCropFragment != null) mainCropFragment.updateCrops(mainCrops, true);
        if (interCropFragment != null) interCropFragment.updateCrops(interCrops, false);
        if (mainCrops.isEmpty() && interCrops.isEmpty()) {
            showAddCropDialog(null, null);
        }
    }

    @Override
    public ArrayList<Fragment> getFragments() {
        return cropListFragments;
    }
}