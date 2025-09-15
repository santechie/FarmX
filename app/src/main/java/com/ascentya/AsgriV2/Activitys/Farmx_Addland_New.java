package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Block_Model;
import com.ascentya.AsgriV2.Models.Crop_Landselection;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.CropAndVarietySelectDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.model.SoilType;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import es.dmoral.toasty.Toasty;

public class Farmx_Addland_New extends BaseActivity {

    NoDefaultSpinner soiltype, acer_count, anual_revenue, irrigation_type;
    ViewDialog viewDialog;
    AutoCompleteTextView district, taluk;
    List <SoilType> soilTypeList = new ArrayList<>();

    Boolean Main_crop_suggestion = true;
    String main_c = "", inter_c = "", govtscheme_selection = "", livestock_selection = "", healthcard_selection = "", organicfarmer_selection = "", export_seletion = "", ownership_selection = "";

    EditText maincrop, intercrop, humidity;
    List<String> District_data = new ArrayList<>(), taluk_data, acers_no, Soiltype_data, maincrop_data, intercrop_data, annualrevenue_data, irrigation_data;
    Button register;
    List<Block_Model> block_data = new ArrayList<>();
    LabeledSwitch schemeswitch, organic_farmer, ownerwitch, cattleswitch, soilswitch, organicswitch, exportwitch;
    LinearLayout sceme_layout, organic_layout;
    EditText scheme_name, land_name;
    ImageView add_scheme;
    TagContainerLayout scheme_tag;
    View organic_view;
    EditText water_ph, temperature, pollution, moisture;
    ArrayAdapter<String> crop_adpter;
    //List<String> Main_Crop_list, Inter_Crop_List;
    HashMap<String, ArrayList<String>> mainCropMap, interCropMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmx_addland);

        schemeswitch = findViewById(R.id.schemeswitch);
        water_ph = findViewById(R.id.water_phval);
        temperature = findViewById(R.id.temperature_val);
        humidity = findViewById(R.id.humidity);
        land_name = findViewById(R.id.land_name);
        pollution = findViewById(R.id.pollution);
        moisture = findViewById(R.id.moisture);
        organic_farmer = findViewById(R.id.organic_farmer);
        organic_view = findViewById(R.id.organic_view);
        ownerwitch = findViewById(R.id.ownerwitch);
        cattleswitch = findViewById(R.id.cattleswitch);
        soilswitch = findViewById(R.id.soilswitch);
        organicswitch = findViewById(R.id.organicswitch);
        exportwitch = findViewById(R.id.exportwitch);
        sceme_layout = findViewById(R.id.sceme_layout);
        add_scheme = findViewById(R.id.add_scheme);
        scheme_tag = findViewById(R.id.scheme_tag);
        scheme_name = findViewById(R.id.scheme_name);
        organic_layout = findViewById(R.id.organic_layout);
//        Main_Crop_list = new ArrayList<>();
//        Inter_Crop_List = new ArrayList<>();
        soilTypeList.add(new SoilType());
        mainCropMap = new HashMap<>();
        interCropMap = new HashMap<>();
        scheme_tag.setEnableCross(true);
        scheme_tag.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {


            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                scheme_tag.removeTag(position);
            }
        });

        add_scheme.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                if (scheme_name.getText().toString().length() > 0) {


                    scheme_tag.addTag(scheme_name.getText().toString());
                    scheme_name.setText("");
                } else {
                    Toast.makeText(Farmx_Addland_New.this, "Write something to post", Toast.LENGTH_SHORT).show();
                }
            }
        });

        schemeswitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    sceme_layout.setVisibility(View.VISIBLE);
                } else {
                    sceme_layout.setVisibility(View.GONE);
                }
            }
        });

        organic_farmer.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    organic_layout.setVisibility(View.GONE);
                    organic_view.setVisibility(View.GONE);
                } else {
                    organic_layout.setVisibility(View.VISIBLE);
                    organic_view.setVisibility(View.VISIBLE);
                }
            }
        });


        taluk = findViewById(R.id.taluk);


        viewDialog = new ViewDialog(Farmx_Addland_New.this);
        taluk_data = new ArrayList<>();

        register = findViewById(R.id.register);
        district = findViewById(R.id.district);
        soiltype = findViewById(R.id.soiltype);
        acer_count = findViewById(R.id.acer_count);
        maincrop = findViewById(R.id.maincrop);
        intercrop = findViewById(R.id.intercrop);
        anual_revenue = findViewById(R.id.anual_revenue);
        irrigation_type = findViewById(R.id.irrigation_type);

        district.setOnClickListener(view -> district.showDropDown());

        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toasty.normal(Farmx_Addland_New.this, block_data.get(i).getId()).show();
                get_village(block_data.get(i).getId());
            }
        });



        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> block_adpter = new ArrayAdapter(
                Farmx_Addland_New.this,
                R.layout.spinner_item,
                District_data);

        district.setAdapter(block_adpter);

        taluk.setOnClickListener(view ->
                taluk.showDropDown()
        );

        add_mypost();
        getSoilType();
//        updateSoilType();

        INIT();

        intercrop.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                showCropSelectionDialog(interCropMap, intercrop, mainCropMap);

                /*AddCrops_Dialog obj = new AddCrops_Dialog();
                obj.dialog(Farmx_Addland_New.this, Main_Crop_list, Inter_Crop_List, Webservice.Data_crops,
                        "Select crops", humidity.getText().toString(), water_ph.getText().toString(), temperature.getText().toString(), pollution.getText().toString(), moisture.getText().toString(), new Dialog_crops() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void foo(List<String> name) {

                        Inter_Crop_List = name;
//                        for (int i = 0; i <name.size() ; i++) {
//
//                             joined=String.join(",", String.join(",", name));
//
//
//
//                        }
                        intercrop.setText(name.toString());
                    }
                }, false);*/

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validatesignForm()) {
                    add_land();
                }

              /*  if (NetworkDetector.isNetworkStatusAvialable(Farmx_Addland_New.this)) {


                } else {
                    Toast.makeText(Farmx_Addland_New.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }


                    private void showCropSelectionDialog(HashMap<String, ArrayList<String>> cropList, TextView textView,
                                         HashMap<String, ArrayList<String>> existingList){

        CropAndVarietySelectDialog.Configuration  configuration =
                new CropAndVarietySelectDialog.Configuration();

        configuration.selected = cropList;
        configuration.except = existingList;
        configuration.displayVarieties = true;
        configuration.multiSelection = true;

        CropAndVarietySelectDialog cropAndVarietySelectDialog =
                new CropAndVarietySelectDialog(this)
                        .setConfiguration(configuration)
                        .setAction(new CropAndVarietySelectDialog.Action() {
                            @Override
                            public void onSelected(HashMap<String, ArrayList<String>> selectedVarieties) {
                                cropList.clear();
                                String text = "";
                                for (String cropId: selectedVarieties.keySet()){
                                    if (!TextUtils.isEmpty(text)) text += ", ";
                                    Crops_Main crop = Webservice.getCrop(cropId);
                                    ArrayList<String> varietyIds = selectedVarieties.get(cropId);
                                    if (!varietyIds.isEmpty()){
                                        text += crop.getName() + ": ";
                                        cropList.put(cropId, varietyIds);
                                        for (VarietyModel varietyModel: crop.getVarieties()){
                                            if (varietyIds.contains(varietyModel.getId())){
                                                text += varietyModel.getName();
                                            }
                                        }
                                    }else{
                                        cropList.put(cropId, new ArrayList<>());
                                        text += crop.getName();
                                    }
                                }
                                textView.setText(text);
                            }

                            @Override
                            public void onClose() {

                            }
                        });

        cropAndVarietySelectDialog.show();
    }



    private boolean validatesignForm() {
        if (!(district != null && district.getText().length() > 0)) {
            Toast.makeText(this, "Kindly select district", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(taluk != null && taluk.getText().length() > 0)) {
            Toast.makeText(this, "Kindly select taluk", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(acer_count != null && acer_count.getSelectedItem() != null)) {
            Toast.makeText(this, "Kindly select Acre", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(soilTypeList != null && soiltype.getSelectedItem() != null)) {
            Toast.makeText(this, "Kindly select Soil type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(land_name != null && land_name.getText().length() > 0)) {
            Toast.makeText(this, "Kindly enter land name", Toast.LENGTH_SHORT).show();
            return false;
        /*} else if (!(mainCropMap != null && mainCropMap.size() > 0)) {
            maincrop.setError(getString(R.string.required_date));
            return false;*/
        } else {
            return true;
        }
    }

    public void INIT() {

        irrigation_data = new ArrayList<>();

        irrigation_data.add("Surface Irrigation");
        irrigation_data.add("Localized Irrigation");
        irrigation_data.add("Sprinkler Irrigation");
        irrigation_data.add("Drip Irrigation");
        irrigation_data.add("Centre Pivot Irrigation");
        irrigation_data.add("Sub Irrigation");
        irrigation_data.add("Manual Irrigation");

        maincrop.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                showCropSelectionDialog(mainCropMap, maincrop, interCropMap);
                /*AddCrops_Dialog obj = new AddCrops_Dialog();
                obj.dialog(Farmx_Addland_New.this, Main_Crop_list, Inter_Crop_List, Webservice.Data_crops, "Select crops", humidity.getText().toString(), water_ph.getText().toString(), temperature.getText().toString(), pollution.getText().toString(), moisture.getText().toString(), new Dialog_crops() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void foo(List<String> name) {
                        String joined = "0";
                        Main_Crop_list = name;

                        maincrop.setText(name.toString());
                    }
                }, true);*/
            }
        });


        ArrayAdapter<String> irrigation_adpter = new ArrayAdapter(Farmx_Addland_New.this, R.layout.spinner_item,
                irrigation_data);

        irrigation_type.setAdapter(irrigation_adpter);

        acers_no = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            acers_no.add(String.valueOf(i));
        }

        ArrayAdapter<String> acers_adpter = new ArrayAdapter(Farmx_Addland_New.this,
                R.layout.spinner_item, acers_no);

        acer_count.setAdapter(acers_adpter);
        annualrevenue_data = new ArrayList<>();

        annualrevenue_data.add("10 - 40k");
        annualrevenue_data.add("40 -80k");
        annualrevenue_data.add("80 - 100k");
        annualrevenue_data.add("1 - 2l");
        annualrevenue_data.add("2 - 3l");
        annualrevenue_data.add("3 -4l");
        annualrevenue_data.add("4 - 5l");
        annualrevenue_data.add("5 - 6l");
        annualrevenue_data.add("6 - 7l");
        annualrevenue_data.add("7 - 8l");
        annualrevenue_data.add("8 -9l");
        annualrevenue_data.add("9 - 10l");
        annualrevenue_data.add("10l - above");

        ArrayAdapter<String> annualrevenue_adpter = new ArrayAdapter(Farmx_Addland_New.this, R.layout.spinner_item,
                annualrevenue_data);

        anual_revenue.setAdapter(annualrevenue_adpter);





//        Soiltype_data = new ArrayList<>();
//
//        Soiltype_data.add("Sand");
//        Soiltype_data.add("Loamy sand");
//        Soiltype_data.add("Sandy loam");
//        Soiltype_data.add("Loam");
//        Soiltype_data.add("Silt loam");
//        Soiltype_data.add("Silt");
//        Soiltype_data.add("Sandy clay loam");
//        Soiltype_data.add("Clay loam");
//        Soiltype_data.add("Silt clay loam");
//        Soiltype_data.add("Sandy clay");
//        Soiltype_data.add("Silty clay");
//        Soiltype_data.add("Clay");
//
//        ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(Farmx_Addland_New.this, R.layout.spinner_item,
//                soilTypeList);
//
//        soiltype.setAdapter(soiltype_adpter);
//    }
    }


    public void add_land() {
        final JSONArray jsonArray = new JSONArray();
        final JSONArray interjsonArray = new JSONArray();

        String intercrop_name, maincrop_name = null;

        List<Crop_Landselection> maincrop_data = new ArrayList<>();
        String anual = "", irrigation = "", autocomplete_maincrop = "", autocomplete_intercrop = "";


        if (anual_revenue.getSelectedItem() != null) {
            anual = anual_revenue.getSelectedItem().toString();
        }
        if (irrigation_type.getSelectedItem() != null) {
            irrigation = irrigation_type.getSelectedItem().toString();
        }

        if (intercrop.getText().length() > 0) {
            try {
                intercrop_name = Webservice.Data_crops.get(searchFor(intercrop.getText().toString())).getCrop_id();

            } catch (Exception e) {
                intercrop_name = "";
            }


        } else {
            intercrop_name = "";

        }

        if(mainCropMap != null){
            if (!mainCropMap.isEmpty()){
                try {
                    for (String cropId: mainCropMap.keySet()){
                        ArrayList<String> varietyIds = mainCropMap.get(cropId);
                        Crops_Main cropsMain = Webservice.getCrop(cropId);
                        if (!varietyIds.isEmpty()) {
                            for (String varietyId : varietyIds) {
                                JSONObject cropObject = new JSONObject();
                                cropObject.accumulate("crop_id", cropId);
                                if (varietyId != null && !TextUtils.isEmpty(varietyId))
                                    cropObject.accumulate("variety_id", varietyId);
                                cropObject.accumulate("area", cropsMain.getName());
                                cropObject.accumulate("zone", cropsMain.getName());
                                jsonArray.put(cropObject);
                            }
                        }else {
                            JSONObject cropObject = new JSONObject();
                            cropObject.accumulate("crop_id", cropId);
                            cropObject.accumulate("area", cropsMain.getName());
                            cropObject.accumulate("zone", cropsMain.getName());
                            jsonArray.put(cropObject);
                        }
                    }
                }catch (Exception e){ e.printStackTrace(); }
            }/*else{
                toast("Please select at least 1 main crop!");
                return;
            }*/
        }

        if (interCropMap != null && !interCropMap.isEmpty()){
            for (String cropId: interCropMap.keySet()){
                try {
                    ArrayList<String> varietyIds = interCropMap.get(cropId);
                    Crops_Main cropsMain = Webservice.getCrop(cropId);
                    if (!varietyIds.isEmpty()) {
                        for (String varietyId : varietyIds) {
                            JSONObject cropObject = new JSONObject();
                            cropObject.accumulate("crop_id", cropId);
                            if (varietyId != null && !TextUtils.isEmpty(varietyId))
                                cropObject.accumulate("variety_id", varietyId);
                            cropObject.accumulate("area", cropsMain.getName());
                            cropObject.accumulate("zone", cropsMain.getName());
                            interjsonArray.put(cropObject);
                        }
                    }else {
                        JSONObject cropObject = new JSONObject();
                        cropObject.accumulate("crop_id", cropId);
                        cropObject.accumulate("area", cropsMain.getName());
                        cropObject.accumulate("zone", cropsMain.getName());
                        interjsonArray.put(cropObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Maincrop: " + jsonArray);
        System.out.println("Intercrop: " + interjsonArray);

        /*if (Inter_Crop_List != null && Inter_Crop_List.size() > 0) {
            for (int i = 0; i < Inter_Crop_List.size(); i++) {
                List<String> items = Arrays.asList(Inter_Crop_List.get(i).split("\\s*,\\s*"));

                JSONObject obj = new JSONObject();
                try {

//
//
                    obj.accumulate("crop_id", Webservice.Data_crops.get(searchFor(items.get(0))).getCrop_id());
                    obj.accumulate("area", items.get(0));
                    obj.accumulate("zone", items.get(0));

                    interjsonArray.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }*/
        /*if (Main_Crop_list != null && Main_Crop_list.size() > 0) {


            for (int i = 0; i < Main_Crop_list.size(); i++) {
                List<String> items = Arrays.asList(Main_Crop_list.get(i).split("\\s*,\\s*"));

                JSONObject obj = new JSONObject();
                try {
//
//
                    obj.accumulate("crop_id", Webservice.Data_crops.get(searchFor(items.get(0))).getCrop_id());
                    obj.accumulate("area", items.get(0));
                    obj.accumulate("zone", items.get(0));

                    jsonArray.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }*/

//        if (true) return;

        viewDialog.showDialog();

        AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Agripedia/add_farmx_land")
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .addUrlEncodeFormBodyParameter("district", district.getText().toString())
                .addUrlEncodeFormBodyParameter("state", "")
                .addUrlEncodeFormBodyParameter("land_name", land_name.getText().toString())
                .addUrlEncodeFormBodyParameter("humidity", "")
                .addUrlEncodeFormBodyParameter("temperature", "")
                .addUrlEncodeFormBodyParameter("ph_value", "")
                .addUrlEncodeFormBodyParameter("moisture", "")
                .addUrlEncodeFormBodyParameter("weather_prediction", "")
                .addUrlEncodeFormBodyParameter("village", "")
                .addUrlEncodeFormBodyParameter("taluk", taluk.getText().toString())
                .addUrlEncodeFormBodyParameter("acre_count", acer_count.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("soiltype", getSelectedSoilType())
                .addUrlEncodeFormBodyParameter("maincrop", jsonArray.toString())
                .addUrlEncodeFormBodyParameter("intercrop", interjsonArray.toString())
                .addUrlEncodeFormBodyParameter("anual_revenue", anual)
                .addUrlEncodeFormBodyParameter("irrigation_type", irrigation)
                .addUrlEncodeFormBodyParameter("govt_scheme", String.valueOf(schemeswitch.isOn()))
                .addUrlEncodeFormBodyParameter("live_stocks", String.valueOf(cattleswitch.isOn()))
                .addUrlEncodeFormBodyParameter("soilhealth_card", String.valueOf(soilswitch.isOn()))
                .addUrlEncodeFormBodyParameter("organic_farmer", String.valueOf(organicswitch.isOn()))
                .addUrlEncodeFormBodyParameter("export", String.valueOf(exportwitch.isOn()))
                .addUrlEncodeFormBodyParameter("ownership", String.valueOf(ownerwitch.isOn()))
                .addUrlEncodeFormBodyParameter("scheme_used", scheme_tag.getSelectedTagViewText().toString())
                .addUrlEncodeFormBodyParameter("doing_organic", scheme_tag.getSelectedTagViewText().toString())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                if (UserHelper.checkResponse(Farmx_Addland_New.this, jsonObject)){
                    return;
                }

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        System.out.println("dfasdfsdafsfa"+jsonObject.optString("status"));
                        sendBroadcast(Constants.Broadcasts.LAND_UPDATE);
                        Toasty.success(Farmx_Addland_New.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        finish();

                    } else {
                        Toasty.error(Farmx_Addland_New.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                try {
                    Toasty.error(Farmx_Addland_New.this, "Add Land Error!", Toast.LENGTH_SHORT, true).show();
                    System.out.println(GsonUtils.getGson().toJson(anError));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
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

    public void add_mypost() {

        District_data.clear();
        viewDialog.showDialog();
        block_data.clear();

        AndroidNetworking.get(Webservice.get_districttn)
                .addPathParameter("user_id", getSessionManager().getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                /*if (UserHelper.checkResponse(Farmx_Addland_New.this, jsonObject)){
                    return;
                }*/

                System.out.println("District: " + jsonObject);

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        List<String> b_data = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Block_Model obj = new Block_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("district_id"));
                            obj.setBlock_name(jsonArray.getJSONObject(i).optString("district_name"));

                            District_data.add(obj.getBlock_name());
                            block_data.add(obj);
                        }

                    } else {

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();

                    e.printStackTrace();
                }

                ((ArrayAdapter) district.getAdapter()).notifyDataSetChanged();

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();


            }
        });
    }

    public void get_village(String block_id) {
        viewDialog.showDialog();
        taluk.setText("");
        taluk.setAdapter(null);

        AndroidNetworking.post(Webservice.get_villagetn)
                .addUrlEncodeFormBodyParameter("block_id", block_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("Villages: " + jsonObject);
                viewDialog.hideDialog();

              /*  if (UserHelper.checkResponse(Farmx_Addland_New.this, jsonObject)){
                    return;
                }*/


                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        List<String> b_data = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            b_data.add(jsonArray.getJSONObject(i).optString("village_name"));
                        }


                        ArrayAdapter<String> block_adpter = new ArrayAdapter(Farmx_Addland_New.this, R.layout.spinner_item,
                                b_data);

                        taluk.setAdapter(block_adpter);


                    } else {

                    }



                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

                taluk.setHint(taluk.getAdapter().getCount() == 0 ? "No Villages" : "Select Village");


            }

            @Override
            public void onError(ANError anError) {
                try {
                    taluk.setHint(taluk.getAdapter() == null ? "No Villages" : "Village");
                    System.out.println("Error: " + GsonUtils.getGson().toJson(anError));
                }catch (Exception e){}
                viewDialog.hideDialog();


            }
        });
    }

    public void updateSoilType(){
        if (soilTypeList != null){
            ArrayAdapter<String> soilAdapter = new ArrayAdapter(this, R.layout.spinner_item, soilTypeList);
            soiltype.setAdapter(soilAdapter);

            soiltype.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            soilTypeList.get(i).getSoilId();
//                            getSoilType();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    }
            );
        }
    }

    public void getSoilType(){
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.getSoilType)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Soil Response: \n" + response);
                        viewDialog.hideDialog();
                        if (response.optBoolean("status")) {
                            String soilJsonArray = response.optString("data");
                            System.out.println(soilJsonArray);
                            soilTypeList = GsonUtils.getGson()
                                    .fromJson(soilJsonArray, EMarketStorage.SoilReportListType);

                                ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(Farmx_Addland_New.this, R.layout.spinner_item,
                                        soilTypeList);

                                soiltype.setAdapter(soiltype_adpter);
                                updateSoilType();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    public String getSelectedSoilType(){
        if (soiltype.getSelectedItem() != null){
            return ((SoilType) soiltype.getSelectedItem()).getSoilName();
        }
        return "";
    }
}