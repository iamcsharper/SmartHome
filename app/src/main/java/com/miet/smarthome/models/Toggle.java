package com.miet.smarthome.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Toggle {
    private boolean isOn;
    private int address;

    public static Toggle fromJsonObject(JSONObject toggleObject)
            throws JSONException {

        Toggle toggle = new Toggle();

        toggle.setAddress(toggleObject.getInt("address"));
        toggle.setOn(toggleObject.getBoolean("is_on"));

        return toggle;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
