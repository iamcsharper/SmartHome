package com.miet.smarthome.models;

import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Sensor {
    private static int counter = 0;
    public int id;
    public boolean isTriggered = false;
    // У каждого датчика есть свой список триггеров
    private List<Trigger> triggerList = new ArrayList<>();
    // Любое значение датчика можно выразить численно в виде float
    private float value;
    private float prev_value;
    // Название датчика для человекопонятности
    private String name = "";

    public Sensor(int id) {
        this.id = id; // Уникальный идентификатор сенсора
    }

    public Sensor(int id, String name, float value) {
        this(id);
        this.name = name;
        this.value = value;
    }

    public Sensor(int id, String name) {
        this(id);
        this.name = name;
    }

    public Sensor(int id, float value) {
        this(id);
        this.value = value;
    }

    public Sensor() {
    }

    public static Sensor fromJsonObject(JSONObject sensorObject)
            throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String type = sensorObject.getString("type");

        Class c = Class.forName(Sensor.class.getPackage().getName() + ".sensors." + type + "Sensor");
        Sensor sensor = (Sensor) c.newInstance();

        sensor.id = sensorObject.getInt("id");
        sensor.setName(sensorObject.getString("name"));
        sensor.setValue((float) sensorObject.getDouble("value"));

        return sensor;
    }

    public void toJson(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("id").value(this.id);
        writer.name("name").value(this.name);
        writer.name("type").value(this.getClass().getSimpleName().split("Sensor")[0]);
        writer.name("value").value(this.value);
        writer.endObject();
    }

    public float getPrev_value() {
        return prev_value;
    }

    public void setPrev_value(float prev_value) {
        this.prev_value = prev_value;
    }

    public float getDelta() {
        return value - prev_value;
    }

    public void addTrigger(Trigger t) {
        triggerList.add(t);
    }

    public List<Trigger> getTriggerList() {
        return triggerList;
    }

    public boolean hasAnyTriggered() {
        boolean triggered = false;

        for (Trigger t : triggerList) {
            if (t.isTriggered(value))
                triggered = true;
        }

        return triggered;
    }

    public void checkTriggersAndPerform() {
        for (Trigger t : triggerList) {
            // При этом в тот раз не должно было быть триггера.
            if (t.isTriggered(value) && !t.isTriggered(prev_value)) {
                this.isTriggered = true;
                //Log.e("myTag", this.toString());
                t.performAll(this);
            }
            if (!t.isTriggered(value) && t.isTriggered(prev_value)) {
                this.isTriggered = false;
                //t.performAll(this); //TODO а надо ли вызывать чтобы отправить смс аля, да не все норм датчик просто барахлил
            }
        }
    }

    // Форматирование значения.
    // градусы, проценты, ppm, м/с и т.д.
    public abstract String formatValue(float value);

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.prev_value = this.value;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", triggerList=" + triggerList +
                ", value=" + value +
                ", prev_value=" + prev_value +
                ", name='" + name + '\'' +
                '}';
    }

    public void removeTrigger(int pos) {
        this.triggerList.remove(pos);
    }
}
