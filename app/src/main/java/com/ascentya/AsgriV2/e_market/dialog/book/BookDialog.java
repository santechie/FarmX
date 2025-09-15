package com.ascentya.AsgriV2.e_market.dialog.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.e_market.adapters.CartAdapter;
import com.ascentya.AsgriV2.e_market.adapters.StockAdapter;
import com.ascentya.AsgriV2.e_market.data.CartManager;
import com.ascentya.AsgriV2.e_market.data.DummyDataGenerator;
import com.ascentya.AsgriV2.e_market.data.UnitConverter;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.ascentya.AsgriV2.e_market.data.model.Item;
import com.ascentya.AsgriV2.e_market.data.model.ItemType;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookDialog extends BottomSheetDialogFragment {

    private String stockId;
    private StockAdapter.Action stockAction;
    private CartAdapter.Action cartAction;
    private CartManager cartManager;
    private CartItem cartItem;
    private Stock stock;
    private Item item;
    private ItemType itemType;

    private TextView nameTv, itemTv;
    private EditText countEt;
    private ImageView plusBtn, minusBtn;
    private ChipGroup chipGroup;
    private Chip kgChip, tonChip;
    private MaterialButton confirmBtn;

    public BookDialog(String stockId, StockAdapter.Action action) {
        this.stockId = stockId;
        this.stockAction = action;
    }

    public BookDialog(String stockId, CartAdapter.Action action) {
        this.stockId = stockId;
        this.cartAction = action;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cartManager = CartManager.getInstance(getContext());
        return inflater.inflate(R.layout.dialog_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.closeBtn).setOnClickListener(v -> dismiss());

        cartItem = cartManager.getCartItem(stockId);
        stock = getStock();
        item = getItem();
        itemType = getItemType();

        nameTv = view.findViewById(R.id.name);
        itemTv = view.findViewById(R.id.itemName);
        countEt = view.findViewById(R.id.count);
        minusBtn = view.findViewById(R.id.minus);
        plusBtn = view.findViewById(R.id.plus);
        chipGroup = view.findViewById(R.id.chipGroup);
        kgChip = view.findViewById(R.id.kgChip);
        tonChip = view.findViewById(R.id.tonChip);
        confirmBtn = view.findViewById(R.id.confirmBtn);

        if (itemType == null) {
            nameTv.setText(item.getName());
            itemTv.setText("");
        } else {
            nameTv.setText(itemType.getName());
            itemTv.setText(item.getName());
        }

        countEt.setText(String.valueOf(cartItem == null ? stock.getQuantity() : cartItem.getQuantity()));

        minusBtn.setOnClickListener(minusView -> updateCount(false));
        plusBtn.setOnClickListener(plusView -> updateCount(true));
        confirmBtn.setOnClickListener(confirmBtn -> checkBooking());

        setDefaultSelectedChip();

    }

    private void updateCount(boolean isPlus) {
        String selectedUnit = getSelectedUnits();
        String currentCountText = countEt.getText().toString();
        double changeValue = 0f;
        double currentCount = Float.parseFloat(currentCountText);
        double changedCount = 0f;
        switch (selectedUnit) {
            case DummyDataGenerator.Units.KG: {
                changeValue = 50f;
            }
            break;
            case DummyDataGenerator.Units.TON: {
                changeValue = UnitConverter.convert(100f,
                        DummyDataGenerator.Units.KG,
                        DummyDataGenerator.Units.TON);
            }
            break;
        }

        if (!isPlus) {
            changeValue *= -1;
        }

        changedCount = currentCount + changeValue;

        if (changedCount < 0)
            changedCount = 0f;

        countEt.setText(String.format("%.2f", changedCount));

    }

    private String getSelectedUnits() {
        if (chipGroup.getCheckedChipId() == kgChip.getId()) {
            return DummyDataGenerator.Units.KG;
        } else if (chipGroup.getCheckedChipId() == tonChip.getId()) {
            return DummyDataGenerator.Units.TON;
        } else {
            return null;
        }
    }

    private void setDefaultSelectedChip() {
        String unit = cartItem == null ? stock.getUnit() : cartItem.getQuantityType();
        kgChip.setChecked(DummyDataGenerator.Units.KG.equals(unit));
        tonChip.setChecked(DummyDataGenerator.Units.TON.equals(unit));
    }

    private void checkBooking() {
        String selectedUnit = getSelectedUnits();
        String countText = countEt.getText().toString();
        float count = Float.parseFloat(countText);
        if (UnitConverter.convert(count, selectedUnit, stock.getUnit()) > stock.getQuantity()) {
            Toast.makeText(getContext(),
                    "Stock Limit: " + stock.getQuantity() + " " + stock.getUnit(),
                    Toast.LENGTH_LONG).show();
        } else {
            bookNow(count, selectedUnit);
        }
    }

    private void bookNow(float quantity, String unit) {
        CartItem cartItem = new CartItem();
        cartItem.setItemId(item.getId());
        cartItem.setItemTypeId(itemType != null ? itemType.getId() : "0");
        cartItem.setCategoryId(stock.getCategoryId());
        cartItem.setQuantity(quantity);
        cartItem.setQuantityType(unit);
        cartItem.setStockId(stockId);
        cartItem.setPrice(calculatePrice(quantity, unit));

        if (quantity == 0f){
            if (this.cartItem != null){
                removeCartItem();
                Toast.makeText(getContext(), "Removed!", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "Set valid Quantity!", Toast.LENGTH_LONG).show();
                return;
            }
        }else {
            cartManager.addToCart(cartItem);
            refreshStocks();
        }

        Toast.makeText(getContext(),
                "Booked!",
                Toast.LENGTH_LONG).show();
        dismiss();
    }

    private float calculatePrice(float quantity, String unit){
        String stockUnit = stock.getUnit();
        double convertedQuantity = UnitConverter.convert(quantity, unit, stockUnit);
        double percentStock = (convertedQuantity / stock.getQuantity()) * 100f;
        double calculatePrice = (percentStock / 100f) * stock.getPrice();
        System.out.println("Converted Quantity: " + convertedQuantity);
        System.out.println("Percent Stock: " + percentStock+"%");
        System.out.println("Calculated Price: " + calculatePrice);
        return (float) calculatePrice;
    }

    private void refreshStocks(){
        if (stockAction != null)
            stockAction.refreshStocks();
        if (cartAction != null)
            cartAction.refreshStocks();
    }

    private void removeCartItem(){
        if (stockAction != null){
            stockAction.removeCartItem(stockId);
        }

        if (cartAction != null){
            cartAction.deleteCartItem(stockId);
        }
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
