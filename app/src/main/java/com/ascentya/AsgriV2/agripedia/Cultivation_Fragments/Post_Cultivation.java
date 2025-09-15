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
import com.ascentya.AsgriV2.Adapters.PostCultivation_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.AsyncTasks.PostCultivation_Async;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Database_Room.Postdata_Converter;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.PostCultivation_Model;
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

public class Post_Cultivation extends BaseFragment {
    View root_view;
    RecyclerView cultivation_recycler;
    PostCultivation_Adapter preCultivationAdapter;
    List<PostCultivation_Model> Data;
    CircleImageView loader;
    ConstraintLayout loader_view;
    private LinearLayout empty, main_layout, search_do;
    TextView search_empty, searchbelow_texts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.precultivation_layout, container, false);
        cultivation_recycler = root_view.findViewById(R.id.cultivation_recycler);
        cultivation_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        cultivation_recycler.setHasFixedSize(true);
        empty = root_view.findViewById(R.id.empty);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);
        loader = root_view.findViewById(R.id.loading_view);
        loader_view = root_view.findViewById(R.id.loader_view);
        loader.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(Webservice.Searchicon).error(R.drawable.loder_logo).into(loader);
        Data = new ArrayList<>();


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


            GetSingleObject run = new GetSingleObject(getActivity(), new Async_Single<Info_Model>() {
                @Override
                public void onTaskComplete(Info_Model result) {

                    if (result != null) {
                        if (result.getPost_cultivation() != null) {
                            loader_view.setVisibility(View.GONE);
                            Data.clear();
                            Postdata_Converter obj = new Postdata_Converter();

                            Data.addAll(obj.stringToList(result.getPost_cultivation()));

                            loader_view.setVisibility(View.GONE);
                            preCultivationAdapter = new PostCultivation_Adapter(getActivity(), Data);

                            cultivation_recycler.setAdapter(preCultivationAdapter);


                        } else {

                            gettexonomy();

                        }
                    } else {
                        gettexonomy();
                    }


                }
            }, Webservice.Search_id);
            run.execute();


        }


        return root_view;
    }

    public void gettexonomy() {
        AndroidNetworking.post(Webservice.getpostcultivation)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(getContext(), jsonObject)){
                    return;
                }
                try {

                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("ac");
                    List<String> modelList;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        PostCultivation_Model obj = new PostCultivation_Model();


                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                        if (!jsonObject1.getJSONArray("ac_mulching").get(0).equals("")) {
                            obj.setTitle("Mulching");

                            modelList = new ArrayList<>();


                            modelList.add(jsonObject1.getJSONArray("ac_mulching").get(0).toString());

                            obj.setData(modelList);

                            Data.add(obj);
                        }


                        if (!jsonObject1.getJSONArray("ac_weed_control").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Weed_ontrol));


                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_weed_control").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }
                        if (!jsonObject1.getJSONArray("ac_netting").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Netting));


                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_netting").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_pinching").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Pinching));


                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_pinching").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }


                        if (!jsonObject1.getJSONArray("ac_disbudding").get(0).equals("")) {
                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Disbudding));


                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_disbudding").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }


                        if (!jsonObject1.getJSONArray("ac_dead_shoot_removal").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Dead_Shoot_Removal));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_dead_shoot_removal").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }


                        if (!jsonObject1.getJSONArray("ac_soil_loosening_beds").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Soil_Loosening_Beds));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_soil_loosening_beds").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_bending").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Bending));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_bending").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_defoliation").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Defoliation));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_defoliation").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_nipping_tipping").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Nipping_tipping));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_nipping_tipping").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_desuckering").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Desuckering));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_desuckering").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }
                        if (!jsonObject1.getJSONArray("ac_blindness").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Blindness));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_blindness").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_light_requirement").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Light_requirement));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_light_requirement").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }
                        if (!jsonObject1.getJSONArray("ac_shade_regulation").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Shade_regulation));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_shade_regulation").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_earthing_up").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Earthing_up));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_earthing_up").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_inter_cropping").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Inter_cropping));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_inter_cropping").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_ratoon_crop").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Ratoon_crop));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_ratoon_crop").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_detrashing").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Detrashing));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_detrashing").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_propping").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Propping));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_propping").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_repotting").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Repotting));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_repotting").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_staking").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Staking));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_staking").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }
                        if (!jsonObject1.getJSONArray("ac_supporting").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Supporting));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_supporting").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_ablation").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Ablation));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_ablation").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }
                        if (!jsonObject1.getJSONArray("ac_trash_mulching").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Trash_mulching));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_trash_mulching").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_trash_mulching").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Trash_mulching));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_trash_mulching").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }
                        if (!jsonObject1.getJSONArray("ac_training").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Training));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_training").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }
                        if (!jsonObject1.getJSONArray("ac_pruning").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Pruning));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_pruning").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_trailing").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Trailing));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_trailing").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_training_hybrids").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Training_hybrids));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_training_hybrids").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_micronutrient_spray").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Micronutrient_spray));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_micronutrient_spray").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_duration").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Duration));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_duration").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_harvesting").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Harvesting));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_harvesting").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                        if (!jsonObject1.getJSONArray("ac_yield").get(0).equals("")) {

                            obj = new PostCultivation_Model();

                            obj.setTitle(getString(R.string.Yield));

                            modelList = new ArrayList<>();
                            modelList.add(jsonObject1.getJSONArray("ac_yield").get(0).toString());
                            obj.setData(modelList);
                            Data.add(obj);
                        }

                    }

                    if (Data.size() > 0) {
                        empty.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);
                        loader_view.setVisibility(View.GONE);
                        preCultivationAdapter = new PostCultivation_Adapter(getActivity(), Data);

                        cultivation_recycler.setAdapter(preCultivationAdapter);


                        Postdata_Converter con = new Postdata_Converter();

                        PostCultivation_Async fav = new PostCultivation_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onTaskComplete(String result) {

                            }
                        }, Webservice.Search_id, con.ListToString(Data));
                        fav.execute();
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
