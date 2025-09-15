package com.ascentya.AsgriV2.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Farmx_Service;
import com.ascentya.AsgriV2.Activitys.Main_Dashboard;
import com.ascentya.AsgriV2.Models.Block_Model;
import com.ascentya.AsgriV2.Models.DistrictModel;
import com.ascentya.AsgriV2.Models.District_Model;
import com.ascentya.AsgriV2.Models.StateModel;
import com.ascentya.AsgriV2.Models.TalukModel;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.EMarketStorage;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Shared_Preference.Userobject;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ImageUtils;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.ValidateInputs;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.ascentya.AsgriV2.managers.SubscriptionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class
Farmer_Registration extends BaseActivity {
    Button register, Login, register_others;
    NoDefaultSpinner stateSpinner, districtSpinner, citySpinner;
    Spinner farmer_type, gender;
    EditText email_phno, accno, faily_count, mail, pincode, mother_name, name, age, street_name, location;
    CircleImageView kissan_card;
    TextInputEditText password;
    CircleImageView aadhar_card;
    ViewDialog viewDialog;
    String[] afarmer_type = {"Individual", "FPO", "Co-Operative"};
    String[] gender_list = {"Male", "Female", "Others"};
    SessionManager sm;
    String aadhar_path, kissan_path;
    AutoCompleteTextView fpo;
    List<Block_Model> block_data;
    List<District_Model> district_data;
    List<StateModel> stateList = new ArrayList<>();
    List<DistrictModel> districtList = new ArrayList<>();
    List<TalukModel> villageList = new ArrayList<>();
    LinearLayout fpo_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration);

        register = findViewById(R.id.register);
        accno = findViewById(R.id.accno);
        faily_count = findViewById(R.id.faily_count);
        fpo = findViewById(R.id.fpo);
        pincode = findViewById(R.id.pincode);
        stateSpinner = findViewById(R.id.state);
        mother_name = findViewById(R.id.mother_name);
        mail = findViewById(R.id.mail);
        fpo_layout = findViewById(R.id.fpo_layout);
        email_phno = findViewById(R.id.email_phno);
        register_others = findViewById(R.id.register_others);
        Login = findViewById(R.id.Login);
        //taluk = findViewById(R.id.taluk);
        password = findViewById(R.id.password);
        fpo_layout = findViewById(R.id.fpo_layout);
        districtSpinner = findViewById(R.id.district);

        name = findViewById(R.id.name);
        sm = new SessionManager(this);
        farmer_type = findViewById(R.id.farmer_type);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        //district = findViewById(R.id.district);
        street_name = findViewById(R.id.street_name);
        citySpinner = findViewById(R.id.village);
        location = findViewById(R.id.location);
        aadhar_card = findViewById(R.id.aadhar_card);
        kissan_card = findViewById(R.id.kisssan_card);
        viewDialog = new ViewDialog(Farmer_Registration.this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Farmer_Registration.this, Formx_Login_Activity.class);
                startActivity(i);
                finish();
            }
        });

        register_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Farmer_Registration.this, Farmx_Service.class);
                startActivity(i);
            }
        });

        //gender
        ArrayAdapter gender_adapter = new ArrayAdapter(this, R.layout.spinner_row, gender_list);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gender_adapter);

        //afarmer_type
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_row, afarmer_type);
        aa.setDropDownViewResource(R.layout.spinner_row_center);
        farmer_type.setAdapter(aa);

        farmer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase("FPO")) {
                    fpo_layout.setVisibility(View.VISIBLE);
                } else {
                    fpo_layout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //aadhar card
        aadhar_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        aadhar_path = ImageUtils
                                .setImage(Farmer_Registration.this, r, aadhar_card, true);
                    }
                }).show(Farmer_Registration.this);

            }
        });

        kissan_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog pickImageDialog = PickImageDialog.build(new PickSetup());
                pickImageDialog.setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        kissan_path = ImageUtils
                                .setImage(Farmer_Registration.this, r, kissan_card, true);
                    }
                });
                pickImageDialog.show(Farmer_Registration.this);
            }
        });

        //registeration
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {
                    if (mail.getText().toString().trim().length() > 0) {
                        if (!ValidateInputs.isValidEmail(mail.getText().toString().trim())) {

                            mail.setError(getString(R.string.enter_valid_email));

                        } else {
                            Registers();
                        }
                    } else {
                        Registers();
                    }
                }
//                Intent i = new Intent(Farmer_Registration.this, Formx_Login_Activity.class);
//                startActivity(i);
            }
        });

        /*taluk.setOnClickListener(v -> {
            if (taluk.getAdapter() == null) {
                Snackbar.make(findViewById(R.id.mainLay), R.string.select_district, Snackbar.LENGTH_LONG).show();
            } else {
                taluk.showDropDown();
            }
        });*/

        /*taluk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(taluk.getAdapter() == null){
//                    Log.i(this.getClass().getSimpleName(), "Has Focus");
//                    Toast.makeText(Farmer_Registration.this, R.string.select_district, Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.mainLay), R.string.select_district, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        addDummyModels();
        getStates();
    }

    private void addDummyModels(){
        stateList.add(new StateModel());
        districtList.add(new DistrictModel());
        villageList.add(new TalukModel());
        updateStates();
        updateDistricts();
        updateCities();
    }


    public void Registers() {

        File aadhar_f = null, kissan_f = null;
        if (aadhar_path != null && aadhar_path.length() > 0) {
            aadhar_f = new File(aadhar_path);
        }

        if (kissan_path != null && kissan_path.length() > 0) {
            kissan_f = new File(kissan_path);
        }

        String gender_name = null;
        if (gender != null && gender.getSelectedItem() != null) {
            gender_name = (String) gender.getSelectedItem();
        } else {
            gender_name = "";
        }

        viewDialog.showDialog();
        //AndroidNetworking.upload(Webservice.register_f)
        AndroidNetworking.upload("https://vrjaitraders.com/ard_farmx/api/Authentication/registration_all")

                .addMultipartParameter("username", name.getText().toString())
                .addMultipartParameter("email", email_phno.getText().toString())
                .addMultipartParameter("password", password.getText().toString())
                .addMultipartParameter("phone", email_phno.getText().toString())
                .addMultipartParameter("farmer_name", name.getText().toString())
                .addMultipartParameter("farmer_email", mail.getText().toString())
                .addMultipartParameter("farming_type", farmer_type.getSelectedItem().toString())
                .addMultipartParameter("farmer_age", age.getText().toString())
//                .addUrlEncodeFormBodyParameter("farmer_age", age.getText().toString())
                .addMultipartParameter("farmer_street", street_name.getText().toString())
                .addMultipartParameter("farmer_city", getSelectedCity())
                .addMultipartParameter("farmer_location", location.getText().toString())
                .addMultipartFile("farmer_aadhar", aadhar_f)
                .addMultipartFile("farmer_kissan", kissan_f)
                .addMultipartParameter("farmer_gender", gender_name)
                .addMultipartParameter("user_type", "farmer")
                .addMultipartParameter("mother_name", mother_name.getText().toString())
                .addMultipartParameter("pincode", pincode.getText().toString())
                .addMultipartParameter("family_members", faily_count.getText().toString())
                .addMultipartParameter("bank_ac_no", accno.getText().toString())
                .addMultipartParameter("state", getSelectedState())
                .addMultipartParameter("district", getSelectedDistrict())
                .addMultipartParameter("block", getSelectedDistrict())
                .addMultipartParameter("village", getSelectedCity())
                .addMultipartParameter("fpo_name", fpo.getText().toString())
                .addMultipartParameter("registration_no", "")
                .addMultipartParameter("mobile_number", "")
                .addMultipartParameter("qualification_id", "")
                .addMultipartParameter("identity_type", "")
                .addMultipartParameter("identity_number", "")
                .addMultipartParameter("reffered_by", "")
                .addMultipartParameter("is_loan", "")
                .addMultipartParameter("subsidy_information", "")
                .addMultipartParameter("is_smart_user", "")
                .addMultipartParameter("forming_type", "")
                .addMultipartParameter("is_jobcard_holder", "")
                .addMultipartParameter("otp", "")
                .addMultipartParameter("land_line", "")
                .build().getAsJSONObject(new JSONObjectRequestListener() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        Userobject obj = new Userobject();

                        obj.setId(jsonObject.optString("data"));
                        obj.setFirstname(name.getText().toString());
                        obj.setPhno(email_phno.getText().toString());
                        obj.setEmail(email_phno.getText().toString());
                        obj.setFarmer_type(farmer_type.getSelectedItem().toString());
                        obj.setFarmer_type(farmer_type.getSelectedItem().toString());
                        obj.setLandloc(location.getText().toString());
                        obj.setBank_no(accno.getText().toString());
                        obj.setPincode(pincode.getText().toString());
                        obj.setVillage(getSelectedCity());
                        obj.setDistrict(getSelectedDistrict());
                        obj.setState(getSelectedState());
                        obj.setEmail(mail.getText().toString());
                        obj.setCity(getSelectedCity());
                        obj.setStreet_name(street_name.getText().toString());
                        obj.setNoofmember(faily_count.getText().toString());
                        obj.setAge(age.getText().toString());

                        if (gender != null && gender.getSelectedItem() != null) {
                            obj.setGender(gender.getSelectedItem().toString());
                        }

                        obj.setMother_name(mother_name.getText().toString());
                        obj.setIspremium("0");
                        sm.setUser(obj);

                        if (getSubscriptionManager().canLoad()) {
                            getSubscriptionManager().load(new SubscriptionManager.Action() {
                                @Override
                                public void onLoaded(boolean error) {
                                    if (error) {
                                        Toasty.error(Farmer_Registration.this, "Network error!").show();
                                    } else {
                                        loadModules();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(Farmer_Registration.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toasty.normal(Farmer_Registration.this, "Something went wrong! ").show();
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }//response

            @Override
            public void onError(ANError anError) {
                Toasty.normal(Farmer_Registration.this, "Something went wrong!" + anError.getErrorCode()).show();
                viewDialog.hideDialog();
            }
        });
    }//end registration

    public void loadModules(){
        if(getModuleManager().canLoad()){
            getModuleManager().load(new ModuleManager.LoaderAction() {
                @Override
                public void onLoaded(boolean error) {
                    if (error){
                        toast("Modules Load Error!");
                    }else {
                       goToDashboard();
                    }
                }
            });
        }else {
            //loadUserPrivileges();
            toast("No User");
        }
    }

    private void goToDashboard(){
        Intent i = new Intent(Farmer_Registration.this, Main_Dashboard.class);
        startActivity(i);
        finishAffinity();
    }

    public String getSelectedState(){
        if (stateSpinner.getSelectedItem() != null){
            return ((StateModel) stateSpinner.getSelectedItem()).getState();
        }
        return "";
    }

    public String getSelectedDistrict(){
        if (districtSpinner.getSelectedItem() != null){
            return ((DistrictModel) districtSpinner.getSelectedItem()).getDistrictName();
        }
        return "";
    }

    public String getSelectedCity(){
        if (citySpinner.getSelectedItem() != null){
            return ((TalukModel) citySpinner.getSelectedItem()).getTalukName();
        }
        return "";
    }

    private boolean validatesignForm() {

        /*if (!ValidateInputs.isValidNumber(email_phno.getText().toString().trim())) {
//            email_phno.setError(getString(R.string.enter_valid_email));
            Snackbar.make(findViewById(R.id.mainLay), R.string.enter_valid_mobile_number, Snackbar.LENGTH_LONG).show();
            return false;
        } else */
        if (!ValidateInputs.isValidPassword(password.getText().toString().trim())) {
            Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
            name.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }

    public void getStates(){
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.getStates)
       // AndroidNetworking.post("https://vrjaitraders.com/ard_farmx/api/Agripedia/get_states")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("State Reponse: \n" + response);
                viewDialog.hideDialog();
                if (response.optBoolean("status")){
                    String stateJsonArray = response.optString("data");
                    System.out.println(stateJsonArray);
                    stateList = GsonUtils.getGson()
                            .fromJson(stateJsonArray, EMarketStorage.stateListType);
                    updateStates();
                    //updateDistricts();
                    //updateCities();
                }
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                Toasty.error(Farmer_Registration.this, "State Load Error").show();
            }
        });
    }

    public void updateStates(){
        if (stateList != null){
            ArrayAdapter<String> stateAdapter = new ArrayAdapter(this, R.layout.spinner_item, stateList);
            stateSpinner.setAdapter(stateAdapter);

            stateSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            clearDistrict();
                            clearVillage();
                            getDistricts(stateList.get(i).getStateId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    }
            );
        }
    }

    private void clearDistrict(){
        if (districtList != null)
            districtList.clear();
        if (districtSpinner.getAdapter() != null)
            districtSpinner.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, districtList));
    }

    private void clearVillage(){
        if (villageList != null)
            villageList.clear();
        if (citySpinner.getAdapter() != null)
            citySpinner.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, villageList));
    }

    public void getDistricts(String stateId){
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.getDistrict)
                .addUrlEncodeFormBodyParameter("state_id", stateId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                viewDialog.hideDialog();
                if (response.optBoolean("status")){
                    String stateJsonArray = response.optString("data");
                    System.out.println(stateJsonArray);
                    districtList = GsonUtils.getGson()
                            .fromJson(stateJsonArray, EMarketStorage.districtListType);
                    updateDistricts();
                }
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                Toasty.error(Farmer_Registration.this, "District Load Error").show();
            }
        });
    }

    public void updateDistricts(){
        if (districtList != null){
            ArrayAdapter<String> districtAdapter = new ArrayAdapter(this, R.layout.spinner_item, districtList);
            districtSpinner.setAdapter(districtAdapter);

            districtSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            clearVillage();
                            getCities(districtList.get(i).getStateId(), districtList.get(i).getDistrictId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    }
            );
        }
    }

    private void getCities(String stateId, String districtId){
        viewDialog.showDialog();
        AndroidNetworking.post(Webservice.getVillages)
                .addUrlEncodeFormBodyParameter("state_id", stateId)
                .addUrlEncodeFormBodyParameter("district_id", districtId)
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        viewDialog.hideDialog();
                        if (response.optBoolean("status")){
                            villageList = GsonUtils.getGson()
                                    .fromJson(response.optJSONArray("data").toString(),
                                            EMarketStorage.villageListType);
                            updateCities();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        viewDialog.hideDialog();
                        Toasty.error(Farmer_Registration.this, "City Load Error!").show();
                    }
                }
        );
    }

    private void updateCities(){
        if (villageList != null){
            ArrayAdapter<String> villageAdapter = new ArrayAdapter(this, R.layout.spinner_item, villageList);
            citySpinner.setAdapter(villageAdapter);

            citySpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            //getCities(districtList.get(i).getStateId(), districtList.get(i).getDistrictId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    }
            );
        }
    }

    /*public void add_mypostold() {
        viewDialog.showDialog();
        block_data = new ArrayList<>();

        AndroidNetworking.get(Webservice.get_districttn)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        List<String> b_data = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Block_Model obj = new Block_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("district_id"));
                            obj.setBlock_name(jsonArray.getJSONObject(i).optString("district_name"));

                            block_data.add(obj);
                            b_data.add(jsonArray.getJSONObject(i).optString("district_name"));
                        }


                        *//*ArrayAdapter<String> block_adpter = new ArrayAdapter(Farmer_Registration.this, R.layout.spinner_item,
                                b_data);

                        district_m.setAdapter(block_adpter);*//*

                       *//* district_m.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                district_m.showDropDown();
                                return false;
                            }
                        });*//*

                        *//*district_m.setOnClickListener(v -> district_m.showDropDown());

                        district_m.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Object item = adapterView.getItemAtPosition(i);


                                get_village(block_data.get(i).getId());


                            }
                        });*//*


                    } else {

                    }

                    get_fpo();
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

    /*public void add_districtold() {
        viewDialog.showDialog();
        district_data = new ArrayList<>();

        AndroidNetworking.get(Webservice.get_district)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        List<String> b_data = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            District_Model obj = new District_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("dist_id"));
                            obj.setName(jsonArray.getJSONObject(i).optString("district_name"));
                            district_data.add(obj);
                            b_data.add(jsonArray.getJSONObject(i).optString("district_name"));
                        }


                        ArrayAdapter<String> district_adpter = new ArrayAdapter(Farmer_Registration.this, R.layout.spinner_item,
                                b_data);

                      *//*  district.setAdapter(district_adpter);
                        district.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                district.showDropDown();
                                return false;
                            }
                        });*//*

//                        district_m.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                Object item = adapterView.getItemAtPosition(i);
//
//
//
//                                get_village(block_data.get(i).getId());
//
//
//                            }
//                        });


                    } else {

                    }
                    //add_mypost();

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

    /*public void get_village(String block_id) {
        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.get_villagetn)
                .addUrlEncodeFormBodyParameter("block_id", block_id)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();


                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        List<String> b_data = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            b_data.add(jsonArray.getJSONObject(i).optString("village_name"));
                        }


                        ArrayAdapter<String> block_adpter = new ArrayAdapter(Farmer_Registration.this, R.layout.spinner_item,
                                b_data);

                        //taluk.setAdapter(block_adpter);

                      *//*  taluk.setOnTouchListener((v, event) -> {
                            if(block_adpter.isEmpty()){
                                Log.i(this.getClass().getSimpleName(), "Has Focus");
                                Toast.makeText(Farmer_Registration.this, R.string.select_district, Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(R.id.mainLay), R.string.select_district, Snackbar.LENGTH_LONG).show();
                            }else {
                                taluk.showDropDown();
                            }
                            return false;
                        });*//*


//                        taluk.setOnFocusChangeListener((v, hasFocus) -> {
//                            if (hasFocus){
//                                if(block_adpter.isEmpty()){
//                                    Log.i(this.getClass().getSimpleName(), "Has Focus");
//                                    Toast.makeText(Farmer_Registration.this, R.string.select_district, Toast.LENGTH_LONG).show();
//                                    Snackbar.make(findViewById(R.id.mainLay), R.string.select_district, Snackbar.LENGTH_LONG).show();
//                                }else {
//                                    taluk.showDropDown();
//                                }
//                            }
//                        });
//
//                        taluk.setOnClickListener(v -> {
//
//                        });


                    } else {

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

    /*public void get_fpo() {
        viewDialog.showDialog();

        AndroidNetworking.get(Webservice.get_fpolist)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        List<String> b_data = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            b_data.add(jsonArray.getJSONObject(i).optString("fpo_name"));
                        }


                        ArrayAdapter<String> block_adpter = new ArrayAdapter(Farmer_Registration.this, R.layout.spinner_item,
                                b_data);

                        fpo.setAdapter(block_adpter);
                        fpo.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                fpo.showDropDown();
                                return false;
                            }
                        });


                    } else {

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

}
