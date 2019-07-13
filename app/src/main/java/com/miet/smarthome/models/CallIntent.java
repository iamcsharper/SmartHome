package com.miet.smarthome.models;

import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.miet.smarthome.MainActivity;

public class CallIntent implements IIntent {

    @Override
    public void perform(Sensor sensor) {
        Snackbar.make(MainActivity.mainView, "О нет! Утечка газа составляет " + ((GasSensor) sensor).formatValue(sensor.getValue()) + " Это выше нормы на "
                + sensor.formatValue(sensor.getDelta()) + "! Вызываю газовую службу. Виу-виу...", Snackbar.LENGTH_LONG).show();
    }
}
