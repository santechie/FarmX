package com.ascentya.AsgriV2.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.Adapters.MyCart_Adapter;
import com.ascentya.AsgriV2.Database_Room.entities.CartItemEntity;
import com.ascentya.AsgriV2.Interfaces_Class.Mycart_Interface;
import com.ascentya.AsgriV2.Models.FarmXBuy_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.CartItemUtils;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyCart extends AppCompatActivity implements Mycart_Interface {
    LinearLayout cart_bottom_bar, cart_view_empty;
    RecyclerView cart_items_recycler;
    SessionManager sm;
    List<FarmXBuy_Model> Data = new ArrayList<>();
    List<CartItemEntity> cartItemEntities;
    MyCart_Adapter myCart_adapter;

    Button cart_checkout_btn;
    LinearLayout back;
    TextView cart_total_price;
    Double total_price = 0.0;
    ViewDialog viewDialog;
    Button continue_shopping_btn;
    String cart_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        cart_bottom_bar = findViewById(R.id.cart_bottom_bar);
        viewDialog = new ViewDialog(this);
        cart_view_empty = findViewById(R.id.cart_view_empty);
        cart_total_price = findViewById(R.id.cart_total_price);
        continue_shopping_btn = findViewById(R.id.continue_shopping_btn);
        back = findViewById(R.id.back);
        cart_checkout_btn = findViewById(R.id.cart_checkout_btn);
        cart_items_recycler = findViewById(R.id.cart_items_recycler);
        cart_items_recycler.setLayoutManager(new LinearLayoutManager(this));
        sm = new SessionManager(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        cart_checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                clear_mypost();
                checkOut();
            }
        });

        continue_shopping_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        myCart_adapter = new MyCart_Adapter(MyCart.this, sm.getUser().getId(), viewDialog, this);

        getCartItems();
    }

    @Override
    public List<FarmXBuy_Model> getList() {
        return Data;
    }

    @Override
    public void mycart(String total, Boolean check) {
        getCartItems();
    }

    /*public void add_mypost() {


        AndroidNetworking.post(Webservice.get_cart)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Data = new ArrayList<>();

                total_price = 0.0;
                try {
                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            FarmXBuy_Model obj = new FarmXBuy_Model();
                            cart_id = jsonArray.getJSONObject(i).optString("cart_id");
                            obj.setProduct_name(jsonArray.getJSONObject(i).optString("product_name"));
                            obj.setCart_id(jsonArray.getJSONObject(i).optString("cart_id"));
                            obj.setProduct_desc(jsonArray.getJSONObject(i).optString("product_desc"));
                            obj.setProduct_id(jsonArray.getJSONObject(i).optString("prod_id"));
                            obj.setProduct_date(jsonArray.getJSONObject(i).optString("created_date"));
                            obj.setProduct_price(jsonArray.getJSONObject(i).optString("product_price"));
                            obj.setProduct_status(jsonArray.getJSONObject(i).optString("status"));
                            obj.setProduct_image(jsonArray.getJSONObject(i).optString("product_image"));
                            obj.setCat_id(jsonArray.getJSONObject(i).optString("cat_id"));
                            obj.setProductuser_id(jsonArray.getJSONObject(i).optString("user_id"));
                            obj.setProduct_quantity(jsonArray.getJSONObject(i).optString("quantity"));
                            Data.add(obj);
                        }


                    } else {
//                        Toasty.error(MyCart.this, jsonObject.optString("message"), Toast.LENGTH_SHORT, true).show();
                        cart_view_empty.setVisibility(View.VISIBLE);
                        cart_items_recycler.setVisibility(View.GONE);
                        cart_bottom_bar.setVisibility(View.GONE);
                    }


                    if (Data.size() > 0) {
                        cart_total_price.setText("Rs."+String.valueOf(total_price));
                    } else {

                    }

                    getCartItems();
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {


            }
        });
    }*/

    private void getCartItems(){
        CartItemUtils.getCartItems(this, cartItemEntities -> {
            this.cartItemEntities = (List<CartItemEntity>) cartItemEntities;
            Data.clear();
            if (cartItemEntities != null) {
                for (CartItemEntity cartItemEntity : this.cartItemEntities){
                    Data.add(CartItemUtils.toFarmXBuyModel(cartItemEntity));
                }
            }
            updateUI();
        });
    }

    private void updateUI(){
        if (cartItemEntities != null && !cartItemEntities.isEmpty()){
            cart_items_recycler.setAdapter(myCart_adapter);
            cart_view_empty.setVisibility(View.GONE);
            cart_items_recycler.setVisibility(View.VISIBLE);
            cart_bottom_bar.setVisibility(View.VISIBLE);
        }else {
            cart_view_empty.setVisibility(View.VISIBLE);
            cart_items_recycler.setVisibility(View.GONE);
            cart_bottom_bar.setVisibility(View.GONE);
        }
        updateTotalPrice();
    }

    public void checkOut(){
        Intent i = new Intent(MyCart.this, Select_Address.class);
        startActivity(i);
    }

    public void updateTotalPrice(){
        total_price = 0.0;
        for (FarmXBuy_Model model: Data){
            try {
                total_price =
                        total_price +
                                (Double.parseDouble(model.getProduct_price())
                                        * Double.parseDouble(model.getProduct_quantity()));
            }catch (Exception e){ }
        }
        cart_total_price.setText(ProductUtils.getPrice(String.valueOf(total_price)));
    }
}