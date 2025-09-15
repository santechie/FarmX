package com.ascentya.AsgriV2.utility.activity.soil_test;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.utility.adapter.SoilTestResultAdapter;
import com.ascentya.AsgriV2.utility.model.SoilTest;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import org.json.JSONObject;

import java.util.Random;

public class SoilTestResultActivity extends BaseActivity {

    private TextView requestHashTv, dateTv, landSoilTv, labTv;
    private RecyclerView recyclerView;
    private SoilTestResultAdapter adapter;

    private SoilTest testRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_test_result);

        setToolbarTitle("Soil Test Result", true);

        requestHashTv = findViewById(R.id.requestHash);
        landSoilTv = findViewById(R.id.land);
        dateTv = findViewById(R.id.date);
        labTv = findViewById(R.id.lab);

        recyclerView = findViewById(R.id.recyclerView);

        testRequest = getFromIntent("soil_test_request", SoilTest.class);

        if (testRequest == null){
            finish();
            return;
        }

        adapter = new SoilTestResultAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        requestHashTv.setText(testRequest.getTicketNumber());
        landSoilTv.setText(testRequest.getLandName() + " / " + testRequest.getSoiltype());
        dateTv.setText(DateUtils.splitDate(testRequest.getCreatedAt()));
        labTv.setText(testRequest.getLabName());

        //loadSoilTestResult();
        updateUI();
    }

    private void setUpGauge(){
        HalfGauge guage = findViewById(R.id.guage);

        Range startRange = new Range();
        startRange.setColor(Color.parseColor("#CE0000"));
        startRange.setFrom(0);
        startRange.setFrom(35);

        Range midRange = new Range();
        midRange.setColor(Color.parseColor("#E3E500"));
        midRange.setFrom(36);
        midRange.setFrom(70);

        Range endRange = new Range();
        endRange.setColor(Color.parseColor("#00b20b"));
        endRange.setFrom(71);
        endRange.setFrom(100);

        guage.setMinValue(0);
        guage.setMaxValue(100);

        guage.addRange(startRange);
        guage.addRange(midRange);
        guage.addRange(endRange);

        guage.setValue((int) (new Random().nextFloat() * 100));
    }

    private void loadSoilTestResult(){
        showLoading();
        AndroidNetworking.post(Webservice.getSoilTestResult)
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                //.addUrlEncodeFormBodyParameter("result_id", testRequest.getResultId())
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        if (UserHelper.checkResponse(SoilTestResultActivity.this, response)){
                            return;
                        }
                        try {
                            if (response.getBoolean("status")){
                                /*testResult = GsonUtils.getGson()
                                        .fromJson(response.getJSONArray("data")
                                                .getJSONObject(0).toString(), SoilTestResult.class);*/
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            updateUI();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        //testResult = null;
                        Toast.makeText(SoilTestResultActivity.this,
                                "Test Result Loading Error!",
                                Toast.LENGTH_SHORT).show();
                        updateUI();
                    }
                }
        );
    }

    private void updateUI(){
        /*findViewById(R.id.noData)
                .setVisibility(testResult == null ? View.VISIBLE : View.INVISIBLE);*/
        //if (testResult != null){
            adapter.update(testRequest);
        //}
        setUpGauge();
    }
}