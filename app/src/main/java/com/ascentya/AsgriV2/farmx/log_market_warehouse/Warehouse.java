package com.ascentya.AsgriV2.farmx.log_market_warehouse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Warehouse extends Fragment {
    View root_view;
    TextView industry;
    LinearLayout back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.activity_post_harvest, container, false);

        industry = root_view.findViewById(R.id.industry);
        getexpecteddata();


        return root_view;
    }

    public void getexpecteddata() {

        AndroidNetworking.get(Webservice.getpostharvest_byplace + "chennai")

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {


                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        industry.setText(jsonObject.getJSONObject("data").optString("name") + jsonObject.getJSONObject("data").optString("address"));
//                        address.setText(jsonObject.getJSONObject("data").optString("address"));
//                        pincode.setText(jsonObject.getJSONObject("data").optString("pincode"));
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