package com.ascentya.AsgriV2.Mycrops_Mainfragments.Cropsub_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Activitys.Payment_Activity;
import com.ascentya.AsgriV2.Adapters.Members_Adapter;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.Member_Edit;
import com.ascentya.AsgriV2.Models.Members_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Members_fragments extends BaseFragment {
    View root_view;
    RecyclerView members_recycler;
    LinearLayout addmember_layout, memberslisting_layout;
    ElasticImageView add_members;
    Members_Adapter adapter;
    List<Members_Model> Data;
    Spinner member_gender;
    EditText member_name, member_age, member_relation, member_farmingexp;
    ElasticButton addmember;
    SessionManager sm;
    TextView memberslisting;


    Boolean ispremium;
    ElasticButton premium;

    ViewDialog viewDialog;
    boolean deleteAccess;


    public void onResume() {
        super.onResume();
        getmembers();
        DeleteBus.getInstance().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        DeleteBus.getInstance().unregister(this);
    }


    @Subscribe
    public void onTeamClicked(DeleteEvent event) {
        if (event.getFlag().equalsIgnoreCase("show_addlayout")) {
            memberslisting_layout.setVisibility(View.GONE);
            addmember_layout.setVisibility(View.VISIBLE);
        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.members_layout, container, false);

        members_recycler = root_view.findViewById(R.id.members_recycler);
        premium = root_view.findViewById(R.id.premium);
        members_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        members_recycler.setHasFixedSize(true);
        viewDialog = new ViewDialog(getActivity());

        sm = new SessionManager(getActivity());

        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Payment_Activity.class);
                startActivity(i);
            }
        });

        deleteAccess = getModuleManager().canDelete(Components.MyFarm.MEMBERS);
        //deleteAccess = checkSubscription(Components.MyFarm.MEMBERS, ModuleManager.ACCESS.DELETE);
        // updating the values to table.
        return root_view;

    }

    public void getmembers() {

        /*if (requireActivity() != null
                && requireActivity() instanceof BaseActivity
                && !((BaseActivity) requireActivity()).checkSubscription(Components.MyFarm.MEMBERS , ModuleManager.ACCESS.VIEW))
            return;*/

        viewDialog.showDialog();
        Data = new ArrayList<>();

        AndroidNetworking.get(Webservice.getmemberlist + sm.getUser().getId())

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

                        for (int i = 0; i < jsonArray.length(); i++) {


                            Members_Model obj = new Members_Model();
                            obj.setMember_id(jsonArray.getJSONObject(i).optString("member_id"));
                            obj.setMember_name(jsonArray.getJSONObject(i).optString("member_name"));
                            if (jsonArray.getJSONObject(i).optString("member_age").equalsIgnoreCase("0")) {
                                obj.setMember_age("");

                            } else {
                                obj.setMember_age(jsonArray.getJSONObject(i).optString("member_age"));

                            }
                            obj.setMember_gender(jsonArray.getJSONObject(i).optString("member_gender"));
                            obj.setFarming_exp(jsonArray.getJSONObject(i).optString("member_experience"));
                            obj.setRelation(jsonArray.getJSONObject(i).optString("member_relation"));
                            obj.setMember_payment(jsonArray.getJSONObject(i).optString("member_payment"));
                            obj.setMember_bilingtype(jsonArray.getJSONObject(i).optString("member_bilingtype"));
                            obj.setCreated_at(parseDate(jsonArray.getJSONObject(i).optString("created_at")));
                            Data.add(obj);
                        }


                        adapter = new Members_Adapter(getActivity(),
                                new Members_Adapter.Action() {
                                    @Override
                                    public void update(){
                                        getmembers();
                                    }

                                    @Override
                                    public boolean showDelete() {
                                        return deleteAccess;
                                    }

                                    @Override
                                    public boolean canDelete() {

                                        deleteAccess = getModuleManager().canDelete(Components.MyFarm.MEMBERS);
                                        return false;
                                    }
                                },
                                Data,
                                viewDialog,sm.getUser().getId(),
                                new Member_Edit() {
                                    @Override
                                    public void update_member() {
                                        getmembers();
                                    }
                                });

                        members_recycler.setAdapter(adapter);

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();

                    e.printStackTrace();
                }

                if (root_view != null) {
                    System.out.println("Root View Updated");
                    root_view.findViewById(R.id.noMembers)
                            .setVisibility(Data.isEmpty() ? View.VISIBLE : View.GONE);
                }else {
                    System.out.println("Root View Null");
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();

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
