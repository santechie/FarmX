package com.ascentya.AsgriV2.utility.activity.soil_test;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.SoilTestLabAdapter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.utility.model.SoilTestLab;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestLabsActivity extends BaseActivity implements SoilTestLabAdapter.Action {

    private RecyclerView recyclerView;
    private MaterialButton nextBtn;

    private SoilTestLabAdapter adapter;

    private ArrayList<SoilTestLab> soilTestLabs = new ArrayList<>();
    private int selectedLabIndex = 0;

    private Double latitude, longitude;
    private Constants.TestType testType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_test_labs);

        testType = getFromIntent("test_type", Constants.TestType.class);

        setToolbarTitle(testType.name + " Labs", true);
        setMenu(R.menu.soil_test_menu);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        recyclerView = findViewById(R.id.recyclerView);
        nextBtn = findViewById(R.id.next);

        adapter = new SoilTestLabAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        nextBtn.setOnClickListener(view -> openSoilTestProcedures());

        loadSoilTestLabs();
    }

    private void loadSoilTestLabs(){
        soilTestLabs.clear();
        showLoading();
        System.out.println("Location: " + latitude + " - " + longitude);
        AndroidNetworking.post(Webservice.getSoilTestLabs)
                .addUrlEncodeFormBodyParameter("latitude", String.valueOf(latitude))
                .addUrlEncodeFormBodyParameter("longitude", String.valueOf(longitude))
                .addUrlEncodeFormBodyParameter("type", testType.type)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                if (UserHelper.checkResponse(TestLabsActivity.this, response)){
                    return;
                }
                System.out.println("Soil Test Labs:\n" + response);
                try {
                    if (response.optBoolean("status")){
                        soilTestLabs.addAll(GsonUtils.getGson()
                                .fromJson(response.optJSONArray("data").toString(),
                                        EMarketStorage.SoilTestLabListType));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    updateSoilTestLabs();
                }
            }

            @Override
            public void onError(ANError anError) {
                try {
                    System.out.println("Lab Load Error: \n" + GsonUtils.getGson().toJson(anError));
                }catch (Exception e){}
                hideLoading();
                Toasty.error(TestLabsActivity.this, testType.name + " Labs Error!").show();
            }
        });
        updateSoilTestLabs();
    }

    private void updateSoilTestLabs(){
        adapter.notifyDataSetChanged();
        updateUI();
    }

    private void updateUI(){
        findViewById(R.id.noDataTv).setVisibility(soilTestLabs.isEmpty() ?
                 View.VISIBLE : View.INVISIBLE);
        nextBtn.setEnabled(!soilTestLabs.isEmpty());
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

    private void openSoilTestProcedures(){
        SoilTestLab lab = soilTestLabs.get(selectedLabIndex);
        openActivity(TestProcedureActivity.class,
                Pair.create("soil_test_lab", lab),
                Pair.create("latitude", latitude),
                Pair.create("longitude", longitude),
                Pair.create("test_type", testType));
    }

    @Override
    public List<SoilTestLab> getSoilTestLabs() {
        return soilTestLabs;
    }

    @Override
    public int getSelectedItem() {
        return selectedLabIndex;
    }

    @Override
    public void setSelectedItem(int position) {
        //Toast.makeText(this, "Selected: " + position, Toast.LENGTH_SHORT).show();
        selectedLabIndex = position;
        updateSoilTestLabs();
    }
}