package com.ascentya.AsgriV2.agripedia.PHD_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Symptoms_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.GetSingleObject;
import com.ascentya.AsgriV2.AsyncTasks.PhdSym_Asyntc;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
import com.ascentya.AsgriV2.Interfaces_Class.AsyncTaskCompleteListener;
import com.ascentya.AsgriV2.Interfaces_Class.Async_Single;
import com.ascentya.AsgriV2.Models.PointerImages_Model;
import com.ascentya.AsgriV2.Models.Symtoms_Model;
import com.ascentya.AsgriV2.Object_Converter.PSymtoms_Converter;
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

public class phd_Symtoms_Fragment extends BaseFragment {

    View root_view;
    Symptoms_Adapter symtoms_adapter;
    List<Symtoms_Model> Data;
    RecyclerView symptoms_recycler;
    TextView title, search_empty, searchbelow_texts;
    LinearLayout empty, main_layout, search_do;
    Lang_Token tk;
    CircleImageView loader;
    ConstraintLayout loader_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.symptoms_layout, container, false);
        Data = new ArrayList<>();
        tk = new Lang_Token(getActivity());
        symptoms_recycler = root_view.findViewById(R.id.symptoms_recycler);
        empty = root_view.findViewById(R.id.empty);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);
        symptoms_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        symptoms_recycler.setHasFixedSize(true);
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
            GetSingleObject run = new GetSingleObject(getActivity(), new Async_Single<Info_Model>() {
                @Override
                public void onTaskComplete(Info_Model result) {
                    loader_view.setVisibility(View.GONE);
                    title = root_view.findViewById(R.id.title);

                    if (result != null && result.getPhd_symtoms() != null) {
                        Data.clear();

                        PSymtoms_Converter con = new PSymtoms_Converter();

                        Data.addAll(con.stringToSomeObjectList(result.getPhd_symtoms()));

                        if (Data.size() > 0) {
                            title.setText(getString(R.string.phd_Symptoms) + " " + Webservice.Searchvalue);

                            symtoms_adapter = new Symptoms_Adapter(getActivity(), Data);

                            symptoms_recycler.setAdapter(symtoms_adapter);
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
        AndroidNetworking.post(Webservice.getphdssymtoms)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }

                try {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Symtoms_Model obj = new Symtoms_Model();

                        obj.setTitle(jsonArray.getJSONObject(i).optString("ph_disease").trim().split("###")[Integer.parseInt(lang)]);
//                        obj.setCover_image(jsonArray.getJSONObject(i).optString("varieties_imgs"));

                        JSONArray jsonArray_sym = jsonArray.getJSONObject(i).getJSONArray("ph_disease_symptoms");

                        obj.setGallery_Images(jsonArray.getJSONObject(i).getJSONArray("ph_disease_video").toString());
                        List<PointerImages_Model> sym_Data = new ArrayList<>();
                        PointerImages_Model pointer_obj = null;
                        for (int j = 0; j < jsonArray_sym.length(); j++) {
                            pointer_obj = new PointerImages_Model();


                            String n_sym = jsonArray_sym.getJSONObject(j).getJSONArray("content").get(0).toString().trim();
                            if (!n_sym.equalsIgnoreCase("")) {
                                pointer_obj.setDesc(n_sym);


                            }

                            JSONArray jsonArray1 = jsonArray_sym.getJSONObject(j).getJSONArray("image");
                            if (jsonArray1.length() > 0) {

                                obj.setDp_img(jsonArray1.get(0).toString());

                                pointer_obj.setImages(jsonArray1.toString());
                            }


                            sym_Data.add(pointer_obj);

                        }

                        obj.setSymtoms(sym_Data);

                        Data.add(obj);

                    }
                    loader_view.setVisibility(View.GONE);
                    if (Data.size() > 0) {
                        title.setText(getString(R.string.phd_Symptoms) + " " + Webservice.Searchvalue);

                        PSymtoms_Converter con = new PSymtoms_Converter();

                        PhdSym_Asyntc obj = new PhdSym_Asyntc(getActivity(), new AsyncTaskCompleteListener<String>() {
                            @Override
                            public void onTaskComplete(String result) {

                            }
                        }, Webservice.Search_id, con.someObjectListToString(Data));
                        obj.execute();
                        symtoms_adapter = new Symptoms_Adapter(getActivity(), Data);

                        symptoms_recycler.setAdapter(symtoms_adapter);
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
