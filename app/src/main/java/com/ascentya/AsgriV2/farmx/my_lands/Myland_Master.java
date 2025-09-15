package com.ascentya.AsgriV2.farmx.my_lands;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Master_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.UpdateMaster;
import com.ascentya.AsgriV2.Models.MylandMaster_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Add_Master;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Myland_Master extends AppCompatActivity {
    RecyclerView mylands_recycler;
    TextView nodata;
    List<MylandMaster_Model> Data;
    ViewDialog viewDialog;
    SessionManager sm;
    Master_Adapter masterAdapter;
    ImageView goback, addmaster;
    String land_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myland__master);
        viewDialog = new ViewDialog(Myland_Master.this);
        sm = new SessionManager(this);
        land_id = getIntent().getStringExtra("land");


        mylands_recycler = findViewById(R.id.mymaster_recycler);
        nodata = findViewById(R.id.nodata);
        goback = findViewById(R.id.goback);
        addmaster = findViewById(R.id.addmaster);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addmaster.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Add_Master obj = new Add_Master();
                obj.dialog(Myland_Master.this, "Add Master", land_id, sm.getUser().getId(), viewDialog, new UpdateMaster() {
                    @Override
                    public void updatemaster() {
                        add_master();
                    }
                });

            }
        });


        mylands_recycler.setLayoutManager(new LinearLayoutManager(this));
        mylands_recycler.hasFixedSize();


        add_master();
    }

    public void add_master() {
        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.mastersfromid)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        Data = new ArrayList<>();


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            System.out.println("Master: " + i + " " + jsonArray.getJSONObject(i));

                            MylandMaster_Model obj = new MylandMaster_Model();
                            obj.setMastername(jsonArray.getJSONObject(i).optString("device_name"));
                            obj.setMasterid(jsonArray.getJSONObject(i).optString("id"));
                            obj.setMaster_status(jsonArray.getJSONObject(i).optString("Success"));

                            Data.add(obj);
                        }

                    } else {
                        Toasty.error(Myland_Master.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }

                    masterAdapter = new Master_Adapter(Myland_Master.this, Data, true, land_id);
                    mylands_recycler.setAdapter(masterAdapter);
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