package com.ascentya.AsgriV2.Mycrops_Mainfragments.Cropsub_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abdeveloper.library.MultiSelectDialog;
import com.abdeveloper.library.MultiSelectModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Payment_Activity;
import com.ascentya.AsgriV2.Adapters.Memberamount_Adapter;
import com.ascentya.AsgriV2.Adapters.Mycrops_EssentialAdapter;
import com.ascentya.AsgriV2.Adapters.Others_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.add_others;
import com.ascentya.AsgriV2.Models.AddMemberamount_Model;
import com.ascentya.AsgriV2.Models.CropEssentials_Model;
import com.ascentya.AsgriV2.Models.Mycrops_List_Model;
import com.ascentya.AsgriV2.Models.others_model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.Add_resource_Dialog;
import com.ascentya.AsgriV2.Utils.Addothers_Dialog;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticFloatingActionButton;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import org.apache.commons.lang3.Range;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class MyCrops_Fragments extends Fragment {
    View root_view;
    ViewDialog viewDialog;
    List<Mycrops_List_Model> Data;
    SessionManager sm;

    AppCompatAutoCompleteTextView land_name, crop, select_activity, resources, lastchemaical;


    List<String> landdata, cropdata, activitydata, member_data, animal_data, others_data;
//    lastchemaical lastchemaical;

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
    String Humidity_ts, pollution_ts, moisture_ts, light_ts, temt_ts, ph_ts;

    List<CropEssentials_Model> Data_Es;
    Mycrops_EssentialAdapter cropEssentialAdapter;
    RecyclerView cropessentials_recycler;

    private BottomSheetBehavior mBehavior;
    private View bottom_sheet;
    private BottomSheetDialog mBottomSheetDialog;

    Button proceed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.testactivity, container, false);

        sm = new SessionManager(getActivity());
        cal = Calendar.getInstance();
        selected_res = new ArrayList<>();

        viewDialog = new ViewDialog(getActivity());
        addactivity = root_view.findViewById(R.id.addactivity);
        resources = root_view.findViewById(R.id.resources);
        land_name = root_view.findViewById(R.id.land_name);
        end_date = root_view.findViewById(R.id.end_date);
        start_date = root_view.findViewById(R.id.start_date);
        header_animal = root_view.findViewById(R.id.header_animal);
        header_machine = root_view.findViewById(R.id.header_machine);
        machine_recycler = root_view.findViewById(R.id.machine_recycler);
        animal_recycler = root_view.findViewById(R.id.animal_recycler);
        crop = root_view.findViewById(R.id.crop);
        others_recycler = root_view.findViewById(R.id.others_recycler);
        member_recycler = root_view.findViewById(R.id.member_recycler);
        member_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        member_recycler.setHasFixedSize(true);
        select_activity = root_view.findViewById(R.id.select_activity);
        member = root_view.findViewById(R.id.add_layout);
        header_others = root_view.findViewById(R.id.header_others);
        bottom_sheet = root_view.findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        animal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        animal_recycler.setHasFixedSize(true);

        machine_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        machine_recycler.setHasFixedSize(true);


        others_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        others_recycler.setHasFixedSize(true);


        addmember = root_view.findViewById(R.id.addmember);
        add_animal = root_view.findViewById(R.id.add_animal);
        add_machine = root_view.findViewById(R.id.add_machine);
        headeer = root_view.findViewById(R.id.headeer);
        add_miscellaneous = root_view.findViewById(R.id.add_miscellaneous);
        addactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatesignForm()) {

                    if (NetworkDetector.isNetworkStatusAvialable(getActivity())) {
                        add_land();
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
//                    add_land();

                }

            }
        });

        add_miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Addothers_Dialog obj = new Addothers_Dialog();
                obj.dialog(getActivity(), "Add a miscellaneous", new add_others() {
                    @Override
                    public void crop_suggest(others_model name) {

                        addothermodel.add(name);
                        header_others.setVisibility(View.VISIBLE);
                        other_adapter = new Others_Adapter(getActivity(), addothermodel, false);
                        others_recycler.setAdapter(other_adapter);

                    }
                });

            }
        });

        landdata = new ArrayList<>();
        cropdata = new ArrayList<>();
        activitydata = new ArrayList<>();
        resourcedata = new ArrayList<>();
        addmemberamount = new ArrayList<>();
        addanimalamount = new ArrayList<>();
        addmachineamount = new ArrayList<>();
        addothermodel = new ArrayList<>();
        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Add_resource_Dialog obj = new Add_resource_Dialog();
//                obj.dialog(getActivity(), member_data, "member", new add_resource() {
//                    @Override
//                    public void crop_suggest(AddMemberamount_Model name) {
//
//
//                        addmemberamount.add(name);
//                        headeer.setVisibility(View.VISIBLE);
//                        member_adapter = new Memberamount_Adapter(getActivity(), addmemberamount, false);
//                        member_recycler.setAdapter(member_adapter);
//
//                    }
//                });
            }
        });


        ArrayAdapter<String> crop_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                Webservice.crops);

        crop.setAdapter(crop_adpter);

        crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog(getActivity(), i);


            }
        });


        activitydata = new ArrayList<>();
        activitydata.add("Sowing");
        activitydata.add("Field preparation");
        activitydata.add("Transplanted");
        activitydata.add("Direct sowing");
        activitydata.add("Manure and fertilizer");
        activitydata.add("Inter cultural practices");
        activitydata.add("Irrigation");
        activitydata.add("Harvesting");
        ArrayAdapter<String> activity_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                activitydata);
        select_activity.setAdapter(activity_adpter);

        resourcedata = new ArrayList<>();
        MultiSelectModel obj = new MultiSelectModel(1, "Human");
        resourcedata.add(obj);
        obj = new MultiSelectModel(2, "Machine");
        resourcedata.add(obj);

        obj = new MultiSelectModel(3, "Miscellaneous");
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

                                    }
//                                    else if (selectedNames.get(i).equals("Animal")) {
//                                        add_animal.setVisibility(View.VISIBLE);
//                                    }
                                    else if (selectedNames.get(i).equals("Miscellaneous")) {
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

                multiSelectDialog.show(getActivity().getSupportFragmentManager(), "multiSelectDialog");

            }
        });

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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


//                Add_resource_Dialog obj = new Add_resource_Dialog();
//                obj.dialog(getActivity(), animal_data, "animal", new add_resource() {
//                    @Override
//                    public void crop_suggest(AddMemberamount_Model name) {
//
//                        addanimalamount.add(name);
//                        header_animal.setVisibility(View.VISIBLE);
//                        member_adapter = new Memberamount_Adapter(getActivity(), addanimalamount, false);
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

//                Add_resource_Dialog obj = new Add_resource_Dialog();
//                obj.dialog(getActivity(), data, "machine", new add_resource() {
//                    @Override
//                    public void crop_suggest(AddMemberamount_Model name) {
//
//                        addmachineamount.add(name);
//                        header_machine.setVisibility(View.VISIBLE);
//                        member_adapter = new Memberamount_Adapter(getActivity(), addmachineamount, false);
//                        machine_recycler.setAdapter(member_adapter);
//                    }
//                });
            }
        });
        getmembers();

        return root_view;
    }

    public void getmembers() {
        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.add_lmalist + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }

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


                ArrayAdapter<String> watersource_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                        landdata);
                land_name.setAdapter(watersource_adpter);

            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void add_land() {
        Gson gson = new GsonBuilder().create();
        JsonArray member_Array = gson.toJsonTree(addmemberamount).getAsJsonArray();
        JsonArray machine_Array = gson.toJsonTree(addmachineamount).getAsJsonArray();
        JsonArray animal_Array = gson.toJsonTree(addanimalamount).getAsJsonArray();
        JsonArray others_Array = gson.toJsonTree(addothermodel).getAsJsonArray();


        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.add_activity)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
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
                .addUrlEncodeFormBodyParameter("is_premium", sm.getUser().getIspremium())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        land_name.setText("");
                        crop.setText("");
                        select_activity.setText("");
                        resources.setText("");
                        start_date.setText("");
                        end_date.setText("");
                        headeer.setVisibility(View.GONE);
                        header_animal.setVisibility(View.GONE);
                        header_machine.setVisibility(View.GONE);
                        header_others.setVisibility(View.GONE);
                        addmachineamount.clear();
                        addmemberamount.clear();
                        addanimalamount.clear();
                        addothermodel.clear();


                        animal_recycler.setAdapter(null);
                        member_recycler.setAdapter(null);
                        others_recycler.setAdapter(null);
                        machine_recycler.setAdapter(null);
                        addmember.setVisibility(View.GONE);
                        add_animal.setVisibility(View.GONE);
                        add_machine.setVisibility(View.GONE);
                        add_miscellaneous.setVisibility(View.GONE);
                        selected_res.clear();

                        Toasty.success(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    } else {
                        showBottomSheetDialog();
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
            Toast.makeText(getActivity(), R.string.selectland, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(crop.getText().toString().length() > 0)) {
            Toast.makeText(getActivity(), R.string.selectcrop, Toast.LENGTH_SHORT).show();

            return false;
        } else if (!(select_activity.getText().toString().length() > 2)) {
            Toast.makeText(getActivity(), R.string.select_activity, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void dialog(Context context, Integer id) {
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.cropchecker_activity_dialog);

        cropessentials_recycler = dialog.findViewById(R.id.cropessentials_recycler);

        cropessentials_recycler.setLayoutManager(new LinearLayoutManager(context));
        cropessentials_recycler.setHasFixedSize(true);
//        gethumidity(id);
        getcropsessential(id);


        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();


        proceed = (Button) dialog.findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void getcropsessential(Integer pos) {
        Data_Es = new ArrayList<>();
        CropEssentials_Model obj = new CropEssentials_Model();

        obj.setDisc("This temperature might affect this crop");
        obj.setIcon(R.drawable.temperature);
        obj.setValues("26");
        obj.setTitle("Temperature");
        obj.setActual_value(Webservice.Data_crops.get(pos).getTempreture() + " %");
        String[] temstr = Webservice.Data_crops.get(pos).getTempreture().split("-", 2);
//        obj.setSuccess(floatbetween(Float.parseFloat(temt_ts), Float.parseFloat(temstr[0].trim()), Float.parseFloat(temstr[1].trim())));
        obj.setStatusicon(R.drawable.equal);
        Data_Es.add(obj);


        obj = new CropEssentials_Model();

        obj.setDisc("This is suitable for this crop");
        obj.setIcon(R.drawable.humidity);
        obj.setValues("26");
        obj.setTitle("Humidity");
        obj.setActual_value(Webservice.Data_crops.get(pos).getHumidity() + " %");
        String[] humstr = Webservice.Data_crops.get(pos).getHumidity().split("-", 2);
//        obj.setSuccess(intbetween(Integer.parseInt(Humidity_ts), Integer.parseInt(humstr[0].trim()), Integer.parseInt(humstr[1].trim())));


        obj.setStatusicon(R.drawable.equal);
        Data_Es.add(obj);
        obj = new CropEssentials_Model();


        obj.setDisc("This pollution level may affect this crop");
        obj.setIcon(R.drawable.pollution);
        obj.setValues("26");
        obj.setActual_value(Webservice.Data_crops.get(pos).getPollution() + " %");
        obj.setTitle("Pollution");

        String[] pollustr = Webservice.Data_crops.get(pos).getPollution().split("-", 2);
//        obj.setSuccess(intbetween(Integer.parseInt(pollution_ts), Integer.parseInt(pollustr[0].trim()), Integer.parseInt(pollustr[1].trim())));

        obj.setStatusicon(R.drawable.equal);
        Data_Es.add(obj);

        obj = new CropEssentials_Model();


        obj.setDisc("This PH level is suitable for this crop");
        obj.setIcon(R.drawable.ph);
        obj.setValues("26");
        obj.setTitle("pH");
        obj.setActual_value(Webservice.Data_crops.get(pos).getWaterph());

        String[] phstr = Webservice.Data_crops.get(pos).getWaterph().split("-", 2);
//        obj.setSuccess(floatbetween(Float.parseFloat(ph_ts), Float.parseFloat(phstr[0].trim()), Float.parseFloat(phstr[1].trim())));

        obj.setStatusicon(R.drawable.success);
        Data_Es.add(obj);


        obj = new CropEssentials_Model();


        obj.setDisc("This moisture level may affect this crop");
        obj.setIcon(R.drawable.moisure);
        obj.setValues("26");
        obj.setTitle("Moisture");
        obj.setActual_value(Webservice.Data_crops.get(pos).getMoisture() + "%");

        String[] moisustr = Webservice.Data_crops.get(pos).getMoisture().split("-", 2);
//        obj.setSuccess(intbetween(Integer.parseInt(moisture_ts), Integer.parseInt(moisustr[0].trim()), Integer.parseInt(moisustr[1].trim())));


        obj.setStatusicon(R.drawable.equal);
        Data_Es.add(obj);
        obj = new CropEssentials_Model();
        obj.setDisc("This will not affect this crop");
        obj.setIcon(R.drawable.solids);
        obj.setValues("26");
        obj.setTitle("Pollution");
        obj.setActual_value("40-50 %");
//        obj.setStatusicon(R.drawable.success);
        Data_Es.add(obj);


        cropEssentialAdapter = new Mycrops_EssentialAdapter(getActivity(), Data_Es);

        cropessentials_recycler.setAdapter(cropEssentialAdapter);

    }

    public void gethumidity(final Integer pos) {
        Data_Es = new ArrayList<>();
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

                    if (light_val.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 100);
                        if (test.contains(Integer.parseInt(light_val))) {
                            light_ts = light_val;
                        } else {
                            light_ts = "0";
                        }
                    } else {
                        light_ts = "0";
                    }


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
                    String temt_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field1");

                    if (temt_val.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 100);
                        if (test.contains(Integer.parseInt(temt_val))) {
                            temt_ts = temt_val;
                        } else {
                            temt_ts = "0";
                        }
                    } else {
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

                    if (ph_val.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 18);
                        if (test.contains(Integer.parseInt(ph_val))) {
                            ph_ts = ph_val;
                        } else {
                            ph_ts = "0";
                        }
                    } else {
                        ph_ts = "0";
                    }

                    proceed.setText("Proceed");


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }


//                CropEssentials_Model obj = new CropEssentials_Model();
//                if (temt_ts.equalsIgnoreCase("0")) {
//                    obj.setName("Temperature : Not Connected");
//
//                } else {
//                    obj.setName("Temperature : " + temt_ts + " °C");
//
//                }
//                obj.setDisc("This temperature might affect this crop");
//                obj.setIcon(R.drawable.temperature);
//                obj.setValues("26");
//                obj.setTitle("temt");
//                obj.setActual_value(Webservice.Data_crops.get(pos).getTempreture() + " %");
//                String[] temstr = Webservice.Data_crops.get(pos).getTempreture().split("-", 2);
//                obj.setSuccess(floatbetween(Float.parseFloat(temt_ts), Float.parseFloat(temstr[0].trim()), Float.parseFloat(temstr[1].trim())));
//                obj.setStatusicon(R.drawable.equal);
//                Data_Es.add(obj);
//
//
//                obj = new CropEssentials_Model();
//                if (Humidity_ts.equalsIgnoreCase("0")) {
//                    obj.setName("Humidity :  Not Connected");
//
//                } else {
//                    obj.setName("Humidity : " + Humidity_ts + "%");
//
//
//                }
//                obj.setDisc("This is suitable for this crop");
//                obj.setIcon(R.drawable.humidity);
//                obj.setValues("26");
//                obj.setTitle("Humidity");
//                obj.setActual_value(Webservice.Data_crops.get(pos).getHumidity() + " %");
//                String[] humstr = Webservice.Data_crops.get(pos).getHumidity().split("-", 2);
//                obj.setSuccess(intbetween(Integer.parseInt(Humidity_ts), Integer.parseInt(humstr[0].trim()), Integer.parseInt(humstr[1].trim())));
//
//
//                obj.setStatusicon(R.drawable.equal);
//                Data_Es.add(obj);
//                obj = new CropEssentials_Model();
//
//                if (pollution_ts.equalsIgnoreCase("0")) {
//                    obj.setName("Pollution :  Not Connected");
//
//                } else {
//                    obj.setName("Pollution : " + pollution_ts + "%");
//
//                }
//                obj.setDisc("This pollution level may affect this crop");
//                obj.setIcon(R.drawable.pollution);
//                obj.setValues("26");
//                obj.setActual_value(Webservice.Data_crops.get(pos).getPollution() + " %");
//                obj.setTitle("Pollution");
//
//                String[] pollustr = Webservice.Data_crops.get(pos).getPollution().split("-", 2);
//                obj.setSuccess(intbetween(Integer.parseInt(pollution_ts), Integer.parseInt(pollustr[0].trim()), Integer.parseInt(pollustr[1].trim())));
//
//                obj.setStatusicon(R.drawable.equal);
//                Data_Es.add(obj);
//
//                obj = new CropEssentials_Model();
//
//
//                if (ph_ts.equalsIgnoreCase("0")) {
//                    obj.setName("PH - Not Connected");
//
//                } else {
//                    obj.setName("PH - " + ph_ts);
//
//                }
//
//                obj.setDisc("This PH level is suitable for this crop");
//                obj.setIcon(R.drawable.ph);
//                obj.setValues("26");
//                obj.setTitle("Pollution");
//                String[] phstr = Webservice.Data_crops.get(pos).getWaterph().split("-", 2);
//                obj.setSuccess(floatbetween(Float.parseFloat(ph_ts), Float.parseFloat(phstr[0].trim()), Float.parseFloat(phstr[1].trim())));
//
//                obj.setStatusicon(R.drawable.success);
//                Data_Es.add(obj);
//
//
//                obj = new CropEssentials_Model();
//
//                if (moisture_ts.equalsIgnoreCase("0")) {
//                    obj.setName("Moisture : Not Connected");
//
//                } else {
//                    obj.setName("Moisture : " + moisture_ts + "%");
//
//                }
//                obj.setDisc("This moisture level may affect this crop");
//                obj.setIcon(R.drawable.moisure);
//                obj.setValues("26");
//                obj.setTitle("Pollution");
//                obj.setActual_value(Webservice.Data_crops.get(pos).getMoisture() + "%");
//
//                String[] moisustr = Webservice.Data_crops.get(pos).getMoisture().split("-", 2);
//                obj.setSuccess(intbetween(Integer.parseInt(moisture_ts), Integer.parseInt(moisustr[0].trim()), Integer.parseInt(moisustr[1].trim())));
//
//
//                obj.setStatusicon(R.drawable.equal);
//                Data_Es.add(obj);
//                obj = new CropEssentials_Model();
//                obj.setName("Total dissolved solids : 60%");
//                obj.setDisc("This will not affect this crop");
//                obj.setIcon(R.drawable.solids);
//                obj.setValues("26");
//                obj.setTitle("Pollution");
//                obj.setActual_value("40-50 %");
//                obj.setStatusicon(R.drawable.success);
//                Data_Es.add(obj);
//
//                cropEssentialAdapter = new CropEssential_Adapter(getActivity(), Data_Es);
//
//                cropessentials_recycler.setAdapter(cropEssentialAdapter);

//                runLayoutAnimation(cropessentials_recycler);


            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public static boolean floatbetween(float i, float minValueInclusive, float maxValueInclusive) {
        return (i >= minValueInclusive && i <= maxValueInclusive);
    }

    public static boolean intbetween(int i, int minValueInclusive, int maxValueInclusive) {
        return (i >= minValueInclusive && i <= maxValueInclusive);
    }


    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_basic, null);
        ElasticButton premuim = view.findViewById(R.id.premium);
        premuim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Payment_Activity.class);
                startActivity(i);
            }
        });


        mBottomSheetDialog = new BottomSheetDialog(getActivity());
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }
}
