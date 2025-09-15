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
import com.ascentya.AsgriV2.Adapters.Varieties_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.AsyncTasks.Varieties_Async;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.Varieties_Models;
import com.ascentya.AsgriV2.Object_Converter.Varities_Converter;
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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Varieties_Fragment extends BaseFragment {

    private View root_view;
    private RecyclerView varieties;
    private Varieties_Adapter varietiesAdapter;
    private List<Varieties_Models> Data;
    private TextView title, search_empty, searchbelow_texts;
    private LinearLayout empty, main_layout, search_do;

    private CircleImageView loader;
    private ConstraintLayout loader_view;
    private Lang_Token tk;
    private boolean loading = true;
    private int previousTotal = 0;
    NestedScrollView nested_view;

    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.varities_layout, container, false);
        varieties = root_view.findViewById(R.id.varieties);

        mLayoutManager = new LinearLayoutManager(getActivity());

        varieties.setLayoutManager(mLayoutManager);
        varieties.setHasFixedSize(true);
        Data = new ArrayList<>();
        tk = new Lang_Token(getActivity());
        loader = root_view.findViewById(R.id.loading_view);
        nested_view = root_view.findViewById(R.id.nested_view);
        loader_view = root_view.findViewById(R.id.loader_view);
        loader.setVisibility(View.VISIBLE);
        empty = root_view.findViewById(R.id.empty);
        title = root_view.findViewById(R.id.title);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);

        varieties.setItemViewCacheSize(20);
        varieties.setDrawingCacheEnabled(true);
        varieties.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        nested_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {

                            if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                                loading = false;
                                gettexonomy(String.valueOf(totalItemCount), false, Webservice.state_id);

                            }
                        }
                    }
                }
            }
        });
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
            loader.setVisibility(View.GONE);
            loader_view.setVisibility(View.GONE);

        } else {
            empty.setVisibility(View.GONE);
            loader_view.setVisibility(View.VISIBLE);

            main_layout.setVisibility(View.VISIBLE);

            Glide.with(getActivity()).load(Webservice.Searchicon).error(R.drawable.loder_logo).into(loader);

            title = root_view.findViewById(R.id.title);
            title.setText(getString(R.string.varietiesof) + " " + Webservice.Searchvalue);


            GetSingleObject run = new GetSingleObject(getActivity(), new Async_Single<Info_Model>() {
                @Override
                public void onTaskComplete(Info_Model result) {

                    if (result != null) {
                        if (result.getVarieties() != null) {
                            loader_view.setVisibility(View.GONE);
                            Data.clear();
                            Varities_Converter con = new Varities_Converter();

                            Data.addAll(con.stringToSomeObjectList(result.getVarieties()));

                            loader.setVisibility(View.GONE);

                            List<Varieties_Models> lang_data = new ArrayList<>();

                            for (int i = 0; i < Data.size(); i++) {

//                                if (Data.get(i).getLang_id().equalsIgnoreCase(Webservice.lang_id.toString())){
                                lang_data.add(Data.get(i));
//                                }


                            }


                            varietiesAdapter = new Varieties_Adapter(getActivity(), lang_data);

                            varieties.setAdapter(varietiesAdapter);


                        } else {
                            gettexonomy("0", true, Webservice.state_id);


                        }
                    } else {
                        gettexonomy("0", true, Webservice.state_id);
                    }


                }
            }, Webservice.Search_id);
            run.execute();


        }

        return root_view;
    }

    public void gettexonomy(final String lang, final Boolean check, final String lang_id) {


        String sid = "0";
        if (lang_id.equalsIgnoreCase("Meghalaya")) {
            sid = "2";
        } else {
            sid = "0";
        }


        AndroidNetworking.post(Webservice.getvarities)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .addUrlEncodeFormBodyParameter("count_id", lang)
                .addUrlEncodeFormBodyParameter("state_id", sid)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (UserHelper.checkResponse(getContext(), jsonObject)){
                    return;
                }
                loader_view.setVisibility(View.GONE);


                try {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {


                        Varieties_Models obj = new Varieties_Models();

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        obj.setTitle(jsonObject1.optString("varieties_name").trim());
                        obj.setImages(jsonObject1.optString("varieties_imgs"));

                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("varieties_desc");

                        obj.setDetailed_des(jsonArray1.toString());
                        if (jsonArray1.length() > 0) {

                            obj.setDisc(jsonArray1.get(0).toString().trim());


                        }

                        obj.setLang_id(jsonObject1.optString("lang_id"));

                        Data.add(obj);

                    }

                    if (Data.size() > 0) {
                        loading = true;

                        Varities_Converter con = new Varities_Converter();

                        Varieties_Async obj = new Varieties_Async(getActivity(), new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onTaskComplete(String result) {

                            }
                        }, Webservice.Search_id, con.someObjectListToString(Data));
                        obj.execute();


                        loader.setVisibility(View.GONE);

                        if (check) {

//                            List <Varieties_Models> lang_data = new ArrayList<>();
//
//                            for (int i = 0; i <Data.size() ; i++) {
//
//                                if (Data.get(i).getLang_id().equalsIgnoreCase(Webservice.lang_id.toString())){
//                                    lang_data.add(Data.get(i));
//                                }
//
//
//
//                            }
                            varietiesAdapter = new Varieties_Adapter(getActivity(), Data);

                            varieties.setAdapter(varietiesAdapter);
                        } else {
                            varietiesAdapter.notifyDataSetChanged();
                        }


                    } else {
                        loader.setVisibility(View.GONE);
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
