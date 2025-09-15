package com.ascentya.AsgriV2.Event_Bus;

import com.squareup.otto.Bus;

public class CheckBus {
    private static Bus instance = null;

    private CheckBus() {
        instance = new Bus();
    }

    public static Bus getInstance() {
        if (instance == null) {
            instance = new Bus();
        }
        return instance;
    }
}
