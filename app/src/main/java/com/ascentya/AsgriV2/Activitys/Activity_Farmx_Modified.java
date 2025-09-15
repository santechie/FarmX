package com.ascentya.AsgriV2.Activitys;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Activity_Adapter;
import com.ascentya.AsgriV2.Adapters.LandCropAdapter;
import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.Models.Activity_Model;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.LandCropModel;
import com.ascentya.AsgriV2.Models.LandDeviceData;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.CropUtils;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.my_land.activities.LandActivitiesActivity;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Activity_Farmx_Modified extends AppCompatActivity {
    Maincrops_Model landModel;
    String usercrop_id, crop_id, acre_count, yield_unit, crop_type, crop_icon;
    TextView crop_name, acre, profit_yield, current_date, name, temp_val, humidity_val, moisture_val, ph_val, sowing_date, harvest_date, yield;
    ImageView crop_image;
    SessionManager sm;
    ViewDialog viewDialog;
    Double yield_val = 0.0;
    RecyclerView soil_recycler, land_recycler, water_recycler, cultivation_recycler, postharvest_recycler, harvest_recycler;
    Button plough, land_prepare, water_analysis, cultivation, post_harvest;
    Activity_Adapter plough_adapter;

    List<Activity_Model> plough_Data;
    ViewDialog dialog_loader;
    Button observation;

    CardView cropCard;
    LinearLayout mainCropLay, interCropLay;

    List<Activity_Cat_Model> soil_data, land_data, water_data, cultivation_data, postharvest_data;
    TextView supervisor_name, supervisor_district, landName, acreCount;

    List<LandCropModel> mainCrops, interCrops;
    boolean isLand;

    private LandDeviceData landDeviceData;

    private LandCropAdapter mainCropAdapter, interCropAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        //update_land(sm.getUser().getId());
//        show_dialog(sm.getUser().getId(), usercrop_id);

        //int i = 1/0;
    }

    public static void open(Context context, Maincrops_Model land, String landId, String landName, boolean isLand, List<LandCropModel> mainCrops, List<LandCropModel> interCrops, String cropName, String acreCount, String cropType){
        Intent i = new Intent(context, Activity_Farmx_Modified.class);
        i.putExtra("land", GsonUtils.getGson().toJson(land));
        i.putExtra("is_land", isLand);
        i.putExtra("land_name", landName);
        i.putExtra("usercrop", landId);
        i.putExtra("main_crops", GsonUtils.getGson().toJson(mainCrops));
        i.putExtra("inter_crops", GsonUtils.getGson().toJson(interCrops));
        i.putExtra("acre", acreCount);
        i.putExtra("crop_id", cropName);
        i.putExtra("crop_type", cropType);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__farmx_modified);
        crop_name = findViewById(R.id.crop_name);
        supervisor_district = findViewById(R.id.supervisor_district);
        supervisor_name = findViewById(R.id.supervisor_name);

        cropCard = findViewById(R.id.cropCard);

        mainCropLay = findViewById(R.id.mainCropLay);
        interCropLay = findViewById(R.id.interCropLay);

        landName = findViewById(R.id.landName);
        acreCount = findViewById(R.id.acreCount);

        supervisor_district.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supervisor_district.getText().toString().trim()));
                startActivity(intent);
//                Toasty.normal(Activity_Farmx_Modified.this, "Call").show();
            }
        });

        observation = findViewById(R.id.observation);
        dialog_loader = new ViewDialog(this);
        land_prepare = findViewById(R.id.land_prepare);
        water_analysis = findViewById(R.id.water_analysis);
        cultivation = findViewById(R.id.cultivation);
        post_harvest = findViewById(R.id.post_harvest);
        soil_recycler = findViewById(R.id.soil_recycler);
        land_recycler = findViewById(R.id.land_recycler);
        water_recycler = findViewById(R.id.water_recycler);
        cultivation_recycler = findViewById(R.id.cultivation_recycler);
        postharvest_recycler = findViewById(R.id.postharvest_recycler);
        harvest_recycler = findViewById(R.id.harvest_recycler);
        yield = findViewById(R.id.yield);
        plough = findViewById(R.id.plough);
        plough_Data = new ArrayList<>();
        Activity_Model obj = new Activity_Model();
        obj.setType("Test");
        plough_Data.add(obj);


        soil_recycler.setLayoutManager(new LinearLayoutManager(this));
        soil_recycler.setHasFixedSize(true);
        soil_recycler.setNestedScrollingEnabled(false);
        land_recycler.setLayoutManager(new LinearLayoutManager(this));
        land_recycler.setHasFixedSize(true);
        land_recycler.setNestedScrollingEnabled(false);
        water_recycler.setLayoutManager(new LinearLayoutManager(this));
        water_recycler.setHasFixedSize(true);
        water_recycler.setNestedScrollingEnabled(false);

        cultivation_recycler.setLayoutManager(new LinearLayoutManager(this));
        cultivation_recycler.setHasFixedSize(true);
        cultivation_recycler.setNestedScrollingEnabled(false);

        postharvest_recycler.setLayoutManager(new LinearLayoutManager(this));
        postharvest_recycler.setHasFixedSize(true);
        postharvest_recycler.setNestedScrollingEnabled(false);
        plough.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent i = new Intent(Activity_Farmx_Modified.this, Add_HelperActivity.class);
                i.putExtra("main_crops", GsonUtils.getGson().toJson(mainCrops));
                i.putExtra("inter_crops", GsonUtils.getGson().toJson(interCrops));
                i.putExtra("is_land", isLand);
                i.putExtra("crop", crop_id);
                i.putExtra("land", usercrop_id);
                i.putExtra("cat", "1");
                i.putExtra("crop_type", getIntent().getStringExtra("crop_type"));
                startActivity(i);
            }
        });
        land_prepare.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent i = new Intent(Activity_Farmx_Modified.this, Add_HelperActivity.class);
                i.putExtra("main_crops", GsonUtils.getGson().toJson(mainCrops));
                i.putExtra("inter_crops", GsonUtils.getGson().toJson(interCrops));
                i.putExtra("is_land", isLand);
                i.putExtra("crop", crop_id);
                i.putExtra("land", usercrop_id);
                i.putExtra("cat", "2");
                i.putExtra("crop_type", getIntent().getStringExtra("crop_type"));
                startActivity(i);
            }
        });
        water_analysis.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent i = new Intent(Activity_Farmx_Modified.this, Add_HelperActivity.class);
                i.putExtra("main_crops", GsonUtils.getGson().toJson(mainCrops));
                i.putExtra("inter_crops", GsonUtils.getGson().toJson(interCrops));
                i.putExtra("is_land", isLand);
                i.putExtra("crop", crop_id);
                i.putExtra("land", usercrop_id);
                i.putExtra("cat", "3");
                i.putExtra("crop_type", getIntent().getStringExtra("crop_type"));
                startActivity(i);
            }
        });
        cultivation.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent i = new Intent(Activity_Farmx_Modified.this, Add_HelperActivity.class);
                i.putExtra("main_crops", GsonUtils.getGson().toJson(mainCrops));
                i.putExtra("inter_crops", GsonUtils.getGson().toJson(interCrops));
                i.putExtra("is_land", isLand);
                i.putExtra("crop", crop_id);
                i.putExtra("land", usercrop_id);
                i.putExtra("cat", "4");
                i.putExtra("crop_type", getIntent().getStringExtra("crop_type"));
                startActivity(i);
            }
        });
        post_harvest.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                Intent i = new Intent(Activity_Farmx_Modified.this, Add_HelperActivity.class);
                i.putExtra("main_crops", GsonUtils.getGson().toJson(mainCrops));
                i.putExtra("inter_crops", GsonUtils.getGson().toJson(interCrops));
                i.putExtra("is_land", isLand);
                i.putExtra("crop", crop_id);
                i.putExtra("land", usercrop_id);
                i.putExtra("cat", "5");
                i.putExtra("crop_type", getIntent().getStringExtra("crop_type"));
                startActivity(i);
            }
        });

        observation.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

            }
        });
        harvest_date = findViewById(R.id.harvest_date);
        profit_yield = findViewById(R.id.profit_yield);
        acre = findViewById(R.id.acre);
        temp_val = findViewById(R.id.temp);
        humidity_val = findViewById(R.id.humidity);
        moisture_val = findViewById(R.id.moisture);
        ph_val = findViewById(R.id.ph);
        viewDialog = new ViewDialog(this);
        name = findViewById(R.id.name);
        sm = new SessionManager(this);
        crop_image = findViewById(R.id.crop_image);
        current_date = findViewById(R.id.current_date);
        Date c = Calendar.getInstance().getTime();

        name.setText("Hello " + sm.getUser().getFirstname());

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        current_date.setText(formattedDate);

//        gethumidity();


        //temp_val.setText("24.01");
        //humidity_val.setText("43");
        if (getIntent() != null) {
            landModel = GsonUtils.getGson()
                    .fromJson(getIntent().getStringExtra("land"), Maincrops_Model.class);
            usercrop_id = getIntent().getStringExtra("usercrop");
            crop_id = getIntent().getStringExtra("crop_id");
            acre_count = getIntent().getStringExtra("acre");
            crop_type = getIntent().getStringExtra("crop_type");
            fetchCropList();
            isLand = getIntent().getBooleanExtra("is_land", true);
            acreCount.setText(acre_count + " ac");
            landName.setText(getIntent().getStringExtra("land_name"));
            acre.setText(acre_count + "ac");
            System.out.println("Crop Id: " + crop_id);
            if (crop_id != null){
                Crops_Main maincrops_model = Webservice.getCrop(crop_id);
                if (maincrops_model != null)
                    crop_name.setText(maincrops_model.getName());
            }
            else
                crop_name.setText("");

//            String[] temstr = Webservice.Data_crops.get(searchFor(crop_id)).getTempreture().split("-", 2);
//            String[] humstr = Webservice.Data_crops.get(searchFor(crop_id)).getHumidity().split("-", 2);


//            temp_val.setText(temstr[0].trim());
//            humidity_val.setText(humstr[0].trim());
//            crop_icon = Webservice.Data_crops.get(searchFor(crop_id)).getIcon();
            Glide.with(this).load(Webservice.Data_crops.get(searchFor(crop_id)).getIcon()).into(crop_image);

            //getexpecteddata();
            add_Supervisor();
            loadLandDeviceData();
            setUpCropList();
        }

        findViewById(R.id.activities).setOnClickListener(view ->
                LandActivitiesActivity.open(Activity_Farmx_Modified.this, landModel, isLand ? null : crop_id));
    }

    private void fetchCropList(){
        if (getIntent().hasExtra("main_crops")){
            mainCrops = GsonUtils.getGson().fromJson(
                    getIntent().getStringExtra("main_crops"), CropUtils.typeToken);
            System.out.println("Main Crops: " + mainCrops.size());
        }else {
            mainCrops = new ArrayList<>();
        }

        if (getIntent().hasExtra("inter_crops")){
            interCrops = GsonUtils.getGson().fromJson(
                    getIntent().getStringExtra("inter_crops"), CropUtils.typeToken);
            System.out.println("Inter Crops: " + interCrops.size());
        }else {
            interCrops = new ArrayList<>();
        }
    }

    private LandCropAdapter.Action mainAction = new LandCropAdapter.Action() {
        @Override
        public List<LandCropModel> getLandCrops() {
            return mainCrops;
        }

        @Override
        public void onClicked(int position) {

        }
    };

    private LandCropAdapter.Action interAction = new LandCropAdapter.Action() {
        @Override
        public List<LandCropModel> getLandCrops() {
            return interCrops;
        }

        @Override
        public void onClicked(int position) {

        }
    };

    private void setUpCropList(){
        if (isLand){

            mainCropAdapter = new LandCropAdapter(mainAction);
            interCropAdapter = new LandCropAdapter(interAction);

            ((RecyclerView) findViewById(R.id.mainCropRv)).setAdapter(mainCropAdapter);
            ((RecyclerView) findViewById(R.id.interCropRv)).setAdapter(interCropAdapter);

            ((RecyclerView) findViewById(R.id.mainCropRv)).setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            ((RecyclerView) findViewById(R.id.interCropRv)).setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            mainCropLay.setVisibility(View.VISIBLE);
            interCropLay.setVisibility(View.VISIBLE);
            cropCard.setVisibility(View.GONE);

            for (LandCropModel model: mainCrops){
                getexpecteddata(model);
            }
            for (LandCropModel model: interCrops){
                getexpecteddata(model);
            }
        }else {
            mainCropLay.setVisibility(View.GONE);
            interCropLay.setVisibility(View.GONE);
            cropCard.setVisibility(View.VISIBLE);
            getexpecteddata(null);
        }
    }

    private void loadLandDeviceData(){
        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.getLandDeviceData)
//                .addUrlEncodeFormBodyParameter("land_id", usercrop_id)
                // Todo Device Data
//                .addUrlEncodeFormBodyParameter("land_id", "51")
                .addUrlEncodeFormBodyParameter("land_id", usercrop_id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        viewDialog.hideDialog();
                        try {
                            if (response.optBoolean("status")){
                                landDeviceData = GsonUtils.getGson()
                                        .fromJson(response.optString("data"),
                                                LandDeviceData.class);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            updateLandDeviceData();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        viewDialog.hideDialog();
                        Toasty.error(Activity_Farmx_Modified.this, "Land Device Data Load Failed").show();
                    }
                });
    }

    private void updateLandDeviceData(){
        if (landDeviceData != null){
            temp_val.setText(checkValue(landDeviceData.getTemperature()));
            humidity_val.setText(checkValue(landDeviceData.getHumidity()));
            moisture_val.setText(checkValue(landDeviceData.getSoilMoisture()));
            ph_val.setText(checkValue(landDeviceData.getPh()));
        }else {
            temp_val.setText(checkValue(null));
            humidity_val.setText(checkValue(null));
            moisture_val.setText(checkValue(null));
            ph_val.setText(checkValue(null));
        }
    }

    private String checkValue(String value){
        return value == null ? "0" : value;
    }


//    public void getmembers() {
//
//        AndroidNetworking.get(Webservice.getcrop_byid + crop_id + "/" + usercrop_id)
//
//                .build().getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//
//                try {
//
//
//                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
//
////                        Userobject obj = new Userobject();
////                        obj.setId(jsonObject.getJSONObject("data").optString("id"));
////                        obj.setFirstname(jsonObject.getJSONObject("data").optString("username"));
////                        obj.setPhno(jsonObject.getJSONObject("data").optString("phone"));
////                        obj.setEmail(jsonObject.getJSONObject("data").optString("email"));
////                        obj.setIspremium(jsonObject.getJSONObject("data").optString("is_premium"));
////                        obj.setSearch_name("none");
////                        sm.setUser(obj);
////                        Intent i = new Intent(Payment_Activity.this, Mycrops_Main.class);
////                        i.putExtra("page", "crop");
////                        startActivity(i);
////                        finishAffinity();
//
////                        if (jsonObject.getJSONObject("data").optString("id"))
//
//                        ploughing_step = new ploughing("Ploughing", Farmx_Activity.this, crop_id, usercrop_id, jsonObject.getJSONObject("data").optString("ploughing_date"), jsonObject.getJSONObject("data").optString("ploughing_cost"), jsonObject.getJSONObject("data").optString("ploughing_enddate"), jsonObject.getJSONObject("data").optString("ploughing_by"));
//                        sowing_step = new sowing("Sowing", Farmx_Activity.this, crop_id, usercrop_id, jsonObject.getJSONObject("data").optString("sowing_date"), jsonObject.getJSONObject("data").optString("sowing_cost"), jsonObject.getJSONObject("data").optString("sowing_enddate"), jsonObject.getJSONObject("data").optString("sowing_by"));
//                        irrigation_step = new Irrigation("Irrigation", Farmx_Activity.this, crop_id, usercrop_id, jsonObject.getJSONObject("data").optString("irrigation_date"), jsonObject.getJSONObject("data").optString("irrigation_cost"), jsonObject.getJSONObject("data").optString("irrigation_enddate"));
//                        fertilizer_step = new Fertilizer("Fertilizer", Farmx_Activity.this, crop_id, usercrop_id, jsonObject.getJSONObject("data").optString("fertilizer_date"), jsonObject.getJSONObject("data").optString("fertilizer_cost"), jsonObject.getJSONObject("data").optString("fertilizer_enddate"));
//                        intercultural_step = new Intercultural_practices("Intercultural practices", Farmx_Activity.this, crop_id, usercrop_id, jsonObject.getJSONObject("data").optString("intercultural_date"), jsonObject.getJSONObject("data").optString("intercultual_cost"), jsonObject.getJSONObject("data").optString("intercultural_enddate"));
//                        harvest_step = new Harvest("Harvest", Farmx_Activity.this, crop_id, usercrop_id, jsonObject.getJSONObject("data").optString("harvest_date"), jsonObject.getJSONObject("data").optString("harvest_cost"), jsonObject.getJSONObject("data").optString("harvest_enddate"), jsonObject.getJSONObject("data").optString("yield"), jsonObject.getJSONObject("data").optString("commends"), yield_unit, jsonObject.getJSONObject("data").optString("harvest_by"));
//                        verticalStepperForm = findViewById(R.id.stepper_form);
//                        verticalStepperForm
//                                .setup(Farmx_Activity.this, ploughing_step, sowing_step, irrigation_step, fertilizer_step, intercultural_step, harvest_step)
//
//                                .includeConfirmationStep(false)
//                                .init();
//
//                        if (!jsonObject.getJSONObject("data").optString("ploughing_date").equalsIgnoreCase("")) {
//
//                            if (!jsonObject.getJSONObject("data").optString("sowing_date").equalsIgnoreCase("")) {
//                                if (!jsonObject.getJSONObject("data").optString("irrigation_date").equalsIgnoreCase("")) {
//                                    if (!jsonObject.getJSONObject("data").optString("fertilizer_date").equalsIgnoreCase("")) {
//                                        if (!jsonObject.getJSONObject("data").optString("intercultural_date").equalsIgnoreCase("")) {
//                                            if (!jsonObject.getJSONObject("data").optString("harvest_date").equalsIgnoreCase("")) {
//                                                if (yield_val != 0.0) {
//                                                    profit_yield.setText(String.valueOf(Double.parseDouble(jsonObject.getJSONObject("data").optString("yield")) - yield_val));
//                                                } else {
//                                                    profit_yield.setText("");
//                                                }
////                                                yield_val = );
//                                                final Handler handler = new Handler(Looper.getMainLooper());
//                                                handler.postDelayed(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        //Do something after 100ms
//                                                        verticalStepperForm.goToStep(5, true);
//                                                    }
//                                                }, 500);
//                                            } else {
//
//                                                final Handler handler = new Handler(Looper.getMainLooper());
//                                                handler.postDelayed(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        //Do something after 100ms
//                                                        verticalStepperForm.goToStep(5, true);
//                                                    }
//                                                }, 500);
//
//
//                                            }
//                                        } else {
//                                            final Handler handler = new Handler(Looper.getMainLooper());
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    //Do something after 100ms
//                                                    verticalStepperForm.goToStep(4, true);
//
//                                                }
//                                            }, 500);
//                                        }
//                                    } else {
//                                        final Handler handler = new Handler(Looper.getMainLooper());
//                                        handler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                verticalStepperForm.goToStep(3, true);
//                                                //Do something after 100ms
//                                            }
//                                        }, 500);
//
//                                    }
//                                } else {
//                                    final Handler handler = new Handler(Looper.getMainLooper());
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            //Do something after 100ms
//                                            verticalStepperForm.goToStep(2, true);
//
//                                        }
//                                    }, 500);
//                                }
//                            } else {
//                                final Handler handler = new Handler(Looper.getMainLooper());
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //Do something after 100ms
//                                        verticalStepperForm.goToStep(1, true);
//
//                                    }
//                                }, 500);
//                            }
//
//                        } else {
//                            final Handler handler = new Handler(Looper.getMainLooper());
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //Do something after 100ms
//                                    verticalStepperForm.goToStep(1, true);
//
//                                }
//                            }, 100);
//                        }
//
//
//                    } else {
//                        ploughing_step = new ploughing("Ploughing", Farmx_Activity.this, crop_id, usercrop_id, "", "", "", "Equipment");
//                        sowing_step = new sowing("Sowing", Farmx_Activity.this, crop_id, usercrop_id, "", "", "", "Equipment");
//                        irrigation_step = new Irrigation("Irrigation", Farmx_Activity.this, crop_id, usercrop_id, "", "", "");
//                        fertilizer_step = new Fertilizer("Fertilizer", Farmx_Activity.this, crop_id, usercrop_id, "", "", "");
//                        intercultural_step = new Intercultural_practices("Intercultural practices", Farmx_Activity.this, crop_id, usercrop_id, "", "", "");
//                        harvest_step = new Harvest("Harvest", Farmx_Activity.this, crop_id, usercrop_id, "", "", "", "", "", yield_unit, "Equipment");
//                        verticalStepperForm = findViewById(R.id.stepper_form);
//                        verticalStepperForm
//                                .setup(Farmx_Activity.this, ploughing_step, sowing_step, irrigation_step, fertilizer_step, intercultural_step, harvest_step)
//
//                                .includeConfirmationStep(false)
//                                .init();
//                    }
//
////                    getexpecteddata();
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//
//
//            }
//        });
//
//
//    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getCrop_id();


            if (unitString.equals(data.toLowerCase())) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }

    /*public void gettemt() {

        AndroidNetworking.get(Webservice.update_temperature)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    viewDialog.hideDialog();
                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String temp = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field1");


                    try {
                        Double temt_val = Double.valueOf(temp);
                        Range<Double> test = Range.between(0.00, 100.00);
                        if (test.contains(temt_val)) {
                            temp_val.setText(String.valueOf(temt_val));
                        } else {
                            temp_val.setText("0");
                        }
                    } catch (Exception e) {
                        temp_val.setText("0");
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


    }*/

    /*public void gethumidity() {
        viewDialog.showDialog();
//        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.update_humidity)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String hum_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field2");
                    if (hum_val.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 100);
                        if (test.contains(Integer.parseInt(hum_val))) {

                            humidity_val.setText(hum_val);
                        } else {
                            humidity_val.setText("0");
                        }
                    } else {
                        humidity_val.setText("0");
                    }


                    gettemt();

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
    }*/

    public void add_Supervisor() {
        AndroidNetworking.post(Webservice.supervisor_list)
                .addUrlEncodeFormBodyParameter("land_id", usercrop_id)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONObject jsonArray = jsonObject.getJSONObject("data");

                        supervisor_name.setText(jsonArray.getString("user_name"));
                        supervisor_district.setText(jsonArray.getString("user_phone"));
                    } else {

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {


            }
        });

    }

    public void getexpecteddata(LandCropModel model) {

        AndroidNetworking.get(Webservice.getexpected_bycropid + (model != null ? model.getCropId() : crop_id))

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        String yieldDuring = jsonObject.getJSONObject("data").optString("yield_during");
                        String yieldUnit = jsonObject.getJSONObject("data").optString("yield_unit");
                        String harvestDuring = jsonObject.getJSONObject("data").optString("harvesting_during");

                        if (model == null){

                            profit_yield.setText("");

                            yield_unit = jsonObject.getJSONObject("data").optString("yield_unit");
                            yield.setText(yieldDuring + " " + yieldUnit);
                            harvest_date.setText(harvestDuring);

                        }else {

                            model.setYieldDuring(yieldDuring);
                            model.setYieldUnit(yieldUnit);
                            model.setHarvestDuring(harvestDuring);

                            if(mainCrops.contains(model)){
                                mainCropAdapter.notifyItemChanged(mainCrops.indexOf(model));
                            }else {
                                interCropAdapter.notifyItemChanged(interCrops.indexOf(model));
                            }

                        }

                        yield_val = Double.parseDouble(jsonObject.getJSONObject("data").optString("yield_during"));
//                        if (yield_val != 0.0) {
//                            profit_yield.setText(String.valueOf(yield_val - Double.parseDouble(jsonObject.getJSONObject("data").optString("yield_during"))));
//                        } else {
//                            profit_yield.setText("");
//                        }
                    }

//                    getmembers();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }


    /*public void getactivity() {

        AndroidNetworking.get(Webservice.getactivity_title)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {


                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("Soil Preparation");


                        for (int i = 0; i < jsonArray.length(); i++) {

//                            Subactivity_Model obj = new Subactivity_Model();
//                            obj.setName();

                        }


                    }

//
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }*/


    public void update_land(String id) {
        soil_data = new ArrayList<>();
        land_data = new ArrayList<>();
        water_data = new ArrayList<>();
        cultivation_data = new ArrayList<>();
        postharvest_data = new ArrayList<>();
        dialog_loader.showDialog();


        AndroidNetworking.post(Webservice.activity_list)
                .addUrlEncodeFormBodyParameter("user_id", id)
                .addUrlEncodeFormBodyParameter("land_id", usercrop_id)
                .addUrlEncodeFormBodyParameter("crop_type", crop_type)
                .addUrlEncodeFormBodyParameter("crop_id", crop_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();

                System.out.println("Response: \n" + jsonObject);


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            if (jsonArray.getJSONObject(i).optString("service_id").trim().equalsIgnoreCase("1")) {

                                Activity_Cat_Model obj = new Activity_Cat_Model();
                                obj.setFence_id(jsonArray.getJSONObject(i).optString("id"));
                                obj.setSowing_by(jsonArray.getJSONObject(i).optString("sowing_by"));
                                obj.setCrop_id(jsonArray.getJSONObject(i).optString("crop_id"));
                                obj.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                                obj.setService_id(jsonArray.getJSONObject(i).optString("service_id").trim());
                                obj.setPrepare_type(jsonArray.getJSONObject(i).optString("prepare_type"));
                                obj.setCrop_type(jsonArray.getJSONObject(i).optString("crop_type"));
                                obj.setFencing_by(jsonArray.getJSONObject(i).optString("fencing_by"));
                                obj.setEquipment_name(jsonArray.getJSONObject(i).optString("equipment_name"));
                                obj.setEquipment_type(jsonArray.getJSONObject(i).optString("equipment_type"));
                                obj.setVendor_name(jsonArray.getJSONObject(i).optString("vendor_name"));
                                obj.setContract_type(jsonArray.getJSONObject(i).optString("contract_type"));
                                obj.setMaterial_name(jsonArray.getJSONObject(i).optString("material_name"));
                                obj.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                                obj.setStart_date(jsonArray.getJSONObject(i).optString("start_date"));
                                obj.setEnd_date(jsonArray.getJSONObject(i).optString("end_date"));
                                obj.setDays_count(jsonArray.getJSONObject(i).optString("days_count"));
                                obj.setMember_count(jsonArray.getJSONObject(i).optString("member_count"));
                                obj.setTotal_amount(jsonArray.getJSONObject(i).optString("total_amount"));
                                obj.setCreated_date(jsonArray.getJSONObject(i).optString("created_date"));
                                if (isLand || obj.getCrop_id().equals(crop_id))
                                    soil_data.add(obj);
                            } else if (jsonArray.getJSONObject(i).optString("service_id").equalsIgnoreCase("2")) {
                                Activity_Cat_Model obj = new Activity_Cat_Model();
                                obj.setSowing_by(jsonArray.getJSONObject(i).optString("sowing_by"));
                                obj.setCrop_id(jsonArray.getJSONObject(i).optString("crop_id"));
                                obj.setFence_id(jsonArray.getJSONObject(i).optString("id"));
                                obj.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                                obj.setService_id(jsonArray.getJSONObject(i).optString("service_id"));
                                obj.setPrepare_type(jsonArray.getJSONObject(i).optString("prepare_type"));
                                obj.setCrop_type(jsonArray.getJSONObject(i).optString("crop_type"));
                                obj.setFencing_by(jsonArray.getJSONObject(i).optString("fencing_by"));
                                obj.setEquipment_name(jsonArray.getJSONObject(i).optString("equipment_name"));
                                obj.setEquipment_type(jsonArray.getJSONObject(i).optString("equipment_type"));
                                obj.setVendor_name(jsonArray.getJSONObject(i).optString("vendor_name"));
                                obj.setContract_type(jsonArray.getJSONObject(i).optString("contract_type"));
                                obj.setMaterial_name(jsonArray.getJSONObject(i).optString("material_name"));
                                obj.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                                obj.setStart_date(jsonArray.getJSONObject(i).optString("start_date"));
                                obj.setEnd_date(jsonArray.getJSONObject(i).optString("end_date"));
                                obj.setDays_count(jsonArray.getJSONObject(i).optString("days_count"));
                                obj.setMember_count(jsonArray.getJSONObject(i).optString("member_count"));
                                obj.setTotal_amount(jsonArray.getJSONObject(i).optString("total_amount"));
                                obj.setCreated_date(jsonArray.getJSONObject(i).optString("created_date"));
                                if (isLand || obj.getCrop_id().equals(crop_id))
                                    land_data.add(obj);
                            } else if (jsonArray.getJSONObject(i).optString("service_id").equalsIgnoreCase("3")) {
                                Activity_Cat_Model obj = new Activity_Cat_Model();
                                obj.setSowing_by(jsonArray.getJSONObject(i).optString("sowing_by"));
                                obj.setCrop_id(jsonArray.getJSONObject(i).optString("crop_id"));
                                obj.setFence_id(jsonArray.getJSONObject(i).optString("id"));
                                obj.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                                obj.setService_id(jsonArray.getJSONObject(i).optString("service_id"));
                                obj.setPrepare_type(jsonArray.getJSONObject(i).optString("prepare_type"));
                                obj.setCrop_type(jsonArray.getJSONObject(i).optString("crop_type"));
                                obj.setFencing_by(jsonArray.getJSONObject(i).optString("fencing_by"));
                                obj.setEquipment_name(jsonArray.getJSONObject(i).optString("equipment_name"));
                                obj.setEquipment_type(jsonArray.getJSONObject(i).optString("equipment_type"));
                                obj.setVendor_name(jsonArray.getJSONObject(i).optString("vendor_name"));
                                obj.setContract_type(jsonArray.getJSONObject(i).optString("contract_type"));
                                obj.setMaterial_name(jsonArray.getJSONObject(i).optString("material_name"));
                                obj.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                                obj.setStart_date(jsonArray.getJSONObject(i).optString("start_date"));
                                obj.setEnd_date(jsonArray.getJSONObject(i).optString("end_date"));
                                obj.setDays_count(jsonArray.getJSONObject(i).optString("days_count"));
                                obj.setMember_count(jsonArray.getJSONObject(i).optString("member_count"));
                                obj.setTotal_amount(jsonArray.getJSONObject(i).optString("total_amount"));
                                obj.setCreated_date(jsonArray.getJSONObject(i).optString("created_date"));
                                if (isLand || obj.getCrop_id().equals(crop_id))
                                    water_data.add(obj);
                            } else if (jsonArray.getJSONObject(i).optString("service_id").equalsIgnoreCase("4")) {
                                Activity_Cat_Model obj = new Activity_Cat_Model();
                                obj.setSowing_by(jsonArray.getJSONObject(i).optString("sowing_by"));
                                obj.setCrop_id(jsonArray.getJSONObject(i).optString("crop_id"));
                                obj.setFence_id(jsonArray.getJSONObject(i).optString("id"));
                                obj.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                                obj.setService_id(jsonArray.getJSONObject(i).optString("service_id"));
                                obj.setPrepare_type(jsonArray.getJSONObject(i).optString("prepare_type"));
                                obj.setCrop_type(jsonArray.getJSONObject(i).optString("crop_type"));
                                obj.setFencing_by(jsonArray.getJSONObject(i).optString("fencing_by"));
                                obj.setEquipment_name(jsonArray.getJSONObject(i).optString("equipment_name"));
                                obj.setEquipment_type(jsonArray.getJSONObject(i).optString("equipment_type"));
                                obj.setVendor_name(jsonArray.getJSONObject(i).optString("vendor_name"));
                                obj.setContract_type(jsonArray.getJSONObject(i).optString("contract_type"));
                                obj.setMaterial_name(jsonArray.getJSONObject(i).optString("material_name"));
                                obj.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                                obj.setStart_date(jsonArray.getJSONObject(i).optString("start_date"));
                                obj.setEnd_date(jsonArray.getJSONObject(i).optString("end_date"));
                                obj.setDays_count(jsonArray.getJSONObject(i).optString("days_count"));
                                obj.setMember_count(jsonArray.getJSONObject(i).optString("member_count"));
                                obj.setTotal_amount(jsonArray.getJSONObject(i).optString("total_amount"));
                                obj.setCreated_date(jsonArray.getJSONObject(i).optString("created_date"));
                                if (isLand || obj.getCrop_id().equals(crop_id))
                                    cultivation_data.add(obj);
                            } else if (jsonArray.getJSONObject(i).optString("service_id").equalsIgnoreCase("5")) {
                                Activity_Cat_Model obj = new Activity_Cat_Model();
                                obj.setSowing_by(jsonArray.getJSONObject(i).optString("sowing_by"));
                                obj.setCrop_id(jsonArray.getJSONObject(i).optString("crop_id"));
                                obj.setFence_id(jsonArray.getJSONObject(i).optString("id"));
                                obj.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                                obj.setService_id(jsonArray.getJSONObject(i).optString("service_id"));
                                obj.setPrepare_type(jsonArray.getJSONObject(i).optString("prepare_type"));
                                obj.setCrop_type(jsonArray.getJSONObject(i).optString("crop_type"));
                                obj.setFencing_by(jsonArray.getJSONObject(i).optString("fencing_by"));
                                obj.setEquipment_name(jsonArray.getJSONObject(i).optString("equipment_name"));
                                obj.setEquipment_type(jsonArray.getJSONObject(i).optString("equipment_type"));
                                obj.setVendor_name(jsonArray.getJSONObject(i).optString("vendor_name"));
                                obj.setContract_type(jsonArray.getJSONObject(i).optString("contract_type"));
                                obj.setMaterial_name(jsonArray.getJSONObject(i).optString("material_name"));
                                obj.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                                obj.setStart_date(jsonArray.getJSONObject(i).optString("start_date"));
                                obj.setEnd_date(jsonArray.getJSONObject(i).optString("end_date"));
                                obj.setDays_count(jsonArray.getJSONObject(i).optString("days_count"));
                                obj.setMember_count(jsonArray.getJSONObject(i).optString("member_count"));
                                obj.setTotal_amount(jsonArray.getJSONObject(i).optString("total_amount"));
                                obj.setCreated_date(jsonArray.getJSONObject(i).optString("created_date"));
                                if (isLand || obj.getCrop_id().equals(crop_id))
                                    postharvest_data.add(obj);
                            }
                        }

                    } else {
                        Toasty.error(Activity_Farmx_Modified.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }

                    plough_adapter = new Activity_Adapter(Activity_Farmx_Modified.this, soil_data, crop_icon);
                    soil_recycler.setAdapter(plough_adapter);

                    plough_adapter = new Activity_Adapter(Activity_Farmx_Modified.this, land_data, crop_icon);
                    land_recycler.setAdapter(plough_adapter);

                    plough_adapter = new Activity_Adapter(Activity_Farmx_Modified.this, water_data, crop_icon);
                    water_recycler.setAdapter(plough_adapter);

                    plough_adapter = new Activity_Adapter(Activity_Farmx_Modified.this, cultivation_data, crop_icon);
                    cultivation_recycler.setAdapter(plough_adapter);

                    plough_adapter = new Activity_Adapter(Activity_Farmx_Modified.this, postharvest_data, crop_icon);
                    postharvest_recycler.setAdapter(plough_adapter);

                } catch (Exception e) {
                    dialog_loader.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                dialog_loader.hideDialog();
                try {
                    System.out.println("Activities Error: " + GsonUtils.getGson().toJson(anError));
                }catch (Exception e){ e.printStackTrace(); }
            }
        });
    }


    /*public void show_dialog(String id, String usercrop_id) {
        dialog_loader.showDialog();

        AndroidNetworking.post(Webservice.delete_animal)
                .addUrlEncodeFormBodyParameter("animal_id", id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialog_loader.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {


                    } else {
//                        Toasty.error(ctx, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                dialog_loader.hideDialog();

            }
        });


    }*/
}