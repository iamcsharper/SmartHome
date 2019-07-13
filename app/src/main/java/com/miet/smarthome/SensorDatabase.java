package com.miet.smarthome;

import android.net.wifi.p2p.WifiP2pManager;
import android.util.EventLog;
import android.util.Log;
import android.widget.Toast;

import com.miet.smarthome.models.GasSensor;
import com.miet.smarthome.models.Sensor;
import com.miet.smarthome.networking.SensorData;
import com.miet.smarthome.models.TemperatureSensor;
import com.miet.smarthome.models.Trigger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SensorDatabase {
    public static final List<Sensor> sensors = new ArrayList<>();

    private List<EventHandler> onDataChangedListeners = new ArrayList<>();

    public void addDataChangedListener(EventHandler listener) {
        this.onDataChangedListeners.add(listener);
    }

    public void removeDataChangedListener(EventHandler listener) {
        this.onDataChangedListeners.remove(listener);
    }

    private static final SensorDatabase ourInstance = new SensorDatabase();

    public static SensorDatabase getInstance() {
        return ourInstance;
    }

    private SensorDatabase() {
        GasSensor gasSensor = new GasSensor(0, "Газ кухня", 0);
        TemperatureSensor temperatureSensor = new TemperatureSensor(1, "Темп. спальня", 0);

        sensors.add(gasSensor);
        sensors.add(temperatureSensor);
    }

    // TODO рассмотреть все возможные варианты реализации
    public synchronized void updateTriggers(List<Trigger> triggers) {
        synchronized (sensors) {
            for (Trigger trigger : triggers) {
                for (Sensor sensor : sensors) {
                    if (sensor.id == trigger.getSensorId()) {
                        sensor.addTrigger(trigger);
                        break;
                    }
                }
            }
        }
    }

    public synchronized void addSensor(Sensor sensor) {
        synchronized (sensors) {
            sensors.add(sensor);
        }
    }

    // TODO будем считать, что база отправляет нам структуру из данных id<->значение
    public synchronized void updateSensors(List<SensorData> newSensorData) {
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
}
