package com.ascentya.AsgriV2.farmx.my_lands;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Realtime_FromAscentya;
import com.ascentya.AsgriV2.Models.Crop_Realfield;
import com.ascentya.AsgriV2.Models.MylandMaster_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MyLand_Slaves extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView mylands_recycler;
    TextView nodata;
    List<MylandMaster_Model> Data;
    ViewDialog viewDialog;
    SessionManager sm;
    ImageView goback;
    Realtime_FromAscentya masterAdapter;
    NoDefaultSpinner crop;
    List<Crop_Realfield> cropd_Data = new ArrayList<>();
    String land_id;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_land__slaves);
        viewDialog = new ViewDialog(MyLand_Slaves.this);
        sm = new SessionManager(this);
        goback = findViewById(R.id.goback);
        crop = findViewById(R.id.crop);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        land_id = getIntent().getStringExtra("land_id");

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mylands_recycler = findViewById(R.id.mymaster_recycler);
        nodata = findViewById(R.id.nodata);


        mylands_recycler.setLayoutManager(new LinearLayoutManager(this));
        mylands_recycler.hasFixedSize();

        get_crops();
    }

    public void get_crops() {
        cropd_Data = new ArrayList<>();

        AndroidNetworking.post(Webservice.landzonecrop_get)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            System.out.println("Crop Real Field: " + jsonArray.getJSONObject(i));

                            Crop_Realfield obj = new Crop_Realfield();
                            obj.setId(jsonArray.getJSONObject(i).optString("crop_id"));
                            obj.setName(Webservice.Data_crops.get(searchname(Integer.parseInt(jsonArray.getJSONObject(i).optString("crop_id")))).getName());
                            cropd_Data.add(obj);
                        }

                        ArrayAdapter<String> soiltype_adpter =
                                new ArrayAdapter(MyLand_Slaves.this, R.layout.spinner_item, cropd_Data);

                        crop.setAdapter(soiltype_adpter);

                        crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                              updateData(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {


                            }
                        });

                        crop.setSelection(0);

                    } else {

                    }
                  /*  Data = new ArrayList<>();

                    MylandMaster_Model obj = new MylandMaster_Model();
                    obj.setMastername("Node 1");
                    obj.setMaster_status("Success");
                    Data.add(obj);

                    masterAdapter = new Realtime_FromAscentya(MyLand_Slaves.this, Data, false, viewDialog, getIntent().getStringExtra("pos"), cropd_Data.get(0).getId());
                    mylands_recycler.setAdapter(masterAdapter);*/

                } catch (Exception e) {

                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onError(ANError anError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void loadData(){

        AndroidNetworking.post(Webservice.valuesfrommaster)
                .addUrlEncodeFormBodyParameter("divice_name", "Master: 1")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }

    private void updateData(int position){
        Data = new ArrayList<>();

        MylandMaster_Model obj = new MylandMaster_Model();
        obj.setMastername("Node 1");
        obj.setMaster_status("Success");
        Data.add(obj);

                                /*MylandMaster_Model obj1 = new MylandMaster_Model();
                                obj1.setMastername("Node 2");
                                obj1.setMaster_status("Success");
                                Data.add(obj1);*/

        masterAdapter = new Realtime_FromAscentya(MyLand_Slaves.this,
                Data, false, viewDialog, getIntent().getStringExtra("pos"),
                cropd_Data.get(position).getId());

        mylands_recycler.setAdapter(masterAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        updateData(crop.getSelectedItemPosition());
    }

    private Integer searchname(Integer data) {
        Integer pos = 0;
        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            Integer unitString = Integer.parseInt(Webservice.Data_crops.get(i).getCrop_id());
            if (unitString.equals(data)) {
                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }
}