package com.ascentya.AsgriV2.agripedia.Cultivation_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Fav_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.FavCondition_Async;
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.Database_Room.FavdataConverter;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.Fav_Model;
import com.ascentya.AsgriV2.R;
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

public class Fav_Condition extends BaseFragment {
    View root_view;
    RecyclerView fav_condition;

    List<Fav_Model> Data;
    Fav_Adapter fav_adapter;
    LinearLayout empty, main_layout, search_do;
    TextView title, search_empty, searchbelow_texts;
    CircleImageView loader;
    ConstraintLayout loader_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.favcondition_layout, container, false);
        fav_condition = root_view.findViewById(R.id.fav_condition);
        empty = root_view.findViewById(R.id.empty);
        title = root_view.findViewById(R.id.title);
        main_layout = root_view.findViewById(R.id.main_layout);
        fav_condition.setLayoutManager(new LinearLayoutManager(getActivity()));
        fav_condition.setHasFixedSize(true);
        Data = new ArrayList<>();
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        title.setText(getString(R.string.favorable_condition) + " " + Webservice.Searchvalue);
        loader = root_view.findViewById(R.id.loading_view);

        search_do = root_view.findViewById(R.id.search_do);


        search_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("true"));
            }
        });
        loader_view = root_view.findViewById(R.id.loader_view);
        loader_view.setVisibility(View.VISIBLE);

        Glide.with(getActivity()).load(Webservice.Searchicon).error(R.drawable.loder_logo).into(loader);

        if (Webservice.Searchvalue.equalsIgnoreCase("none")) {
            empty.setVisibility(View.VISIBLE);
            main_layout.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);


            GetSingleObject run = new GetSingleObject(getActivity(), new Async_Single<Info_Model>() {
                @Override
                public void onTaskComplete(Info_Model result) {

                    if (result != null && result.getFav_condition() != null) {
                        loader_view.setVisibility(View.GONE);
                        Data.clear();
                        FavdataConverter obj = new FavdataConverter();

                        Data.addAll(obj.stringToList(result.getFav_condition()));


                        if (Data.size() > 0) {
                            FavdataConverter con = new FavdataConverter();

                            FavCondition_Async fav = new FavCondition_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                                @Override
                                public void onTaskComplete(String result) {

                                }
                            }, Webservice.Search_id, con.ListToString(Data));
                            fav.execute();

                            loader_view.setVisibility(View.GONE);
                            fav_adapter = new Fav_Adapter(getActivity(), Data);

                            fav_condition.setAdapter(fav_adapter);
                        } else {
                            loader_view.setVisibility(View.GONE);
                            search_empty.setText(getString(R.string.no_data));
                            searchbelow_texts.setText("");
                            empty.setVisibility(View.VISIBLE);
                            main_layout.setVisibility(View.GONE);


                        }


                    } else {

                        getfavcondition();

                    }
                }
            }, Webservice.Search_id);
            run.execute();


        }
        return root_view;
    }

    public void getfavcondition() {
        AndroidNetworking.post(Webservice.getfavcondition)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId() )
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(getContext(), jsonObject)){
                    return;
                }
                try {

                    if (!jsonObject.getJSONArray("data").isNull(0)) {
                        JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);

                        JSONArray jsonArray = jsonObject1.getJSONArray("fc_climate_soil");
                        Fav_Model obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Climate_and_soli_condition));
                        List<String> Datas = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String fc_clim = jsonArray.get(i).toString().trim();
                            if (!fc_clim.equalsIgnoreCase("")) {
                                Datas.add(fc_clim);
                            }
                        }
                        obj.setDescription(Datas);
                        if (Datas.size() > 0) {
                            Data.add(obj);
                        }


                        JSONArray jsonArray1 = jsonObject1.getJSONArray("fc_seed_rate");
                        obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Seed_Method_Rate));
                        Datas = new ArrayList<>();
                        for (int i = 0; i < jsonArray1.length(); i++) {

                            String seedprep = jsonArray1.get(i).toString().trim();

                            if (!seedprep.equalsIgnoreCase("")) {
                                Datas.add(seedprep);
                            }


                        }
                        obj.setDescription(Datas);
                        if (Datas.size() > 0) {
                            Data.add(obj);
                        }


                        JSONArray jsonArray2 = jsonObject1.getJSONArray("fc_seed_treatment");
                        obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Seed_treatment));
                        Datas = new ArrayList<>();
                        for (int i = 0; i < jsonArray2.length(); i++) {

                            String seed_treat = jsonArray2.get(i).toString().trim();
                            if (!seed_treat.equalsIgnoreCase("")) {
                                Datas.add(seed_treat);
                            }


                        }
                        obj.setDescription(Datas);
                        if (Datas.size() > 0) {
                            Data.add(obj);
                        }


                        JSONArray jsonArray3 = jsonObject1.getJSONArray("fc_nursery");
                        obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Nursery_Details));
                        Datas = new ArrayList<>();
                        for (int i = 0; i < jsonArray3.length(); i++) {

                            String nur_detail = jsonArray3.get(i).toString().trim();
                            if (!nur_detail.equalsIgnoreCase("")) {
                                Datas.add(nur_detail);
                            }


                        }
                        obj.setDescription(Datas);
                        if (Datas.size() > 0) {
                            Data.add(obj);
                        }

                        if (Data.size() > 0) {
                            FavdataConverter con = new FavdataConverter();

                            FavCondition_Async fav = new FavCondition_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                                @Override
                                public void onTaskComplete(String result) {

                                }
                            }, Webservice.Search_id, con.ListToString(Data));
                            fav.execute();

                            loader_view.setVisibility(View.GONE);
                            fav_adapter = new Fav_Adapter(getActivity(), Data);

                            fav_condition.setAdapter(fav_adapter);
                        } else {
                            loader_view.setVisibility(View.GONE);
                            search_empty.setText(getString(R.string.no_data));
                            searchbelow_texts.setText("");
                            empty.setVisibility(View.VISIBLE);
                            main_layout.setVisibility(View.GONE);


                        }
                    } else {
                        loader_view.setVisibility(View.GONE);
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
