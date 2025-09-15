package com.ascentya.AsgriV2.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Maincrops_adapter;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.login_activities.FarmX_Warehouse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Farmx_Mycro extends BaseActivity {
    LinearLayout warehouse_layout, activity_layout;
    ImageView goback;
    Maincrops_adapter maincropsAdapter;
    List<Maincrops_Model> Data = new ArrayList<>(), intercrop_Data = new ArrayList<>();
    ViewDialog viewDialog;
    RecyclerView maincrop_recycler, intercrop_recycler;
    TextView nodata_main, nodata_inter, place;

    @Override
    protected void onResume() {
        super.onResume();
        getmembers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_mycro);

        viewDialog = new ViewDialog(Farmx_Mycro.this);
        activity_layout = findViewById(R.id.activity_layout);
        maincrop_recycler = findViewById(R.id.maincrop_recycler);
        intercrop_recycler = findViewById(R.id.intercrop_recycler);
        nodata_main = findViewById(R.id.nodata_main);
        nodata_inter = findViewById(R.id.nodata_inter);
        place = findViewById(R.id.place);

        nodata_inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addLand();
            }
        });

        nodata_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              addLand();
            }
        });
        place.setText(sessionManager.getUser().getFirstname());


        maincrop_recycler.setLayoutManager(new LinearLayoutManager(Farmx_Mycro.this));
        maincrop_recycler.setHasFixedSize(true);

        intercrop_recycler.setLayoutManager(new LinearLayoutManager(Farmx_Mycro.this));
        intercrop_recycler.setHasFixedSize(true);

        warehouse_layout = findViewById(R.id.warehouse_layout);
        goback = findViewById(R.id.goback);
//        getmembers();


//        activity_connection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Farmx_Mycro.this, Farmx_Activity.class);
////                startActivity(i);
//
//            }
//        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        activity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Farmx_Mycro.this, Farmx_Activity.class);
//                startActivity(i);

                Intent i = new Intent(Farmx_Mycro.this, Farmx_Addland.class);
                startActivity(i);

            }
        });

//        postharvest_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent i = new Intent(Farmx_Mycro.this, PostHarvest_Activity.class);
////                startActivity(i);
//            }
//        });

        warehouse_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Farmx_Mycro.this, FarmX_Warehouse.class);
                startActivity(i);
            }
        });


    }

    private void addLand(){
        if (sessionManager.isPaid()) {
            Intent i = new Intent(Farmx_Mycro.this, Farmx_Addland.class);
            startActivity(i);
        }else {
            showPayDialog();
        }
    }

    public void getmembers() {
        viewDialog.showDialog();
        Data.clear();
        intercrop_Data.clear();
        AndroidNetworking.get(Webservice.add_farmxcrops + sessionManager.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                System.out.println("Response: \n" + jsonObject);

                viewDialog.hideDialog();

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Maincrops_Model obj = new Maincrops_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("id"));
                            obj.setLand_name(jsonArray.getJSONObject(i).optString("land_name"));
                            obj.setDistrict(jsonArray.getJSONObject(i).optString("district"));
                            obj.setTaluk(jsonArray.getJSONObject(i).optString("taluk"));
                            obj.setAcre_count(jsonArray.getJSONObject(i).optString("acre_count"));
                            obj.setSoiltype(jsonArray.getJSONObject(i).optString("soiltype"));
                            obj.setMaincrop(jsonArray.getJSONObject(i).optString("maincrop"));
                            obj.setMainCropsString(jsonArray.getJSONObject(i).optString("maincrop"));
                            obj.setIntercrop(jsonArray.getJSONObject(i).optString("intercrop"));
                            obj.setInterCropsString(jsonArray.getJSONObject(i).optString("intercrop"));
                            obj.setAnual_revenue(jsonArray.getJSONObject(i).optString("anual_revenue"));
                            obj.setIrrigation_type(jsonArray.getJSONObject(i).optString("irrigation_type"));
                            obj.setGovt_scheme(jsonArray.getJSONObject(i).optString("govt_scheme"));
                            obj.setLive_stocks(jsonArray.getJSONObject(i).optString("live_stocks"));
                            obj.setSoilhealth_card(jsonArray.getJSONObject(i).optString("soilhealth_card"));
                            obj.setOrganic_farmer(jsonArray.getJSONObject(i).optString("organic_farmer"));
                            obj.setExport(jsonArray.getJSONObject(i).optString("export"));
                            obj.setCropId(jsonArray.getJSONObject(i).optString("crop_id"));

                            JSONArray mainCropArray = new JSONArray(jsonArray.getJSONObject(i).optString("maincrop"));
                            JSONArray interCropArray = new JSONArray(jsonArray.getJSONObject(i).optString("intercrop"));

                            System.out.println("Main Crop Array: " + mainCropArray);
                            System.out.println("Inter Crop Array: " + interCropArray);

                            if (mainCropArray != null){
                                for (int m=0; m<mainCropArray.length(); m++){
                                    JSONObject mainCropObj = mainCropArray.getJSONObject(m);
                                    Maincrops_Model mainCropModel = obj.Clone();
                                    mainCropModel.setMaincrop(mainCropObj.optString("crop_id"));
                                    System.out.println("Main Crop: " + mainCropModel.getMaincrop());
                                    Data.add(mainCropModel);
                                }
                            }

                            if (interCropArray != null){
                                for (int m=0; m<interCropArray.length(); m++){
                                    JSONObject interCropObj = interCropArray.getJSONObject(m);
                                    Maincrops_Model interCropModel = obj.Clone();
                                    interCropModel.setIntercrop(interCropObj.optString("crop_id"));
                                    System.out.println("Inter Crop: " + interCropModel.getIntercrop());
                                    intercrop_Data.add(interCropModel);
                                }
                            }



                        }

                    }


                    if (Data.size() > 0) {
                        nodata_main.setVisibility(View.GONE);
                        nodata_inter.setVisibility(View.GONE);
                        maincropsAdapter = new Maincrops_adapter(Farmx_Mycro.this, Data, true);
                        maincrop_recycler.setAdapter(maincropsAdapter);


                       /* for (int i = 0; i < Data.size(); i++) {
                            if (!Data.get(i).getIntercrop().equalsIgnoreCase("")) {
                                intercrop_Data.add(Data.get(i));

                            }
                        }*/
                        if (!(intercrop_Data.size() > 0)) {
                            nodata_inter.setVisibility(View.VISIBLE);
                        }

                        maincropsAdapter = new Maincrops_adapter(Farmx_Mycro.this, intercrop_Data, false);

                        intercrop_recycler.setAdapter(maincropsAdapter);
                    } else {

                        nodata_main.setVisibility(View.VISIBLE);
                        nodata_inter.setVisibility(View.VISIBLE);
                    }


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