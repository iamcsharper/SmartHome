package com.miet.smarthome.models.intents;

import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.miet.smarthome.MainActivity;
import com.miet.smarthome.models.IIntent;
import com.miet.smarthome.models.Sensor;

public class OnIntent implements IIntent {
    @Override
    public void perform(Sensor sensor) {
//        Snackbar.make(MainActivity.mainView,
//                "Уфф, что-то тут жарковато! Порог превышен на "
//                        + sensor.formatValue(sensor.getDelta())
//                        + ". Включаю кондиционер!", Snackbar.LENGTH_LONG).show();
    }
}
