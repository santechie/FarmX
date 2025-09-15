package com.ascentya.AsgriV2.agripedia.Pests_Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Identification_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.AsyncTasks.Identification_Async;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.Identification_Stages;
import com.ascentya.AsgriV2.Object_Converter.PIdentification_Converter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Token_session.Lang_Token;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Identificatoion_Fargmenr extends BaseFragment {
    View root_view;
    RecyclerView identify_recycler;
    Identification_Adapter symtoms_adapter;
    List<Identification_Stages> Data;

    TextView title, search_empty, searchbelow_texts;
    LinearLayout empty, main_layout, search_do;
    Lang_Token tk;
    String crop_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.identificatoin_layout, container, false);
        identify_recycler = root_view.findViewById(R.id.identify_recycler);
        Data = new ArrayList<>();
        tk = new Lang_Token(getActivity());
        empty = root_view.findViewById(R.id.empty);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);
        identify_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        identify_recycler.setHasFixedSize(true);
        search_do = root_view.findViewById(R.id.search_do);


        search_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("true"));
            }
        });

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
                    if (result != null && result.getPests_identification() != null) {
                        Data.clear();
                        PIdentification_Converter con = new PIdentification_Converter();

                        Data.addAll(con.stringToSomeObjectList(result.getPests_identification()));

                        if (Data.size() > 0) {
                            title.setText(getString(R.string.pests_Identify) + " " + Webservice.Searchvalue);

                            symtoms_adapter = new Identification_Adapter(getActivity(), Data);

                            identify_recycler.setAdapter(symtoms_adapter);
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
        AndroidNetworking.post(Webservice.getpestsidentification)
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
                        Identification_Stages obj = new Identification_Stages();
                        crop_id = jsonArray.getJSONObject(i).optString("pest_identify_id");
                        obj.setTitle(jsonArray.getJSONObject(i).optString("pd_pest").trim());
                        obj.setStage(getString(R.string.Identified_Stages) + " - " + jsonArray.getJSONObject(i).optString("pd_pest_identify").trim());
//                        obj.setCover_image(jsonArray.getJSONObject(i).optString("varieties_imgs"));

                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("pd_pest_identify_desc");
                        JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("pd_pest_identify_images");


                        obj.setCover_image(jsonArray.getJSONObject(i).getJSONArray("pd_pest_video").toString());
                        if (jsonArray1.length() > 0) {
                            List<String> controlmeasure = new ArrayList<>();
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                String identify_desc = jsonArray1.get(j).toString().trim();

                                if (!identify_desc.equalsIgnoreCase("")) {
                                    controlmeasure.add(identify_desc);
                                }

                            }

                            obj.setDesc(controlmeasure);
                        }

                        if (jsonArray2.length() > 0) {

                            String diseas_identify = jsonArray2.get(0).toString().trim();

                            if (!diseas_identify.equalsIgnoreCase("")) {
                                obj.setDp(diseas_identify);
                            }

                        }


                        List<String> images = new ArrayList<>();


                        images.add(jsonArray2.toString().trim());


                        obj.setGallery_Images(images);


                        Data.add(obj);

                    }

                    if (Data.size() > 0) {
                        title.setText(getString(R.string.pests_Identify) + " " + Webservice.Searchvalue);


                        PIdentification_Converter con = new PIdentification_Converter();

                        Identification_Async obj = new Identification_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onTaskComplete(String result) {

                            }
                        }, Webservice.Search_id, con.someObjectListToString(Data));
                        obj.execute();
                        symtoms_adapter = new Identification_Adapter(getActivity(), Data);

                        identify_recycler.setAdapter(symtoms_adapter);
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