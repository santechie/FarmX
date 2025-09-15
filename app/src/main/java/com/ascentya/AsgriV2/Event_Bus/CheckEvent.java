package com.ascentya.AsgriV2.Event_Bus;


public class CheckEvent {

    String data;

    public CheckEvent(String flag) {
        this.data = flag;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
