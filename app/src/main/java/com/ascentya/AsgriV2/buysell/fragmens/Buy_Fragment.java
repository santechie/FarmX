package com.ascentya.AsgriV2.buysell.fragmens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Adapters.FarmXBuy_Adapter;
import com.ascentya.AsgriV2.AsyncTasks.MyExecutorService;
import com.ascentya.AsgriV2.Database_Room.entities.CartItemEntity;
import com.ascentya.AsgriV2.Models.FarmXBuy_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.CartItemUtils;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.UserHelper;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.buysell.MyCart;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.my_farm.fragments.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import es.dmoral.toasty.Toasty;

public class Buy_Fragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener, FarmXBuy_Adapter.Listener {

    private boolean canBuy = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    RecyclerView buy_recycler;
    TextView nodata, count;
    SessionManager sm;
    FarmXBuy_Adapter farmXSellAdapter;
    List<FarmXBuy_Model> Data = new ArrayList<>();
    List<CartItemEntity> cartItems = new ArrayList<>();
    FloatingActionButton floatingActionButton;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.farmxbuy_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        buy_recycler = view.findViewById(R.id.buy_recycler);
        buy_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        nodata = view.findViewById(R.id.nodata);
        count = view.findViewById(R.id.count);
        sm = new SessionManager(getActivity());
//        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        view.findViewById(R.id.cartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyCart.class);
                startActivity(i);
            }
        });

        canBuy = getModuleManager().canInsert(Components.BuyAndSell.BUY);

        farmXSellAdapter = new FarmXBuy_Adapter(getActivity(), this, Data, sm.getUser().getId());
        buy_recycler.setAdapter(farmXSellAdapter);
    }

    private void updateData() {
//        getCartItems();
        getItems();
    }

    public void getCartItems() {
        CartItemUtils.getCartItems(getContext(), cartItems -> {
            this.cartItems = (List<CartItemEntity>) cartItems;
            System.out.println("CartItemEntities: " +
                    cartItems);
            updateUI();
        });
    }

    public void getItems() {


        AndroidNetworking.post(Webservice.get_buyproduct)
                .addUrlEncodeFormBodyParameter("user_id", sm.getUser().getId())

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (UserHelper.checkResponse(requireContext(), jsonObject)){
                    return;
                }

                Log.i(Buy_Fragment.class.getSimpleName(),
                        "get_buy_product:\n" + jsonObject);

                try {

                    Data.clear();

                    if (jsonObject.optString("status").equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            System.out.println(i + ": " + jsonObj);
                            FarmXBuy_Model obj = GsonUtils.getGson()
                                    .fromJson(jsonObj.toString(), FarmXBuy_Model.class);
                            /*obj.setProduct_name(jsonObj.optString("prod_name"));
                            obj.setProduct_desc(jsonObj.optString("product_desc"));
                            obj.setProduct_id(jsonObj.optString("prod_id"));
                            obj.setCart_id(jsonObj.optString("cart_id"));
                            obj.setProduct_date(jsonObj.optString("created_date"));
                            obj.setProduct_price(jsonObj.optString("prod_price"));
                            obj.setProduct_status(jsonObj.optString("status"));
                            obj.setProduct_image(jsonObj.optString("prod_image"));
                            obj.setCat_id(jsonObj.optString("cat_id"));
                            obj.setProductuser_id(jsonObj.optString("user_id"));
                            obj.setProduct_quantity(jsonObj.optString("quantity"));*/
                            Data.add(obj);
                        }
                    } else {
                        showToast(jsonObject.optString("message"));
                    }

                    getCartItems();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                showToast("Something went wrong!");
            }
        });
    }

    private void updateUI() {
        updateSwipeRefresh(false);
        setNoDataView(Data.isEmpty());
        updateCount();
        refreshAdapter();
    }

    private void updateCount() {
        if (cartItems.isEmpty()) {
            count.setText("0");
        } else {
            int countNum = 0;
            for (CartItemEntity cartItemEntity : cartItems) {
                if (cartItemEntity.getQuantity() != 0) {
                    countNum++;
                }
            }
            count.setText(String.valueOf(countNum));
        }
    }

    private void setNoDataView(boolean isEmpty) {
        if (nodata == null)
            return;
        if (isEmpty) {
            nodata.setVisibility(View.VISIBLE);
        } else {
            nodata.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSwipeRefresh(boolean isRefreshing) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(isRefreshing);
    }

    private void refreshAdapter() {
        if (farmXSellAdapter != null)
            farmXSellAdapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        if (getActivity() != null)
            Toasty.error(getActivity(), message, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onRefresh() {
        updateData();
    }

    @Override
    public void update() {
        updateData();
    }

    @Override
    public int getQuantity(String productId) {
        if (cartItems != null) {
            for (CartItemEntity cartItemEntity : cartItems) {
                if (cartItemEntity.getProd_id().equals(productId))
                    return cartItemEntity.getQuantity();
            }
        }
        return 0;
    }

    @Override
    public void setQuantity(String productId, int quantity) {
        FarmXBuy_Model model = getModel(productId);
        CartItemEntity cartItemEntity = getCartItemEntity(productId);

        MyExecutorService.Action action = result -> {
            getCartItems();
            updateUI();
        };

        if (cartItemEntity == null && quantity >= 1) {
            // Insert
            cartItemEntity = CartItemUtils.toCartItemEntity(model);
            cartItemEntity.setQuantity(quantity);
            CartItemUtils.insertCartItem(this.getContext(), cartItemEntity, action);
        } else if (cartItemEntity != null) {
            if (quantity == 0) {
                // Delete
                CartItemUtils.deleteCartItem(this.getContext(), cartItemEntity, action);
            } else {
                // Update
                cartItemEntity = CartItemUtils.toCartItemEntity(model, quantity);
                CartItemUtils.updateCartItem(this.getContext(), cartItemEntity, action);
            }
        }
    }

    @Override
    public boolean canBuy() {
        return canBuy;
    }

    private FarmXBuy_Model getModel(String productId) {
        for (FarmXBuy_Model model : Data) {
            if (model.getProduct_id().equals(productId)) {
                return model;
            }
        }
        return null;
    }

    private CartItemEntity getCartItemEntity(String productId) {
        for (CartItemEntity cartItemEntity : cartItems) {
            if (cartItemEntity.getProd_id().equals(productId)) {
                return cartItemEntity;
            }
        }
        return null;
    }
}