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
import com.ascentya.AsgriV2.Adapters.NutrientDeficiency_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.AsyncTasks.NutrientDef_Async;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.NutrientDef_Model;
import com.ascentya.AsgriV2.Models.PointerImages_Model;
import com.ascentya.AsgriV2.Object_Converter.NutrientDeficiency_Converter;
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

public class Nutrient_Deficiency extends BaseFragment {
    private View root_view;
    private RecyclerView fav_condition;
    private List<NutrientDef_Model> Data;
    private NutrientDeficiency_Adapter fav_adapter;
    private LinearLayout empty, main_layout, search_do;
    private TextView title, search_empty, searchbelow_texts;
    private CircleImageView loader;
    private ConstraintLayout loader_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.nutrientdeficiency_layout, container, false);
        fav_condition = root_view.findViewById(R.id.nutrient_Deficiency);
        empty = root_view.findViewById(R.id.empty);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);
        fav_condition.setLayoutManager(new LinearLayoutManager(getActivity()));
        fav_condition.setHasFixedSize(true);
        Data = new ArrayList<>();
        loader = root_view.findViewById(R.id.loading_view);
        loader_view = root_view.findViewById(R.id.loader_view);
        loader.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(Webservice.Searchicon).error(R.drawable.loder_logo).into(loader);

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
                    loader_view.setVisibility(View.GONE);


                    if (result != null && result.getNutrient_dificiency() != null) {
                        NutrientDeficiency_Converter con = new NutrientDeficiency_Converter();
                        Data.clear();
                        Data.addAll(con.stringToSomeObjectList(result.getNutrient_dificiency()));


                        if (Data.size() > 0) {
                            title.setText(getString(R.string.Nutritional_Deficiency) + " " + Webservice.Searchvalue);

                            empty.setVisibility(View.GONE);
                            main_layout.setVisibility(View.VISIBLE);


                            fav_adapter = new NutrientDeficiency_Adapter(getActivity(), Data);

                            fav_condition.setAdapter(fav_adapter);

                        } else {

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
        AndroidNetworking.post(Webservice.getnjutrientdeficiency)
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
                        NutrientDef_Model obj = new NutrientDef_Model();
                        obj.setName(jsonArray.getJSONObject(i).optString("pd_name"));
                        obj.setFirstleter(jsonArray.getJSONObject(i).optString("pd_symbols"));
                        obj.setNd_video(jsonArray.getJSONObject(i).optString("nd_video"));


                        JSONArray jsonArray_sym = jsonArray.getJSONObject(i).getJSONArray("pd_deficiency_symptoms");
                        List<PointerImages_Model> sym_Data = new ArrayList<>();
                        PointerImages_Model pointer_obj = null;
                        for (int j = 0; j < jsonArray_sym.length(); j++) {
                            pointer_obj = new PointerImages_Model();


                            String n_sym = jsonArray_sym.getJSONObject(j).getJSONArray("content").get(0).toString().trim();
                            if (!n_sym.equalsIgnoreCase("")) {
                                pointer_obj.setDesc(n_sym);


                            }

                            JSONArray jsonArray1 = jsonArray_sym.getJSONObject(j).getJSONArray("image");
                            if (jsonArray1 != null) {
                                pointer_obj.setImages(jsonArray1.toString());
                            }


                            sym_Data.add(pointer_obj);

                        }
                        obj.setSymptoms(sym_Data);

                        JSONArray jsonArray_cor = jsonArray.getJSONObject(i).getJSONArray("pd_corrective_measures");
                        List<String> cor_Data = new ArrayList<>();
                        for (int j = 0; j < jsonArray_cor.length(); j++) {

                            String n_cor = jsonArray_cor.get(j).toString().trim();
                            if (!n_cor.equalsIgnoreCase("")) {
                                cor_Data.add(jsonArray_cor.get(j).toString());
                            }

                        }
                        obj.setCorrective_measure(cor_Data);

                        Data.add(obj);


                    }
                    loader_view.setVisibility(View.GONE);

                    if (Data.size() > 0) {
                        title.setText(getString(R.string.Nutritional_Deficiency) + " " + Webservice.Searchvalue);


                        NutrientDeficiency_Converter con = new NutrientDeficiency_Converter();

                        NutrientDef_Async obj = new NutrientDef_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onTaskComplete(String result) {

                            }
                        }, Webservice.Search_id, con.someObjectListToString(Data));
                        obj.execute();
                        fav_adapter = new NutrientDeficiency_Adapter(getActivity(), Data);

                        fav_condition.setAdapter(fav_adapter);
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
