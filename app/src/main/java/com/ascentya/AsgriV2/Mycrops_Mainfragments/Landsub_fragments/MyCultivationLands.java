package com.ascentya.AsgriV2.Mycrops_Mainfragments.Landsub_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Payment_Activity;
import com.ascentya.AsgriV2.Adapters.Mycultivation_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Land_Edit;
import com.ascentya.AsgriV2.Models.Mycultivation_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyCultivationLands extends Fragment {
    View root_view;
    RecyclerView mycultivation_recycler;
    Mycultivation_Adapter adapter;
    List<Mycultivation_Model> Data;
    ViewDialog viewDialog;
    SessionManager sm;
    LinearLayout empty;
    LinearLayout premium_layout;
    Boolean ispremium;
    ElasticButton premium;

    @Override
    public void onResume() {
        super.onResume();
        getlands();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.mycultivation_layout, container, false);
        mycultivation_recycler = root_view.findViewById(R.id.mycultivation_recycler);
        empty = root_view.findViewById(R.id.empty);
        premium = root_view.findViewById(R.id.premium);
        premium_layout = root_view.findViewById(R.id.premium_layout);
        empty.setVisibility(View.GONE);
        mycultivation_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mycultivation_recycler.setHasFixedSize(true);
        viewDialog = new ViewDialog(getActivity());
        sm = new SessionManager(getActivity());
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Payment_Activity.class);
                startActivity(i);
            }
        });


        // updating the values to table.

        if (sm.getUser().getIspremium().equalsIgnoreCase("1")) {
            premium_layout.setVisibility(View.GONE);

        } else {

            premium_layout.setVisibility(View.VISIBLE);
        }


        return root_view;
    }


    public void getlands() {
        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.getregisteredland + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        if (jsonArray.length() > 2 && !sm.getUser().getIspremium().equalsIgnoreCase("1")) {
                            for (int i = 0; i < 2; i++) {

                                Mycultivation_Model obj = new Mycultivation_Model();
                                obj.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                                obj.setLand_name(jsonArray.getJSONObject(i).optString("land_name"));
                                obj.setSoil_type(jsonArray.getJSONObject(i).optString("soil_type"));
                                obj.setDimension(jsonArray.getJSONObject(i).optString("dimension"));
                                obj.setUnit(jsonArray.getJSONObject(i).optString("unit"));
                                obj.setPower_source(jsonArray.getJSONObject(i).optString("power_source"));
                                obj.setWater_source(jsonArray.getJSONObject(i).optString("water_source"));
                                obj.setWater_type(jsonArray.getJSONObject(i).optString("water_type"));
                                obj.setPincode(jsonArray.getJSONObject(i).optString("pincode"));
                                obj.setPanchayat_office(jsonArray.getJSONObject(i).optString("panchayat_office"));
                                obj.setCurrent_location(jsonArray.getJSONObject(i).optString("current_location"));
                                obj.setCreated_at(parseDate(jsonArray.getJSONObject(i).optString("created_at")));
                                obj.setGovernment_scheme(jsonArray.getJSONObject(i).optString("government_scheme"));
                                obj.setSoil_healthcard(jsonArray.getJSONObject(i).optString("soil_healthcard"));
                                obj.setCertified_organic_former(jsonArray.getJSONObject(i).optString("certified_organic_former"));
                                obj.setScheme_year(jsonArray.getJSONObject(i).optString("scheme_year"));
                                obj.setHealthcard_date(jsonArray.getJSONObject(i).optString("healthcard_date"));
                                obj.setCertificate(jsonArray.getJSONObject(i).optString("certificate"));

                                Data.add(obj);
                            }

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                Mycultivation_Model obj = new Mycultivation_Model();
                                obj.setLand_id(jsonArray.getJSONObject(i).optString("land_id"));
                                obj.setLand_name(jsonArray.getJSONObject(i).optString("land_name"));
                                obj.setSoil_type(jsonArray.getJSONObject(i).optString("soil_type"));
                                obj.setDimension(jsonArray.getJSONObject(i).optString("dimension"));
                                obj.setUnit(jsonArray.getJSONObject(i).optString("unit"));
                                obj.setPower_source(jsonArray.getJSONObject(i).optString("power_source"));
                                obj.setWater_source(jsonArray.getJSONObject(i).optString("water_source"));
                                obj.setWater_type(jsonArray.getJSONObject(i).optString("water_type"));
                                obj.setPincode(jsonArray.getJSONObject(i).optString("pincode"));
                                obj.setPanchayat_office(jsonArray.getJSONObject(i).optString("panchayat_office"));
                                obj.setCurrent_location(jsonArray.getJSONObject(i).optString("current_location"));
                                obj.setCreated_at(parseDate(jsonArray.getJSONObject(i).optString("created_at")));
                                obj.setGovernment_scheme(jsonArray.getJSONObject(i).optString("government_scheme"));
                                obj.setSoil_healthcard(jsonArray.getJSONObject(i).optString("soil_healthcard"));
                                obj.setCertified_organic_former(jsonArray.getJSONObject(i).optString("certified_organic_former"));
                                obj.setScheme_year(jsonArray.getJSONObject(i).optString("scheme_year"));
                                obj.setHealthcard_date(jsonArray.getJSONObject(i).optString("healthcard_date"));
                                obj.setCertificate(jsonArray.getJSONObject(i).optString("certificate"));
                                Data.add(obj);
                            }
                        }


                        adapter = new Mycultivation_Adapter(getActivity(), Data, ispremium, viewDialog, new Land_Edit() {
                            @Override
                            public void update_land() {
                                getlands();

                            }
                        }, sm.getUser().getId());

                        mycultivation_recycler.setAdapter(adapter);
                        empty.setVisibility(View.GONE);
                        mycultivation_recycler.setVisibility(View.VISIBLE);
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        mycultivation_recycler.setVisibility(View.GONE);
                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                empty.setVisibility(View.VISIBLE);
                mycultivation_recycler.setVisibility(View.GONE);

            }
        });
    }

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}