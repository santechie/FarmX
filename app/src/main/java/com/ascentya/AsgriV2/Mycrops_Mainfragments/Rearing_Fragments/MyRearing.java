package com.ascentya.AsgriV2.Mycrops_Mainfragments.Rearing_Fragments;

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
import com.ascentya.AsgriV2.Adapters.MYanimals_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Crops_Edit;
import com.ascentya.AsgriV2.Models.Myanimals_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
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

public class MyRearing extends Fragment {
    View root_view;
    RecyclerView mycultivation_recycler;
    MYanimals_Adapter adapter;
    List<Myanimals_Model> Data;
    ViewDialog viewDialog;
    SessionManager sm;
    LinearLayout empty;

    LinearLayout premium_layout;
    Boolean ispremium;
    ElasticButton premium;

    @Override
    public void onResume() {
        super.onResume();

        getanimals();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.myrear_layout, container, false);
        mycultivation_recycler = root_view.findViewById(R.id.myrear_recycler);
        empty = root_view.findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        premium = root_view.findViewById(R.id.premium);
        premium_layout = root_view.findViewById(R.id.premium_layout);

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

//        if (sm.getUser().getIspremium().equalsIgnoreCase("1")) {
//            premium_layout.setVisibility(View.GONE);
//
//        } else {
//
//            premium_layout.setVisibility(View.VISIBLE);
//        }

        return root_view;
    }


    public void getanimals() {
        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.animallist + sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                viewDialog.hideDialog();
                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 2 && !sm.getUser().getIspremium().equalsIgnoreCase("1")) {

                            for (int i = 0; i < 2; i++) {
                                Myanimals_Model obj = new Myanimals_Model();
                                obj.setAnimal_id(jsonArray.getJSONObject(i).optString("animal_id"));
                                obj.setAnimal_name(jsonArray.getJSONObject(i).optString("animal_name"));
                                obj.setAnimal_breed(jsonArray.getJSONObject(i).optString("animal_breed"));
                                obj.setAnimal_petname(jsonArray.getJSONObject(i).optString("animal_count"));
                                obj.setAnimal_gender(jsonArray.getJSONObject(i).optString("animal_gender"));
                                obj.setAnimal_age(jsonArray.getJSONObject(i).optString("animal_age"));
                                obj.setAnimal_prediseases(jsonArray.getJSONObject(i).optString("animal_prediseases"));
                                obj.setDiseases_disc(jsonArray.getJSONObject(i).optString("diseases_disc"));
                                obj.setCreated_at(parseDate(jsonArray.getJSONObject(i).optString("created_at")));
                                Data.add(obj);

                            }
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                Myanimals_Model obj = new Myanimals_Model();
                                obj.setAnimal_id(jsonArray.getJSONObject(i).optString("animal_id"));
                                obj.setAnimal_name(jsonArray.getJSONObject(i).optString("animal_name"));
                                obj.setAnimal_breed(jsonArray.getJSONObject(i).optString("animal_breed"));
                                obj.setAnimal_petname(jsonArray.getJSONObject(i).optString("animal_count"));
                                obj.setAnimal_gender(jsonArray.getJSONObject(i).optString("animal_gender"));
                                obj.setAnimal_age(jsonArray.getJSONObject(i).optString("animal_age"));
                                obj.setAnimal_prediseases(jsonArray.getJSONObject(i).optString("animal_prediseases"));
                                obj.setDiseases_disc(jsonArray.getJSONObject(i).optString("diseases_disc"));
                                obj.setCreated_at(parseDate(jsonArray.getJSONObject(i).optString("created_at")));
                                Data.add(obj);
                            }
                        }


                        adapter = new MYanimals_Adapter(getActivity(), Data, viewDialog, new Crops_Edit() {
                            @Override
                            public void update_crop() {
                                getanimals();
                            }
                        }, sm);

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
