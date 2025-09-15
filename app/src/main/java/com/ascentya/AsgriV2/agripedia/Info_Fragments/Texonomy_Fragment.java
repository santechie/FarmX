package com.ascentya.AsgriV2.agripedia.Info_Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Tax_Adapter;
import com.ascentya.AsgriV2.Database_Room.DatabaseClient;
import com.ascentya.AsgriV2.Database_Room.entities.Info_Model;
import com.ascentya.AsgriV2.Database_Room.ListString;
import com.ascentya.AsgriV2.Event_Bus.DeleteBus;
import com.ascentya.AsgriV2.Event_Bus.DeleteEvent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Texonomy_Fragment extends BaseFragment {

    private View root_view;
    private RecyclerView taxonomy;
    private List<String> Data;
    private Tax_Adapter basicDisc_adapter;
    private LinearLayout empty, main_layout, search_do;
    private TextView search_empty, searchbelow_texts;
    private Lang_Token tk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        root_view = inflater.inflate(R.layout.taxonomy_layout, container, false);
        return root_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taxonomy = root_view.findViewById(R.id.taxonomy);
        empty = root_view.findViewById(R.id.empty);
        search_empty = root_view.findViewById(R.id.search_empty);
        searchbelow_texts = root_view.findViewById(R.id.searchbelow_texts);
        main_layout = root_view.findViewById(R.id.main_layout);
        taxonomy.setLayoutManager(new LinearLayoutManager(getActivity()));
        taxonomy.setHasFixedSize(true);
        Data = new ArrayList<>();
        tk = new Lang_Token(getActivity());

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
            GetTasks obj = new GetTasks(getActivity());
            obj.execute();

        }
    }

    public void gettexonomy(final String lang) {
        System.out.println("Texonomy Loading...");
        Data.clear();
        AndroidNetworking.post(Webservice.gettxny)
                .addUrlEncodeFormBodyParameter("crop_id", Webservice.Search_id)
                .addUrlEncodeFormBodyParameter("user_id", getSessionManager().getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(getContext(), jsonObject)){
                    return;
                }

                try {
                    if (!jsonObject.getJSONArray("data").isNull(0)) {
                        JSONObject jsonObject1 = jsonObject.getJSONArray("data").getJSONObject(0);


                        JSONArray jsonArray1 = jsonObject1.getJSONArray("Taxonomy_Desc");


                        for (int i = 0; i < jsonArray1.length(); i++) {


                            Data.add(jsonArray1.get(i).toString().trim());

                        }
                        if (Data.size() > 0) {
                            basicDisc_adapter = new Tax_Adapter(getActivity(), Data);

                            taxonomy.setAdapter(basicDisc_adapter);
                            empty.setVisibility(View.GONE);
                            main_layout.setVisibility(View.VISIBLE);
                        } else {
                            search_empty.setText(getString(R.string.no_data));
                            searchbelow_texts.setText("");
                            empty.setVisibility(View.VISIBLE);
                            main_layout.setVisibility(View.GONE);
                        }

                        ListString obj = new ListString();
                        obj.someObjectListToString(Data);
                        Store_Tax tax = new Store_Tax(getActivity(), Webservice.Search_id, obj.someObjectListToString(Data));
                        tax.execute();
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

    class Store_Tax extends AsyncTask<Void, Void, Void> {
        private Activity activity;
        String Data;
        String id;

        public Store_Tax(Activity activity, String userid, String Data) {
            this.Data = Data;
            this.id = userid;
            this.activity = activity;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(activity == null)
                return null;

            //adding to database
            DatabaseClient
                    .getInstance(activity)
                    .getAppDatabase()
                    .taskDao()
                    .updatedesc(Data, Integer.parseInt(id));

            return null;
        }

        @Override
        protected void onPostExecute(Void tasks) {
            super.onPostExecute(tasks);


        }

    }

    class GetTasks extends AsyncTask<Void, Void, Info_Model> {

        private Activity activity;

        public GetTasks(Activity activity){
            this.activity = activity;
        }

        @Override
        protected Info_Model doInBackground(Void... voids) {

            if (activity == null) return null;

            Info_Model taskList = DatabaseClient
                    .getInstance(activity.getApplicationContext())
                    .getAppDatabase()
                    .taskDao()
                    .findSpecificEvent(Long.parseLong(Webservice.Search_id));
            return taskList;
        }

        @Override
        protected void onPostExecute(Info_Model tasks) {
            super.onPostExecute(tasks);
            if (tasks != null) {
                if (tasks.getTaxonomy() != null) {
                    Data.clear();
                    ListString obj = new ListString();

                    Data.addAll(obj.stringToSomeObjectList(tasks.getTaxonomy()));


                    if (Data.size() > 0) {
                        basicDisc_adapter = new Tax_Adapter(getActivity(), Data);

                        taxonomy.setAdapter(basicDisc_adapter);
                        empty.setVisibility(View.GONE);
                        main_layout.setVisibility(View.VISIBLE);
                    } else {
                        search_empty.setText(getString(R.string.no_data));
                        searchbelow_texts.setText("");
                        empty.setVisibility(View.VISIBLE);
                        main_layout.setVisibility(View.GONE);
                    }


                } else {

                    gettexonomy(tk.getusename());

                }
            } else {
                gettexonomy(tk.getusename());
            }


        }
    }
}
