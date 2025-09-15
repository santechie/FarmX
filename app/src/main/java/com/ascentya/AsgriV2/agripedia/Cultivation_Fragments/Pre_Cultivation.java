package com.ascentya.AsgriV2.agripedia.Cultivation_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Fav_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.Cultivation_Async;
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

public class Pre_Cultivation extends BaseFragment {
    View root_view;
    RecyclerView cultivation_recycler;
    Fav_Adapter fav_adapter;
    List<Fav_Model> Data;
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
                        if (result.getCultivation() != null) {
                            loader_view.setVisibility(View.GONE);
                            Data.clear();
                            FavdataConverter obj = new FavdataConverter();

                            Data.addAll(obj.stringToList(result.getCultivation()));

                            loader_view.setVisibility(View.GONE);
                            fav_adapter = new Fav_Adapter(getActivity(), Data);

                            cultivation_recycler.setAdapter(fav_adapter);


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
        AndroidNetworking.post(Webservice.getcultivation)
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
                    List<String> modelList;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Fav_Model obj = new Fav_Model();


                        if (!jsonArray.getJSONObject(i).getJSONArray("cutivate_planting_season").get(0).equals("")) {
                            obj.setTitle(getString(R.string.Cultivation_Season));
//

                            modelList = new ArrayList<>();


                            modelList.add(jsonArray.getJSONObject(i).getJSONArray("cutivate_planting_season").get(0).toString());

                            obj.setDescription(modelList);

                            Data.add(obj);
                        }


                        if (!jsonArray.getJSONObject(i).getJSONArray("cutivate_spacing").get(0).equals("")) {

                            obj = new Fav_Model();

                            obj.setTitle(getString(R.string.Plant_Spacing_Information));

                            modelList = new ArrayList<>();

                            JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("cutivate_spacing");

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                modelList.add(jsonArray1.get(j).toString());
                            }

                            obj.setDescription(modelList);
                            Data.add(obj);
                        }


//                        if (!jsonArray.getJSONObject(i).getJSONArray("cutivate_spacing").get(0).equals("")) {
//
//                            obj = new PreCultivation_Model();
//
//                            obj.setTitle("Plant Spacing Information");
//                            obj.setCheck(false);
//                            disc_model = new Cultivation_Disc_Model();
//                            disc_model.setTitle(jsonArray.getJSONObject(i).getJSONArray("cutivate_spacing").toString());
//                            modelList = new ArrayList<>();
//                            modelList.add(disc_model);
//                            obj.setData(modelList);
//                            Data.add(obj);
//                        }
                        if (!jsonArray.getJSONObject(i).getJSONArray("cutivate_field_preparations").get(0).equals("")) {

                            obj = new Fav_Model();

                            obj.setTitle(getString(R.string.Field_Preparation));


                            modelList = new ArrayList<>();
                            modelList.add(jsonArray.getJSONObject(i).getJSONArray("cutivate_field_preparations").get(0).toString());
                            obj.setDescription(modelList);
                            Data.add(obj);
                        }


                        if (!jsonArray.getJSONObject(i).getJSONArray("cutivate_fertilizer_manure").get(0).equals("")) {

                            obj = new Fav_Model();

                            obj.setTitle(getString(R.string.Cutivate_Fertilizer_Manure));

                            modelList = new ArrayList<>();
                            modelList.add(jsonArray.getJSONObject(i).getJSONArray("cutivate_fertilizer_manure").get(0).toString());
                            obj.setDescription(modelList);
                            Data.add(obj);
                        }


                        if (!jsonArray.getJSONObject(i).getJSONArray("cultivate_PGR").get(0).equals("")) {
                            obj = new Fav_Model();

                            obj.setTitle(getString(R.string.PGR_Information));


                            modelList = new ArrayList<>();
                            modelList.add(jsonArray.getJSONObject(i).getJSONArray("cultivate_PGR").get(0).toString());
                            obj.setDescription(modelList);
                            Data.add(obj);
                        }


                        if (!jsonArray.getJSONObject(i).getJSONArray("cutivate_irrigation").get(0).equals("")) {

                            obj = new Fav_Model();

                            obj.setTitle(getString(R.string.Irrigation_Information));


                            modelList = new ArrayList<>();
                            modelList.add(jsonArray.getJSONObject(i).getJSONArray("cutivate_irrigation").get(0).toString());
                            obj.setDescription(modelList);
                            Data.add(obj);
                        }

//                        obj = new PreCultivation_Model();
//
//                        obj.setTitle("test");
//                        obj.setCheck(false);
//                        disc_model = new Cultivation_Disc_Model();
//                        disc_model.setTitle("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s");
//                        modelList = new ArrayList<>();
//                        modelList.add(disc_model);
//                        obj.setData(modelList);
//                        Data.add(obj);
//
//                        obj = new PreCultivation_Model();
//
//                        obj.setTitle("test two");
//                        obj.setCheck(false);
//                        disc_model = new Cultivation_Disc_Model();
//                        disc_model.setTitle("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s");
//                        modelList = new ArrayList<>();
//                        modelList.add(disc_model);
//                        obj.setData(modelList);
//                        Data.add(obj);
//
//                        obj = new PreCultivation_Model();
//
//                        obj.setTitle("test three");
//                        obj.setCheck(false);
//                        disc_model = new Cultivation_Disc_Model();
//                        disc_model.setTitle("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s");
//                        modelList = new ArrayList<>();
//                        modelList.add(disc_model);
//                        obj.setData(modelList);
//                        Data.add(obj);

                    }

                    if (Data.size() > 0) {
                        empty.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);
                        loader_view.setVisibility(View.GONE);

                        fav_adapter = new Fav_Adapter(getActivity(), Data);

                        cultivation_recycler.setAdapter(fav_adapter);


                        FavdataConverter con = new FavdataConverter();

                        Cultivation_Async fav = new Cultivation_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
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

//
//                    preCultivationAdapter = new PreCultivation_Adapter(getActivity(), Data);
//
//                    cultivation_recycler.setAdapter(preCultivationAdapter);


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
