package com.ascentya.AsgriV2.agripedia.Info_Fragments;

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
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.AsyncTasks.NutrientValues_Async;
import com.ascentya.AsgriV2.Database_Room.FavdataConverter;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.Fav_Model;
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

public class Nutrient_Values extends BaseFragment {
    private View root_view;
    private RecyclerView fav_condition;
    private List<Fav_Model> Data;
    private Fav_Adapter fav_adapter;
    private LinearLayout empty, main_layout, search_do;
    private TextView title, search_empty, searchbelow_texts;
    private Lang_Token tk;
    private CircleImageView loader;
    private ConstraintLayout loader_view;
    String crop_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.favcondition_layout, container, false);
        fav_condition = root_view.findViewById(R.id.fav_condition);
        empty = root_view.findViewById(R.id.empty);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);
        fav_condition.setLayoutManager(new LinearLayoutManager(getActivity()));
        fav_condition.setHasFixedSize(true);
        Data = new ArrayList<>();
        tk = new Lang_Token(getActivity());
        title = root_view.findViewById(R.id.title);

        loader = root_view.findViewById(R.id.loading_view);
        loader_view = root_view.findViewById(R.id.loader_view);
        loader.setVisibility(View.VISIBLE);

        search_do = root_view.findViewById(R.id.search_do);


        search_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bus bus = DeleteBus.getInstance();
                bus.post(new DeleteEvent("true"));
            }
        });
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

                    if (result != null) {
                        if (result.getNutrient_values() != null) {
                            loader_view.setVisibility(View.GONE);
                            Data.clear();
                            FavdataConverter obj = new FavdataConverter();

                            Data.addAll(obj.stringToList(result.getNutrient_values()));

                            if (Data.size() > 0) {
                                title.setText(getString(R.string.Nutrirional_Values_of) + " " + Webservice.Searchvalue + " (100 G)");
                                loader_view.setVisibility(View.GONE);
                                fav_adapter = new Fav_Adapter(getActivity(), Data);

                                fav_condition.setAdapter(fav_adapter);
                                empty.setVisibility(View.GONE);
                                main_layout.setVisibility(View.VISIBLE);

                            } else {
                                search_empty.setText(getString(R.string.no_data));
                                searchbelow_texts.setText("");
                                empty.setVisibility(View.VISIBLE);
                                main_layout.setVisibility(View.GONE);
                            }

                        } else {

                            getfavcondition(tk.getusename());

                        }
                    } else {
                        getfavcondition(tk.getusename());
                    }


                }
            }, Webservice.Search_id);
            run.execute();


        }
        return root_view;
    }

    public void getfavcondition(final String lang) {
        AndroidNetworking.post(Webservice.getnjutrient)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }

                try {

                    JSONObject jsonObject_main = jsonObject.getJSONObject("data");

                    JSONArray jsonArray_nut = jsonObject_main.getJSONArray("nv_nut");
                    Fav_Model obj = new Fav_Model();
                    List<String> nutri = new ArrayList<>();
                    if (jsonArray_nut.length() > 0) {

                        obj.setTitle(getString(R.string.Nutritional_Composition));

                        for (int i = 0; i < jsonArray_nut.length(); i++) {
                            JSONObject jsonObject1 = jsonArray_nut.getJSONObject(i);
                            nutri.add(jsonObject1.optString("Nutrients").trim() + " - " + jsonObject1.optString("nutritional_values").trim());
                            obj.setDescription(nutri);
                        }
                        Data.add(obj);
                    }

                    JSONArray jsonArray_min = jsonObject_main.getJSONArray("nv_min");
                    if (jsonArray_min.length() > 0) {
                        obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Mineral_Composition));
                        nutri = new ArrayList<>();
                        for (int i = 0; i < jsonArray_min.length(); i++) {
                            JSONObject jsonObject1 = jsonArray_min.getJSONObject(i);
                            nutri.add(jsonObject1.optString("mineral").trim() + " - " + jsonObject1.optString("Quantity").trim());
                            obj.setDescription(nutri);
                        }
                        Data.add(obj);
                    }


                    JSONArray jsonArray_vit = jsonObject_main.getJSONArray("nv_vit");
                    if (jsonArray_vit.length() > 0) {
                        obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Vitamin_Composition));
                        nutri = new ArrayList<>();
                        for (int i = 0; i < jsonArray_vit.length(); i++) {
                            JSONObject jsonObject1 = jsonArray_vit.getJSONObject(i);
                            nutri.add(jsonObject1.optString("vitamin").trim() + " - " + jsonObject1.optString("Quantity").trim());
                            obj.setDescription(nutri);
                        }
                        Data.add(obj);
                    }

                    JSONArray jsonArray_OC = jsonObject_main.getJSONArray("nv_oc");

                    if (jsonArray_OC.length() > 0) {
                        obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Other_Constituents));
                        nutri = new ArrayList<>();
                        for (int i = 0; i < jsonArray_OC.length(); i++) {
                            JSONObject jsonObject1 = jsonArray_OC.getJSONObject(i);
                            nutri.add(jsonObject1.optString("other_constituents").trim() + " - " + jsonObject1.optString("Quantity").trim());
                            obj.setDescription(nutri);
                        }
                        Data.add(obj);
                    }


                    JSONArray jsonArray_hb = jsonObject_main.getJSONArray("hb");
                    if (jsonArray_hb.length() > 0) {
                        obj = new Fav_Model();
                        obj.setTitle(getString(R.string.Nutritional_Benefits));

                        nutri = new ArrayList<>();
                        for (int i = 0; i < jsonArray_hb.length(); i++) {
                            crop_id = jsonArray_hb.getJSONObject(i).optString("Basic_info_id");

                            JSONArray jsonArray = jsonArray_hb.getJSONObject(i).getJSONArray("healthy_benefits");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                nutri.add(jsonArray.get(j).toString().trim());
                            }


                            obj.setDescription(nutri);
                        }
                        Data.add(obj);
                    }


                    FavdataConverter con = new FavdataConverter();

                    NutrientValues_Async fav = new NutrientValues_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                        @Override
                        public void onTaskComplete(String result) {

                        }
                    }, Webservice.Search_id, con.ListToString(Data));
                    fav.execute();

                    if (Data.size() > 0) {
                        title.setText(getString(R.string.Nutrirional_Values_of) + " " + Webservice.Searchvalue + " (100 G)");

                        loader_view.setVisibility(View.GONE);
                        fav_adapter = new Fav_Adapter(getActivity(), Data);

                        fav_condition.setAdapter(fav_adapter);
                        empty.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);

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
