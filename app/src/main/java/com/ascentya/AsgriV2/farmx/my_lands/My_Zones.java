package com.ascentya.AsgriV2.farmx.my_lands;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Zone_Adapter;
import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class My_Zones extends Fragment {

    RecyclerView mylands_recycler;
    TextView nodata;
    List<Zone_Model> Data;
    ViewDialog viewDialog;
    SessionManager sm;
    Zone_Adapter masterAdapter;
    ImageView goback;
    String land_id;
    View view;
    String val, landcrop_id, user_id , lc_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_zones, container, false);
        val = getArguments().getString("title");
        landcrop_id = getArguments().getString("land_id");
        viewDialog = new ViewDialog(getActivity());
        sm = new SessionManager(getActivity());

        mylands_recycler = view.findViewById(R.id.myzones_recycler);
        nodata = view.findViewById(R.id.nodata);
        goback = view.findViewById(R.id.goback);


        mylands_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mylands_recycler.hasFixedSize();

        Data = new ArrayList<>();


        getlands(val, landcrop_id,user_id,lc_id );


        return view;
    }

    public static My_Zones newInstance(String title, String land_id) {
        My_Zones fragment = new My_Zones();
        Bundle args = new Bundle();

        args.putString("title", title);
        args.putString("land_id", land_id);

        fragment.setArguments(args);
        return fragment;
    }

    public void getlands(String crop_id, String landcrop_id, String user_id, String lcId) {

        Log.i("My Zones", "crop_id: "+crop_id+" # "+"land_crop_id"+landcrop_id);
        Log.i("My Zones", "crop_id: "+crop_id+" # "+"land_id"+land_id);
        Log.i("My Zones", "crop_id: "+user_id+" # "+"land_id"+land_id);
        Log.i("My Zones", "crop_id: "+lc_id+" # "+"land_id"+land_id);

        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.post(Webservice.addlandlist)

                .addUrlEncodeFormBodyParameter("land_id", landcrop_id)
                .addUrlEncodeFormBodyParameter("user_id",user_id )
                .addUrlEncodeFormBodyParameter("crop_id", crop_id)
                .addUrlEncodeFormBodyParameter("lc_id", lcId)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                viewDialog.hideDialog();
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Zone_Model obj = new Zone_Model();
                            obj.setZone_id(jsonArray.getJSONObject(i).optString("zone_id"));
                            obj.setZone_name(jsonArray.getJSONObject(i).optString("zone_name"));
                            obj.setCrop_name(jsonArray.getJSONObject(i).optString("crop_id"));
                            obj.setCrop_icons_images(jsonArray.getJSONObject(i).optString("crop_icons_images"));
                            obj.setScientific_name(jsonArray.getJSONObject(i).optString("scientific_name"));
                            obj.setSoil_ph(jsonArray.getJSONObject(i).optString("soil_ph"));
                            obj.setTemperature(jsonArray.getJSONObject(i).optString("temperature"));
                            obj.setHumidity(jsonArray.getJSONObject(i).optString("humidity"));
                            obj.setSoil_moisture(jsonArray.getJSONObject(i).optString("soil_moisture"));
                            obj.setPollution(jsonArray.getJSONObject(i).optString("pollution"));
                            obj.setLight_visibility(jsonArray.getJSONObject(i).optString("light_visibility"));
                            Data.add(obj);
                        }
                        masterAdapter = new Zone_Adapter(getActivity(), Data, true, landcrop_id, sm.getUser().getId());
                        mylands_recycler.setAdapter(masterAdapter);

                    } else {

                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();


            }
        });
    }
}