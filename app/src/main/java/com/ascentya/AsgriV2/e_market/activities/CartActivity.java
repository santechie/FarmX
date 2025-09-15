package com.ascentya.AsgriV2.e_market.activities;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.e_market.adapters.CartAdapter;
import com.ascentya.AsgriV2.e_market.data.DummyDataGenerator;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.ascentya.AsgriV2.e_market.dialog.book.BookDialog;
import com.ascentya.AsgriV2.e_market.dialog.delete_item.DeleteDialog;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartActivity extends BaseActivity implements CartAdapter.Action {

    private RecyclerView recyclerView;
    private ConstraintLayout detailsLayout, emptyCartLayout;
    private TextView totalTv, countTv;
    private MaterialButton checkoutBtn, addItemsBtn;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setToolbarTitle("Cart", true);

        recyclerView = findViewById(R.id.recyclerView);
        detailsLayout = findViewById(R.id.detailsLayout);
        emptyCartLayout = findViewById(R.id.cartEmptyLayout);
        totalTv = findViewById(R.id.total);
        countTv = findViewById(R.id.count);
        checkoutBtn = findViewById(R.id.checkout);
        addItemsBtn = findViewById(R.id.addItems);

        cartAdapter = new CartAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        addItemsBtn.setOnClickListener(view -> finish());
        checkoutBtn.setOnClickListener(view -> openActivity(CheckoutActivity.class));

        updateUI();
    }

    private void updateUI(){
        updateView();
        updateTotal();
        updateCount();
        updateAdapter();
    }

    private void updateView() {
        if (getCartItems().isEmpty()){
            detailsLayout.setVisibility(View.GONE);
            emptyCartLayout.setVisibility(View.VISIBLE);
        }else {
            detailsLayout.setVisibility(View.VISIBLE);
            emptyCartLayout.setVisibility(View.GONE);
        }
    }

    private void updateTotal(){
        totalTv.setText(ProductUtils.getPrice(getTotalPrice()));
    }

    private void updateCount(){
        countTv.setText(String.valueOf(getCartItems().size()));
    }

    private void updateAdapter(){
        cartAdapter.refresh();
    }

    @Override
    public List<CartItem> getCartItems() {
        return cartManager.getAllCartItems();
    }

    @Override
    public void deleteCartItem(String stockId) {
        DeleteDialog deleteDialog = new DeleteDialog(stockId, this);
        deleteDialog.show(getSupportFragmentManager(), DeleteDialog.class.getSimpleName());
    }

    @Override
    public void editCartItem(String stockId) {
        BookDialog bookDialog = new BookDialog(stockId, this);
        bookDialog.show(getSupportFragmentManager(), BookDialog.class.getSimpleName());
    }

    @Override
    public void refreshStocks() {
        updateUI();
    }

    @Override
    public Stock getStock(String stockId) {
        return DummyDataGenerator.getStock(stockId);
    }

    @Override
    public Item getItem(String categoryId, String itemId) {
        return dataRepository.getItem(categoryId, itemId);
    }

    @Override
    public ItemType getItemType(String categoryId, String itemId, String itemTypeId) {
        return dataRepository.getItemType(categoryId, itemId, itemTypeId);
    }

    private String getTotalPrice(){
        float totalPrice = 0;
        for (CartItem cartItem: getCartItems()){
            if (!ProductUtils.isZero(String.valueOf(cartItem.getPrice()))){
                totalPrice += cartItem.getPrice();
            }
        }

        return String.valueOf(totalPrice);
    }
}