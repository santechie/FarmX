package com.ascentya.AsgriV2.farmx.my_lands;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Farmx_Addland_New;
import com.ascentya.AsgriV2.Adapters.MyLands_Adapter;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class My_Lands extends BaseActivity {
    RecyclerView mylands_recycler;
    TextView nodata;
    List<Maincrops_Model> Data;
    ViewDialog viewDialog;
    SessionManager sm;
    MyLands_Adapter myLands_adapter;
    ImageView goback;
    ImageView add_land;

    @Override
    protected void onResume() {
        super.onResume();
        getmembers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__lands);

        viewDialog = new ViewDialog(My_Lands.this);
        sm = new SessionManager(this);

        mylands_recycler = findViewById(R.id.mylands_recycler);

        nodata = findViewById(R.id.nodata);
        add_land = findViewById(R.id.add_land);

        add_land.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                if (sessionManager.isPaid()) {
                    Intent i = new Intent(My_Lands.this, Farmx_Addland_New.class);
                    startActivity(i);
                }else {
                    showPayDialog();
                }
            }
        });
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mylands_recycler.setLayoutManager(new LinearLayoutManager(this));
        mylands_recycler.hasFixedSize();

    }

    public void getmembers() {

        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get("https://vrjaitraders.com/ard_farmx/api/Agripedia/farmxcrops/" + sm.getUser().getId()).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("fasfdsfds"+jsonObject);
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        System.out.println("fasfdsfds"+jsonArray);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Maincrops_Model obj = new Maincrops_Model();
                            System.out.println("fasfdsfds"+jsonArray.getJSONObject(i).optString("land_name"));
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

                            JSONArray mainCropArray = new JSONArray(jsonArray.getJSONObject(i).optString("maincrop"));
                            String interCrop = jsonArray.optJSONObject(i).optString("intercrop");
                            JSONArray interCropArray = new JSONArray();
                            if (interCrop != null && !interCrop.equals(""))
                                interCropArray = new JSONArray(interCrop);


                            System.out.println("Main Crop Array: " + mainCropArray);
                            System.out.println("Inter Crop Array: " + interCropArray);

                            if (mainCropArray != null && mainCropArray.length() != 0){
                                //for (int m=0; m<mainCropArray.length(); m++){
                                    JSONObject mainCropObj = mainCropArray.getJSONObject(0);
                                    Maincrops_Model mainCropModel = obj.Clone();
                                    mainCropModel.setMaincrop(mainCropObj.optString("crop_id"));
                                    System.out.println("Main Crop: " + mainCropModel.getMaincrop());
                                    Data.add(mainCropModel);
                                //}
                            }

                            /*if (interCropArray != null){
                                for (int m=0; m<interCropArray.length(); m++){
                                    JSONObject interCropObj = interCropArray.getJSONObject(m);
                                    Maincrops_Model interCropModel = obj.Clone();
                                    interCropModel.setIntercrop(interCropObj.optString("crop_id"));
                                    System.out.println("Inter Crop: " + interCropModel.getIntercrop());
                                    Data.add(interCropModel);
                                }
                            }*/

                            //Data.add(obj);

                        }

                    } else {

                    }


                    if (Data.size() > 0) {

//                        if (!getModuleManager().canInsert(ModuleManager.Components.MyFarm.LAND)){
//                            add_land.setVisibility(View.GONE);
//                        }
                        mylands_recycler.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                        myLands_adapter = new MyLands_Adapter(My_Lands.this, Data, true,
                                sm.getUser().getId(), viewDialog);
                        mylands_recycler.setAdapter(myLands_adapter);

                    } else {
                        mylands_recycler.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }

                    viewDialog.hideDialog();
                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

                viewDialog.hideDialog();
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();

            }
        });
    }
}