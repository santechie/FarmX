package com.ascentya.AsgriV2.agripedia.Diseases_Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.ControlMeasure_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.DiseaseControlm_Async;
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.ControlMeasure_Model;
import com.ascentya.AsgriV2.Object_Converter.PControl_Converter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Token_session.Lang_Token;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.bumptech.glide.Glide;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Control_Measure extends BaseFragment {
    View root_view;
    RecyclerView controlmeasure_recycler;
    ControlMeasure_Adapter symtoms_adapter;
    List<ControlMeasure_Model> Data;

    TextView title, search_empty, searchbelow_texts;
    LinearLayout empty, main_layout, search_do;
    Lang_Token tk;
    CircleImageView loader;
    ConstraintLayout loader_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.controlmeasures_layout, container, false);
        controlmeasure_recycler = root_view.findViewById(R.id.controlmeasure_recycler);
        Data = new ArrayList<>();
        tk = new Lang_Token(getActivity());
        controlmeasure_recycler = root_view.findViewById(R.id.controlmeasure_recycler);
        empty = root_view.findViewById(R.id.empty);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);
        controlmeasure_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        controlmeasure_recycler.setHasFixedSize(true);

        search_do = root_view.findViewById(R.id.search_do);


        search_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("true"));
            }
        });

        loader = root_view.findViewById(R.id.loading_view);
        loader_view = root_view.findViewById(R.id.loader_view);
        loader.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(Webservice.Searchicon).error(R.drawable.loder_logo).into(loader);

        if (Webservice.Searchvalue.equalsIgnoreCase("none")) {
            empty.setVisibility(View.VISIBLE);
            main_layout.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);

            title = root_view.findViewById(R.id.title);

            GetSingleObject run = new GetSingleObject(getActivity(), new Async_Single<Info_Model>() {
                @Override
                public void onTaskComplete(Info_Model result) {

                    if (result != null && result.getDiseas_controlmeasure() != null) {
                        loader_view.setVisibility(View.GONE);
                        Data.clear();
                        PControl_Converter con = new PControl_Converter();

                        Data.addAll(con.stringToSomeObjectList(result.getDiseas_controlmeasure()));

                        if (Data.size() > 0) {
                            title.setText(getString(R.string.Control_Measure) + " " + Webservice.Searchvalue);

                            symtoms_adapter = new ControlMeasure_Adapter(getActivity(), Data);

                            controlmeasure_recycler.setAdapter(symtoms_adapter);
                            empty.setVisibility(View.GONE);
                            main_layout.setVisibility(View.VISIBLE);

                        } else {
                            search_empty.setText(getString(R.string.no_data));
                            searchbelow_texts.setText("");
                            empty.setVisibility(View.VISIBLE);
                            main_layout.setVisibility(View.GONE);
                        }


                    } else {

                        getsymt(tk.getusename());

                    }
                }
            }, Webservice.Search_id);
            run.execute();


        }
        return root_view;
    }

    public void getsymt(final String lang) {
        AndroidNetworking.post(Webservice.getdiseasscontrolmeasure)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(getContext(), jsonObject)){
                    return;
                }
                try {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ControlMeasure_Model obj = new ControlMeasure_Model();

                        obj.setTitle(jsonArray.getJSONObject(i).optString("pd_disease").trim());
//                        obj.setCover_image(jsonArray.getJSONObject(i).optString("varieties_imgs"));

                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("pd_disease_control_measures");


                        if (jsonArray1.length() > 0) {
                            List<String> controlmeasure = new ArrayList<>();
                            for (int j = 0; j < jsonArray1.length(); j++) {

                                String diseas_control = jsonArray1.get(j).toString().trim();

                                if (!diseas_control.equalsIgnoreCase("")) {
                                    controlmeasure.add(diseas_control);
                                }

                            }

                            obj.setDesc(controlmeasure);
                        }


                        Data.add(obj);

                    }
                    loader_view.setVisibility(View.GONE);

                    if (Data.size() > 0) {
                        title.setText(getString(R.string.Control_Measure) + " " + Webservice.Searchvalue);

                        PControl_Converter con = new PControl_Converter();

                        DiseaseControlm_Async obj = new DiseaseControlm_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onTaskComplete(String result) {

                            }
                        }, Webservice.Search_id, con.someObjectListToString(Data));
                        obj.execute();
                        symtoms_adapter = new ControlMeasure_Adapter(getActivity(), Data);

                        controlmeasure_recycler.setAdapter(symtoms_adapter);
                    } else {
                        search_empty.setText(getString(R.string.no_data));
                        searchbelow_texts.setText("");
                        empty.setVisibility(View.VISIBLE);
                        main_layout.setVisibility(View.GONE);
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
}
