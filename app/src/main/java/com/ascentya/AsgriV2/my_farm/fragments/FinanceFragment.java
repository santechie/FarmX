package com.ascentya.AsgriV2.my_farm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.ascentya.AsgriV2.Utils.LogUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.farmx.myfinance.Expanse;
import com.ascentya.AsgriV2.farmx.myfinance.Revenue;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class FinanceFragment extends BaseFragment
        implements GeneralViewPagerAdapter.Action, BaseActivity.ChangeLand {

    private boolean showToolbar;
    private String landId, cropId, varietyId;
    private Toolbar toolbar;

    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<Crops_Main> crops = new ArrayList<>();
    private int selectedLandIndex = 0, selectedCropIndex = 0, selectedMenu = 0;

    private ImageView cropImageIv;
    private TextView cropNameTv, acreCountTv;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ArrayList<Fragment> fragments = new ArrayList<>();
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
            System.out.println("Land Id: " + landId);
            System.out.println("Crop Id: " + cropId);
            System.out.println("Variety Id: " + varietyId);
        }

        viewPagerAdapter = new GeneralViewPagerAdapter(getChildFragmentManager(), this);

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
        return inflater.inflate(R.layout.fragment_finance, container, false);
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

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        loadLands();
    }

    private void setToolbar(){
        if (showToolbar){
            toolbar.setTitle(getString(R.string.finance_));
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
        if (!lands.isEmpty() && !checkCrop(lands.get(selectedLandIndex), lands, FinanceFragment.this)) return;
        new SelectItemDialog(getString(R.string.crops), (List<Object>)(List<?>) crops, position -> {
            selectedCropIndex = position;
            updateCrop();
        }).show(getChildFragmentManager(), "lands");
    }

    private void loadLands(){
//        if (!checkPrivilege(VIEW_LAND)) return;
        ApiHelper.loadLands(getSessionManager().getUser().getId(), (response, lands, error) -> {
            if (UserHelper.checkResponse(requireContext(), response)){
                return;
            }
            FinanceFragment.this.lands.clear();
            FinanceFragment.this.lands.addAll(lands);
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
//        if (!checkPrivilege(VIEW_CROPS)) return;
        if (lands.isEmpty()) return;
        if (!checkCrop(lands.get(selectedLandIndex), lands, FinanceFragment.this)) return;
        crops.clear();
        crops.addAll(ApiHelper.getAllCrops(lands.get(selectedLandIndex)));
        if (isFirstCrop && cropId != null){
            isFirstCrop = false;
            for (int i=0; i<crops.size(); i++){
                if (crops.get(i).getCrop_id().equals(cropId)){
                    if (varietyId == null) selectedCropIndex = i;
                    else if (varietyId.equals(crops.get(i).getVarietyId())){
                        selectedCropIndex = i;
                    }
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

    private void loadFragments(){

        fragments.clear();

        LogUtils.log(getClass().getSimpleName(),
                "Selected Crop: " + GsonUtils.toJson(crops.get(selectedCropIndex)));

     //   if (checkSubscription(Components.MyFarm.EXPENSE, ModuleManager.ACCESS.VIEW))
            //if (getModuleManager().canView(Components.MyFarm.EXPENSE))
            fragments.add(Expanse.newInstance(getString(R.string.expenses), lands.get(selectedLandIndex).getId(),
                    crops.get(selectedCropIndex).getCrop_id(), crops.get(selectedCropIndex).getLcId()));

      //  if (checkSubscription(Components.MyFarm.INCOME, ModuleManager.ACCESS.VIEW))
          //  if (getModuleManager().canView(Components.MyFarm.INCOME))
            fragments.add(Revenue.newInstance(getString(R.string.revenues), lands.get(selectedLandIndex).getId(),
                    crops.get(selectedCropIndex).getCrop_id(), crops.get(selectedCropIndex).getLcId()));

        viewPagerAdapter.notifyDataSetChanged();
    }

    public static FinanceFragment newInstance(String landId, String cropId, String varietyid){
        return newInstance(false, landId, cropId, varietyid);
    }

    public static FinanceFragment newInstance(boolean showToolbar, String landId, String cropId, String varietyId){
        FinanceFragment financeFragment = new FinanceFragment();
        Bundle args = new Bundle();
        args.putBoolean("toolbar", showToolbar);
        args.putString("land_id", landId);
        args.putString("crop_id", cropId);
        financeFragment.setArguments(args);
        return financeFragment;
    }

    @Override
    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!lands.isEmpty()) checkCrop(lands.get(selectedLandIndex), lands, FinanceFragment.this);
    }

    @Override
    public void onChangeLand() {
        showLands();
    }
}
