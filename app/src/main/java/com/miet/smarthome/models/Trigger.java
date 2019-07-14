package com.miet.smarthome.models;

import android.util.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

    public static Trigger fromJsonObject(JSONObject triggerObject)
            throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        int sensorId = triggerObject.getInt("sensor_id");
        int type = triggerObject.getInt("type");
        float a = (float) triggerObject.getDouble("a");
        float b = (float) triggerObject.getDouble("b");

        Trigger trigger = new Trigger(sensorId, Type.values()[type], a, b);

        JSONArray intents = triggerObject.getJSONArray("intents");

        for (int i = 0; i < intents.length(); i++) {
            JSONObject intentObject = (JSONObject) intents.get(i);
            String intentType = intentObject.getString("type");

            Class c = Class.forName(Trigger.class.getPackage().getName() + ".intents." + intentType + "Intent");
            IIntent intent = (IIntent) c.newInstance();

            trigger.addIntent(intent);
        }

        return trigger;
    }

    public void toJson(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("sensor_id").value(this.sensorId);
        writer.name("type").value(this.type.ordinal());
        writer.name("a").value(this.a);
        writer.name("b").value(this.b);

        writer.name("intents");
        writer.beginArray();

        for (int i = 0; i < intentList.size(); i++) {
            IIntent intent = intentList.get(i);
            writer.beginObject();
            writer.name("type").value(intent.getClass().getSimpleName().split("Intent")[0]);
            writer.endObject();
        }

        writer.endArray();

        writer.endObject();
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

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        /*0*/ Exact, // Точное значение, T = a
        /*1*/ More, // Больше или равно, T >= a
        /*2*/ Less, // Меньше или равно, T <= a
        /*3*/ Range, // Значение из промежутка, T>=a && T <= b
        /*4*/ OutOfRange, // Значение вне промежутка T<a || T > b
    }
}
