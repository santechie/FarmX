package com.ascentya.AsgriV2.buysell.fragmens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Myoders_Model;
import com.ascentya.AsgriV2.Adapters.Myorder_Adapter;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrders extends BaseFragment {
    View view;
    RecyclerView myorder_recycler;
    Myorder_Adapter myorder_adapter;
    List<Myoders_Model> Data;
    SessionManager sm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.myorder_layout, container, false);
        Data = new ArrayList<>();
        sm = new SessionManager(getActivity());

        myorder_recycler = view.findViewById(R.id.myorder_recycler);
        myorder_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        myorder_recycler.setHasFixedSize(true);


//        nodata = view.findViewById(R.id.nodata);
//        sm = new SessionManager(getActivity());
//        floatingActionButton = view.findViewById(R.id.floatingActionButton);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getActivity(), MyCart.class);
//                startActivity(i);
//            }
//        });
        getlands();
        return view;
    }


    public void getlands() {

        Data = new ArrayList<>();

        AndroidNetworking.post(Webservice.orderlist)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }
                try {

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Myoders_Model obj = new Myoders_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("id"));
                            obj.setOrder_id(jsonArray.getJSONObject(i).optString("order_number"));
                            obj.setOrder_price(jsonArray.getJSONObject(i).optString("grand_total"));
                            obj.setPayment_status(jsonArray.getJSONObject(i).optString("payment_status"));
                            obj.setOrder_status(jsonArray.getJSONObject(i).optString("order_status"));
                            obj.setOrdered_date(jsonArray.getJSONObject(i).optString("created_date"));
                            obj.setPayment_mode(jsonArray.getJSONObject(i).optString("payment_mode"));


                            Data.add(obj);
                        }


                    } else {

                    }
                    myorder_adapter = new Myorder_Adapter(getActivity(), Data);
                    myorder_recycler.setAdapter(myorder_adapter);

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
