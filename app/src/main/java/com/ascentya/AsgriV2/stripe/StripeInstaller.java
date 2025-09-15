package com.ascentya.AsgriV2.stripe;

import android.content.Context;

import com.ascentya.AsgriV2.R;
import com.stripe.android.PaymentConfiguration;

public class StripeInstaller {

    public static void init(Context applicationContext){
        PaymentConfiguration
                .init(applicationContext,
                        applicationContext.getString(R.string.stripe_publishable_key));
    }
}
