package com.ascentya.AsgriV2.buysell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.stripe.android.googlepaylauncher.GooglePayEnvironment;
import com.stripe.android.googlepaylauncher.GooglePayLauncher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class Choose_Payment extends AppCompatActivity {
    ThemedToggleButtonGroup paymentOptions;
    Button cart_checkout_btn;
    private GooglePayLauncher googlePayLauncher;
//    private GooglePayPaymentMethodLauncher googlePayPaymentMethodLauncher;
    private int selectedPayment = -1;
    private boolean isGooglePayReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);

        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        paymentOptions = findViewById(R.id.cards);
        cart_checkout_btn = findViewById(R.id.cart_checkout_btn);

        cart_checkout_btn.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                processSelectedPaymentMethod();
            }
        });

        paymentOptions.setOnSelectListener(new Function1<ThemedButton, Unit>() {
            @Override
            public Unit invoke(ThemedButton themedButton) {
                selectedPayment = themedButton.getId();
//                Toasty.info(Choose_Payment.this, "Clicked: " + themedButton.getId()).show();
                return null;
            }
        });

        setUpGooglePay();
    }

    private void processSelectedPaymentMethod() {
        if (selectedPayment < 0) {
            Toasty.normal(this, "Select Payment Method!").show();
            return;
        }

        switch (selectedPayment) {
            // Wallet / UPI
            case R.id.card1:
//                Toasty.normal(this, "Google Pay not Implemented!");
                googlePay();
                break;

            // Credit / Debit Card
            case R.id.card2:
                goToCardPayment();
                break;

            case R.id.card3:
                cashOnDelivery();
                break;

            default:
                Toasty.normal(this, "Not Implemented!").show();
                break;

        }
    }

    private void goToCardPayment() {
        Intent intent = new Intent(this, CardPaymentActivity.class);
        startActivity(intent);
    }

    private void cashOnDelivery() {
        Intent intent = new Intent(this, Status_Screen.class);
        startActivity(intent);
    }

    private void setUpGooglePay() {

      /*  googlePayPaymentMethodLauncher = new GooglePayPaymentMethodLauncher(
                this,
                new GooglePayPaymentMethodLauncher.Config(
                        GooglePayEnvironment.Test,
                        "IN",
                        "FarmX"),
                this::onGooglePayReady,
                this::onGooglePayResult);*/

        googlePayLauncher =
                new GooglePayLauncher(this,
                        new GooglePayLauncher.Config(
                                GooglePayEnvironment.Test,
                                "IN",
                                getString(R.string.app_name)),
                        this::onGooglePayReady,
                        this::onGooglePayResult);
    }

    private void onGooglePayReady(boolean isReady) {
        isGooglePayReady = isReady;
    }

    private void onGooglePayResult(@NonNull GooglePayLauncher.Result result) {
        if (result instanceof GooglePayLauncher.Result.Completed) {
            Toasty.normal(this, "Google Pay Completed!").show();
        } else if (result instanceof GooglePayLauncher.Result.Canceled) {
            Toasty.normal(this, "Google Pay Cancelled!").show();
        } else if (result instanceof GooglePayLauncher.Result.Failed) {
            Toasty.normal(this, "Google Pay Failed!").show();
        }
    }

    private void googlePay() {
        if (!isGooglePayReady) {
            Toasty.normal(this, "Google Pay not Ready!").show();
            return;
        }

        if (googlePayLauncher != null)
            googlePayLauncher.presentForPaymentIntent("<CLIENT_SECRET>");
//            googlePayLauncher.presentForSetupIntent("<CLIENT_SECRET>", "INR");
//            googlePayLauncher.present();
    }
}