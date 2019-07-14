package com.miet.smarthome.models.sensors;

import com.miet.smarthome.models.Sensor;

import java.text.DecimalFormat;

public class TemperatureSensor extends Sensor {
    public TemperatureSensor(int id, String name, float value) {
        super(id, name, value);
    }

    public TemperatureSensor(int id, String name) {
        super(id, name);
    }

    public TemperatureSensor(int id, float value) {
        super(id, value);
    }

    public TemperatureSensor() {}

    @Override
    public String formatValue(float value) {
        return (int) value + " °C";
    }
}
