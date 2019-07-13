package com.miet.smarthome.models;

import java.util.ArrayList;
import java.util.List;

// T - триггер
public class Trigger {
    // У каждого триггера свой список действий
    private List<IIntent> intentList = new ArrayList<>();

    // Константы
    private float a;
    private float b;
    private Type type;

    private int sensorId;

    public Trigger(int sensorId, Type type) {
        this.sensorId = sensorId;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "intentList=" + intentList +
                ", a=" + a +
                ", b=" + b +
                ", type=" + type +
                ", sensorId=" + sensorId +
                '}';
    }

    public Trigger(int sensorId, Type type, float a) {
        this.sensorId = sensorId;
        this.type = type;
        this.a = a;
    }

    public Trigger(int sensorId, Type type, float a, float b) {
        this.sensorId = sensorId;
        this.type = type;
        this.a = a;
        this.b = b;
    }

    public Trigger addIntent(IIntent intent) {
        this.intentList.add(intent);
        return this;
    }

    public List<IIntent> getIntentList() {
        return intentList;
    }

    public void performAll(Sensor sensor) {
        for (IIntent intent : intentList) {
            intent.perform(sensor); // TODO ASYNC/SYNC
        }
    }

    public boolean isTriggered(float value) {
        if (this.type == Type.Exact) {
            return value == a;
        } else if (this.type == Type.More) {
            return value >= a;
        } else if (this.type == Type.Less) {
            return value <= a;
        } else if (this.type == Type.Range) {
            return value >= a && value <= b;
        } else if (this.type == Type.OutOfRange) {
            return value < a || value > b;
        }

        return false;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getType() {
        return this.type.toString();
    }

    public enum Type {
        Exact, // Точное значение, T = a
        More, // Больше или равно, T >= a
        Less, // Меньше или равно, T <= a
        Range, // Значение из промежутка, T>=a && T <= b
        OutOfRange, // Значение вне промежутка T<a || T > b
    }
}
