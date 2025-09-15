package com.ascentya.AsgriV2.my_farm.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.adapters.CropHistoryAdapter;
import com.ascentya.AsgriV2.my_farm.model.CropHistory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CropsHistoryActivity extends BaseActivity implements CropHistoryAdapter.Action {

    private TextView landSpinner;
    private RecyclerView recyclerView;

    private CropHistoryAdapter adapter;

    private int selectedLand = 0;
    private String landId = null;
    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<CropHistory> cropHistoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crops_history);

        setToolbarTitle(getString(R.string.crop_history), true);

        landId = getIntent().getStringExtra("land_id");

        landSpinner = findViewById(R.id.landSpinner);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new CropHistoryAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        landSpinner.setOnClickListener(v -> showSelectLandDialog());

        loadLands();
    }

    private void loadLands(){
        ApiHelper.loadLands(getSessionManager().getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response,ArrayList<Maincrops_Model> lands, boolean error) {

                if (UserHelper.checkResponse(CropsHistoryActivity.this, response)){
                    return;
                }

                CropsHistoryActivity.this.lands.clear();
                CropsHistoryActivity.this.lands.addAll(lands);
                updateLands();
            }
        });
    }

    private void showSelectLandDialog(){
        if (lands.isEmpty())
            return;
        new SelectItemDialog(getString(R.string.lands),(List<Object>)(List<?>) lands, position -> {
            selectedLand = position;
            landId = lands.get(position).getId();
            updateLands();
            //updateCrops();
            //loadZones(landId, lands.get(position).getId());
        }).show(getSupportFragmentManager(), "lands");
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
        if (lands.isEmpty()) return;
        landSpinner.setText(lands.get(selectedLand).getLand_name());
        loadCrops();
    }

    private void loadCrops(){
        showLoading();
        String landId = lands.get(selectedLand).getId();
        ApiHelper.loadCropHistory(landId, new ApiHelper.CropHistoryAction() {
            @Override
            public void onResult(JSONObject response,ArrayList<CropHistory> cropHistoryList, boolean error) {
                hideLoading();

                if (UserHelper.checkResponse(CropsHistoryActivity.this, response)){
                    return;
                }
                CropsHistoryActivity.this.cropHistoryList.clear();
                if (!error){
                    CropsHistoryActivity.this.cropHistoryList.addAll(cropHistoryList);
                }else {
                    Toasty.error(CropsHistoryActivity.this, "Crop History Load Error").show();
                }
                updateCropHistory();
            }
        });
    }

    private void updateCropHistory(){
        findViewById(R.id.noHistory).setVisibility(cropHistoryList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public ArrayList<CropHistory> getCropHistoryList() {
        return cropHistoryList;
    }

    @Override
    public Crops_Main getCrop(String cropId) {
        return Webservice.getCrop(cropId);
    }

    @Override
    public VarietyModel getVariety(String varietyId) {
        return Webservice.getVariety(varietyId);
    }
}