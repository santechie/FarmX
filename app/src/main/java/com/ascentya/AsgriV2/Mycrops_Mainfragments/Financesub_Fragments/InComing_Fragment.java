package com.ascentya.AsgriV2.Mycrops_Mainfragments.Financesub_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Income_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Member_Edit;
import com.ascentya.AsgriV2.Models.Income_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InComing_Fragment extends Fragment {
    View root_view;
    RecyclerView members_recycler;
    Income_Adapter adapter;
    List<Income_Model> Data;
    SessionManager sm;

    ViewDialog viewDialog;
    Calendar cal;

    @Override
    public void onResume() {
        super.onResume();
        getmembers();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.financeincom_layout, container, false);
        members_recycler = root_view.findViewById(R.id.members_recycler);
        members_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        members_recycler.setHasFixedSize(true);
        viewDialog = new ViewDialog(getActivity());

        sm = new SessionManager(getActivity());


        return root_view;
    }

    public void getmembers() {
        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get(Webservice.add_activitylist + sm.getUser().getId())

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

                            Income_Model obj = new Income_Model(jsonArray.getJSONObject(i).optString("activity_id"), jsonArray.getJSONObject(i).optString("activity_land"), jsonArray.getJSONObject(i).optString("activity_crop"), jsonArray.getJSONObject(i).optString("activity"), jsonArray.getJSONObject(i).optString("activity_resources"), jsonArray.getJSONObject(i).optString("activity_start"), jsonArray.getJSONObject(i).optString("activity_end"), jsonArray.getJSONObject(i).optString("members"), jsonArray.getJSONObject(i).optString("machines"), jsonArray.getJSONObject(i).optString("animals"), jsonArray.getJSONObject(i).optString("others"));
                            Data.add(obj);

                        }

                        adapter = new Income_Adapter(getActivity(), sm.getUser().getId(), Data, viewDialog, new Member_Edit() {
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

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();

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

    public void hidesoftkeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
