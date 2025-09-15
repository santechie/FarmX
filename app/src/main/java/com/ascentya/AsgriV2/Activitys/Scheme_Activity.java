package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Scheme_Adapter;
import com.ascentya.AsgriV2.Models.Scheme_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Scheme_Activity extends BaseActivity {
    RecyclerView scheme_recycler;
    Scheme_Adapter scheme_adapter;
    List<Scheme_Model> Data;
    ImageView goback;
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);
        viewDialog = new ViewDialog(this);

        scheme_recycler = findViewById(R.id.scheme_recycler);
        goback = findViewById(R.id.goback);
        scheme_recycler.setLayoutManager(new LinearLayoutManager(this));
        scheme_recycler.setHasFixedSize(true);

        goback.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });

        get_fpo();
    }

    public void get_fpo() {
        Data = new ArrayList<>();
        viewDialog.showDialog();

        AndroidNetworking.get(Webservice.schemes_cat)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(Scheme_Activity.this, jsonObject)){
                    return;
                }

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            Scheme_Model obj = new Scheme_Model();
                            obj.setScheme_id(jsonArray.getJSONObject(i).optString("scheme_id"));
                            obj.setScheme_name(jsonArray.getJSONObject(i).optString("scheme_title"));
                            obj.setScheme_icon(Webservice.website_path + jsonArray.getJSONObject(i).optString("scheme_icon"));
                            obj.setScheme_value(jsonArray.getJSONObject(i).optString("scheme_value"));

                            if (getModuleManager()
                                    .canView(Components
                                            .findComponent(obj.getScheme_value(),
                                                    Modules.SCHEME))){
                                Data.add(obj);
                            }
                        }
                    } else {

                    }

                    scheme_adapter = new Scheme_Adapter(Scheme_Activity.this, Data);
                    scheme_recycler.setAdapter(scheme_adapter);
                } catch (Exception e) {
                    viewDialog.hideDialog();

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