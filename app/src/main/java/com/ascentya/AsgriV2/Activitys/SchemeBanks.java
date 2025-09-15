package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Scheme_Bank;
import com.ascentya.AsgriV2.Models.Banks_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SchemeBanks extends BaseActivity {
    RecyclerView scheme_recycler;
    Scheme_Bank scheme_adapter;
    List<Banks_Model> Data;
    ImageView goback;
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_banks);

        scheme_recycler = findViewById(R.id.scheme_recycler);
        goback = findViewById(R.id.goback);
        viewDialog = new ViewDialog(this);

        scheme_recycler.setLayoutManager(new LinearLayoutManager(this));
        scheme_recycler.setHasFixedSize(true);
        Data = new ArrayList<>();

        goback.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });


        add_zone(getIntent().getStringExtra("scheme_id"));

    }

    public void add_zone(String uniq) {
        viewDialog.showDialog();

        //Toasty.normal(this, "Loan Id: "+uniq).show();

       // AndroidNetworking.post(Webservice.schemes)
        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Agripedia/schemes")
                .addUrlEncodeFormBodyParameter("loan_id", uniq)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(SchemeBanks.this, jsonObject)){
                    return;
                }

                Log.i("Schemes", jsonObject.toString());
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Banks_Model obj = new Banks_Model();
                            obj.setScheme(jsonArray.getJSONObject(i).optString("schemes"));
                            obj.setBank_name(jsonArray.getJSONObject(i).optString("banks"));
                            obj.setInterest_rate(jsonArray.getJSONObject(i).optString("interest"));
                            obj.setEligibility(jsonArray.getJSONObject(i).optString("eligibility"));
                            obj.setShort_eligible(jsonArray.getJSONObject(i).optString("eligibility_short"));
                            obj.setShort_amount(jsonArray.getJSONObject(i).optString("amount_short"));
                            obj.setShort_interest(jsonArray.getJSONObject(i).optString("interest_short"));
                            obj.setOthers(jsonArray.getJSONObject(i).optString("other"));
                            obj.setAmount(jsonArray.getJSONObject(i).optString("amount"));

                            Data.add(obj);
                        }
                    } else {

                    }
                    scheme_adapter = new Scheme_Bank(SchemeBanks.this, Data);
                    scheme_recycler.setAdapter(scheme_adapter);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();


            }
        });


    }
}