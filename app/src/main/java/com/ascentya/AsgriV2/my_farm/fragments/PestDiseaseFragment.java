package com.ascentya.AsgriV2.my_farm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.ZoneReport;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.activities.PestDetailActivity;
import com.ascentya.AsgriV2.my_farm.activities.ReportPestDiseaseActivity;
import com.ascentya.AsgriV2.my_farm.adapters.PestDiseaseAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.ascentya.AsgriV2.managers.AccessManager.Modules.VIEW_PEST_DISEASES;

import org.json.JSONObject;

public class PestDiseaseFragment extends BaseFragment implements PestDiseaseAdapter.Action, BaseActivity.ChangeLand {

    private boolean showToolbar;
    private String landId, cropId, zoneId, userId;
    private Toolbar toolbar;

    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<Crops_Main> crops = new ArrayList<>();
    private ArrayList<Zone_Model> zones = new ArrayList<>();
    private ArrayList<ZoneReport> reports = new ArrayList<>();

    private int selectedLandIndex = 0, selectedCropIndex = 0, selectedZoneIndex = 0;

    private ChipGroup zoneGroup;
    private RecyclerView pestRv;
    private PestDiseaseAdapter pestAdapter;
    private FloatingActionButton addPest;

    private BaseActivity.ReceiverInterface pestUpdates = new BaseActivity.ReceiverInterface() {
        @Override
        public void onReceive(Intent intent) {
            if (getView() != null) loadZones();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            showToolbar = getArguments().getBoolean("toolbar");
            landId = getArguments().getString("land_id");
            cropId = getArguments().getString("crop_id");
            zoneId = getArguments().getString("zone_id");
        }

        registerReceiver(Constants.Broadcasts.PEST_UPDATE, pestUpdates);
        pestAdapter = new PestDiseaseAdapter(this);

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
        return inflater.inflate(R.layout.fragment_pest_disease, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        view.findViewById(R.id.appBarLay).setVisibility(showToolbar ? View.VISIBLE : View.GONE);

        setToolbar();

        view.findViewById(R.id.landContainer).setOnClickListener(v -> showLands());
        view.findViewById(R.id.cropContainer).setOnClickListener(v -> showCrops());
        addPest = view.findViewById(R.id.add);


        zoneGroup = view.findViewById(R.id.zoneGroup);
        zoneGroup.setSelectionRequired(true);

        pestRv = view.findViewById(R.id.recyclerView);
        pestRv.setLayoutManager(new LinearLayoutManager(getContext()));
        pestRv.setAdapter(pestAdapter);


        addPest.setOnClickListener(v -> addPest());

        loadLands();

        if (!getModuleManager().canInsert(Components.MyFarm.PEST_DISEASE))
            addPest.setVisibility(View.INVISIBLE);


        zoneGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                selectedZoneIndex = checkedId;
                //updateZones();
                onChange();
            }
        });
    }

    private void setToolbar() {
        if (showToolbar) {
            toolbar.setTitle(getString(R.string.pest_disease));
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }

    private void showLands() {
        new SelectItemDialog(getString(R.string.lands), (List<Object>) (List<?>) lands, position -> {
            selectedLandIndex = position;
            updateLand();
        }).show(getChildFragmentManager(), "lands");
    }

    private void showCrops() {
        if (!lands.isEmpty() && !checkCrop(lands.get(selectedLandIndex), lands, PestDiseaseFragment.this)) return;
        new SelectItemDialog(getString(R.string.crops), (List<Object>) (List<?>) crops, position -> {
            selectedCropIndex = position;
            updateCrop();
        }).show(getChildFragmentManager(), "lands");
    }

    private void addPest() {
        /*if (!checkSubscription(Components.MyFarm.PEST_DISEASE, ModuleManager.ACCESS.VIEW))
            return;*/

        if (zones.isEmpty()){
            toast("No Zones!");
            return;
        }

        //Reports_Dialog obj = new Reports_Dialog();
        //obj.setAction(() -> loadPestDiseases());
        //obj.dialog(requireActivity(), getString(R.string.report), lands.get(selectedLandIndex).getId(), null,
        //        zones.get(selectedZoneIndex).getZone_id(), getSessionManager().getUser().getId());

        openActivity(ReportPestDiseaseActivity.class,
                Pair.create("land", lands.get(selectedLandIndex)),
                Pair.create("crop", crops.get(selectedCropIndex)),
                Pair.create("zone", zones.get(selectedZoneIndex)));
    }

    private boolean isFirstLand = true;
    private boolean isFirstCrop = true;
    private boolean isFirstZone = true;

    private void loadLands() {
      //  if (!checkSubscription(Components.MyFarm.LAND)) return;

        showLoading();
        ApiHelper.loadLands(getSessionManager().getUser().getId(), (response,lands, error) -> {
            hideLoading();
            PestDiseaseFragment.this.lands.clear();
            PestDiseaseFragment.this.lands.addAll(lands);
            updateLand();
        });
    }

    private void updateLand() {
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

    private void loadCrops() {
        //if (!checkSubscription(Components.MyFarm.CROP)) return;
        if (lands.isEmpty()) return;
        if (!checkCrop(lands.get(selectedLandIndex), lands, PestDiseaseFragment.this)) return;
        crops.clear();
        crops.addAll(ApiHelper.getAllCrops(lands.get(selectedLandIndex)));
        updateCrop();
        updateButton();
    }

    private void updateCrop() {
        if (getView() == null) return;
        if (isFirstCrop && cropId != null) {
            isFirstCrop = false;
            for (int i = 0; i < crops.size(); i++) {
                if (crops.get(i).getCrop_id().equals(cropId)) {
                    selectedCropIndex = i;
                }
            }
        }
        ((TextView) getView().findViewById(R.id.cropSpinner))
                .setText(crops.get(selectedCropIndex).getName());
        selectedZoneIndex = 0;
        loadZones();
    }

    private void updateButton() {
        if (addPest != null)
            addPest.setVisibility(View.VISIBLE);
    }

    private void loadZones() {

        //if (!checkPrivilege(VIEW_ZONES)) return;

        String landId = lands.get(selectedLandIndex).getId();
        String cropId = crops.get(selectedCropIndex).getCrop_id();
        String lcId = crops.get(selectedCropIndex).getLcId();
        String userId = getSessionManager().getUser().getId();
        showLoading();
        ApiHelper.loadZones(landId, userId, cropId, lcId, new ApiHelper.ZoneAction() {
            @Override
            public void onLoadComplete(JSONObject response, ArrayList<Zone_Model> zones, boolean error) {
                hideLoading();

                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                PestDiseaseFragment.this.zones.clear();
                PestDiseaseFragment.this.zones.addAll(zones);
                updateZones();
            }
        });
    }

    private void updateZones() {

        zoneGroup.removeAllViews();

        if (zones.isEmpty()) {
            reports.clear();
            pestAdapter.notifyDataSetChanged();
            return;
        }

        if (isFirstZone && zoneId != null) {
            isFirstZone = false;
            for (int i = 0; i < zones.size(); i++) {
                Zone_Model zone = zones.get(i);
                if (zone.getZone_id().equals(zoneId)) {
                    selectedZoneIndex = i;
                }
            }
        }

        int zoneIndex = 0;
        for (Zone_Model zone : zones) {
            Chip chip = new Chip(getContext());
            chip.setText(zone.getZone_name());
            chip.setCheckable(true);
            chip.setChecked(selectedZoneIndex == zoneIndex);
            chip.setId(zoneIndex++);
            zoneGroup.addView(chip);
        }

        onChange();
    }

    private void onChange() {
        // Maincrops_Model land = lands.get(selectedLandIndex);
        // Crops_Main cropsMain = crops.get(selectedCropIndex);
        loadPestDiseases();
    }

    private void loadPestDiseases() {
        //if (!checkPrivilege(VIEW_PEST_HISTORY)) return;

        if (lands.isEmpty() || crops.isEmpty() || zones.isEmpty()) return;

        String landId = lands.get(selectedLandIndex).getId();
        String cropId = crops.get(selectedCropIndex).getCrop_id();
        String zoneId = zones.get(selectedZoneIndex).getZone_id();

        showLoading();
        ApiHelper.loadPestDisease(getSessionManager().getUser().getId(), landId, zoneId, new ApiHelper.PestAction() {
            @Override
            public void onLoadComplete(JSONObject response,ArrayList<ZoneReport> reports, boolean error) {
                hideLoading();
                if (UserHelper.checkResponse(requireContext(), response)){
                    return;
                }
                PestDiseaseFragment.this.reports.clear();
                PestDiseaseFragment.this.reports.addAll(reports);
                updatePestDiseases();
            }
        });
    }

    private void updatePestDiseases() {
        pestAdapter.notifyDataSetChanged();
    }

    public static PestDiseaseFragment newInstance(String landId, String cropId, String zoneId) {
        return newInstance(false, landId, cropId, zoneId);
    }

    public static PestDiseaseFragment newInstance(boolean showToolbar, String landId, String cropId, String zoneId) {
        PestDiseaseFragment fragment = new PestDiseaseFragment();
        Bundle args = new Bundle();
        args.putBoolean("toolbar", showToolbar);
        args.putString("land_id", landId);
        args.putString("crop_id", cropId);
        args.putString("zone_id", zoneId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public ArrayList<ZoneReport> getReports() {
        return reports;
    }

    @Override
    public void onClicked(int position) {
        openWithAccess(PestDetailActivity.class,
                VIEW_PEST_DISEASES,
                Pair.create("land", lands.get(selectedLandIndex)),
                Pair.create("crop", crops.get(selectedCropIndex)),
                Pair.create("zone", zones.get(selectedZoneIndex)),
                Pair.create("zone_report", getReports().get(position)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!lands.isEmpty()) checkCrop(lands.get(selectedLandIndex), lands, PestDiseaseFragment.this);
    }

    @Override
    public void onChangeLand() {
        showLands();
    }
}
