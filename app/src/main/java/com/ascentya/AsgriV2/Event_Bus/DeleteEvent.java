package com.ascentya.AsgriV2.Event_Bus;

public class DeleteEvent {

    String flag;

    public DeleteEvent(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
