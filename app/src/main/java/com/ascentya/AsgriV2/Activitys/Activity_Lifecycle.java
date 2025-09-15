package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.ViewPagerAdapter;
import com.ascentya.AsgriV2.Models.ActivityCycle_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class Activity_Lifecycle extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    ViewDialog viewDialog;
    List<ActivityCycle_Model> Data_crop;
    ViewPagerAdapter adapter;
    TextView empty;
    Boolean sowing, nursery, transplanting;

    public static final String SOWING = "Sowing";
    public static final String FIELD = "Field";
    public static final String IRRIGATION = "Irrigation";
    public static final String CULTIVATION = "Cultivation";
    public static final String HARVESTING = "Harvesting";
    public static final String YIELD = "Yield";
    public static final String NURSERY = "Nursery";
    public static final String TRANSPLANTING = "Transplanting";
    public static final String MANURES = "Manures";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__lifecycle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        empty = (TextView) findViewById(R.id.empty);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Crop Cycle");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        viewDialog = new ViewDialog(Activity_Lifecycle.this);


        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).setCustomView(R.layout.customselected_tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (getIntent().getStringExtra("crop") != null) {
            getlifecycle(getIntent().getStringExtra("crop"));
        }

    }


    private void setupTabIcons() {

        Integer i = 0;

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(SOWING);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.top_drawable, 0, 0);
        tabLayout.getTabAt(i).setCustomView(tabOne);
        i++;

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(FIELD);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_field, 0, 0);
        tabLayout.getTabAt(i).setCustomView(tabTwo);
        i++;

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(IRRIGATION);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_irrigation, 0, 0);
        tabLayout.getTabAt(i).setCustomView(tabThree);
        i++;

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText(CULTIVATION);
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_cultivation, 0, 0);
        tabLayout.getTabAt(i).setCustomView(tabfour);
        i++;

        TextView tabfive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfive.setText(HARVESTING);
        tabfive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_harvesting, 0, 0);
        tabLayout.getTabAt(i).setCustomView(tabfive);
        i++;

        TextView tabsix = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabsix.setText(YIELD);
        tabsix.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_yield, 0, 0);
        tabLayout.getTabAt(i).setCustomView(tabsix);
        i++;

        if (nursery) {
            TextView tabseven = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabseven.setText(NURSERY);
            tabseven.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_nursery, 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabseven);
            i++;
        }

        if (transplanting) {
            TextView tabeight = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabeight.setText(TRANSPLANTING);

            tabeight.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_transplanting, 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabeight);
            i++;
        }


        TextView tabnine = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabnine.setText(MANURES);
        tabnine.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.drawable_manure, 0, 0);
        tabLayout.getTabAt(i).setCustomView(tabnine);
        i++;

        if (false) {
            TextView tabten = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabten.setText("Direct Sowing");
            tabten.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.top_drawable, 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabten);
            i++;
        }


        tabLayout.setSelectedTabIndicatorHeight((int) (3.5 * getResources().getDisplayMetrics().density));

    }


    public void getlifecycle(String id) {
        viewDialog.showDialog();
        Data_crop = new ArrayList<>();

        Log.i("Crop", (Webservice.get_cropscycle+id).replace("http", ""));

        AndroidNetworking.get(Webservice.get_cropscycle + id)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                empty.setVisibility(View.GONE);

                viewDialog.hideDialog();
                if (UserHelper.checkResponse(Activity_Lifecycle.this, jsonObject)){
                    return;
                }
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONObject jsonArray = jsonObject.getJSONArray("data").getJSONObject(0);
                        ActivityCycle_Model sowingModel = new ActivityCycle_Model();
                        sowingModel.setTitle(SOWING);

                        sowingModel.setDisc(jsonArray.optString("Sowing_Information"));
                        sowingModel.setPic(jsonArray.optString("Sowing_Information_image"));
                        Data_crop.add(sowingModel);


//                        if (!jsonArray.getJSONArray("Direct_Sowing").get(0).equals("")) {
//                            sowing = true;
//                            obj = new ActivityCycle_Model();
//                            obj.setTitle("Direct Sowing");
//                            obj.setDisc(jsonArray.optString("Direct_Sowing"));
//
//                            Data_crop.add(obj);
//                        } else {
//                            sowing = false;
//                        }

                        ActivityCycle_Model fieldModel = new ActivityCycle_Model();
                        fieldModel.setTitle(FIELD);

                        fieldModel.setDisc(jsonArray.optString("Field_preparation"));
                        fieldModel.setPic(jsonArray.optString("Field_preparation_image"));
                        Data_crop.add(fieldModel);

                        if (!jsonArray.getJSONArray("Nursery").get(0).equals("")) {
                            ActivityCycle_Model nurseryModel = new ActivityCycle_Model();
                            nurseryModel.setTitle(NURSERY);
                            nurseryModel.setDisc(jsonArray.optString("Nursery"));
                            nurseryModel.setPic(jsonArray.optString("Nursery_image"));
                            Data_crop.add(nurseryModel);
                            nursery = true;
                        } else {
                            nursery = false;
                        }


                        if (!jsonArray.getJSONArray("Transplanting").get(0).equals("")) {
                            ActivityCycle_Model transplantingModel = new ActivityCycle_Model();
                            transplantingModel.setTitle(TRANSPLANTING);
                            transplantingModel.setDisc(jsonArray.optString("Transplanting"));
                            transplantingModel.setPic(jsonArray.optString("Field_preparation_image"));
                            Data_crop.add(transplantingModel);
                            transplanting = true;
                        } else {
                            transplanting = false;
                        }

                        ActivityCycle_Model irrigationModel = new ActivityCycle_Model();
                        irrigationModel.setTitle(IRRIGATION);
                        irrigationModel.setDisc(jsonArray.optString("Irrigation"));
                        irrigationModel.setPic(jsonArray.optString("Irrigation_image"));
                        Data_crop.add(irrigationModel);


                        ActivityCycle_Model cultivationModel = new ActivityCycle_Model();
                        cultivationModel.setTitle(CULTIVATION);
                        cultivationModel.setDisc(jsonArray.optString("After_cultivation"));
                        cultivationModel.setPic(jsonArray.optString("After_cultivation_image"));
                        Data_crop.add(cultivationModel);


                        ActivityCycle_Model harvestingModel = new ActivityCycle_Model();
                        harvestingModel.setTitle(HARVESTING);
                        harvestingModel.setDisc(jsonArray.optString("Harvesting"));
                        harvestingModel.setPic(jsonArray.optString("Harvesting_image"));
                        Data_crop.add(harvestingModel);


                        ActivityCycle_Model yieldModel = new ActivityCycle_Model();
                        yieldModel.setTitle(YIELD);
                        yieldModel.setDisc(jsonArray.optString("Yield"));
                        yieldModel.setPic(jsonArray.optString("Yield_image"));
                        Data_crop.add(yieldModel);

                        ActivityCycle_Model manuresModel = new ActivityCycle_Model();
                        manuresModel.setTitle(MANURES);
                        manuresModel.setDisc(jsonArray.optString("Application_manures"));
                        manuresModel.setPic(jsonArray.optString("Application_manures_image"));
                        Data_crop.add(manuresModel);


                    } else {

                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

                adapter = new ViewPagerAdapter(getSupportFragmentManager(), new ViewPagerAdapter.Action() {
                    @Override
                    public String getTabTitle(int position) {
                        TabLayout.Tab tab = tabLayout.getTabAt(position);
                        if (tab != null) {
                            TextView textView = ((TextView) tab.getCustomView());
                            if(textView != null){
                                System.out.println("Tab Name: " + textView.getText());
                                return textView.getText().toString();
                            }
                        }
                        return position == 0 ? SOWING : FIELD;
                    }
                }, Data_crop);

                tabLayout.setupWithViewPager(viewPager);

                System.out.println("Tab Count: "+tabLayout.getTabCount());

                viewPager.setAdapter(adapter);

                System.out.println("Tab Count: "+tabLayout.getTabCount());

                setupTabIcons();

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                empty.setVisibility(View.VISIBLE);
            }
        });
    }

}