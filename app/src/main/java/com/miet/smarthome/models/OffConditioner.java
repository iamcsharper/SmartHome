package com.miet.smarthome.models;

import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.miet.smarthome.MainActivity;

public class OffConditioner implements IIntent {
    @Override
    public void perform(Sensor sensor) {
        Snackbar.make(MainActivity.mainView,
                "что-то прохладно"
                        + sensor.formatValue(sensor.getValue())
                        + " как-никак... А вот когда было "
                        + sensor.formatValue(sensor.getPrev_value()) + " - хорошо было..", Snackbar.LENGTH_LONG).show();
    }
}
