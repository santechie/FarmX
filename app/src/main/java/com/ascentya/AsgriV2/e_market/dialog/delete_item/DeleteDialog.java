package com.ascentya.AsgriV2.e_market.dialog.delete_item;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.adapters.CartAdapter;
import com.ascentya.AsgriV2.e_market.adapters.StockAdapter;
import com.ascentya.AsgriV2.e_market.data.CartManager;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DeleteDialog extends BottomSheetDialogFragment {

    private String stockId;
    private StockAdapter.Action stockAction;
    private CartAdapter.Action cartAction;
    private CartManager cartManager;
    private CartItem cartItem;
    private Stock stock;
    private Item item;
    private ItemType itemType;

    private TextView nameTv;
    private MaterialButton yesBtn, noBtn;

    public DeleteDialog(String stockId, StockAdapter.Action action) {
        this.stockId = stockId;
        this.stockAction = action;
    }

    public DeleteDialog(String stockId, CartAdapter.Action action) {
        this.stockId = stockId;
        this.cartAction = action;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cartManager = CartManager.getInstance(getContext());
        return inflater.inflate(R.layout.dialog_emarket_delete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartItem = cartManager.getCartItem(stockId);
        stock = getStock();
        item = getItem();
        itemType = getItemType();

        nameTv = view.findViewById(R.id.name);
        yesBtn = view.findViewById(R.id.yesBtn);
        noBtn = view.findViewById(R.id.noBtn);

        if (itemType != null) {
            String text = "Do you want to remove <b>" + itemType.getName() + "</b> from cart?";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                nameTv.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            }else {
                nameTv.setText(Html.fromHtml(text));
            }
        } else {
            nameTv.setText("Do you want to remove?");
        }

        yesBtn.setOnClickListener(view1 -> {
            dismiss();
            cartManager.removeFromCart(stockId);
            refreshStocks();
        });
        noBtn.setOnClickListener(view1 -> dismiss());
    }

    private void refreshStocks(){
        if (stockAction != null)
            stockAction.refreshStocks();
        if (cartAction != null)
            cartAction.refreshStocks();
    }

    private Stock getStock(){
        if (stockAction != null)
            return stockAction.getStock(stockId);
        if (cartAction != null)
            return cartAction.getStock(stockId);
        return null;
    }

    private Item getItem(){
        if (stockAction != null)
            return stockAction.getItem(getStock().getCategoryId(), getStock().getItemId());
        if (cartAction != null)
            return cartAction.getItem(getStock().getCategoryId(), getStock().getItemId());
        return null;
    }

    private ItemType getItemType(){
        if (stockAction != null)
            return stockAction.getItemType(getStock().getCategoryId(), getStock().getItemId(), getStock().getItemTypeId());
        if (cartAction != null)
            return cartAction.getItemType(getStock().getCategoryId(), getStock().getItemId(), getStock().getItemTypeId());
        return null;
    }
}
