package com.miet.smarthome;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.miet.smarthome.models.intents.CallIntent;
import com.miet.smarthome.models.intents.OffIntent;
import com.miet.smarthome.models.intents.OnIntent;
import com.miet.smarthome.models.Trigger;
import com.miet.smarthome.networking.SensorData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    Toolbar toolbar;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;

    public static View mainView;

    boolean running = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.running = false;

        try {
            SettingsDatabase.getInstance().save();
            Log.e("Main", "Saved config!");
        } catch (IOException e) {
            Log.e("Main", "Error saving: " + e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager m = getPackageManager();
        String s = getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException ignored) {}

        try {
            SettingsDatabase.getInstance().load(s);
        } catch (IOException e) {
            Log.e("Main", Log.getStackTraceString(e));
        }

        setContentView(R.layout.activity_main);

        mainView = findViewById(android.R.id.content);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

      /*  FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Установлена аццкая температура.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                List<SensorData> sensorData = new ArrayList<>();
                sensorData.add(new SensorData(0, 1128));
                sensorData.add(new SensorData(1, 50));
                SensorDatabase.getInstance().updateSensors(sensorData);
            }
        });*/


        makeData();

        try {
            SettingsDatabase.getInstance().save();
        } catch (IOException e) {
            Log.e("Main", Log.getStackTraceString(e));
        }
    }

    float gas = 1000;
    float temp = 12;
    int inc = 1;
    long ticks = 0;

    long kek = 0;


    void makeData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ticks % 5000 == 0) {
                    //TODO
//                    try {
//                       SettingsDatabase.getInstance().save();
//                    } catch (IOException e) {
//                        Log.e("MakeData", e.toString());
//                    }
                }
                if (ticks % 1000 == 0) {
                    List<SensorData> sensorData = new ArrayList<>();
                    gas = (float) (1000.f+100.f*(0.5f+Math.sin(kek*628f)));
                    sensorData.add(new SensorData(0, gas));
                    sensorData.add(new SensorData(1, temp += inc));

                    SensorDatabase.getInstance().updateSensors(sensorData);


                    if (temp < 10) {
                        inc = 1;
                    }
                    if (temp > 40) {
                        inc = -1;
                    }
                    kek++;
                }

                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        makeData();
                    }
                })).start();


                ticks++;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
