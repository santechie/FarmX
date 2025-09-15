package com.ascentya.AsgriV2.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
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
import com.ascentya.AsgriV2.login_activities.Formx_Login_Activity;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MyProfile extends AppCompatActivity {
    Button register, Login, register_others;
    Spinner farmer_type, gender;
    EditText email_phno, accno, faily_count, mail, pincode, mother_name, name, age, street_name, city, location;
    CircleImageView kissan_card;
    CircleImageView aadhar_card;
    ViewDialog viewDialog;
    String[] country = {"Individual", "FPO", "Co-Operative"};
    String[] gender_list = {"Male", "Female", "Others"};
    SessionManager sm;
    String aadhar_path, kissan_path;
    NoDefaultSpinner state, district_m, taluk;
    AutoCompleteTextView district, fpo;
    List<Block_Model> block_data;
    List<District_Model> district_data;
    LinearLayout fpo_layout;

    List<StateModel> stateList = new ArrayList<>();
    List<DistrictModel> districtList = new ArrayList<>();
    List<TalukModel> villageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        register = findViewById(R.id.register);
        accno = findViewById(R.id.accno);
        faily_count = findViewById(R.id.faily_count);
        fpo = findViewById(R.id.fpo);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);
        mother_name = findViewById(R.id.mother_name);
        mail = findViewById(R.id.mail);
        fpo_layout = findViewById(R.id.fpo_layout);
        email_phno = findViewById(R.id.email_phno);
        register_others = findViewById(R.id.register_others);
        Login = findViewById(R.id.Login);
        taluk = findViewById(R.id.taluk);
        fpo_layout = findViewById(R.id.fpo_layout);
        district_m = findViewById(R.id.district_m);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MyProfile.this, Formx_Login_Activity.class);
                startActivity(i);
                finish();
            }
        });

        register_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyProfile.this, Farmx_Service.class);
                startActivity(i);
            }
        });

        name = findViewById(R.id.name);
        sm = new SessionManager(this);
        farmer_type = findViewById(R.id.farmer_type);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        district = findViewById(R.id.district);
        street_name = findViewById(R.id.street_name);
//        city = findViewById(R.id.city);
        location = findViewById(R.id.location);
        aadhar_card = findViewById(R.id.aadhar_card);
        kissan_card = findViewById(R.id.kisssan_card);
        viewDialog = new ViewDialog(MyProfile.this);


        location.setText(sm.getUser().getLandloc());
        accno.setText(sm.getUser().getBank_no());
        age.setText(sm.getUser().getAge());
        email_phno.setText(sm.getUser().getPhno());
        name.setText(sm.getUser().getFirstname());
        mother_name.setText(sm.getUser().getMother_name());
        faily_count.setText(sm.getUser().getNoofmember());
        street_name.setText(sm.getUser().getStreet_name());
//        city.setText(sm.getUser().getCity());
        mail.setText(sm.getUser().getEmail());
        //state.setText(sm.getUser().getState());
        //taluk.setText(sm.getUser().getVillage());
        pincode.setText(sm.getUser().getPincode());

        add_district();

        ArrayAdapter gender_adapter = new ArrayAdapter(this, R.layout.spinner_row, gender_list);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gender_adapter);


        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_row, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        farmer_type.setAdapter(aa);

        if (sm.getUser().getFarmer_type() != null) {
            int spinnerPosition = aa.getPosition(sm.getUser().getFarmer_type());
            farmer_type.setSelection(spinnerPosition);
        }

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

        if (sm.getUser().getGender() != null) {
            int spinnerPosition = gender_adapter.getPosition(sm.getUser().getGender());
            gender.setSelection(spinnerPosition);
        }
        aadhar_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        aadhar_path =
                                ImageUtils.setImage(MyProfile.this, r, aadhar_card, true);
                    }
                }).show(MyProfile.this);

            }
        });
        kissan_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        kissan_path = ImageUtils
                                .setImage(MyProfile.this, r, kissan_card, true);
                    }
                }).show(MyProfile.this);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {

                    if (mail.getText().toString().length() > 0) {
                        if (!ValidateInputs.isValidEmail(mail.getText().toString().trim())) {
                            mail.setError(getString(R.string.required_date));

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

        loadStates();

    }

    private void loadStates(){
        AndroidNetworking.post(Webservice.getStates)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("State Reponse: \n" + response);
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
                Toasty.error(MyProfile.this, "State Load Error").show();
            }
        });
    }

    public void updateStates(){
        if (stateList != null){
            ArrayAdapter<String> stateAdapter = new ArrayAdapter(this, R.layout.spinner_row_space, stateList);
            state.setAdapter(stateAdapter);

            state.setOnItemSelectedListener(
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

            String savedState = sm.getUser().getState();

            for(int i=0; i<stateList.size(); i++){
                StateModel stateModel = stateList.get(i);
                if (stateModel.getState().equals(savedState)){
                    state.setSelection(i);
                }
            }
        }
    }

    public String getSelectedState(){
        if (state.getSelectedItem() != null){
            return ((StateModel) state.getSelectedItem()).getState();
        }
        return "";
    }

    public void getDistricts(String stateId){
        AndroidNetworking.post(Webservice.getDistrict)
                .addUrlEncodeFormBodyParameter("state_id", stateId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
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
                Toasty.error(MyProfile.this, "District Load Error").show();
            }
        });
    }

    public void updateDistricts(){
        if (districtList != null){
            ArrayAdapter<String> districtAdapter = new ArrayAdapter(this, R.layout.spinner_row_space, districtList);
            district_m.setAdapter(districtAdapter);

            district_m.setOnItemSelectedListener(
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

            String selectedDistrict = sm.getUser().getDistrict();

            for (int i=0; i<districtList.size(); i++){
                DistrictModel districtModel = districtList.get(i);
                if (selectedDistrict.equals(districtModel.getDistrictName())){
                    district_m.setSelection(i);
                }
            }
        }
    }

    public String getSelectedDistrict(){
        if (district_m.getSelectedItem() != null){
            return ((DistrictModel) district_m.getSelectedItem()).getDistrictName();
        }
        return "";
    }

    private void getCities(String stateId, String districtId){
        AndroidNetworking.post(Webservice.getVillages)
                .addUrlEncodeFormBodyParameter("state_id", stateId)
                .addUrlEncodeFormBodyParameter("district_id", districtId)
                .build().getAsJSONObject(
                new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.optBoolean("status")){
                            villageList = GsonUtils.getGson()
                                    .fromJson(response.optJSONArray("data").toString(),
                                            EMarketStorage.villageListType);
                            updateCities();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(MyProfile.this, "Village Load Error!").show();
                    }
                }
        );
    }

    private void updateCities(){
        if (villageList != null){
            ArrayAdapter<String> villageAdapter = new ArrayAdapter(this, R.layout.spinner_row_space, villageList);
            taluk.setAdapter(villageAdapter);

            taluk.setOnItemSelectedListener(
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

            String selectedVillage = sm.getUser().getVillage();

            for (int i=0; i<villageList.size(); i++){
                if (villageList.get(i).getTalukName().equals(selectedVillage)){
                    taluk.setSelection(i);
                }
            }
        }
    }

    public String getSelectedVillage(){
        if (taluk.getSelectedItem() != null){
            return ((TalukModel) taluk.getSelectedItem()).getTalukName();
        }
        return "";
    }

    private void clearDistrict(){
        if (districtList != null)
            districtList.clear();
        if (district_m.getAdapter() != null)
            district_m.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, districtList));
    }

    private void clearVillage(){
        if (villageList != null)
            villageList.clear();
        if (taluk.getAdapter() != null)
            taluk.setAdapter(new ArrayAdapter(this, R.layout.spinner_item, villageList));
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
        AndroidNetworking.upload(Webservice.edit_farmer)
                .addMultipartParameter("user_id", sm.getUser().getId())
                .addMultipartParameter("username", name.getText().toString())
                .addMultipartParameter("email", email_phno.getText().toString())
                .addMultipartParameter("phone", email_phno.getText().toString())
                .addMultipartParameter("farmer_name", name.getText().toString())
                .addMultipartParameter("farmer_email", mail.getText().toString())
                .addMultipartParameter("farming_type", farmer_type.getSelectedItem().toString())
                .addMultipartParameter("farmer_age", age.getText().toString())
//                .addUrlEncodeFormBodyParameter("farmer_age", age.getText().toString())
                .addMultipartParameter("farmer_street", street_name.getText().toString())
//                .addMultipartParameter("farmer_city", city.getText().toString())
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
                .addMultipartParameter("block", district.getText().toString())
                .addMultipartParameter("village", getSelectedVillage())
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
                        obj.setId(sm.getUser().getId());
                        obj.setFirstname(name.getText().toString());
                        obj.setPhno(email_phno.getText().toString());
                        obj.setEmail(email_phno.getText().toString());
                        obj.setFarmer_type(farmer_type.getSelectedItem().toString());
                        obj.setFarmer_type(farmer_type.getSelectedItem().toString());
                        obj.setLandloc(location.getText().toString());
                        obj.setBank_no(accno.getText().toString());
                        obj.setPincode(pincode.getText().toString());
                        obj.setVillage(getSelectedVillage());
                        obj.setDistrict(district.getText().toString());
                        obj.setState(getSelectedState());
                        obj.setEmail(mail.getText().toString());
//                        obj.setCity(city.getText().toString());
                        obj.setStreet_name(street_name.getText().toString());
                        obj.setNoofmember(faily_count.getText().toString());
                        obj.setAge(age.getText().toString());

                        if (gender != null && gender.getSelectedItem() != null) {
                            obj.setGender(gender.getSelectedItem().toString());
                        }


                        obj.setMother_name(mother_name.getText().toString());
                        obj.setIspremium("0");
                        sm.setUser(obj);

                        Intent i = new Intent(MyProfile.this, Main_Dashboard.class);
                        i.putExtra("location", true);
                        startActivity(i);


                    } else {
                        Toast.makeText(MyProfile.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//

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


    private boolean validatesignForm() {
        if (!ValidateInputs.isValidNumber(email_phno.getText().toString().trim())) {
            email_phno.setError(getString(R.string.required_date));
            return false;
        } else if (!ValidateInputs.isValidInput(name.getText().toString().trim())) {
            name.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }

    public void add_mypost() {
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


                        ArrayAdapter<String> block_adpter = new ArrayAdapter(MyProfile.this, R.layout.spinner_item,
                                b_data);

                        /*district_m.setAdapter(block_adpter);

                        district_m.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                district_m.showDropDown();
                                return false;
                            }
                        });

                        district_m.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Object item = adapterView.getItemAtPosition(i);


                                get_village(block_data.get(i).getId());


                            }
                        });*/


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
    }

    public void add_district() {
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

                        ArrayAdapter<String> district_adpter = new ArrayAdapter(MyProfile.this, R.layout.spinner_item,
                                b_data);

                        district.setAdapter(district_adpter);
                        district.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                district.showDropDown();
                                return false;
                            }
                        });

                        district.setText(sm.getUser().getDistrict());


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
                    add_mypost();

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

    public void get_village(String block_id) {
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


                        ArrayAdapter<String> block_adpter = new ArrayAdapter(MyProfile.this, R.layout.spinner_item,
                                b_data);

                        taluk.setAdapter(block_adpter);

                        taluk.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                //taluk.showDropDown();
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
    }

    public void get_fpo() {
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


                        ArrayAdapter<String> block_adpter = new ArrayAdapter(MyProfile.this, R.layout.spinner_item,
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
    }

}
