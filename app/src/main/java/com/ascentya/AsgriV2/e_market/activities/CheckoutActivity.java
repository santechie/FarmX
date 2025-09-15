package com.ascentya.AsgriV2.e_market.activities;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.e_market.data.model.Address;
import com.ascentya.AsgriV2.e_market.data.model.CartItem;
import com.google.android.material.button.MaterialButton;

public class CheckoutActivity extends BaseActivity {

    public static final int GOOGLE_PAY = 1;
    public static final int CARD = 2;
    public static final int COD = 3;

    private ConstraintLayout addressLayout;
    private TextView addressLine1, addressLine2, addressLine3, addressLine4;
    private MaterialButton selectAddressBtn;

    private EditText mobileNumberEt;

    private TextView subTotalTv, deliveryChargeTv, taxTv, totalTv;
    private RadioGroup paymentGroup;
    private RadioButton googlePayRb, cardRb, codRb;

    private AppCompatButton payNowBtn;

    private Address address;

    private int paymentMethod = GOOGLE_PAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        setToolbarTitle("Checkout", true);

        addressLayout = findViewById(R.id.address);
        addressLine1 = addressLayout.findViewById(R.id.line1);
        addressLine2 = addressLayout.findViewById(R.id.line2);
        addressLine3 = addressLayout.findViewById(R.id.line3);
        addressLine4 = addressLayout.findViewById(R.id.line4);
        mobileNumberEt = findViewById(R.id.mobileNumber);
        selectAddressBtn = findViewById(R.id.selectAddress);
        subTotalTv = findViewById(R.id.subTotal);
        deliveryChargeTv = findViewById(R.id.delivery);
        taxTv = findViewById(R.id.tax);
        totalTv = findViewById(R.id.total);
        paymentGroup = findViewById(R.id.paymentGroup);
        googlePayRb = findViewById(R.id.googlePay);
        cardRb = findViewById(R.id.card);
        codRb = findViewById(R.id.cashOnDelivery);
        payNowBtn = findViewById(R.id.payNow);

        selectAddressBtn.setOnClickListener(view ->
                openActivity(AddressListActivity.class));

        payNowBtn.setOnClickListener(view -> payNow());

        setUpPaymentModeListener();
        checkAddress();
        updatePaymentDetails();
        updatePaymentButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAddress();
    }

    private void setUpPaymentModeListener(){
        paymentGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.googlePay:
                    paymentMethod = GOOGLE_PAY;
                    break;
                case R.id.card:
                    paymentMethod = CARD;
                    break;
                case R.id.cashOnDelivery:
                    paymentMethod = COD;
                    break;
            }
            updatePaymentButton();
        });
    }

    private void checkAddress(){
        address = storage.getSelectedAddress();
        addressLayout.setVisibility(address == null ? View.GONE : View.VISIBLE);
        selectAddressBtn.setText((address == null ? "Select" : "Change") + " Address");
        if (address != null){
            addressLine1.setText(String.format("%s, %s", address.getDoorNumber(), address.getLandmark()));
            addressLine2.setText(String.format("%s, %s", address.getStreet(), address.getArea()));
            addressLine3.setText(String.format("%s - %s", address.getCity(), address.getPincode()));
            addressLine4.setText(String.format("%s, %s", address.getDistrict(), address.getState()));
        }
    }

    private void updatePaymentDetails(){
        float subtotal = getSubTotal();
        float deliveryCharge = getDeliveryCharge();
        float tax = getTax(subtotal + deliveryCharge);
        float total = getTotal(subtotal, deliveryCharge, tax);

        subTotalTv.setText(ProductUtils.getPrice(String.valueOf(subtotal)));
        deliveryChargeTv.setText(ProductUtils.getPrice(String.valueOf(deliveryCharge)));
        taxTv.setText(ProductUtils.getPrice(String.valueOf(tax)));
        totalTv.setText(ProductUtils.getPrice(String.valueOf(total)));
    }

    private void updatePaymentButton(){
        switch (paymentMethod){
            case GOOGLE_PAY:
            case CARD:
                payNowBtn.setText("Pay Now");
                break;
            case COD:
                payNowBtn.setText("Confirm Order");
                break;
        }
    }

    private void payNow(){
        switch (paymentMethod){
            case GOOGLE_PAY:
            case CARD:
            case COD:
                openActivity(OrderConfirmationActivity.class);
        }
    }

    private float getSubTotal(){
        float totalPrice = 0;
        for (CartItem cartItem: cartManager.getAllCartItems()){
            if (!ProductUtils.isZero(String.valueOf(cartItem.getPrice()))){
                totalPrice += cartItem.getPrice();
            }
        }

        return totalPrice;
    }

    private float getDeliveryCharge(){
        return 1500f;
    }

    private float getTax(float total){
        return total * 0.06f;
    }

    private float getTotal(float subTotal, float deliveryCharge, float tax){
        return subTotal + deliveryCharge + tax;
    }
}