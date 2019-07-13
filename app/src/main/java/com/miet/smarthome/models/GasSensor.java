package com.miet.smarthome.models;

import java.text.DecimalFormat;

public class GasSensor extends Sensor {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public GasSensor(int id, String name, float value) {
        super(id, name, value);
    }

    public GasSensor(int id, String name) {
        super(id, name);
    }

    public GasSensor(int id, float value) {
        super(id, value);
    }

    public GasSensor(int id) {
        super(id);
    }


    @Override
    public String formatValue(float value) {
        return decimalFormat.format(value) + " ppm";
    }
}
