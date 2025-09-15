package com.ascentya.AsgriV2.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.ActivityImageAdapter;
import com.ascentya.AsgriV2.Adapters.Memberamount_Adapter;
import com.ascentya.AsgriV2.Adapters.Others_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.ContractUpdate;
import com.ascentya.AsgriV2.Interfaces_Class.add_others;
import com.ascentya.AsgriV2.Interfaces_Class.add_resource;
import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.Models.AddMemberamount_Model;
import com.ascentya.AsgriV2.Models.ImageModel;
import com.ascentya.AsgriV2.Models.Members_Model;
import com.ascentya.AsgriV2.Models.others_model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Add_resource_Dialog;
import com.ascentya.AsgriV2.Utils.Addothers_Dialog;
import com.ascentya.AsgriV2.Utils.DIalogwith_Image;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ImageUtils;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.google.gson.Gson;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Edit_Activity extends BaseActivity implements ActivityImageAdapter.Action, ContractUpdate {
    Button addmember, addmaterial, register;
    Memberamount_Adapter member_adapter;
    Others_Adapter other_adapter;
    List<AddMemberamount_Model> addmemberamount;
    List<others_model> addmachineamount;
    List<Members_Model> member_data;
    RecyclerView member_recycler, material_recycler;
    List<String> activity_type;
    LinearLayout header, header_machine, member_layout, material_layout, machine_layout, contract_layout;
    RadioGroup contract_method, activity_method, machinary_method;
    EditText vendor_name, cost;
    NoDefaultSpinner Type_activity;
    View vendor_view;
    EditText date, enddate;
    Calendar cal;
    SessionManager sm;
    String contract_selected = "", sowed_by = "", machine_selected = "", crop_id, land_id, lc_id, cat_id;
    ViewDialog viewDialog;
    LinearLayout back;
    Activity_Cat_Model Data;

    CheckBox completedCb;

    RecyclerView imagesRv;
    ActivityImageAdapter imageAdapter;
    ArrayList<ImageModel> imageModels = new ArrayList<>();
    ArrayList<FileImageItem> fileModels = new ArrayList<>();
    View addImageBtn;

    TextView contractCostTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_helper);

        addmember = findViewById(R.id.addmember);
        back = findViewById(R.id.back);
        register = findViewById(R.id.register);
        enddate = findViewById(R.id.enddate);
        viewDialog = new ViewDialog(this);
        cost = findViewById(R.id.cost);
        date = findViewById(R.id.date);
        cal = Calendar.getInstance();
        sm = new SessionManager(this);
        addImageBtn = findViewById(R.id.addImage);


        crop_id = getIntent().getStringExtra("crop");
        land_id = getIntent().getStringExtra("land");
        lc_id = getIntent().getStringExtra("lc_id");
        cat_id = getIntent().getStringExtra("cat");
        Data = getIntent().getExtras().getParcelable("act");

        System.out.println("Parcellable Item: " + GsonUtils.getGson().toJson(Data));
        System.out.println("Parcellable Item Crop Id: " + Data.getCrop_id());

        vendor_view = findViewById(R.id.vendor_view);
        Type_activity = findViewById(R.id.Type_activity);
        vendor_name = findViewById(R.id.vendor_name);
        machinary_method = findViewById(R.id.machinary_method);
        activity_method = findViewById(R.id.activity_method);
        machine_layout = findViewById(R.id.machine_layout);
        contract_layout = findViewById(R.id.contract_layout);
        material_layout = findViewById(R.id.material_layout);
        member_layout = findViewById(R.id.member_layout);
        header = findViewById(R.id.header);
        contract_method = findViewById(R.id.contract_method);
        header_machine = findViewById(R.id.header_others);
        addmaterial = findViewById(R.id.addmaterial);
        member_recycler = findViewById(R.id.member_recycler);
        material_recycler = findViewById(R.id.others_recycler);
        imagesRv = findViewById(R.id.imagesRv);
        completedCb = findViewById(R.id.completedCb);
        contractCostTv = findViewById(R.id.contractCost);

        completedCb.setChecked(Data.getStatus().equals(Constants.ActivityStatus.COMPLETED));

        addmemberamount = new ArrayList<>();
        member_data = new ArrayList<>();
        back.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });

        addmachineamount = new ArrayList<>();

        member_recycler.setLayoutManager(new LinearLayoutManager(this));
        material_recycler.setLayoutManager(new LinearLayoutManager(this));
        member_recycler.setHasFixedSize(true);
        material_recycler.setHasFixedSize(true);

        imageAdapter = new ActivityImageAdapter(this);
        imagesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesRv.setAdapter(imageAdapter);

        register.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                if (validatesignForm()) {
                    add_animal();
                }

            }
        });


        get_cat();

        getmembers();


//        contract_method.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        date.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(Edit_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                        date.setError(null);
                        date.setText(String.format("%d", dayOfMonth) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", year));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        enddate.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(Edit_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
                        enddate.setError(null);
                        enddate.setText(String.format("%d", dayOfMonth) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", year));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date start_date = null;
                try {
                    start_date = sdf.parse(date.getText().toString());
                    dpd.getDatePicker().setMinDate(start_date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dpd.show();
            }
        });


        machinary_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                machine_selected = radioButton.getText().toString();
                if (radioButton.getText().toString().equalsIgnoreCase("Self")) {
                    vendor_name.setVisibility(View.GONE);
                    vendor_view.setVisibility(View.GONE);

                } else if (radioButton.getText().toString().equalsIgnoreCase("Rental")) {
                    vendor_name.setVisibility(View.VISIBLE);
                    vendor_view.setVisibility(View.VISIBLE);
                }
            }
        });
        contract_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if (radioButton.getText().toString().equalsIgnoreCase("Labour")) {

                    member_layout.setVisibility(View.VISIBLE);
                    material_layout.setVisibility(View.GONE);
                } else if (radioButton.getText().toString().equalsIgnoreCase("Material")) {

                    member_layout.setVisibility(View.GONE);
                    material_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        activity_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                if (radioButton.getText().toString().equalsIgnoreCase("Self")) {

                    machine_layout.setVisibility(View.GONE);
                    contract_layout.setVisibility(View.GONE);
                } else if (radioButton.getText().toString().equalsIgnoreCase("Machinery")) {

                    machine_layout.setVisibility(View.VISIBLE);
                    contract_layout.setVisibility(View.GONE);
                } else if (radioButton.getText().toString().equalsIgnoreCase("Contract")) {

                    machine_layout.setVisibility(View.GONE);
                    contract_layout.setVisibility(View.VISIBLE);
                }


            }
        });

        addmaterial.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {


                Addothers_Dialog obj = new Addothers_Dialog();
                obj.dialog(Edit_Activity.this, "Add Material", new add_others() {
                    @Override
                    public void crop_suggest(others_model name) {

                        addmachineamount.add(name);
                        updateContractCost();
                        header_machine.setVisibility(View.VISIBLE);
                        other_adapter = new Others_Adapter(Edit_Activity.this, addmachineamount, false);
                        material_recycler.setAdapter(other_adapter);
                        other_adapter.setContractUpdate(Edit_Activity.this);

                    }
                });


            }
        });

        addmember.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                Add_resource_Dialog obj = new Add_resource_Dialog();
                obj.dialog(Edit_Activity.this, member_data, sm.getUser().getId(), "member", new add_resource() {
                    @Override
                    public void crop_suggest(AddMemberamount_Model name) {


                        addmemberamount.add(name);
                        updateContractCost();
                        header.setVisibility(View.VISIBLE);
                        member_adapter = new Memberamount_Adapter(Edit_Activity.this, addmemberamount, false);
                        member_recycler.setAdapter(member_adapter);
                        member_adapter.setContractUpdate(Edit_Activity.this);
                    }
                });
            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        enddate.setText(Data.getEnd_date());
        date.setText(Data.getStart_date());

        if (Data.getSowing_by().equalsIgnoreCase("Contract")) {
            activity_method.check(R.id.contract);
        } else if (Data.getSowing_by().equalsIgnoreCase("Machinery")) {
            activity_method.check(R.id.machine);
        } else {
            activity_method.check(R.id.self);

        }
        cost.setText(Data.getTotal_amount());
        vendor_name.setText(Data.getVendor_name());


        if (Data.getEquipment_type().equalsIgnoreCase("Rental")) {
            machinary_method.check(R.id.rental);
        } else {
            machinary_method.check(R.id.self_machine);
        }

        if (Data.getContract_type().equalsIgnoreCase("Labour")) {
            contract_method.check(R.id.labour);

            try {
                JSONArray jsonArray = new JSONArray(Data.getMember_id());

                for (int i = 0; i < jsonArray.length(); i++) {
                    AddMemberamount_Model obj = new AddMemberamount_Model();
                    obj.setName(jsonArray.getJSONObject(i).optString("name"));
                    obj.setBillingtype(jsonArray.getJSONObject(i).optString("billingtype"));
                    obj.setAmount(jsonArray.getJSONObject(i).optString("amount"));
                    obj.setHours(jsonArray.getJSONObject(i).optString("hours"));
                    addmemberamount.add(obj);
                }

                member_adapter = new Memberamount_Adapter(Edit_Activity.this, addmemberamount, false);
                member_recycler.setAdapter(member_adapter);
                member_adapter.setContractUpdate(Edit_Activity.this);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (Data.getContract_type().equalsIgnoreCase("Material")) {
            contract_method.check(R.id.material);

            try {


                JSONArray jsonArray_material = new JSONArray(Data.getMaterial_name());

                for (int i = 0; i < jsonArray_material.length(); i++) {
                    others_model obj = new others_model();
                    obj.setTitle(jsonArray_material.getJSONObject(i).optString("title"));
                    obj.setAmount(jsonArray_material.getJSONObject(i).optString("amount"));

                    addmachineamount.add(obj);
                }

                other_adapter = new Others_Adapter(Edit_Activity.this, addmachineamount, false);
                material_recycler.setAdapter(other_adapter);
                other_adapter.setContractUpdate(Edit_Activity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        if (Data.getSowing_by().equalsIgnoreCase("Contract")){
            try {

                addmemberamount.clear();
                JSONArray jsonArray = new JSONArray(Data.getMember_id());

                for (int i = 0; i < jsonArray.length(); i++) {
                    AddMemberamount_Model obj = new AddMemberamount_Model();
                    obj.setName(jsonArray.getJSONObject(i).optString("name"));
                    obj.setBillingtype(jsonArray.getJSONObject(i).optString("billingtype"));
                    obj.setAmount(jsonArray.getJSONObject(i).optString("amount"));
                    obj.setHours(jsonArray.getJSONObject(i).optString("hours"));
                    addmemberamount.add(obj);
                }

                member_adapter = new Memberamount_Adapter(Edit_Activity.this, addmemberamount, false);
                member_recycler.setAdapter(member_adapter);
                member_adapter.setContractUpdate(Edit_Activity.this);

                addmachineamount.clear();
                JSONArray jsonArray_material = new JSONArray(Data.getMaterial_name());
                for (int i = 0; i < jsonArray_material.length(); i++) {
                    others_model obj = new others_model();
                    obj.setTitle(jsonArray_material.getJSONObject(i).optString("title"));
                    obj.setAmount(jsonArray_material.getJSONObject(i).optString("amount"));
                    addmachineamount.add(obj);
                }
                other_adapter = new Others_Adapter(Edit_Activity.this, addmachineamount, false);
                material_recycler.setAdapter(other_adapter);
                other_adapter.setContractUpdate(Edit_Activity.this);

                updateCost();
                updateContractCost();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        loadImages();
    }

    private void updateCost(){
        try {
            int total = Integer.parseInt(Data.getTotal_amount());
            for (AddMemberamount_Model member: addmemberamount){
                total -= Integer.valueOf(member.getAmount());
            }
            for (others_model other: addmachineamount){
                total -= Integer.valueOf(other.getAmount());
            }
            cost.setText(String.valueOf(total));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateContractCost(){
        int contractTotal = 0;
        try {

            for (others_model other: addmachineamount){
                contractTotal += Integer.parseInt(other.getAmount());
            }

            for (AddMemberamount_Model member: addmemberamount){
                contractTotal += Integer.parseInt(member.getAmount());
            }

            contractCostTv.setText(String.valueOf(contractTotal));
        }catch (Exception e){ e.printStackTrace(); }
    }

    private void addImage(){
        PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
            @Override
            public void onPickResult(PickResult result) {
                String imagePath = ImageUtils
                        .setImage(Edit_Activity.this, result, null, true);
                toast("Image: " + imagePath);

                ImageModel imageModel = new ImageModel();
                imageModel.setPath(imagePath);
                imageModel.setNew(true);
                imageModels.add(imageModel);

                update();
            }
        }).show(this);
    }

    private void loadImages(){
        if (Data != null){
            try {
                Data.prepareImages(new JSONArray(Data.getImagesString()));
                imageModels.addAll(Data.getImages());
                update();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void getmembers() {
        member_data = new ArrayList<>();
        AndroidNetworking.get(Webservice.getmemberlist + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Members_Model obj = new Members_Model();
                            obj.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                            obj.setMember_name(jsonArray.getJSONObject(i).optString("member_name"));
                            obj.setMember_age(jsonArray.getJSONObject(i).optString("member_age"));
                            obj.setMember_gender(jsonArray.getJSONObject(i).optString("member_gender"));
                            obj.setFarming_exp(jsonArray.getJSONObject(i).optString("farming_experience"));
                            obj.setRelation(jsonArray.getJSONObject(i).optString("member_relation"));
                            member_data.add(obj);
                        }


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

    public void add_animal() {


        viewDialog.showDialog();
        String days_diff;
        if (date.getText().toString().length() > 0 && enddate.getText().toString().length() > 0) {
            days_diff = dates(date.getText().toString(), enddate.getText().toString());
        } else {

            days_diff = "";
        }


        Gson gson = new Gson();
        String machine_array = gson.toJson(addmachineamount);
        String member_array = gson.toJson(addmemberamount);


        int selectedId = contract_method.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) findViewById(selectedId);


        int selected_activityId = activity_method.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton activity_radioButton = (RadioButton) findViewById(selected_activityId);
        sowed_by = activity_radioButton.getText().toString();

        int total = 0;

        try {
            total = Integer.parseInt(cost.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        if (sowed_by.equalsIgnoreCase("contract")) {
            contract_selected = radioButton.getText().toString();
            try {
                total += Integer.parseInt(contractCostTv.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            contract_selected = "";
        }


        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(Webservice.up_activity);

        request.addMultipartParameter("sowing_id", Data.getFence_id());
        request.addMultipartParameter("user_id", sm.getUser().getId());
        request.addMultipartParameter("crop_id", Data.getCrop_id());
        request.addMultipartParameter("land_id", land_id);
        request.addMultipartParameter("lc_id", lc_id);
        request.addMultipartParameter("start_date", date.getText().toString());
        request.addMultipartParameter("end_date", enddate.getText().toString());
        request.addMultipartParameter("no_of_days", days_diff);
        request.addMultipartParameter("no_of_person", "");
        request.addMultipartParameter("equip_name", "");
        request.addMultipartParameter("equip_type", machine_selected);
        request.addMultipartParameter("fertilize_by", "");
        request.addMultipartParameter("vendor_name", vendor_name.getText().toString());
        request.addMultipartParameter("contract_type", contract_selected);
        request.addMultipartParameter("material_name", machine_array);
        request.addMultipartParameter("member_id", member_array);
        request.addMultipartParameter("total_amt", String.valueOf(total));
        request.addMultipartParameter("prepare_type", Type_activity.getSelectedItem().toString());
        request.addMultipartParameter("sowing_by", sowed_by);
        request.addMultipartParameter("service_id", cat_id);
        request.addMultipartParameter("crop_type", getIntent().getStringExtra("crop_type"));
        request.addMultipartParameter("status", completedCb.isChecked() ?
                Constants.ActivityStatus.COMPLETED : Constants.ActivityStatus.NOT_COMPLETED);

        for (String image: getNewImages()){
            request.addMultipartFile("images[]", new File(image));
        }

//        toast(String.valueOf(total));

        request.build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        sendBroadcast(Constants.Broadcasts.ACTIVITY_UPDATE);
//                        count.setText("");
//                        animal.setError(null);
                        Toasty.success(Edit_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        finish();
                    } else {
                        Toasty.error(Edit_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                //sendBroadcast(Constants.Broadcasts.ACTIVITY_UPDATE);
            }
        });

        /*AndroidNetworking.post(Webservice.up_activity)
                .addUrlEncodeFormBodyParameter("sowing_id", Data.getFence_id())
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("crop_id", Data.getCrop_id())
                .addUrlEncodeFormBodyParameter("land_id", land_id)
                .addUrlEncodeFormBodyParameter("lc_id", lc_id)
                .addUrlEncodeFormBodyParameter("start_date", date.getText().toString())
                .addUrlEncodeFormBodyParameter("end_date", enddate.getText().toString())
                .addUrlEncodeFormBodyParameter("no_of_days", days_diff)
                .addUrlEncodeFormBodyParameter("no_of_person", "")
                .addUrlEncodeFormBodyParameter("equip_name", "")
                .addUrlEncodeFormBodyParameter("equip_type", machine_selected)
                .addUrlEncodeFormBodyParameter("fertilize_by", "")
                .addUrlEncodeFormBodyParameter("vendor_name", vendor_name.getText().toString())
                .addUrlEncodeFormBodyParameter("contract_type", contract_selected)
                .addUrlEncodeFormBodyParameter("material_name", machine_array)
                .addUrlEncodeFormBodyParameter("member_id", member_array)
                .addUrlEncodeFormBodyParameter("total_amt", cost.getText().toString())
                .addUrlEncodeFormBodyParameter("prepare_type", Type_activity.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("sowing_by", sowed_by)
                .addUrlEncodeFormBodyParameter("service_id", cat_id)
                .addUrlEncodeFormBodyParameter("crop_type", getIntent().getStringExtra("crop_type"))
//        Type_activity.getSelectedItem().toString()
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        sendBroadcast(Constants.Broadcasts.ACTIVITY_UPDATE);
//                        count.setText("");
//                        animal.setError(null);

                        Toasty.success(Edit_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        finish();
                    } else {
                        Toasty.error(Edit_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                //sendBroadcast(Constants.Broadcasts.ACTIVITY_UPDATE);

            }
        });*/


    }

    private ArrayList<String> getNewImages(){
        ArrayList<String> images = new ArrayList<>();
        for (ImageModel image: imageModels){
            if (image.getNew()){
                images.add(image.getImage());
            }
        }
        return images;
    }

    public void get_cat() {
        viewDialog.showDialog();


        activity_type = new ArrayList<>();
        AndroidNetworking.post(Webservice.landservicecat)

                .addUrlEncodeFormBodyParameter("service_id", cat_id)
//        Type_activity.getSelectedItem().toString()
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();


                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {


                            activity_type.add(jsonArray.getJSONObject(i).optString("type_name"));

                        }

                    } else {
                        Toasty.error(Edit_Activity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


                    }

                    ArrayAdapter<String> soiltype_adpter = new ArrayAdapter(Edit_Activity.this, R.layout.spinner_item,
                            activity_type);

                    Type_activity.setAdapter(soiltype_adpter);


                    Type_activity.setSelection(activity_type.indexOf(Data.getPrepare_type()));


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


    private boolean validatesignForm() {
        if (!(Type_activity != null && Type_activity.getSelectedItem() != null)) {
            Toast.makeText(this, "Kindly select Activity type", Toast.LENGTH_SHORT).show();
            return false;
//        } else if (!(soiltype != null && soiltype.getSelectedItem() != null)) {
//            Toast.makeText(this, "Kindly select Soil type", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (main_c.equals("")) {
//            maincrop.setError(getString(R.string.required_date));
//            return false;
        } else {
            return true;
        }
    }

    public String dates(String start, String end) {

        String diff_days = "";
        Date start_date, end_date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            start_date = sdf.parse(start);
            end_date = sdf.parse(end);


            long diff = end_date.getTime() - start_date.getTime();
            diff_days = String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return diff_days;
    }

    @Override
    public ArrayList<ImageModel> getImages() {
        return imageModels;
    }

    @Override
    public void update() {
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClicked(int position) {
        /*String selectedImage = getImages().get(position).getImage();
        ArrayList<String> images = new ArrayList<>();
        List<String> newImages = getImageList();
        boolean isLast = newImages.indexOf(selectedImage) == newImages.size() - 1;
        boolean isFirst = newImages.indexOf(selectedImage) == 0;
        if (isFirst){
            images.addAll(newImages);
            System.out.println("First: " + images);
        }else if (isLast){
            images.add(selectedImage);
            images.addAll(newImages.subList(0, images.indexOf(selectedImage)));
            System.out.println("Last: " + images);
        }else {
            images.add(selectedImage);
            images.addAll(newImages.subList(newImages.indexOf(selectedImage)+1, newImages.size()-1));
            images.addAll(newImages.subList(0, newImages.indexOf(selectedImage)-1));
            System.out.println("Middle: " + images);
        }*/
        DIalogwith_Image.showImageViewer(this, "Images", getImageList());
    }

    @Override
    public void onDelete(int position) {
        imageModels.remove(position);
        update();
    }

    @Override
    public boolean showDelete(int position) {
        return position < imageModels.size() && imageModels.get(position).getNew();
    }

    private List<String> getImageList(){
        ArrayList<String> images = new ArrayList<>();
        if (Data != null) {
            for (ImageModel image : Data.getImages()) {
                images.add(image.getImage());
            }
        }
        return images;
    }

    @Override
    public void onContractUpdate() {
        updateContractCost();
    }
}