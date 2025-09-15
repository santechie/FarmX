package com.ascentya.AsgriV2.Activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.CropEssential_Adapter;
import com.ascentya.AsgriV2.Adapters.CustomListAdapter;
import com.ascentya.AsgriV2.Adapters.SuggestedCrops_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Suggest_Interface;
import com.ascentya.AsgriV2.Models.CropEssentials_Model;
import com.ascentya.AsgriV2.Models.SuggetstedCrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.CustomAutoCompleteTextView;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.apache.commons.lang3.Range;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Crop_Checker extends AppCompatActivity {
    RecyclerView cropessentials_recycler, crop_suggestion;
    CropEssential_Adapter cropEssentialAdapter;

    SuggestedCrops_Adapter suggestedCrops_adapter;
    ViewDialog viewDialog;
    List<CropEssentials_Model> Data;
    List<SuggetstedCrops_Model> suggest_Data;
    CustomAutoCompleteTextView autocompleteitem;
    SessionManager sm;
    TextView nocrops;
    String Humidity_ts, pollution_ts, moisture_ts, light_ts, temt_ts, ph_ts;
    TextView fav_title;
    LinearLayout logo_layout;
    String crop_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop__checker);

        Data = new ArrayList<>();

        viewDialog = new ViewDialog(Crop_Checker.this);
        autocompleteitem = findViewById(R.id.search_name);
        nocrops = findViewById(R.id.nocrops);
        fav_title = findViewById(R.id.fav_title);
        sm = new SessionManager(this);
        logo_layout = findViewById(R.id.logo_layout);
        logo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        autocompleteitem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autocompleteitem.showDropDown();
                return false;
            }
        });

        CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocompleteitem, Webservice.Data_crops);


        autocompleteitem.setAdapter(adapter);
        autocompleteitem.setOnItemClickListener(onItemClickListener);
        autocompleteitem.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        autocompleteitem.post(new Runnable() {
            @Override
            public void run() {
                autocompleteitem.showDropDown();
            }
        });
        autocompleteitem.setDropDownVerticalOffset(0);


        cropessentials_recycler = findViewById(R.id.cropessentials_recycler);
        crop_suggestion = findViewById(R.id.crop_suggestion);
        crop_suggestion.setLayoutManager(new LinearLayoutManager(Crop_Checker.this, LinearLayoutManager.HORIZONTAL, false));
        crop_suggestion.setHasFixedSize(true);

        cropessentials_recycler.setLayoutManager(new LinearLayoutManager(Crop_Checker.this));
        cropessentials_recycler.setHasFixedSize(true);


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    hideKeyboard(Crop_Checker.this);
                    //                    Toast.makeText(Crop_Checker.this , adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
//                    getmembers(getCategoryPos(adapterView.getItemAtPosition(i).toString()));
                    gethumidity(getCategoryPos(adapterView.getItemAtPosition(i).toString()));

//                    gethumidity(getCategoryPos(adapterView.getItemAtPosition(i).toString()));

                }
            };


    public void getmembers(final Integer pos) {
        viewDialog.showDialog();

        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.getcrop_realtimeupdate + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {


                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONObject jsonobj = jsonObject.getJSONObject("data");


                        CropEssentials_Model obj = new CropEssentials_Model();
                        obj.setName("Temperature : " + jsonobj.optString("crop_temperature") + " °C");
                        obj.setDisc("This temperature might affect this crop");
                        obj.setIcon(R.drawable.temperature);
                        obj.setValues("26");
                        obj.setTitle("temt");
                        obj.setActual_value(Webservice.Data_crops.get(pos).getTempreture() + " %");
                        String[] temstr = Webservice.Data_crops.get(pos).getTempreture().split("-", 2);
                        obj.setSuccess(floatbetween(Float.parseFloat(jsonobj.optString("crop_temperature")), Float.parseFloat(temstr[0]), Float.parseFloat(temstr[1])));


                        obj.setStatusicon(R.drawable.equal);
                        Data.add(obj);


                        obj = new CropEssentials_Model();
                        obj.setName("Humidity : " + jsonobj.optString("crop_humidity") + "%");
                        obj.setDisc("This is suitable for this crop");
                        obj.setIcon(R.drawable.humidity);
                        obj.setValues("26");
                        obj.setTitle("Humidity");
                        obj.setActual_value(Webservice.Data_crops.get(pos).getHumidity() + " %");
                        String[] humstr = Webservice.Data_crops.get(pos).getHumidity().split("-", 2);
                        obj.setSuccess(intbetween(Integer.parseInt(jsonobj.optString("crop_humidity")), Integer.parseInt(humstr[0]), Integer.parseInt(humstr[1])));


                        obj.setStatusicon(R.drawable.equal);
                        Data.add(obj);
                        obj = new CropEssentials_Model();
                        obj.setName("Pollution : " + jsonobj.optString("crop_pollution") + "%");
                        obj.setDisc("This pollution level may affect this crop");
                        obj.setIcon(R.drawable.pollution);
                        obj.setValues("26");
                        obj.setActual_value(Webservice.Data_crops.get(pos).getPollution() + " %");
                        obj.setTitle("Pollution");

                        String[] pollustr = Webservice.Data_crops.get(pos).getPollution().split("-", 2);
                        obj.setSuccess(intbetween(Integer.parseInt(jsonobj.optString("crop_pollution")), Integer.parseInt(pollustr[0]), Integer.parseInt(pollustr[1])));

                        obj.setStatusicon(R.drawable.equal);
                        Data.add(obj);

//        obj = new CropEssentials_Model();
//        obj.setName("PH - 10");
//        obj.setDisc("This PH level is suitable for this crop");
//        obj.setIcon(R.drawable.ph);
//        obj.setValues("26");
//        obj.setTitle("Pollution");
//        obj.setStatusicon(R.drawable.success);
//        Data.add(obj);


                        obj = new CropEssentials_Model();
                        obj.setName("Moisture : " + jsonobj.optString("crop_moisture") + "%");
                        obj.setDisc("This moisture level may affect this crop");
                        obj.setIcon(R.drawable.moisure);
                        obj.setValues("26");
                        obj.setTitle("Pollution");
                        obj.setActual_value(Webservice.Data_crops.get(pos).getMoisture() + "%");

                        String[] moisustr = Webservice.Data_crops.get(pos).getMoisture().split("-", 2);
                        obj.setSuccess(intbetween(Integer.parseInt(jsonobj.optString("crop_moisture")), Integer.parseInt(moisustr[0]), Integer.parseInt(moisustr[1])));


                        obj.setStatusicon(R.drawable.equal);
                        Data.add(obj);
//                        obj = new CropEssentials_Model();
//                        obj.setName("Total dissolved solids : 60%");
//                        obj.setDisc("This will not affect this crop");
//                        obj.setIcon(R.drawable.solids);
//                        obj.setValues("26");
//                        obj.setTitle("Pollution");
//                        obj.setActual_value("40-50 %");
//                        obj.setStatusicon(R.drawable.success);
//                        Data.add(obj);


                        suggest_Data = new ArrayList<>();
                        for (int i = 0; i < Webservice.Data_crops.size(); i++) {

                            String[] temdatastr = Webservice.Data_crops.get(pos).getTempreture().split("-", 2);
                            String[] humdatastr = Webservice.Data_crops.get(pos).getHumidity().split("-", 2);
                            String[] polludatastr = Webservice.Data_crops.get(pos).getPollution().split("-", 2);
                            String[] moisudatastr = Webservice.Data_crops.get(pos).getMoisture().split("-", 2);


//                            if (floatbetween(Float.parseFloat(jsonobj.optString("crop_temperature")), Float.parseFloat(temdatastr[0]), Float.parseFloat(temdatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_humidity")), Integer.parseInt(humdatastr[0]), Integer.parseInt(humdatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_pollution")), Integer.parseInt(polludatastr[0]), Integer.parseInt(polludatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_moisture")), Integer.parseInt(moisudatastr[0]), Integer.parseInt(moisudatastr[1]))) {
                            SuggetstedCrops_Model suggest_obj = new SuggetstedCrops_Model();
                            suggest_obj.setName(Webservice.Data_crops.get(i).getName());
                            suggest_obj.setId(Webservice.Data_crops.get(i).getCrop_id());
                            suggest_obj.setDissolved_solids(Webservice.Data_crops.get(i).getDissolved_solids());
                            suggest_obj.setHumidity(Webservice.Data_crops.get(i).getHumidity());
                            suggest_obj.setIcon(Webservice.Data_crops.get(i).getIcon());
                            suggest_obj.setMoisture(Webservice.Data_crops.get(i).getMoisture());
                            suggest_obj.setPollution(Webservice.Data_crops.get(i).getPollution());
                            suggest_obj.setTempreture(Webservice.Data_crops.get(i).getTempreture());
                            suggest_obj.setWaterph(Webservice.Data_crops.get(i).getWaterph());

                            suggest_Data.add(suggest_obj);


//                            }
                            if (suggest_Data.size() >= 10) {
                                break;
                            }

                        }

                        if (suggest_Data.size() == 0) {
                            nocrops.setVisibility(View.VISIBLE);
                        } else {
                            nocrops.setVisibility(View.GONE);
                        }

                        suggestedCrops_adapter = new SuggestedCrops_Adapter(Crop_Checker.this, suggest_Data, new Suggest_Interface() {
                            @Override
                            public void crop_suggest(SuggetstedCrops_Model name) {
                                autocompleteitem.setText(name.getName());
                                autocompleteitem.dismissDropDown();
//                                getmembers(getCategoryPos(name.getName()));
                                gethumidity(getCategoryPos(name.getName()));

                            }
                        });
                        crop_suggestion.setAdapter(suggestedCrops_adapter);


                        cropEssentialAdapter = new CropEssential_Adapter(Crop_Checker.this, Data);

                        cropessentials_recycler.setAdapter(cropEssentialAdapter);

                        runLayoutAnimation(cropessentials_recycler);
                        runLayoutrightAnimation(crop_suggestion);


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


    public static String floatbetween(float i, float minValueInclusive, float maxValueInclusive) {
        String range_string;
        if (i >= minValueInclusive && i <= maxValueInclusive) {

            range_string = "medium";
        } else if (i < minValueInclusive) {
            range_string = "low";
        } else if (i > maxValueInclusive) {
            range_string = "high";
        } else {
            range_string = "high";
        }


        return range_string;
    }

    public static String intbetween(int i, int minValueInclusive, int maxValueInclusive) {
        String range_string;
        if (i >= minValueInclusive && i <= maxValueInclusive) {

            range_string = "medium";
        } else if (i < minValueInclusive) {
            range_string = "low";
        } else if (i > maxValueInclusive) {
            range_string = "high";
        } else {
            range_string = "high";
        }


        return range_string;


        //        return (i >= minValueInclusive && i <= maxValueInclusive);
    }

    private int getCategoryPos(String category) {
//        Toast.makeText(this, String.valueOf(Webservice.crops.indexOf(category)), Toast.LENGTH_SHORT).show();
        return Integer.parseInt(Webservice.Data_crops.get(searchFor(category)).getCrop_id());
    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getName().toLowerCase().trim();
            String C_name = Webservice.Data_crops.get(i).getS_name().toLowerCase().trim();


            if (unitString.equals(data.toLowerCase().trim()) || C_name.equals(data.toLowerCase().trim())) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }


    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void runLayoutrightAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_recyclerright);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    public void gethumidity(final Integer pos) {
        viewDialog.showDialog();
        Data = new ArrayList<>();
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
                            Humidity_ts = hum_val;
                        } else {
                            Humidity_ts = "0";
                        }
                    } else {
                        Humidity_ts = "0";
                    }

                    getcropid(pos);


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

    public void getcropid(final Integer pos) {

        AndroidNetworking.get(Webservice.update_crop)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    crop_Id = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field7");
                    getpollution(pos);
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

    public void getpollution(final Integer pos) {

        AndroidNetworking.get(Webservice.update_pollution)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String pol_value = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field3");

                    if (pol_value.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 100);
                        if (test.contains(Integer.parseInt(pol_value))) {
                            pollution_ts = pol_value;
                        } else {
                            pollution_ts = "0";
                        }
                    } else {
                        pollution_ts = "0";
                    }

                    getmoisture(pos);
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

    public void getmoisture(final Integer pos) {

        AndroidNetworking.get(Webservice.update_moisture)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String moisture_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field4");

                    if (moisture_val.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 100);
                        if (test.contains(Integer.parseInt(moisture_val))) {
                            moisture_ts = moisture_val;
                        } else {
                            moisture_ts = "0";
                        }
                    } else {
                        moisture_ts = "0";
                    }

                    getlight(pos);


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

    public void getlight(final Integer pos) {

        AndroidNetworking.get(Webservice.update_light)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String light_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field5");


                    try {
                        Double temt_val = Double.valueOf(light_val);
                        Range<Double> test = Range.between(0.00, 5000.00);
                        if (test.contains(temt_val)) {
                            light_ts = String.valueOf(temt_val);
                        } else {
                            light_ts = "0";
                        }
                    } catch (Exception e) {
                        light_ts = "0";
                    }


//                    if (light_val.matches("-?(0|[1-9]\\d*)")) {
//                        Range<Integer> test = Range.between(0, 5000);
//                        if (test.contains(Integer.parseInt(light_val))) {
//                            light_ts = light_val;
//                        } else {
//                            light_ts = "0";
//                        }
//                    } else {
//                        light_ts = "0";
//                    }


                    gettemt(pos);


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

    public void gettemt(final Integer pos) {

        AndroidNetworking.get(Webservice.update_temperature)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String temp = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field1");


                    try {
                        Double temt_val = Double.valueOf(temp);
                        Range<Double> test = Range.between(0.00, 100.00);
                        if (test.contains(temt_val)) {
                            temt_ts = String.valueOf(temt_val);
                        } else {
                            temt_ts = "0";
                        }
                    } catch (Exception e) {
                        temt_ts = "0";
                    }


                    getph(pos);


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

    public void getph(final Integer pos) {

        AndroidNetworking.get(Webservice.update_ph)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String ph_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field6");

                    try {
                        Range<Double> test = Range.between(0.00, 18.00);
                        if (test.contains(Double.parseDouble(ph_val))) {
                            ph_ts = ph_val;
                        } else {
                            ph_ts = "0";
                        }
                    } catch (Exception e) {
                        ph_ts = "0";
                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }


                CropEssentials_Model obj = new CropEssentials_Model();


                if (temt_ts.equalsIgnoreCase("0")) {
                    obj.setName("Temperature : Not Connected");

                } else {
                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                        obj.setName("Temperature : " + temt_ts + " °C");

                    } else {
                        obj.setName("Temperature : Not Connected");
                    }

                }
                obj.setDisc("This temperature might affect this crop");
                obj.setIcon(R.drawable.temperature);
                obj.setValues("26");
                obj.setTitle("temt");
                obj.setActual_value(Webservice.Data_crops.get(pos).getTempreture() + " %");
                String[] temstr = Webservice.Data_crops.get(pos).getTempreture().split("-", 2);

                obj.setSuccess(floatbetween(Float.parseFloat(temt_ts), Float.parseFloat(temstr[0].trim()), Float.parseFloat(temstr[1].trim())));
                obj.setStatusicon(R.drawable.equal);
                Data.add(obj);


                obj = new CropEssentials_Model();
                if (Humidity_ts.equalsIgnoreCase("0")) {
                    obj.setName("Humidity :  Not Connected");

                } else {
                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                        obj.setName("Humidity : " + Humidity_ts + "%");
                    } else {
                        obj.setName("Humidity :  Not Connected");

                    }

                }
                obj.setDisc("This is suitable for this crop");
                obj.setIcon(R.drawable.humidity);
                obj.setValues("26");
                obj.setTitle("Humidity");
                obj.setActual_value(Webservice.Data_crops.get(pos).getHumidity() + " %");
                String[] humstr = Webservice.Data_crops.get(pos).getHumidity().split("-", 2);
                obj.setSuccess(intbetween(Integer.parseInt(Humidity_ts), Integer.parseInt(humstr[0].trim()), Integer.parseInt(humstr[1].trim())));


                obj.setStatusicon(R.drawable.equal);
                Data.add(obj);
                obj = new CropEssentials_Model();

                if (pollution_ts.equalsIgnoreCase("0")) {
                    obj.setName("Pollution :  Not Connected");

                } else {
                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                        obj.setName("Pollution : " + pollution_ts + "%");


                    } else {
                        obj.setName("Pollution :  Not Connected");

                    }

                }
                obj.setDisc("This pollution level may affect this crop");
                obj.setIcon(R.drawable.pollution);
                obj.setValues("26");
                obj.setActual_value(Webservice.Data_crops.get(pos).getPollution() + " %");
                obj.setTitle("Pollution");

                String[] pollustr = Webservice.Data_crops.get(pos).getPollution().split("-", 2);
                obj.setSuccess(intbetween(Integer.parseInt(pollution_ts), Integer.parseInt(pollustr[0].trim()), Integer.parseInt(pollustr[1].trim())));

                obj.setStatusicon(R.drawable.equal);
                Data.add(obj);

                obj = new CropEssentials_Model();


                if (ph_ts.equalsIgnoreCase("0")) {
                    obj.setName("PH - Not Connected");

                } else {
                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                        obj.setName("PH : " + ph_ts);

                    } else {
                        obj.setName("PH - Not Connected");

                    }

                }

                obj.setDisc("This PH level is suitable for this crop");
                obj.setIcon(R.drawable.ph);
                obj.setValues("26");
                obj.setTitle("Pollution");
                obj.setActual_value(Webservice.Data_crops.get(pos).getWaterph());

                String[] phstr = Webservice.Data_crops.get(pos).getWaterph().split("-", 2);
                obj.setSuccess(floatbetween(Float.parseFloat(ph_ts), Float.parseFloat(phstr[0].trim()), Float.parseFloat(phstr[1].trim())));

                obj.setStatusicon(R.drawable.success);
                Data.add(obj);


                obj = new CropEssentials_Model();

                if (moisture_ts.equalsIgnoreCase("0")) {
                    obj.setName("Moisture : Not Connected");

                } else {
                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                        obj.setName("Moisture : " + moisture_ts + "%");

                    } else {
                        obj.setName("Moisture : Not Connected");


                    }


                }
                obj.setDisc("This moisture level may affect this crop");
                obj.setIcon(R.drawable.moisure);
                obj.setValues("26");
                obj.setTitle("Pollution");
                obj.setActual_value(Webservice.Data_crops.get(pos).getMoisture() + "%");

                String[] moisustr = Webservice.Data_crops.get(pos).getMoisture().split("-", 2);
                obj.setSuccess(intbetween(Integer.parseInt(moisture_ts), Integer.parseInt(moisustr[0].trim()), Integer.parseInt(moisustr[1].trim())));


                obj.setStatusicon(R.drawable.equal);
                Data.add(obj);
//                obj = new CropEssentials_Model();
//
//
//                if (light_ts.equalsIgnoreCase("0")) {
//                    obj.setName("Visibility : Not Connected");
//
//                } else {
//                    obj.setName("Visibility : " + light_ts );
//
//                }
//
//
//
//
//                 obj.setDisc("This will not affect this crop");
//                obj.setIcon(R.drawable.solids);
//                obj.setValues("26");
//                obj.setTitle("Pollution");
//                obj.setActual_value("40-50 %");
//                obj.setStatusicon(R.drawable.success);
//                Data.add(obj);
                fav_title.setVisibility(View.VISIBLE);

                suggest_Data = new ArrayList<>();
                for (int i = 0; i < Webservice.Data_crops.size(); i++) {

                    String[] temdatastr = Webservice.Data_crops.get(pos).getTempreture().split("-", 2);
                    String[] humdatastr = Webservice.Data_crops.get(pos).getHumidity().split("-", 2);
                    String[] polludatastr = Webservice.Data_crops.get(pos).getPollution().split("-", 2);
                    String[] moisudatastr = Webservice.Data_crops.get(pos).getMoisture().split("-", 2);


//                            if (floatbetween(Float.parseFloat(jsonobj.optString("crop_temperature")), Float.parseFloat(temdatastr[0]), Float.parseFloat(temdatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_humidity")), Integer.parseInt(humdatastr[0]), Integer.parseInt(humdatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_pollution")), Integer.parseInt(polludatastr[0]), Integer.parseInt(polludatastr[1])) && intbetween(Integer.parseInt(jsonobj.optString("crop_moisture")), Integer.parseInt(moisudatastr[0]), Integer.parseInt(moisudatastr[1]))) {
                    SuggetstedCrops_Model suggest_obj = new SuggetstedCrops_Model();
                    suggest_obj.setName(Webservice.Data_crops.get(i).getName());
                    suggest_obj.setId(Webservice.Data_crops.get(i).getCrop_id());
                    suggest_obj.setDissolved_solids(Webservice.Data_crops.get(i).getDissolved_solids());
                    suggest_obj.setHumidity(Webservice.Data_crops.get(i).getHumidity());
                    suggest_obj.setIcon(Webservice.Data_crops.get(i).getIcon());
                    suggest_obj.setMoisture(Webservice.Data_crops.get(i).getMoisture());
                    suggest_obj.setPollution(Webservice.Data_crops.get(i).getPollution());
                    suggest_obj.setTempreture(Webservice.Data_crops.get(i).getTempreture());
                    suggest_obj.setWaterph(Webservice.Data_crops.get(i).getWaterph());

                    suggest_Data.add(suggest_obj);


//                            }
                    if (suggest_Data.size() >= 10) {
                        break;
                    }

                }

                if (suggest_Data.size() == 0) {
                    nocrops.setVisibility(View.VISIBLE);
                } else {
                    nocrops.setVisibility(View.GONE);
                }

                suggestedCrops_adapter = new SuggestedCrops_Adapter(Crop_Checker.this, suggest_Data, new Suggest_Interface() {
                    @Override
                    public void crop_suggest(SuggetstedCrops_Model name) {
                        autocompleteitem.setText(name.getName());
                        autocompleteitem.dismissDropDown();
//                                getmembers(getCategoryPos(name.getName()));
                        gethumidity(getCategoryPos(name.getName()));

                    }
                });
                crop_suggestion.setAdapter(suggestedCrops_adapter);


                cropEssentialAdapter = new CropEssential_Adapter(Crop_Checker.this, Data);

                cropessentials_recycler.setAdapter(cropEssentialAdapter);

                runLayoutAnimation(cropessentials_recycler);
                runLayoutrightAnimation(crop_suggestion);


            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

}