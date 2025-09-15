package com.ascentya.AsgriV2.Activitys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.SellCat_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.farmx.my_lands.My_Lands;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Farmx_Myfarm extends BaseActivity {
    LinearLayout mycrops_layout, mycattle_layout, myfinance_layout, pestcontrol_layout;
    ImageView goback;
    ViewDialog v_dialog;
    Dialog dialog;
    TextView title, cropname;
    EditText product_title, product_disc, product_cost;
    NoDefaultSpinner product_category;
    CircleImageView attachment;
    List<String> cat_data;
    List<SellCat_Model> sellcat_data;
    String cat_id;
    String attachment_path;

    @Override
    protected void onResume() {
        super.onResume();

        if (Webservice.crops.size() > 0) {

        } else {
            if (NetworkDetector.isNetworkStatusAvialable(Farmx_Myfarm.this)) {

                getcrops();
            } else {
                Toast.makeText(Farmx_Myfarm.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getcat_buysell() {

        sellcat_data = new ArrayList<>();
        cat_data = new ArrayList<>();

        v_dialog.showDialog();

        AndroidNetworking.get(Webservice.getbuysell_cat)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                v_dialog.hideDialog();

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            SellCat_Model obj = new SellCat_Model();
                            obj.setCat_id(jsonArray.getJSONObject(i).optString("cat_id"));
                            obj.setCat_name(jsonArray.getJSONObject(i).optString("category_name"));

                            sellcat_data.add(obj);
                            cat_data.add(jsonArray.getJSONObject(i).optString("category_name"));
                        }
                    } else {

                    }


                } catch (Exception e) {
                    v_dialog.hideDialog();
                    e.printStackTrace();
                }

//                add_mypost();

            }

            @Override
            public void onError(ANError anError) {

                v_dialog.hideDialog();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx__myfarm);

        mycrops_layout = findViewById(R.id.mycrops_layout);
        v_dialog = new ViewDialog(this);
        mycattle_layout = findViewById(R.id.mycattle_layout);
        myfinance_layout = findViewById(R.id.myfinance_layout);
        pestcontrol_layout = findViewById(R.id.pestcontrol_layout);
        goback = findViewById(R.id.goback);
        getcat_buysell();
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mycrops_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkDetector.isNetworkStatusAvialable(Farmx_Myfarm.this)) {
                    if (sessionManager.isGuest()){
                        showRegisterDialog();
                    }else {
                        Intent i = new Intent(Farmx_Myfarm.this, Farmx_Mycro.class);
                        startActivity(i);
                    }
                } else {


                    Toast.makeText(Farmx_Myfarm.this, "Please check your network connection", Toast.LENGTH_SHORT).show();

                }


            }
        });

        pestcontrol_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isGuest()){
                    showRegisterDialog();
                }else {
                    Intent i = new Intent(Farmx_Myfarm.this, FarmX_MyStockActivity.class);
                    startActivity(i);
                }
            }
        });


        myfinance_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkDetector.isNetworkStatusAvialable(Farmx_Myfarm.this)) {
                    if (sessionManager.isGuest()){
                        showRegisterDialog();
                    }else {
                        Intent i = new Intent(Farmx_Myfarm.this, My_Lands.class);
                        startActivity(i);
                    }
                } else {
                    Toast.makeText(Farmx_Myfarm.this, "Please check your network connection", Toast.LENGTH_SHORT).show();

                }


//                Toast.makeText(Farmx_Myfarm.this, R.string.update, Toast.LENGTH_SHORT).show();
            }
        });

        mycattle_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isGuest()){
                    showRegisterDialog();
                }else {
                    Intent i = new Intent(Farmx_Myfarm.this, Farmx_Cattle.class);
                    startActivity(i);
                }
            }
        });

    }



    public void getcrops() {
        v_dialog.showDialog();
        AndroidNetworking.get(Webservice.getname_icon)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                v_dialog.hideDialog();
                Webservice.Data_crops.clear();
                Webservice.crops.clear();

                try {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Crops_Main obj = new Crops_Main();
                        JSONObject jobj = jsonArray.getJSONObject(i);
                        obj.setName(jobj.optString("crop_name").trim());
                        obj.setIcon("https://vrjaitraders.com/ard_farmx/" + jobj.optString("crop_icons_images").trim());
                        obj.setCrop_id(jobj.optString("Basic_info_id").trim());
                        obj.setS_name(jobj.optString("scientific_name").trim());
                        obj.setTempreture(jobj.optString("temperature").trim());
                        obj.setPollution("40-50");
                        obj.setHumidity(jobj.optString("humidity").trim());
                        obj.setMoisture(jobj.optString("soil_moisture").trim());
                        obj.setWaterph(jobj.optString("soil_ph").trim());
                        obj.setVarieties(GsonUtils.fromJson(jobj.getJSONArray("varieties").toString(),
                                EMarketStorage.varietyListType));
                        Webservice.crops.add(jobj.optString("crop_name").trim());
                        Webservice.Data_crops.add(obj);
                    }


                } catch (Exception e) {
                    v_dialog.hideDialog();
                    e.printStackTrace();

                }


            }

            @Override
            public void onError(ANError anError) {
                v_dialog.hideDialog();

            }
        });
    }

}