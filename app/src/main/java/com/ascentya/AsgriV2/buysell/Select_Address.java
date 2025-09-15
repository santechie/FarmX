package com.ascentya.AsgriV2.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.Address_Adapter;
import com.ascentya.AsgriV2.Interfaces_Class.Mycart_Interface;
import com.ascentya.AsgriV2.Models.Address_Model;
import com.ascentya.AsgriV2.Models.FarmXBuy_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class Select_Address extends AppCompatActivity {
    LinearLayout cart_bottom_bar, cart_view_empty, add;
    RecyclerView cart_items_recycler;
    SessionManager sm;
    List<Address_Model> Data = new ArrayList<>();
    Address_Adapter myCart_adapter;
    //    Button cart_checkout_btn;
    LinearLayout back, cart_view;
    TextView cart_total_price;
    Double total_price;
    ViewDialog viewDialog;
    Button addaddress;

    @Override
    protected void onResume() {
        super.onResume();
        add_mypost();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        cart_bottom_bar = findViewById(R.id.cart_bottom_bar);
        viewDialog = new ViewDialog(this);
        cart_view_empty = findViewById(R.id.cart_view_empty);
        cart_total_price = findViewById(R.id.cart_total_price);
        add = findViewById(R.id.add);
        cart_view = findViewById(R.id.cart_view);
        addaddress = findViewById(R.id.addaddress);
        back = findViewById(R.id.back);
//        cart_checkout_btn = findViewById(R.id.cart_checkout_btn);
        cart_items_recycler = findViewById(R.id.address_recycler);
        cart_items_recycler.setLayoutManager(new LinearLayoutManager(this));
        sm = new SessionManager(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        add.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                addAddress();
            }
        });


//        cart_checkout_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clear_mypost();
//            }
//        });

        addaddress.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                addAddress();
            }
        });

        myCart_adapter = new Address_Adapter(Select_Address.this, Data, sm.getUser().getId(), viewDialog, new Mycart_Interface() {
            @Override
            public List<FarmXBuy_Model> getList() {
                return null;
            }

            @Override
            public void mycart(String total, Boolean check) {
                add_mypost();
//                                    cart_total_price.setText("Total price : "+total);
            }
        });

        cart_items_recycler.setAdapter(myCart_adapter);

    }

    private void addAddress(){
        Intent i = new Intent(Select_Address.this, My_Address.class);
        startActivity(i);
    }


    public void add_mypost() {


        AndroidNetworking.post(Webservice.get_address)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(Select_Address.this, jsonObject)){
                    return;
                }
                Data.clear();
                total_price = 0.0;
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Address_Model obj = new Address_Model();
                            obj.setId(jsonArray.getJSONObject(i).optString("add_id"));
                            obj.setName(jsonArray.getJSONObject(i).optString("add_name"));
                            obj.setStreet_name(jsonArray.getJSONObject(i).optString("add_street"));
                            obj.setCity(jsonArray.getJSONObject(i).optString("add_city"));
                            obj.setPincode(jsonArray.getJSONObject(i).optString("add_pincode"));
                            obj.setFlatnum(jsonArray.getJSONObject(i).optString("flatnum"));
                            obj.setMarkus(jsonArray.getJSONObject(i).optString("markus"));
                            obj.setDate(jsonArray.getJSONObject(i).optString("created_date"));
                            Data.add(obj);
                        }


                    } else {
//                        Toasty.error(MyCart.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        cart_view_empty.setVisibility(View.VISIBLE);
                        cart_view.setVisibility(View.GONE);
                        cart_bottom_bar.setVisibility(View.GONE);
                    }


                    if (Data.size() > 0) {
                        cart_view_empty.setVisibility(View.GONE);
                        cart_view.setVisibility(View.VISIBLE);

                    } else {
                        cart_view_empty.setVisibility(View.VISIBLE);
                        cart_view.setVisibility(View.GONE);
                    }

                    myCart_adapter.updateList(Data);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }

    public void clear_mypost() {
        viewDialog.showDialog();
        Data = new ArrayList<>();

        AndroidNetworking.post(Webservice.clear_cart)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId().trim())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                if (UserHelper.checkResponse(Select_Address.this, jsonObject)){
                    return;
                }

                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {
                        Intent i = new Intent(Select_Address.this, Select_Address.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toasty.error(Select_Address.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();

                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                viewDialog.hideDialog();
                Intent i = new Intent(Select_Address.this, Select_Address.class);
                startActivity(i);
                finish();


            }
        });
    }
}