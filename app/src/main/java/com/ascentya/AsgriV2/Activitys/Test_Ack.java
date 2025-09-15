package com.ascentya.AsgriV2.Activitys;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Memberamount_Adapter;
import com.ascentya.AsgriV2.Adapters.Others_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.add_others;
import com.ascentya.AsgriV2.Models.AddMemberamount_Model;
import com.ascentya.AsgriV2.Models.Income_Model;
import com.ascentya.AsgriV2.Models.Mycrops_List_Model;
import com.ascentya.AsgriV2.Models.others_model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Add_resource_Dialog;
import com.ascentya.AsgriV2.Utils.Addothers_Dialog;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class Test_Ack extends AppCompatActivity {
    ViewDialog viewDialog;
    List<Mycrops_List_Model> Data;
    SessionManager sm;

    AppCompatAutoCompleteTextView land_name, crop, select_activity, resources, lastchemaical;


    List<String> landdata, cropdata, activitydata, member_data, animal_data, others_data;

    ArrayList<MultiSelectModel> resourcedata;

    LinearLayout member, header_animal, header_machine, header_others;
    ElasticFloatingActionButton addmember, add_machine, add_animal, add_miscellaneous;

    RecyclerView member_recycler, machine_recycler, animal_recycler, others_recycler;
    Memberamount_Adapter member_adapter;
    Others_Adapter other_adapter;

    List<AddMemberamount_Model> addmemberamount, addanimalamount, addmachineamount;
    List<others_model> addothermodel;
    LinearLayout headeer;
    TextInputEditText start_date, end_date;
    ElasticButton addactivity;
    Calendar cal;
    ArrayList<Integer> selected_res;
    Boolean completed_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editactivity_layout);
        Bundle data = getIntent().getExtras();
        final Income_Model activity = (Income_Model) data.getParcelable("activity");


        sm = new SessionManager(this);
        cal = Calendar.getInstance();
        selected_res = new ArrayList<>();

        viewDialog = new ViewDialog(this);
        addactivity = findViewById(R.id.addactivity);
        resources = findViewById(R.id.resources);
        land_name = findViewById(R.id.land_name);
        end_date = findViewById(R.id.end_date);
        start_date = findViewById(R.id.start_date);
        header_animal = findViewById(R.id.header_animal);
        header_machine = findViewById(R.id.header_machine);
        machine_recycler = findViewById(R.id.machine_recycler);
        animal_recycler = findViewById(R.id.animal_recycler);
        crop = findViewById(R.id.crop);
        others_recycler = findViewById(R.id.others_recycler);
        member_recycler = findViewById(R.id.member_recycler);
        member_recycler.setLayoutManager(new LinearLayoutManager(this));
        member_recycler.setHasFixedSize(true);
        select_activity = findViewById(R.id.select_activity);
        member = findViewById(R.id.add_layout);
        header_others = findViewById(R.id.header_others);
        headeer = findViewById(R.id.headeer);


        addmember = findViewById(R.id.addmember);
        add_animal = findViewById(R.id.add_animal);
        add_machine = findViewById(R.id.add_machine);
        add_miscellaneous = findViewById(R.id.add_miscellaneous);


        land_name.setText(activity.getActivity_land());
        crop.setText(activity.getActivity_crop());
        select_activity.setText(activity.getActivity());
        resources.setText(activity.getActivity_resources());
        start_date.setText(activity.getActivity_start());
        end_date.setText(activity.getActivity_end());
        completed_act = getIntent().getBooleanExtra("completed", false);

        if (completed_act) {
            land_name.setEnabled(false);
            crop.setEnabled(false);
            resources.setEnabled(false);
            select_activity.setEnabled(false);
            start_date.setEnabled(false);
            end_date.setEnabled(false);
            addmember.setEnabled(false);
            add_animal.setEnabled(false);
            add_machine.setEnabled(false);
            add_miscellaneous.setEnabled(false);
            addactivity.setEnabled(false);
            addactivity.setVisibility(View.GONE);

        } else {
            land_name.setEnabled(true);
            crop.setEnabled(true);
            resources.setEnabled(true);
            select_activity.setEnabled(true);
            start_date.setEnabled(true);
            end_date.setEnabled(true);
            addmember.setEnabled(true);
            add_animal.setEnabled(true);
            add_machine.setEnabled(true);
            add_miscellaneous.setEnabled(true);
            addactivity.setEnabled(true);
            addactivity.setVisibility(View.VISIBLE);
            activitydata = new ArrayList<>();
            activitydata.add("Sowing");
            activitydata.add("Field preparation");
            activitydata.add("Transplanted");
            activitydata.add("Direct sowing");
            activitydata.add("Manure and fertilizer");
            activitydata.add("Inter cultural practices");
            activitydata.add("Irrigation");
            activitydata.add("Harvesting");
            ArrayAdapter<String> activity_adpter = new ArrayAdapter(this, R.layout.spinner_item,
                    activitydata);
            select_activity.setAdapter(activity_adpter);

            ArrayAdapter<String> crop_adpter = new ArrayAdapter(this, R.layout.spinner_item,
                    Webservice.crops);

            crop.setAdapter(crop_adpter);

            getmembers();

        }

        try {
            addmemberamount = new ArrayList<>();
            addanimalamount = new ArrayList<>();
            addmachineamount = new ArrayList<>();
            addothermodel = new ArrayList<>();
            JSONArray json_members = new JSONArray(activity.getMembers());
            JSONArray json_machine = new JSONArray(activity.getMachines());
            JSONArray json_animal = new JSONArray(activity.getAnimals());
            JSONArray json_other = new JSONArray(activity.getOthers());

            if (json_members.length() == 0) {


            } else {
                addmember.setVisibility(View.VISIBLE);
                member.setVisibility(View.VISIBLE);
                headeer.setVisibility(View.VISIBLE);
                for (int i = 0; i < json_members.length(); i++) {
                    AddMemberamount_Model obj = new AddMemberamount_Model();
                    obj.setName(json_members.getJSONObject(i).optString("name"));
                    obj.setHours(json_members.getJSONObject(i).optString("hours"));
                    obj.setBillingtype(json_members.getJSONObject(i).optString("billingtype"));
                    obj.setAmount(json_members.getJSONObject(i).optString("amount"));
                    addmemberamount.add(obj);
                }


                member_adapter = new Memberamount_Adapter(Test_Ack.this, addmemberamount, completed_act);
                member_recycler.setAdapter(member_adapter);

            }

            if (json_machine.length() == 0) {

            } else {
                add_machine.setVisibility(View.VISIBLE);
                header_machine.setVisibility(View.VISIBLE);
                for (int i = 0; i < json_machine.length(); i++) {
                    AddMemberamount_Model obj = new AddMemberamount_Model();
                    obj.setName(json_machine.getJSONObject(i).optString("name"));
                    obj.setHours(json_machine.getJSONObject(i).optString("hours"));
                    obj.setBillingtype(json_machine.getJSONObject(i).optString("billingtype"));
                    obj.setAmount(json_machine.getJSONObject(i).optString("amount"));
                    addmachineamount.add(obj);
                }

                member_adapter = new Memberamount_Adapter(Test_Ack.this, addmachineamount, completed_act);
                machine_recycler.setAdapter(member_adapter);


            }
            if (json_animal.length() == 0) {

            } else {
                add_animal.setVisibility(View.VISIBLE);
                header_animal.setVisibility(View.VISIBLE);
                for (int i = 0; i < json_animal.length(); i++) {
                    AddMemberamount_Model obj = new AddMemberamount_Model();
                    obj.setName(json_animal.getJSONObject(i).optString("name"));
                    obj.setHours(json_animal.getJSONObject(i).optString("hours"));
                    obj.setBillingtype(json_animal.getJSONObject(i).optString("billingtype"));
                    obj.setAmount(json_animal.getJSONObject(i).optString("amount"));
                    addanimalamount.add(obj);
                }

                member_adapter = new Memberamount_Adapter(Test_Ack.this, addanimalamount, completed_act);
                animal_recycler.setAdapter(member_adapter);
            }
            if (json_other.length() == 0) {

            } else {
                add_miscellaneous.setVisibility(View.VISIBLE);
                header_others.setVisibility(View.VISIBLE);
                for (int i = 0; i < json_other.length(); i++) {
                    others_model obj = new others_model();
                    obj.setTitle(json_other.getJSONObject(i).optString("title"));
                    obj.setAmount(json_other.getJSONObject(i).optString("amount"));
                    addothermodel.add(obj);
                }

                other_adapter = new Others_Adapter(Test_Ack.this, addothermodel, completed_act);
                others_recycler.setAdapter(other_adapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        animal_recycler.setLayoutManager(new LinearLayoutManager(this));
        animal_recycler.setHasFixedSize(true);

        machine_recycler.setLayoutManager(new LinearLayoutManager(this));
        machine_recycler.setHasFixedSize(true);


        others_recycler.setLayoutManager(new LinearLayoutManager(this));
        others_recycler.setHasFixedSize(true);


        addactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {
                    add_land(activity.getActivity_id());

                }

            }
        });

        add_miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Addothers_Dialog obj = new Addothers_Dialog();
                obj.dialog(Test_Ack.this, "Add member", new add_others() {
                    @Override
                    public void crop_suggest(others_model name) {

                        addothermodel.add(name);
                        header_others.setVisibility(View.VISIBLE);
                        other_adapter = new Others_Adapter(Test_Ack.this, addothermodel, false);
                        others_recycler.setAdapter(other_adapter);

                    }
                });

            }
        });

        landdata = new ArrayList<>();
        cropdata = new ArrayList<>();
        activitydata = new ArrayList<>();
        resourcedata = new ArrayList<>();

        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Add_resource_Dialog obj = new Add_resource_Dialog();
//                obj.dialog(Test_Ack.this, member_data, "member", new add_resource() {
//                    @Override
//                    public void crop_suggest(AddMemberamount_Model name) {
//
//
//
//                        addmemberamount.add(name);
////                        headeer.setVisibility(View.VISIBLE);
//                        member_adapter = new Memberamount_Adapter(Test_Ack.this, addmemberamount, false);
//                        member_recycler.setAdapter(member_adapter);
////                        member_adapter.notifyDataSetChanged();
////                        member_recycler.invalidate();
//                    }
//                });
            }
        });


        resourcedata = new ArrayList<>();
        MultiSelectModel obj = new MultiSelectModel(1, "Human");
        resourcedata.add(obj);
        obj = new MultiSelectModel(2, "Machine");
        resourcedata.add(obj);
        obj = new MultiSelectModel(3, "Animal");
        resourcedata.add(obj);

        obj = new MultiSelectModel(4, "Miscellaneous");
        resourcedata.add(obj);


        resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiSelectDialog multiSelectDialog = new MultiSelectDialog()
                        .title("select") //setting title for dialog
                        .titleSize(25)
                        .preSelectIDsList(selected_res)
                        .positiveText("Done")
                        .negativeText("Cancel")
                        .setMinSelectionLimit(1) //you can set minimum checkbox selection limit (Optional)
                        .setMaxSelectionLimit(resourcedata.size()) //you can set maximum checkbox selection limit (Optional)

                        .multiSelectList(resourcedata) // the multi select model list with ids and name
                        .onSubmit(new MultiSelectDialog.SubmitCallbackListener() {
                            @Override
                            public void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString) {
                                //will return list of selected IDS
                                selected_res = selectedIds;
                                addmember.setVisibility(View.GONE);
                                add_machine.setVisibility(View.GONE);
                                add_animal.setVisibility(View.GONE);
                                add_miscellaneous.setVisibility(View.GONE);
                                for (int i = 0; i < selectedIds.size(); i++) {


                                    if (selectedNames.get(i).equals("Human")) {
                                        addmember.setVisibility(View.VISIBLE);


                                    } else if (selectedNames.get(i).equals("Machine")) {
                                        add_machine.setVisibility(View.VISIBLE);

                                    } else if (selectedNames.get(i).equals("Animal")) {
                                        add_animal.setVisibility(View.VISIBLE);
                                    } else if (selectedNames.get(i).equals("Miscellaneous")) {
                                        add_miscellaneous.setVisibility(View.VISIBLE);

                                    } else {
                                        member.setVisibility(View.GONE);
                                        add_machine.setVisibility(View.GONE);
                                        add_animal.setVisibility(View.GONE);
                                        add_miscellaneous.setVisibility(View.GONE);
                                    }
                                }

                                resources.setText(dataString);


                            }

                            @Override
                            public void onCancel() {
                                Log.d(TAG, "Dialog cancelled");
                            }


                        });

                multiSelectDialog.show(getSupportFragmentManager(), "multiSelectDialog");

            }
        });

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(Test_Ack.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {

                        start_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(Test_Ack.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {

                        end_date.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        add_animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Add_resource_Dialog obj = new Add_resource_Dialog();
//                obj.dialog(Test_Ack.this, animal_data, "animal", new add_resource() {
//                    @Override
//                    public void crop_suggest(AddMemberamount_Model name) {
//
//                        addanimalamount.add(name);
//                        header_animal.setVisibility(View.VISIBLE);
//                        member_adapter = new Memberamount_Adapter(Test_Ack.this, addanimalamount, false);
//                        animal_recycler.setAdapter(member_adapter);
//                    }
//                });
            }
        });

        add_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> data = new ArrayList<>();
                data.add("Tracter");
                data.add("Balers");
                data.add("Combines");
                data.add("plows");

                Add_resource_Dialog obj = new Add_resource_Dialog();
//                obj.dialog(Test_Ack.this, data, "machine", new add_resource() {
//                    @Override
//                    public void crop_suggest(AddMemberamount_Model name) {
//
//                        addmachineamount.add(name);
//                        header_machine.setVisibility(View.VISIBLE);
//                        member_adapter = new Memberamount_Adapter(Test_Ack.this, addmachineamount, false);
//                        machine_recycler.setAdapter(member_adapter);
//                    }
//                });
            }
        });


    }

    public void getmembers() {
        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.add_lmalist + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {
                    landdata = new ArrayList<>();
                    member_data = new ArrayList<>();
                    animal_data = new ArrayList<>();

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray_land = jsonObject1.getJSONArray("land");
                        JSONArray jsonArray_member = jsonObject1.getJSONArray("member");
                        JSONArray jsonArray_animal = jsonObject1.getJSONArray("animal");


                        for (int i = 0; i < jsonArray_land.length(); i++) {
                            landdata.add(jsonArray_land.getJSONObject(i).optString("land_name"));
                        }
                        for (int j = 0; j < jsonArray_member.length(); j++) {
                            member_data.add(jsonArray_member.getJSONObject(j).optString("member_name"));

                        }
                        for (int k = 0; k < jsonArray_animal.length(); k++) {
                            animal_data.add(jsonArray_animal.getJSONObject(k).optString("animal_name"));

                        }


                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }


                ArrayAdapter<String> watersource_adpter = new ArrayAdapter(Test_Ack.this, R.layout.spinner_item,
                        landdata);
                land_name.setAdapter(watersource_adpter);

            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void add_land(String activity_id) {
        Gson gson = new GsonBuilder().create();
        JsonArray member_Array = gson.toJsonTree(addmemberamount).getAsJsonArray();
        JsonArray machine_Array = gson.toJsonTree(addmachineamount).getAsJsonArray();
        JsonArray animal_Array = gson.toJsonTree(addanimalamount).getAsJsonArray();
        JsonArray others_Array = gson.toJsonTree(addothermodel).getAsJsonArray();


        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.update_activity)
                .addUrlEncodeFormBodyParameter("activity_id", activity_id)
                .addUrlEncodeFormBodyParameter("activity_land", land_name.getText().toString())
                .addUrlEncodeFormBodyParameter("activity_crop", crop.getText().toString())
                .addUrlEncodeFormBodyParameter("activity", select_activity.getText().toString())
                .addUrlEncodeFormBodyParameter("activity_resources", resources.getText().toString())
                .addUrlEncodeFormBodyParameter("activity_start", start_date.getText().toString())
                .addUrlEncodeFormBodyParameter("activity_end", end_date.getText().toString())
                .addUrlEncodeFormBodyParameter("members", member_Array.toString())
                .addUrlEncodeFormBodyParameter("machines", machine_Array.toString())
                .addUrlEncodeFormBodyParameter("animals", animal_Array.toString())
                .addUrlEncodeFormBodyParameter("others", others_Array.toString())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        finish();
                        Toasty.success(Test_Ack.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(Test_Ack.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();


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

    private boolean validatesignForm() {
        if (!(land_name.getText().toString().length() > 2)) {
            land_name.setError(getString(R.string.required_date));
            return false;
        } else if (!(crop.getText().toString().length() > 0)) {
            crop.setError(getString(R.string.required_date));
            return false;
        } else if (!(select_activity.getText().toString().length() > 2)) {
            select_activity.setError(getString(R.string.required_date));
            return false;
        } else {
            return true;
        }
    }
}