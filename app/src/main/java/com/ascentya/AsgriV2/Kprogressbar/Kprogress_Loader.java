package com.ascentya.AsgriV2.Kprogressbar;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

public class Kprogress_Loader {
    KProgressHUD hud;

    public void Initial(Context context) {
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    public void progress() {
        hud.show();

    }

    public void stop() {

        if (hud.isShowing()) {
            hud.dismiss();
        }


    }
}
