package com.ascentya.AsgriV2.utility.activity.soil_test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.SoilTestRequestAdapter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.LocationUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.utility.model.SoilTest;
import com.ascentya.AsgriV2.utility.model.WaterTest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestRequestsActivity extends BaseActivity
        implements SoilTestRequestAdapter.Action, LocationUtils.Action {

    private RecyclerView recyclerView;
    private MaterialButton newRequestButton;

    private SoilTestRequestAdapter adapter;
    private LocationUtils locationUtils;

    private ArrayList<SoilTest> soilTestRequests = new ArrayList<>();
    private ArrayList<WaterTest> waterTests = new ArrayList<>();

    private Constants.TestType testType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_test_history);

        testType = getFromIntent("test_type", Constants.TestType.class);

        setToolbarTitle(testType.name + " " + "Requests",  true);
        setMenu(R.menu.soil_test_menu);

        newRequestButton = findViewById(R.id.newRequest);
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new SoilTestRequestAdapter(testType, this);
        locationUtils = new LocationUtils(this, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (!getModuleManager().canInsert(Components.Utility.SOIL_TEST) &&
                !getModuleManager().canInsert(Components.Utility.WATER_TEST)){
            newRequestButton.setVisibility(View.GONE);
        }

        newRequestButton.setOnClickListener(view -> checkLocation());

        loadSoilTestRequests();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.whatsApp){
            openWhatsApp(Constants.SAMPLE_NUMBER);
        }else if (item.getItemId() == R.id.call){
            openCall(Constants.SAMPLE_NUMBER);
        }else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void loadSoilTestRequests(){

//        AccessManager.Modules.Operation operation =
//                testType.type.equals(Constants.TestTypes.SoilTest.type) ?
//                        ST_VIEW_REQUEST : WT_VIEW_REQUEST;
//
//        if (!checkPrivilege(operation)) return;

        soilTestRequests.clear();
        showLoading();


        AndroidNetworking.post(Webservice.getSoilTestRequests)
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .addUrlEncodeFormBodyParameter("type", testType.type)
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        if (UserHelper.checkResponse(TestRequestsActivity.this, response)){
                            return;
                        }
                        System.out.println("getSoilTestRequests:\n" + response);
                        try {
                            if (response.optBoolean("status")){
                                if (testType.type.equals(Constants.TestTypes.SoilTest.type)) {
                                    soilTestRequests.addAll(GsonUtils.getGson()
                                            .fromJson(response.getJSONArray("data").toString(),
                                                    EMarketStorage.SoilTestRequestListType));
                                }else {
                                    waterTests.addAll(GsonUtils.getGson()
                                            .fromJson(response.getJSONArray("data").toString(),
                                                    EMarketStorage.WaterTestRequestListType));
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            updateTestRequests();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        System.out.println("getSoilTestRequests Error:\n" + GsonUtils.getGson().toJson(anError));
                        Toasty.error(TestRequestsActivity.this,
                                "Test Requests Load Error!").show();
                    }
                }
        );

        System.out.println("User Id: " + sessionManager.getUser().getId());
        System.out.println("Type: " + testType.type);

        //updateTestRequests();
    }

    private void updateTestRequests(){
        adapter.notifyDataSetChanged();
        updateUi();
    }

    private void updateUi(){
        ((TextView)findViewById(R.id.noDataTv)).setText("No " + testType.name + " Requests!");
        findViewById(R.id.noDataTv).setVisibility((soilTestRequests.isEmpty() && waterTests.isEmpty()) ?
                View.VISIBLE : View.INVISIBLE);
    }

    private void checkLocation(){
        locationUtils.getLocation();
    }

    private void openSoilTestLabs(Double latitude, Double longitude){
        if(testType.type.equals(Constants.TestTypes.SoilTest.type)) {
            openWithSubscription(TestLabsActivity.class, Components.Utility.SOIL_TEST,
                    ModuleManager.ACCESS.INSERT,
                    Pair.create("latitude", latitude),
                    Pair.create("longitude", longitude),
                    Pair.create("test_type", testType));
        }else{
            openWithSubscription(TestLabsActivity.class,
                    Components.Utility.WATER_TEST, ModuleManager.ACCESS.INSERT,
                    Pair.create("latitude", latitude),
                    Pair.create("longitude", longitude),
                    Pair.create("test_type", testType));
        }
    }

    @Override
    public List<SoilTest> getSoilRequestList() {
        return soilTestRequests;
    }

    @Override
    public List<WaterTest> getWaterRequestList() {
        return waterTests;
    }

    @Override
    public void onResultClicked(int position) {
        if(testType.type.equals(Constants.TestTypes.SoilTest.type)) {
            openWithSubscription(SoilTestResultActivity.class,
                    Components.Utility.SOIL_TEST, ModuleManager.ACCESS.VIEW,
                    Pair.create("soil_test_request",
                            soilTestRequests.get(position)));
        }else {
            openWithSubscription(WaterTestResultActivity.class,
                    Components.Utility.WATER_TEST, ModuleManager.ACCESS.VIEW,
                    Pair.create("water_test",
                            waterTests.get(position)));
        }
    }

    @Override
    public void onLocationReceived(Double latitude, Double longitude) {
        openSoilTestLabs(latitude, longitude);
    }

    @Override
    public void onPermissionDenied() {
        Toasty.error(this, "Need Location Permission to Proceed!").show();
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtils.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationUtils.onActivityResult(requestCode, resultCode, data);
    }
}