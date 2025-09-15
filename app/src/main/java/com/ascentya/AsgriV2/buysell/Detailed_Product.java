package com.ascentya.AsgriV2.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.AsyncTasks.MyExecutorService;
import com.ascentya.AsgriV2.Database_Room.entities.CartItemEntity;
import com.ascentya.AsgriV2.Models.FarmXBuy_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Shared_Preference.SessionManager;
import com.ascentya.AsgriV2.Utils.CartItemUtils;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.data.Components;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.activities.BaseActivity;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.bumptech.glide.Glide;

public class Detailed_Product extends BaseActivity {

    private TextView countTv;
    private Button addBtn;
    private ImageView image, plusBtn, minusBtn;
    private LinearLayout buttonLay;

    private SessionManager sessionManager;
    private FarmXBuy_Model product;
    private CartItemEntity cartItemEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_product);

        countTv = findViewById(R.id.cart_item_quantity);
        addBtn = findViewById(R.id.addButton);
        image = findViewById(R.id.imageView);
        plusBtn = findViewById(R.id.cart_item_quantity_plusBtn);
        minusBtn = findViewById(R.id.cart_item_quantity_minusBtn);
        buttonLay = findViewById(R.id.addquantity);

        if (!getModuleManager().canInsert(Components.BuyAndSell.BUY)){
            addBtn.setVisibility(View.INVISIBLE);
            buttonLay.setVisibility(View.INVISIBLE);
        }

        product = GsonUtils.getGson().fromJson(
                getIntent().getStringExtra(Constants.BUY_PRODUCT),
                FarmXBuy_Model.class
        );

        sessionManager = new SessionManager(this);

        Log.i(Detailed_Product.class.getSimpleName(), "Product:+\n"+getIntent().getStringExtra(Constants.BUY_PRODUCT));

        findViewById(R.id.goback).setOnClickListener(v -> finish());

        findViewById(R.id.cart).setOnClickListener(v -> {
            Intent intent = new Intent(this, MyCart.class);
            startActivity(intent);
        });

        findViewById(R.id.checkOut).setOnClickListener(v -> {
            Intent i = new Intent(this, Select_Address.class);
            startActivity(i);
        });

        addBtn.setOnClickListener(v -> addCart());
        plusBtn.setOnClickListener(v -> updateQuantity(getQuantity() + 1));
        minusBtn.setOnClickListener(v -> updateQuantity(getQuantity() - 1));

        getCartEntity();
    }

    private void getCartEntity(){
        new MyExecutorService(new MyExecutorService.MyRunnable(this, cartItemEntity -> {
            this.cartItemEntity = (CartItemEntity) cartItemEntity;
            updateUI();
        }) {
            @Override
            public Object runForResult() {
                return getCartItemDao().get(product.getProduct_id());
            }
        });
    }

    private void updateUI(){
        setNameAndDescription();
        loadImage();
        updateButtonLay();
        updateCount();
    }

    private void setNameAndDescription(){
        ((TextView) findViewById(R.id.product_title)).setText(product.getProduct_name());
        ((TextView) findViewById(R.id.product_desc)).setText(product.getProduct_desc());
        ((TextView) findViewById(R.id.price)).setText(ProductUtils.getPrice(product.getProduct_price()));
    }

    private void loadImage(){
        Glide.with(this).load(product.getProduct_image()).into(image);
    }

    private void updateButtonLay(){
        if (addBtn.getVisibility() == View.INVISIBLE) return;
        int quantity = getQuantity();
        if (quantity == 0){
            buttonLay.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
        }else {
            buttonLay.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);
        }
    }

    private void updateCount(){
        countTv.setText(getQuantity()+"");
    }

    private void addCart(){
        if (!checkSubscription(Modules.BUY_AND_SELL, ModuleManager.ACCESS.INSERT)) return;
        CartItemEntity cartItemEntity = CartItemUtils.toCartItemEntity(product, 1);
        CartItemUtils.insertCartItem(this, cartItemEntity, object -> getCartEntity());
    }

    private void updateQuantity(int quantity){
        if (cartItemEntity == null){
            // Insert
            addCart();
        }else if (quantity > 0){
            // Update
            CartItemEntity cartItemEntity = CartItemUtils.toCartItemEntity(product, quantity);
            CartItemUtils.updateCartItem(this, cartItemEntity, object -> getCartEntity());
        }else {
            // Delete
            CartItemEntity cartItemEntity = CartItemUtils.toCartItemEntity(product, quantity);
            CartItemUtils.deleteCartItem(this, cartItemEntity, object -> getCartEntity());
        }
    }

    private int getQuantity(){
        return cartItemEntity != null ? cartItemEntity.getQuantity() : 0;
    }
}