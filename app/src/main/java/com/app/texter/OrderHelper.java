package com.app.texter;

public class OrderHelper {
    String call_state;
    String auto_rec;

    public OrderHelper() {}

    public OrderHelper(String call_state, String auto_rec) {
        this.call_state = call_state;
        this.auto_rec = auto_rec;
    }

    public String getAuto_rec() {
        return auto_rec;
    }

    public void setAuto_rec(String auto_rec) {
        this.auto_rec = auto_rec;
    }

    public String getCall_state() {
        return call_state;
    }

    public void setCall_state(String call_state) {
        this.call_state = call_state;
    }

}
