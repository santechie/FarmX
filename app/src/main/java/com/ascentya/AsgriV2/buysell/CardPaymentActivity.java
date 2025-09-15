package com.ascentya.AsgriV2.buysell;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONObject;

public class CardPaymentActivity extends AppCompatActivity {

    private CardInputWidget cardInputWidget;
    private String paymentIntentClientSecret;
    private PaymentLauncher paymentLauncher;
    private PaymentConfiguration paymentConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);

        setTitle("Card Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardInputWidget = findViewById(R.id.cardInputWidget);


        findViewById(R.id.payNow).setOnClickListener(view -> validateCard());

        setUpPaymentConfig();
        getPaymentIntentClientSecret();
    }

    private void getPaymentIntentClientSecret(){
        AndroidNetworking
                .post("https://farmx-android-api.herokuapp.com/create_payment_intent")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("" +
                                "PaymentIntentClientSecret LandDeviceData:\n" + response);
                        paymentIntentClientSecret = response.optString("secret");
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void setUpPaymentConfig(){
        paymentConfiguration = PaymentConfiguration.getInstance(this);
        paymentLauncher = PaymentLauncher.Companion.create(this,
                paymentConfiguration.getPublishableKey(),
                paymentConfiguration.getStripeAccountId(),
                this::onPaymentResult);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void validateCard(){
        PaymentMethodCreateParams paymentMethodCreateParams =
                cardInputWidget.getPaymentMethodCreateParams();
        if (paymentMethodCreateParams != null){
            final ConfirmPaymentIntentParams confirmPaymentIntentParams =
                    ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                            paymentMethodCreateParams,
                            paymentIntentClientSecret
                    );
            paymentLauncher.confirm(confirmPaymentIntentParams);
        }
    }

    private void onPaymentResult(PaymentResult paymentResult) {
        String message = "";
        if (paymentResult instanceof PaymentResult.Completed) {
            message = "Completed!";
        } else if (paymentResult instanceof PaymentResult.Canceled) {
            message = "Cancelled!";
        } else if (paymentResult instanceof PaymentResult.Failed) {
            // This string comes from the PaymentIntent's error message.
            // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
            message = "Failed: "
                    + ((PaymentResult.Failed) paymentResult).getThrowable().getLocalizedMessage();
            ((PaymentResult.Failed) paymentResult).getThrowable().printStackTrace();
            System.out.println("Payment Result: \n" + GsonUtils.getGson().toJson(paymentResult));
        }

        Toasty.normal(this, "PaymentResult: " + message).show();
    }
}