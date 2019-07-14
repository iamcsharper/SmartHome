package com.miet.smarthome;

import android.util.JsonWriter;
import android.util.Log;

import com.miet.smarthome.models.Sensor;
import com.miet.smarthome.models.Trigger;
import com.miet.smarthome.networking.SensorData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class SensorDatabase {
    public static final List<Sensor> sensors = new ArrayList<>();
    private static final SensorDatabase ourInstance = new SensorDatabase();
    private List<EventHandler> onDataChangedListeners = new ArrayList<>();

    private SensorDatabase() {

    }

    public static SensorDatabase getInstance() {
        return ourInstance;
    }

    public void addDataChangedListener(EventHandler listener) {
        this.onDataChangedListeners.add(listener);
    }

    public void removeDataChangedListener(EventHandler listener) {
        this.onDataChangedListeners.remove(listener);
    }

    // TODO рассмотреть все возможные варианты реализации
    synchronized void updateTriggers(List<Trigger> triggerList) {
        synchronized (sensors) {
            for (Trigger trigger : triggerList) {
                for (Sensor sensor : sensors) {
                    if (sensor.id == trigger.getSensorId()) {
                        sensor.addTrigger(trigger);
                        break;
                    }
                }
            }
        }

        for (EventHandler listener : onDataChangedListeners) {
            listener.handle();
        }
    }

    public synchronized void addSensor(Sensor sensor) {
        synchronized (sensors) {
            sensors.add(sensor);
        }

        for (EventHandler listener : onDataChangedListeners) {
            listener.handle();
        }
    }

    // TODO будем считать, что база отправляет нам структуру из данных id<->значение
    synchronized void updateSensors(List<SensorData> newSensorData) {
        for (SensorData sensorData : newSensorData) {
            for (Sensor sensor : sensors) {
                if (sensor.id == sensorData.id) { // Нашли айди, вышли.
                    sensor.setPrev_value(sensor.getPrev_value());
                    sensor.setValue(sensorData.value);
                    sensor.checkTriggersAndPerform(); //TODO а точно ли нужно сразу вызывать триггеры и действия?)
                    break;
                }
            }
        }

        for (EventHandler listener : onDataChangedListeners) {
            listener.handle();
        }
    }

    void parseJson(JSONObject root) throws JSONException {
        Log.e("SensorDB:parseJson", root.toString());

        JSONArray sensors = root.getJSONArray("sensors");
        JSONArray triggers = root.getJSONArray("triggers");

        Log.e("Sensors", sensors.toString());


        for (int i = 0; i < sensors.length(); i++) {
            JSONObject sensorObject = (JSONObject) sensors.get(i);
            try {
                Sensor sensor = Sensor.fromJsonObject(sensorObject);

                addSensor(sensor);
            } catch (Exception e) {
                Log.e("Sensors parse", Log.getStackTraceString(e));
            }
        }

        List<Trigger> triggerList = new ArrayList<>(triggers.length());

        for (int i = 0; i < triggers.length(); i++) {
            JSONObject object = (JSONObject) triggers.get(i);
            try {
                Trigger trigger = Trigger.fromJsonObject(object);

                triggerList.add(trigger);
            } catch (Exception e) {
                Log.e("Triggers parse", e.toString());
            }
        }

        updateTriggers(triggerList);
    }

    void save(File to) throws IOException {
        Log.d("SensorDB", "Saving...");
        FileOutputStream fos = new FileOutputStream(to);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
        writer.setIndent("  ");
        writer.beginObject();

        writer.name("sensors");
        writer.beginArray();

        for (int i = 0; i < sensors.size(); i++) {
            sensors.get(i).toJson(writer);
        }

        writer.endArray();

        writer.name("triggers");
        writer.beginArray();

        for (int i = 0; i < sensors.size(); i++) {
            List<Trigger> triggersList = sensors.get(i).getTriggerList();
            for (int j = 0; j < triggersList.size(); j++) {
                triggersList.get(j).toJson(writer);
            }
        }

        writer.endArray();
        writer.endObject();
        writer.close();
    }
}
