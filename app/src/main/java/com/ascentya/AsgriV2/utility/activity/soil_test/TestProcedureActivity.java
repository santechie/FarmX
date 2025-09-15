package com.ascentya.AsgriV2.utility.activity.soil_test;

import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.MyTextUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.dialog.SelectItemDialog;
import com.ascentya.AsgriV2.dialog.VideoPlayerDialog;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.utility.model.SoilTestLab;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class TestProcedureActivity extends BaseActivity {

    private EditText nameTv, mobileNumberTv;
    private TextView landTv, ticketNumberTv, addressTv;
    private MaterialButton submitBtn;
    private ArrayList<Maincrops_Model> landList = new ArrayList();
    private int selectedLandIndex = 0;
    private SoilTestLab soilTestLab;
    private String ticketNumber;
    private Double latitude, longitude;
    private Constants.TestType testType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_test_procedure);

        testType = getFromIntent("test_type", Constants.TestType.class);

        setToolbarTitle( testType.name + " Procedures", true);
        setMenu(R.menu.soil_test_menu);
        setOverrideOnPause();

        if(savedInstanceState != null && savedInstanceState.containsKey("soil_test_lab"))
            getIntent().putExtra("soil_test_lab", savedInstanceState.getString("soil_test_lab"));

        soilTestLab = getFromIntent("soil_test_lab", SoilTestLab.class);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        if (soilTestLab == null){
            finish();
            return;
        }

        nameTv = findViewById(R.id.name);
        mobileNumberTv = findViewById(R.id.mobileNumber);
        addressTv = findViewById(R.id.address);
        landTv = findViewById(R.id.landSpinner);
        ticketNumberTv = findViewById(R.id.ticketNumber);
        findViewById(R.id.image).setOnClickListener(view -> openImage());
        findViewById(R.id.video).setOnClickListener(view -> openVideo());

        submitBtn = findViewById(R.id.submit);
        submitBtn.setOnClickListener(view -> requestSoilTest());

        landTv.setOnClickListener(view -> showLandDialog());

        nameTv.setText(sessionManager.getUser().getFirstname());
        mobileNumberTv.setText(sessionManager.getUser().getPhno());
        addressTv.setText(MyTextUtils.getFormattedAddress(sessionManager.getUser()));

        replaceTypeText(findViewById(R.id.title));
        replaceTypeText(findViewById(R.id.info));
        replaceTypeText(findViewById(R.id.titleTwo));

        loadLands();
    }

    private void replaceTypeText(TextView textView){
        textView.setText(textView.getText().toString().replaceAll("soil", getType()));
    }

    public String getType(){
        if (testType.type.equals(Constants.TestTypes.SoilTest.type)){
            return "soil";
        }else {
            return "water";
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("soil_test_lab", GsonUtils.getGson().toJson(soilTestLab));
    }

    private void showLandDialog(){

        if (landList.isEmpty()){
            Toasty.normal(this, "No Lands!").show();
            return;
        }

        SelectItemDialog itemDialog = new SelectItemDialog("Select Land", (ArrayList) landList, position -> {
            selectedLandIndex = position;
            updateLands();
        });
        itemDialog.show(getSupportFragmentManager(), "SelectItem");
    }

    private void loadLands(){

        showLoading();
        landList.clear();
        AndroidNetworking.get(Webservice.add_farmxcrops + sessionManager.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                if (UserHelper.checkResponse(TestProcedureActivity.this, jsonObject)){
                    return;
                }

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
                            obj.setIntercrop(jsonArray.getJSONObject(i).optString("intercrop"));
                            obj.setAnual_revenue(jsonArray.getJSONObject(i).optString("anual_revenue"));
                            obj.setIrrigation_type(jsonArray.getJSONObject(i).optString("irrigation_type"));
                            obj.setGovt_scheme(jsonArray.getJSONObject(i).optString("govt_scheme"));
                            obj.setLive_stocks(jsonArray.getJSONObject(i).optString("live_stocks"));
                            obj.setSoilhealth_card(jsonArray.getJSONObject(i).optString("soilhealth_card"));
                            obj.setOrganic_farmer(jsonArray.getJSONObject(i).optString("organic_farmer"));
                            obj.setExport(jsonArray.getJSONObject(i).optString("export"));
                            landList.add(obj);
                        }

                    } else {

                    }


                   /* if (Data.size() > 0) {

                        mylands_recycler.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                        myLands_adapter = new MyLands_Adapter(My_Lands.this, Data, true, sm.getUser().getId(), viewDialog);
                        mylands_recycler.setAdapter(myLands_adapter);


                    } else {

                        mylands_recycler.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }*/

                    updateLands();


                } catch (Exception e) {
                    hideLoading();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                hideLoading();
            }
        });
    }

    private void updateLands(){
        submitBtn.setEnabled(!landList.isEmpty());
        if (landList.isEmpty()){
            landTv.setText("No Lands!");
            return;
        }

        landTv.setText(landList.get(selectedLandIndex).getLand_name());
        loadTicketNumber();
    }

    private void loadTicketNumber(){
        updateTicketNumber(true);
        ticketNumber = null;
        AndroidNetworking.post(Webservice.getSoilTestTicket)
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .addUrlEncodeFormBodyParameter("land_id", landList.get(selectedLandIndex).getId())
                .addUrlEncodeFormBodyParameter("lab_id", soilTestLab.getId() + "")
                .addUrlEncodeFormBodyParameter("type", testType.type)
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (UserHelper.checkResponse(TestProcedureActivity.this, response)){
                            return;
                        }
                        try {
                            if (response.optBoolean("status")){
                                ticketNumber = response.optString("data");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            updateTicketNumber(false);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        updateTicketNumber(false);
                        Toasty.error(TestProcedureActivity.this,
                                "Load Ticket Number Error!").show();
                        ticketNumber = null;
                    }
                }
        );
    }

    private void updateTicketNumber(boolean showLoading){
        if (showLoading){
            ticketNumberTv.setText("");
            ticketNumberTv.setHint("Loading...");
        }else {
            ticketNumberTv.setText(ticketNumber != null ? ticketNumber : "");
            ticketNumberTv.setHint(ticketNumber != null ? "Ticket Number" : "Error!");
        }
    }

    private void openImage(){
        String imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRz0QIV-xlaNlmebjpWpYR6m9HP6BFgNrwjA&usqp=CAU";
        DIalogwith_Image.showImageViewer(this, "Soil Procedure",
                new ArrayList<String>(Collections.singleton(imageUrl)));
    }

    private void openVideo(){
        String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";
//        VideoPlayerDialog.showVideo(getSupportFragmentManager(), videoUrl);
        VideoPlayerDialog.showVideoDialog(this, videoUrl);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.whatsApp){
            openWhatsApp(Constants.SAMPLE_NUMBER);
        }else if (item.getItemId() == R.id.call){
            openCall(Constants.SAMPLE_NUMBER);
        }else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void requestSoilTest(){
        String name = nameTv.getText().toString().trim();
        String mobileNumber = mobileNumberTv.getText().toString().trim();

        if (ticketNumber == null){
            Toasty.normal(this, "Please wait, Ticket Number is Generating!").show();
            loadTicketNumber();
            return;
        }

        if(!ValidateInputs.isValidName(name)){
            Toasty.error(this, "Enter Valid Name!").show();
            return;
        }

        if (!ValidateInputs.isValidNumber(mobileNumber)){
            Toasty.error(this, "Enter Valid Mobile Number!").show();
            return;
        }

        showLoading();

        AndroidNetworking.post(Webservice.soilTestRequest)
                .addUrlEncodeFormBodyParameter("user_id", sessionManager.getUser().getId())
                .addUrlEncodeFormBodyParameter("name", name)
                .addUrlEncodeFormBodyParameter("mobile_number", mobileNumber)
                .addUrlEncodeFormBodyParameter("ticket_number", ticketNumber)
                .addUrlEncodeFormBodyParameter("land_id", landList.get(selectedLandIndex).getId())
                .addUrlEncodeFormBodyParameter("lab_id", String.valueOf(soilTestLab.getId()))
                .addUrlEncodeFormBodyParameter("latitude", String.valueOf(latitude))
                .addUrlEncodeFormBodyParameter("longitude", String.valueOf(longitude))
                .addUrlEncodeFormBodyParameter("type", testType.type)
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideLoading();
                        if (UserHelper.checkResponse(TestProcedureActivity.this, response)){
                            return;
                        }
                        try {
                            if (response.optBoolean("status")){
                                Toasty.normal(TestProcedureActivity.this,
                                        testType.name + " Requested!").show();
                                finishAffinity();
                            }else {
                                Toasty.normal(TestProcedureActivity.this,
                                        "Can't Request " + testType.name).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hideLoading();
                        System.out.println("Soil Test Request Error: \n"
                                + GsonUtils.getGson().toJson(anError));
                        Toasty.error(TestProcedureActivity.this,
                                "Soil Test Request Error!").show();
                    }
                }
        );
    }
}