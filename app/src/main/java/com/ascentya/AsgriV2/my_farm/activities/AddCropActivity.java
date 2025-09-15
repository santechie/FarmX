package com.ascentya.AsgriV2.my_farm.activities;

import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Crops_Main;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.VarietyModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ApiHelper;
import com.ascentya.AsgriV2.Utils.LogUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.CropAndVarietySelectDialog;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.ascentya.AsgriV2.my_farm.model.CropType;
import com.ascentya.AsgriV2.my_farm.model.LandDetail;
import com.ascentya.AsgriV2.my_farm.views.Bar;
import com.google.android.material.button.MaterialButton;
import com.takisoft.datetimepicker.DatePickerDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddCropActivity extends BaseActivity {

    private TextView landSpinner;
    private TextView cropSpinner;
    private TextView typeSpinner;
    private EditText zoneCountEt;
    private MaterialButton submitBtn;
    private Bar bar;

    private int selectedLand = 0, selectedType;
    private String landId = null;
    private ArrayList<Maincrops_Model> lands = new ArrayList<>();
    private ArrayList<CropType> cropTypeList = new ArrayList<>();

    private String cropId, varietyId;

    private LandDetail landDetail;
    private TextView totalAcreTv, freeAcreTv, occupiedAcreTv;
    private TextView sowingDateTv, areaTypeTv;
    private EditText acreEt;

    private Calendar selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);

        setToolbarTitle("Add Crop", true);

        landId = getIntent().getStringExtra("land_id");

        landSpinner = findViewById(R.id.landSpinner);
        cropSpinner = findViewById(R.id.cropSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        zoneCountEt = findViewById(R.id.zoneEt);
        submitBtn = findViewById(R.id.submit);
        bar = findViewById(R.id.horizontalBar);

        totalAcreTv = findViewById(R.id.totalAcre);
        freeAcreTv = findViewById(R.id.freeAcre);
        occupiedAcreTv = findViewById(R.id.occupiedAcre);
        sowingDateTv = findViewById(R.id.sowingDate);
        areaTypeTv = findViewById(R.id.areaSpinner);
        acreEt = findViewById(R.id.areaEt);

        landSpinner.setOnClickListener(v -> showSelectLandDialog());
        cropSpinner.setOnClickListener(view -> showSelectCropDialog());
        typeSpinner.setOnClickListener(v -> showSelectTypeDialog());
        submitBtn.setOnClickListener(v -> validate());
        sowingDateTv.setOnClickListener(v -> showDateDialog());
        areaTypeTv.setOnClickListener(v -> showAreaTypeDialog());

        cropTypeList.add(new CropType("Main", "main"));
        cropTypeList.add(new CropType("Inter", "inter"));


        loadLands();
        updateCropType();
    }

    private void loadLands(){
        showLoading();
        ApiHelper.loadLands(getSessionManager().getUser().getId(), new ApiHelper.LandAction() {
            @Override
            public void onLoadComplete(JSONObject response,ArrayList<Maincrops_Model> lands, boolean error) {
                hideLoading();

                /*if (UserHelper.checkResponse(AddCropActivity.this, response)){
                    return;
                }*/

                AddCropActivity.this.lands.clear();
                AddCropActivity.this.lands.addAll(lands);
                updateLands();
            }
        });
    }

    private void showSelectLandDialog(){
        if (lands.isEmpty())
            return;
        new SelectItemDialog("Lands",(List<Object>)(List<?>) lands, position -> {
            selectedLand = position;
            landId = lands.get(position).getId();
            updateLands();
            //updateCrops();
            //loadZones(landId, lands.get(position).getId());
        }).show(getSupportFragmentManager(), "lands");
    }

    private void updateLands(){
        int i = 0;
        for (Maincrops_Model land: lands){
            if (landId != null && land.getId().equals(landId)){
                selectedLand = i;
            }
            i++;
        }
        if (landId == null && !lands.isEmpty()) landId = lands.get(selectedLand).getId();
        updateLandText();
        updateLandDetail(false);
    }

    private void updateLandText(){
        if (lands.isEmpty()) return;
        landSpinner.setText(lands.get(selectedLand).getLand_name());
    }

    private void updateLandDetail(boolean validate){
        showLoading();
        landDetail = null;
        ApiHelper.landDetails(lands.get(selectedLand).getId(), new ApiHelper.LandDetailAction() {
            @Override
            public void onResult(JSONObject response,boolean status, LandDetail landDetail) {
                hideLoading();

               /* if (UserHelper.checkResponse(AddCropActivity.this, response)){
                    return;
                }*/
                if (status){
                    AddCropActivity.this.landDetail = landDetail;
                    bar.setTotal((int) landDetail.total);
                    bar.updateBar(landDetail.free, landDetail.occupied, "", "");
                    totalAcreTv.setText(landDetail.total + " Acre");
                    freeAcreTv.setText(landDetail.free + " Acre");
                    occupiedAcreTv.setText(landDetail.occupied + " Acre");
                    if (validate) validate();
                }else {
                    Toasty.error(AddCropActivity.this, "Land Detail Failed").show();
                }
            }
        });
    }

    private void showSelectCropDialog(){

        CropAndVarietySelectDialog.Configuration  configuration =
                new CropAndVarietySelectDialog.Configuration();

        if (cropId != null){
            HashMap<String, ArrayList<String>> selectedMap = new HashMap<>();
            ArrayList<String> selectedVariety = new ArrayList<>();
            selectedVariety.add(varietyId);
            selectedMap.put(cropId, selectedVariety);
            configuration.selected = selectedMap;
        }
//        configuration.except = existingList;
        configuration.displayVarieties = true;
        configuration.multiSelection = false;

        CropAndVarietySelectDialog cropAndVarietySelectDialog =
                new CropAndVarietySelectDialog(this)
                        .setConfiguration(configuration)
                        .setAction(new CropAndVarietySelectDialog.Action() {
                            @Override
                            public void onSelected(HashMap<String, ArrayList<String>> selectedVarieties) {
//                                cropList.clear();
                                String text = "";
                                for (String cropId: selectedVarieties.keySet()){
                                    AddCropActivity.this.cropId = cropId;
                                    AddCropActivity.this.varietyId = null;
                                    if (!TextUtils.isEmpty(text)) text += ", ";
                                    Crops_Main crop = Webservice.getCrop(cropId);
                                    ArrayList<String> varietyIds = selectedVarieties.get(cropId);
                                    if (!varietyIds.isEmpty()){
                                        text += crop.getName() + ": ";
//                                        cropList.put(cropId, varietyIds);
                                        for (VarietyModel varietyModel: crop.getVarieties()){
                                            if (varietyIds.contains(varietyModel.getId())){
                                                AddCropActivity.this.varietyId = varietyModel.getId();
                                                text += varietyModel.getName();
                                            }
                                        }
                                    }else{
//                                        cropList.put(cropId, new ArrayList<>());
                                        text += crop.getName();
                                    }
                                }
                                cropSpinner.setText(text);
                            }

                            @Override
                            public void onClose() {

                            }
                        });

        cropAndVarietySelectDialog.show();

    }

    private void showSelectTypeDialog(){
        new SelectItemDialog("Type",(List<Object>)(List<?>) cropTypeList, position -> {
            selectedType = position;
            updateCropType();
        }).show(getSupportFragmentManager(), "lands");
    }

    private void updateCropType(){
        typeSpinner.setText(cropTypeList.get(selectedType).getName());
    }

    private void showDateDialog(){
        Calendar cal = selectedDate == null ? Calendar.getInstance() : selectedDate;
        DatePickerDialog dpd = new DatePickerDialog(this, (DatePickerDialog.OnDateSetListener)
                (view1, year, month, dayOfMonth) -> {
            selectedDate = Calendar.getInstance();
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, month);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        Calendar fromDate = Calendar.getInstance();
        fromDate.add(Calendar.MONTH, -6);
        Calendar toDate = Calendar.getInstance();
        toDate.add(Calendar.MONTH, 6);
        dpd.getDatePicker().setMinDate(fromDate.getTimeInMillis());
        dpd.getDatePicker().setMaxDate(toDate.getTimeInMillis());
        dpd.show();
    }

    private void updateDate(){
        sowingDateTv.setText(DateUtils.getServerDateFormat(selectedDate.getTime().toString()));
    }

    private void showAreaTypeDialog(){

    }

    private void validate() {

        if (selectedLand >= lands.size()){
            Toasty.normal(this, "Select Land!").show();
            return;
        }

        if (cropId == null || Webservice.getCrop(cropId) == null){
            Toasty.normal(this, "Select Crop!").show();
            return;
        }

        if (selectedDate == null){
            Toasty.normal(this, "Select Sowing Date").show();
            return;
        }

        if (landDetail != null){
            try {
                float acreCount = Float.parseFloat(acreEt.getText().toString());
                if (landDetail.free < acreCount){
                    Toasty.normal(this, "Acre Exceed Limit, Available: "+landDetail.free+" Acre, Given: "+acreCount).show();
                    return;
                }
            }catch (Exception e){
                Toasty.normal(this, "Enter valid Acre Count").show();
                e.printStackTrace();
                return;
            }
        }else {
            updateLandDetail(true);
            return;
        }

        addCrop();
    }

    private void addCrop(){
        showLoading();
        AndroidNetworking.post(Webservice.addCrop)
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .addUrlEncodeFormBodyParameter("land_id", lands.get(selectedLand).getId())
                .addUrlEncodeFormBodyParameter("crop_id", cropId)
                .addUrlEncodeFormBodyParameter("variety_id", TextUtils.isEmpty(varietyId) ? "" : varietyId)
                .addUrlEncodeFormBodyParameter("type", cropTypeList.get(selectedType).getValue())
                .addUrlEncodeFormBodyParameter("zone_count",
                        TextUtils.isEmpty(zoneCountEt.getText().toString()) ?
                                "0" : zoneCountEt.getText().toString())
                .addUrlEncodeFormBodyParameter("acre", acreEt.getText().toString())
                .addUrlEncodeFormBodyParameter("sowing_date", DateUtils.getServerDateFormat(selectedDate.getTime().toString()))

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.log(this.getClass().getSimpleName(), response.toString());
                hideLoading();

                /*if (UserHelper.checkResponse(AddCropActivity.this, response)){
                    return;
                }*/

                try {
                    if (response.optBoolean("status")){
                        sendBroadcast(Constants.Broadcasts.LAND_UPDATE);
                        sendBroadcast(Constants.Broadcasts.ZONE_UPDATE);
                        Toasty.normal(AddCropActivity.this, "Crop Added!").show();
                        finish();
                    }else {
                        String message = response.optString("message");
                        message = TextUtils.isEmpty(message) ? "Can't add Crop" : message;
                        Toasty.normal(AddCropActivity.this, message).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
                Toasty.error(AddCropActivity.this, "Add Crop Error!").show();
            }
        });
    }
}