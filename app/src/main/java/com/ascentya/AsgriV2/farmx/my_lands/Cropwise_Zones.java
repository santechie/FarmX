package com.ascentya.AsgriV2.farmx.my_lands;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.CropwiseZoneExpandableAdapter;
import com.ascentya.AsgriV2.Models.Cat_Model;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import androidx.appcompat.app.AppCompatActivity;

public class Cropwise_Zones extends AppCompatActivity {
    ViewDialog viewDialog;
    LinkedHashMap<Integer, ArrayList<Zone_Model>> Data;
    LinkedHashMap<Integer, Cat_Model> cat_Data;


    SessionManager sm;

    //    private TabLayout tab;
//    private ViewPager viewPager;
    TextView empty;
    String land_id, userId, lcId;
    ImageView back;

    ExpandableListView listView;
    CropwiseZoneExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropwise_zones);
        sm = new SessionManager(this);
//        viewPager = findViewById(R.id.viewpager);
        back = findViewById(R.id.back);
        listView = findViewById(R.id.expandableListView);

        Data = new LinkedHashMap<>();
        cat_Data = new LinkedHashMap<>();
        land_id = getIntent().getStringExtra("land");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


//        tab = findViewById(R.id.tabs);
        viewDialog = new ViewDialog(this);
//        Data = new ArrayList<>();

//        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


//         getmembers();

        adapter = new CropwiseZoneExpandableAdapter();
        adapter.setLandId(Integer.parseInt(land_id));
        adapter.setUserId(Integer.parseInt(sm.getUser().getId()));
        listView.setAdapter(adapter);

        get_crops(land_id);

    }

//    public void getmembers() {
//
//
//
//        Cat_Model obj = new Cat_Model();
//        obj.setId("0");
//        obj.setName("My forum");
//        Data.add(obj);
//        obj = new Cat_Model();
//        obj.setId("01");
//        obj.setName("All");
//        Data.add(obj);
//
//
//        viewDialog.showDialog();
//        AndroidNetworking.get(Webservice.getcat)
//
//                .build().getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                viewDialog.hideDialog();
//
//                try {
//
//                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
//
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        tab.addTab(tab.newTab().setText("My forum"));
//                        tab.addTab(tab.newTab().setText("All"));
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            tab.addTab(tab.newTab().setText(jsonArray.getJSONObject(i).optString("category_title")));
//                            cat_Data.add(jsonArray.getJSONObject(i).optString("category_title"));
//                            Cat_Model obj = new Cat_Model();
//                            obj.setId(jsonArray.getJSONObject(i).optString("category_id"));
//                            obj.setName(jsonArray.getJSONObject(i).optString("category_title"));
//                            Data.add(obj);
//                        }
//                    } else {
//
//                    }
//
//
//                } catch (Exception e) {
//                    viewDialog.hideDialog();
//                    e.printStackTrace();
//                }
//
//                PlansPagerAdapter adapter = new PlansPagerAdapter
//                        (getSupportFragmentManager(), tab.getTabCount(), Data);
////                tab.setupWithViewPager(viewPager);
//                viewPager.setAdapter(adapter);
//
//                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
//
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//
//                viewDialog.hideDialog();
//            }
//        });
//    }


    public void get_crops(String land_id) {
//        tab.removeAllTabs();
        Data.clear();


//        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.landzonecrop_get)
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

//                            Data.add(Webservice.Data_crops.get(searchname(Integer.parseInt(jsonArray.getJSONObject(i).optString("crop_id")))).getName());
//                            tab.addTab(tab.newTab().setText(Webservice.Data_crops.get(searchname(Integer.parseInt(jsonArray.getJSONObject(i).optString("crop_id")))).getName()));

                            int nameId = searchname(Integer.parseInt(jsonArray.getJSONObject(i).optString("crop_id")));
                            int cropId = Integer.parseInt(jsonArray.getJSONObject(i).optString("crop_id"));
                            String name = Webservice.Data_crops.get(nameId).getName();

//                            cat_Data.add(Webservice.Data_crops.get(searchname(Integer.parseInt(jsonArray.getJSONObject(i).optString("crop_id")))).getName());
                            Cat_Model obj = new Cat_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("crop_id"));
                            obj.setName(Webservice.Data_crops.get(searchname(Integer.parseInt(jsonArray.getJSONObject(i).optString("crop_id")))).getName());

                            cat_Data.put(cropId, obj);

                            Log.i("CropId", cropId + "");
                            Log.i("Cat_Model", "Id: " + obj.getId() + " Name: " + obj.getName());

                            getlands(cropId,userId,lcId, land_id);

                        }

//                        test((int) cat_Data.keySet().toArray()[0], land_id);

                        setUpList();

                    } else {

                        Toast.makeText(Cropwise_Zones.this, "Crops Loading Status Failed", Toast.LENGTH_LONG).show();

                    }

//                    ZonePageAdapter adapter = new ZonePageAdapter
//                            (getSupportFragmentManager(), tab.getTabCount(), Data, land_id);
//                tab.setupWithViewPager(viewPager);
//                    viewPager.setAdapter(adapter);

//                    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                Toast.makeText(Cropwise_Zones.this, "Check your Network Connection!", Toast.LENGTH_LONG).show();
            }


        });


    }

   /* public void test(int cropId, String landId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, My_Zones.newInstance(cropId + "", land_id));
        transaction.commit();
    }*/

    public void getlands(int crop_id, String landcrop_id, String userId, String lcId ) {

        viewDialog.showDialog();

        Log.i("getLands Called.", "crop_id: " + crop_id + " / " + "land_id: " + landcrop_id);

        AndroidNetworking.post(Webservice.addlandlist)
                .addUrlEncodeFormBodyParameter("land_id", landcrop_id)
                .addUrlEncodeFormBodyParameter("user_id", userId)
                .addUrlEncodeFormBodyParameter("lc_id", lcId)
                .addUrlEncodeFormBodyParameter("crop_id", String.valueOf(crop_id))
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.i("getLands Data.", crop_id + " " + jsonObject.toString());

                Log.i("getLands Ended.", crop_id + "");

                viewDialog.hideDialog();
                if (UserHelper.checkResponse(Cropwise_Zones.this, jsonObject)){
                    return;
                }

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Zone_Model obj = new Zone_Model();
                            obj.setZone_id(jsonArray.getJSONObject(i).optString("zone_id"));
                            obj.setZone_name(jsonArray.getJSONObject(i).optString("zone_name"));
                            obj.setCrop_name(jsonArray.getJSONObject(i).optString("crop_id"));
                            obj.setCrop_icons_images(jsonArray.getJSONObject(i).optString("crop_icons_images"));
                            obj.setScientific_name(jsonArray.getJSONObject(i).optString("scientific_name"));
                            obj.setSoil_ph(jsonArray.getJSONObject(i).optString("soil_ph"));
                            obj.setTemperature(jsonArray.getJSONObject(i).optString("temperature"));
                            obj.setHumidity(jsonArray.getJSONObject(i).optString("humidity"));
                            obj.setSoil_moisture(jsonArray.getJSONObject(i).optString("soil_moisture"));
                            obj.setPollution(jsonArray.getJSONObject(i).optString("pollution"));
                            obj.setLight_visibility(jsonArray.getJSONObject(i).optString("light_visibility"));
                            if (!Data.containsKey(crop_id))
                                Data.put(crop_id, new ArrayList<>());
                            Data.get(crop_id).add(obj);
                        }
//                       masterAdapter = new Zone_Adapter(getActivity(), Data, true, landcrop_id, sm.getUser().getId());
//                        mylands_recycler.setAdapter(masterAdapter);

                       /* Log.i("Names:", cat_Data.size() + "");

                        for (int i = 0; i < Data.size(); i++) {
                            Log.i("Name: " + cat_Data.values().toArray()[i],
                                    "Size: " + Data.get(cat_Data.keySet().toArray()[i]).size());
                        }*/

                        setUpList();

                    } else {

                    }


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


    private void setUpList() {
        adapter.updateData(cat_Data, Data);
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