package com.miet.smarthome;

import android.util.Log;

import com.miet.smarthome.io.IOUtils;
import com.miet.smarthome.models.Trigger;
import com.miet.smarthome.models.intents.CallIntent;
import com.miet.smarthome.models.intents.OffIntent;
import com.miet.smarthome.models.intents.OnIntent;
import com.miet.smarthome.models.sensors.GasSensor;
import com.miet.smarthome.models.sensors.TemperatureSensor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsDatabase {
    private static final String defaultConfig = "{\"sensors\":[{\"id\":0,\"name\":\"Газ кухня\",\"type\":\"Gas\",\"value\":954.3685913085938},{\"id\":1,\"name\":\"Темп. спальня\",\"type\":\"Temperature\",\"value\":17.0}],\"triggers\":[{\"sensor_id\":0,\"type\":1,\"a\":1050.0,\"b\":0.0,\"intents\":[{\"type\":\"Call\"}]},{\"sensor_id\":1,\"type\":1,\"a\":30.0,\"b\":0.0,\"intents\":[{\"type\":\"On\"}]},{\"sensor_id\":1,\"type\":2,\"a\":20.0,\"b\":0.0,\"intents\":[{\"type\":\"Off\"}]}]}";
    private static SettingsDatabase _self = new SettingsDatabase();
    private String path;
    private File configFile;

    private SettingsDatabase() {
        // TODO load from file.
        //config.put("a", "b");
    }

    public static SettingsDatabase getInstance() {
        return _self;
    }

    public void load(String path) throws IOException {
        this.path = path + '/';
        this.configFile = new File(this.path + "sensors.json");

        //this.configFile.delete();

        Log.e("debug", "Path to local directory: " + path);

        if (!this.configFile.exists()) {
            try {
                Log.e("debug", this.configFile.createNewFile() ? "A NEW FILE CREATED!" : "NO CREATION");
            } catch (IOException e) {
                Log.e("debug", "An error occurred during creation: " + e.toString());
            }
        }

        try {
            this.parse();
        } catch (IOException e) {
            Log.e("parse", e.toString());
        }
    }

    public void save() throws IOException {
        Log.d("SettingsDB", "Saving...");
        SensorDatabase.getInstance().save(configFile);
    }

    // TODO split config and sensors.json files
    private void parseJson(JSONObject root) throws JSONException {
        SensorDatabase.getInstance().parseJson(root);
    }

    /***
     * Общий метод парсинга файла настроек. В случае если это первый запуск, записывает placeholder
     */
    private void parse() throws IOException {
        FileInputStream fis = new FileInputStream(this.configFile);

        String s = IOUtils.readStreamToString(fis);

        Log.e("FileContents", s);

        if (s.length() < 1) {
            FileOutputStream fos = new FileOutputStream(this.configFile);

            IOUtils.writeStringToStream(fos, defaultConfig);
            try {
                parseJson(new JSONObject(defaultConfig));
            } catch (JSONException e) {
                Log.e("SettingsDatabase:parse1", e.toString());
            }
        } else {
            try {
                parseJson(new JSONObject(s));
            } catch (JSONException e) {
                Log.e("SettingsDatabase:parse2", e.toString());
            }
        }
    }

}
