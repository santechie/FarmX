package com.ascentya.AsgriV2.Mycrops_Mainfragments.Cropsub_fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.NetworkDetector;
import com.ascentya.AsgriV2.Utils.NoDefaultSpinner;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.skydoves.elasticviews.ElasticButton;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;

import es.dmoral.toasty.Toasty;

public class AddNewCrop_Fragments extends BaseFragment {
    View root_view;
    Spinner land, member;
    EditText plougheddate, wateravailability;
    ElasticButton addmember;
    AutoCompleteTextView crop;
    ViewDialog viewDialog;
    SessionManager sm;
    Calendar cal;
    List<String> member_data;
    List<String> land_data;
    NoDefaultSpinner gender;
    List<String> genderdata;
    EditText member_name, member_age, relation;
    TextInputLayout layout_name;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.activity_test__ack, container, false);
        gender = root_view.findViewById(R.id.gender);
        sm = new SessionManager(getActivity());
        genderdata = new ArrayList<>();
        viewDialog = new ViewDialog(getActivity());

        layout_name = root_view.findViewById(R.id.layout_name);
        relation = root_view.findViewById(R.id.relation);
        member_age = root_view.findViewById(R.id.member_age);
        member_name = root_view.findViewById(R.id.member_name);
        addmember = root_view.findViewById(R.id.addmember);

        addmember.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {

                /*if (checkUser()) {

                }*/

                if (validateForm()) {
                    if (NetworkDetector.isNetworkStatusAvialable(requireActivity())) {
                        add_land();
                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
//                    add_land();
                }

            }
        });


        genderdata.clear();
        genderdata.add("Male");
        genderdata.add("Female");
        genderdata.add("Others");

        ArrayAdapter<String> gender_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item,
                genderdata);

        gender.setAdapter(gender_adpter);

        if (!getModuleManager().canInsert(Components.MyFarm.MEMBERS)){

//            addmember.setVisibility(View.GONE);
//                        layout_name.setVisibility(View.GONE);
        }

//        sm = new SessionManager(getActivity());
//        cal = Calendar.getInstance();
//        crop = root_view.findViewById(R.id.crop);
//        land = root_view.findViewById(R.id.land);
//        member = root_view.findViewById(R.id.member);
//        viewDialog = new ViewDialog(getActivity());
//        plougheddate = root_view.findViewById(R.id.plougheddate);
//        wateravailability = root_view.findViewById(R.id.wateravailability);
//        addmember = root_view.findViewById(R.id.addcrop);
//
//
//        getlands();
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_dropdown_item, Webservice.crops);
//
//        crop.setAdapter(adapter);
//
//        crop.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus)
//                    crop.showDropDown();
//
//            }
//        });
//
//        crop.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                crop.showDropDown();
//                return false;
//            }
//        });
//
//
//        plougheddate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view1, int year, int month, int dayOfMonth) {
//
//                        plougheddate.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
//
//                        wateravailability.requestFocus();
//                        openkeyboard(wateravailability);
//
//                    }
//                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//                dpd.show();
//            }
//        });
//
//        addmember.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (land.getSelectedItemPosition() != 0){
//                    if (member.getSelectedItemPosition() != 0){
//                        if (validateForm()) {
//                            add_land();
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), R.string.membercheck, Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(getActivity(), R.string.landcheck, Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });
        return root_view;
    }

    @SuppressLint("StringFormatInvalid")
    private boolean validateForm() {
        if (!(member_name.getText().toString().length() > 2)) {
            member_name.setError(getString(R.string.required));
            return false;
        } else if(relation.getText().toString().trim().length() != 12){
            relation.setError(String.format(getString(R.string.enter_valid_), "Aadhaar Number"));
            return false;
        }else if(member_age.getText().toString().trim().length() != 2){
            member_age.setError(String.format(getString(R.string.enter_valid_), "Age"));
            return false;
        } else {
            return true;
        }
    }

   /* private boolean checkUser(){
        return ((BaseActivity) getActivity()).checkSubscription(Components.MyFarm.MEMBERS , ModuleManager.ACCESS.INSERT);
    }*/



    public void add_land() {
        viewDialog.showDialog();

        AndroidNetworking.post(Webservice.addmember)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .addUrlEncodeFormBodyParameter("member_name", member_name.getText().toString())
                .addUrlEncodeFormBodyParameter("member_gender", gender.getSelectedItem().toString())
                .addUrlEncodeFormBodyParameter("member_age", member_age.getText().toString())
                .addUrlEncodeFormBodyParameter("member_relation", "")
                .addUrlEncodeFormBodyParameter("member_experience", "")
                .addUrlEncodeFormBodyParameter("member_payment", "")
                .addUrlEncodeFormBodyParameter("member_bilingtype", "")
                .addUrlEncodeFormBodyParameter("member_aadhar", relation.getText().toString())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        member_name.setText("");
                        member_age.setText("");
                        relation.setText("");
                        genderdata.clear();
                        genderdata.add("Male");
                        genderdata.add("Female");
                        genderdata.add("Others");

                        ArrayAdapter<String> gender_adpter = new ArrayAdapter(getActivity(), R.layout.spinner_item, genderdata);
                        gender.setAdapter(gender_adpter);
                        Toasty.success(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.error(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                    Toasty.error(getActivity(), jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

//    public void getlands() {
//        viewDialog.showDialog();
//        land_data = new ArrayList<>();
//        AndroidNetworking.get(Webservice.getregisteredland + sm.getUser().getId())
//
//                .build().getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                viewDialog.hideDialog();
//
//                try {
//                    land_data.add(getString(R.string.selectland));
//                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            land_data.add(jsonArray.getJSONObject(i).optString("land_name"));
//
////                            Mycultivation_Model obj = new Mycultivation_Model();
////                            obj.setLand_id(jsonArray.getJSONObject(i).optString("id"));
////                            obj.setLand_name(jsonArray.getJSONObject(i).optString("land_name"));
////                            obj.setCultivation_onfield(jsonArray.getJSONObject(i).optString("land_cultivation_first"));
////                            obj.setLand_dimon(jsonArray.getJSONObject(i).optString("land_dimension"));
////                            obj.setLand_location(jsonArray.getJSONObject(i).optString("land_location"));
////                            obj.setLand_cultivationdate(jsonArray.getJSONObject(i).optString("land_cultivation_date"));
////                            obj.setLast_cultivated_crop(jsonArray.getJSONObject(i).optString("land_lastcultivated_crop"));
////                            obj.setIrrigation_info(jsonArray.getJSONObject(i).optString("land_irrigation"));
//
//                        }
//
//
//
//
//                    }
//
//                    land_data.add(getActivity().getString(R.string.Add_Land));
//
//                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                            getActivity(), R.layout.spinner_item, land_data) {
//                        @Override
//                        public boolean isEnabled(int position) {
//                            if (position == 0) {
//                                // Disable the first item from Spinner
//                                // First item will be use for hint
//                                return false;
//                            } else {
//                                return true;
//                            }
//                        }
//
//                        @Override
//                        public View getDropDownView(int position, View convertView,
//                                                    ViewGroup parent) {
//                            View view = super.getDropDownView(position, convertView, parent);
//                            TextView tv = (TextView) view;
//                            if (position == 0) {
//                                // Set the hint text color gray
//                                tv.setTextColor(Color.GRAY);
//                            } else {
//                                tv.setTextColor(Color.BLACK);
//                            }
//                            return view;
//                        }
//                    };
//                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
//                    land.setAdapter(spinnerArrayAdapter);
//
//                    land.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            String selectedItemText = (String) parent.getItemAtPosition(position);
//                            // If user change the default selection
//                            // First item is disable and it is used for hint
//
//                            if (selectedItemText.equalsIgnoreCase(getString(R.string.Add_Land))){
//
//                                ((BottomNavigationView)getActivity().findViewById(R.id.navigation_mycrop)).setSelectedItemId(R.id.navigation_land);
//                            }else {
//                                if (position > 0) {
//                                    // Notify the selected item text
//
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//
//
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//                viewDialog.hideDialog();
//
//            }
//        });
//    }


    public void getmembers() {
        member_data = new ArrayList<>();
        AndroidNetworking.get(Webservice.getmemberlist + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                try {
                    member_data.add(getString(R.string.selectmem));

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            member_data.add(jsonArray.getJSONObject(i).optString("member_name"));
//
//                            Members_Model obj = new Members_Model();
//                            obj.setMember_id(jsonArray.getJSONObject(i).optString("id"));
//                            obj.setMember_name(jsonArray.getJSONObject(i).optString("member_name"));
//                            obj.setMember_age(jsonArray.getJSONObject(i).optString("member_age"));
//                            obj.setMember_gender(jsonArray.getJSONObject(i).optString("member_gender"));
//                            obj.setFarming_exp(jsonArray.getJSONObject(i).optString("farming_experience"));
//                            obj.setRelation(jsonArray.getJSONObject(i).optString("member_relation"));
//
                        }


                    }

                    member_data.add(getString(R.string.Add_member));

                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            getActivity(), R.layout.spinner_item, member_data) {
                        @Override
                        public boolean isEnabled(int position) {
                            if (position == 0) {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
                            } else {
                                return true;
                            }
                        }

                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if (position == 0) {
                                // Set the hint text color gray
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                    member.setAdapter(spinnerArrayAdapter);

                    member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItemText = (String) parent.getItemAtPosition(position);
                            // If user change the default selection
                            // First item is disable and it is used for hint
                            if (selectedItemText.equalsIgnoreCase(getString(R.string.Add_member))) {

                                TabLayout tabs = (TabLayout) ((Activity) getActivity()).findViewById(R.id.tab_layout);
                                tabs.getTabAt(1).select();

                                Bus bus = DeleteBus.getInstance();
                                bus.post(new DeleteEvent("show_addlayout"));


                            } else {
                                if (position > 0) {

                                    if (parent.getCount() == position) {
                                        Toast.makeText(getActivity(), "member", Toast.LENGTH_SHORT).show();
                                    }
                                    // Notify the selected item text

                                }
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }



    public void openkeyboard(final EditText field) {
        field.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(field, 0);
            }
        }, 200);

    }

}
