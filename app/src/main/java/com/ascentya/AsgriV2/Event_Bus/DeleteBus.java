package com.ascentya.AsgriV2.Event_Bus;


import com.squareup.otto.Bus;

/**
 * Created by Bala on 11-01-2017.
 */
public class DeleteBus {
    private static Bus instance = null;

    private DeleteBus() {
        instance = new Bus();
    }

    public static Bus getInstance() {
        if (instance == null) {
            instance = new Bus();
        }
        return instance;
    }
}
