package com.ascentya.AsgriV2.e_market.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.data.Modules;
import com.ascentya.AsgriV2.e_market.data.DummyDataGenerator;
import com.ascentya.AsgriV2.e_market.data.UnitConverter;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.ascentya.AsgriV2.e_market.data.model.Stock;
import com.ascentya.AsgriV2.managers.ModuleManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class StockDetailsActivity extends BaseActivity {

    public static final String STOCK = "stock";

    private TextView categoryTv, itemTypeTv, itemTv, quantityTv, priceTv, dateTv, placeTv;
    private FloatingActionButton plusBtn, minusBtn;
    private MaterialButton bookBtn;
    private ImageView deleteBtn;
    private EditText quantityEt;
    private ChipGroup chipGroup;
    private Chip kgChip, tonChip;
    private Stock stock;
    private CartItem cartItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        setToolbarTitle("Stock Details", true);
        setMenu(R.menu.stock_details_menu);
        stock = getFromIntent(STOCK, Stock.class);
        cartItem = cartManager.getCartItem(stock.getId());

        categoryTv = findViewById(R.id.categoryName);
        itemTypeTv = findViewById(R.id.itemTypeName);
        itemTv = findViewById(R.id.itemName);
        quantityTv = findViewById(R.id.quantity);
        priceTv = findViewById(R.id.price);
        dateTv = findViewById(R.id.date);
        placeTv = findViewById(R.id.place);
        quantityEt = findViewById(R.id.quantityInput);
        chipGroup = findViewById(R.id.chipGroup);
        kgChip = findViewById(R.id.kgChip);
        tonChip = findViewById(R.id.tonChip);

        plusBtn = findViewById(R.id.plusFab);
        minusBtn = findViewById(R.id.minusFab);

        bookBtn = findViewById(R.id.book);
        deleteBtn = findViewById(R.id.delete);

        plusBtn.setOnClickListener(view -> updateCount(true));
        minusBtn.setOnClickListener(view -> updateCount(false));

        chipGroup.setSingleSelection(true);
        chipGroup.setSelectionRequired(true);

        kgChip.setCheckable(true);
        tonChip.setCheckable(true);

        kgChip.setOnCheckedChangeListener(((compoundButton, b) -> {
            System.out.println("KG: " + b);
            if (cartItem != null && b &&
                    !cartItem.getQuantityType()
                            .equals(DummyDataGenerator.Units.KG)) { updateControls(); }
        }));
        tonChip.setOnCheckedChangeListener((compoundButton, b) -> {
            System.out.println("TON: " + b);
            if (cartItem != null && b &&
                    !cartItem.getQuantityType()
                            .equals(DummyDataGenerator.Units.TON)) { updateControls(); }
        });

        bookBtn.setOnClickListener(view -> checkBooking());
        deleteBtn.setOnClickListener(view -> {
            removeFromCart();
        });

        updateUI();
        updateControls();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                openActivity(CartActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        if (stock == null)
            return;

        categoryTv.setText(dataRepository.getCategory(stock.getCategoryId()).getName());
        itemTv.setText("in " + dataRepository.getItem(stock.getCategoryId(), stock.getItemId()).getName());
        itemTypeTv.setText(dataRepository.getItemType(stock.getCategoryId(), stock.getItemId(), stock.getItemTypeId()).getName());
        quantityTv.setText(String.format("%.2f %s", stock.getQuantity(), stock.getUnit()));
        priceTv.setText(ProductUtils.getPrice(stock.getPrice() + ""));
        dateTv.setText(String.format("%s - %s", stock.getDateStart(), stock.getDateEnd()));
        placeTv.setText(stock.getPlaceName());

        if (cartItem == null){
            quantityEt.setText(String.format("%.2f", stock.getQuantity()));
            kgChip.setChecked(stock.getUnit().equals(DummyDataGenerator.Units.KG));
            tonChip.setChecked(stock.getUnit().equals(DummyDataGenerator.Units.TON));
        }else{
            quantityEt.setText(String.format("%.2f", cartItem.getQuantity()));
            kgChip.setChecked(cartItem.getQuantityType().equals(DummyDataGenerator.Units.KG));
            tonChip.setChecked(cartItem.getQuantityType().equals(DummyDataGenerator.Units.TON));
        }
    }

    private void updateControls() {

        cartItem = cartManager.getCartItem(stock.getId());

        if (cartItem != null) {
            boolean isUpdate = isUpdate();
            System.out.println("isUpdate: "+isUpdate);
            if (!isUpdate) {
                bookBtn.setText("Booked");
                bookBtn.setEnabled(false);

                quantityEt.setText(String.format("%.2f", cartItem.getQuantity()));
                kgChip.setChecked(cartItem.getQuantityType().equals(DummyDataGenerator.Units.KG));
                tonChip.setChecked(cartItem.getQuantityType().equals(DummyDataGenerator.Units.TON));
            } else {
                bookBtn.setText("Update");
                bookBtn.setEnabled(true);
            }
            deleteBtn.setImageTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.mat_red)));
            deleteBtn.setEnabled(true);
        } else {
            bookBtn.setText("Book");
            bookBtn.setEnabled(true);

//            quantityEt.setText(String.format("%.2f", stock.getQuantity()));
//            kgChip.setChecked(stock.getUnit().equals(DummyDataGenerator.Units.KG));
//            tonChip.setChecked(stock.getUnit().equals(DummyDataGenerator.Units.TON));

            deleteBtn.setImageTintList(
                    ColorStateList.valueOf(
                            ContextCompat.getColor(this, R.color.grey_5)));
            deleteBtn.setEnabled(false);
        }
    }

    private boolean isUpdate() {
        cartItem = cartManager.getCartItem(stock.getId());
        return cartItem != null && (!isSameQuantity() ||
                !isSameQuantityType());
    }

    private boolean isSameQuantity(){
        cartItem = cartManager.getCartItem(stock.getId());
        if (cartItem != null){
            boolean isSameQuantity = Float.parseFloat(quantityEt.getText().toString()) == cartItem.getQuantity();
            System.out.println("isSameQuantity: "+isSameQuantity);
            return isSameQuantity;
        }
        return false;
    }

    private boolean isSameQuantityType() {
        boolean isSame = false;
        cartItem = cartManager.getCartItem(stock.getId());
        if (cartItem != null) {
            switch (cartItem.getQuantityType()) {
                case DummyDataGenerator.Units.KG:
                    isSame = kgChip.isChecked();
                    break;
                case DummyDataGenerator.Units.TON:
                    isSame = tonChip.isChecked();
                    break;
            }
        }
        System.out.println("isSameType: "+isSame);
        return isSame;
    }

    private void removeFromCart() {
        cartManager.removeFromCart(stock.getId());
        cartItem = cartManager.getCartItem(stock.getId());
        updateControls();
    }

    private void updateCount(boolean isPlus) {
        String selectedUnit = getSelectedUnits();
        String currentCountText = quantityEt.getText().toString();
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

        quantityEt.setText(String.format("%.2f", changedCount));

        cartItem = cartManager.getCartItem(stock.getId());
        if(cartItem != null)
            updateControls();
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

    private void checkBooking() {
        if (!checkSubscription(Modules.EMARKET, ModuleManager.ACCESS.INSERT)) return;

        String selectedUnit = getSelectedUnits();
        String countText = quantityEt.getText().toString();
        float count = Float.parseFloat(countText);
        if (UnitConverter.convert(count, selectedUnit, stock.getUnit()) > stock.getQuantity()) {
            Toast.makeText(this,
                    "Stock Limit: " + stock.getQuantity() + " " + stock.getUnit(),
                    Toast.LENGTH_LONG).show();
        } else {
            bookNow(count, selectedUnit);
        }
    }

    private void bookNow(float quantity, String unit) {

        CartItem cartItem = new CartItem();
        cartItem.setItemId(stock.getItemId());
        cartItem.setItemTypeId(stock.getItemTypeId());
        cartItem.setCategoryId(stock.getCategoryId());
        cartItem.setQuantity(quantity);
        cartItem.setQuantityType(unit);
        cartItem.setStockId(stock.getId());
        cartItem.setPrice(calculatePrice(quantity, unit));

        if (quantity == 0f) {
            if (this.cartItem != null) {
                removeFromCart();
                Toast.makeText(this, "Removed!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Set valid Quantity!", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            cartManager.addToCart(cartItem);
            updateControls();
        }

        Toast.makeText(this,
                "Booked!",
                Toast.LENGTH_LONG).show();
    }

    private float calculatePrice(float quantity, String unit) {
        String stockUnit = stock.getUnit();
        double convertedQuantity = UnitConverter.convert(quantity, unit, stockUnit);
        double percentStock = (convertedQuantity / stock.getQuantity()) * 100f;
        double calculatePrice = (percentStock / 100f) * stock.getPrice();
        System.out.println("Converted Quantity: " + convertedQuantity);
        System.out.println("Percent Stock: " + percentStock + "%");
        System.out.println("Calculated Price: " + calculatePrice);
        return (float) calculatePrice;
    }

}