package com.miet.smarthome;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.miet.smarthome.models.Sensor;
import com.miet.smarthome.models.Trigger;

import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class AddTriggerWindow implements PopupWindow.OnDismissListener {

    private Drawable dim = new ColorDrawable(Color.BLACK);
    private ViewGroup root;
    private ViewGroupOverlay overlay;
    private ValueAnimator opacityAnimator;


    private Sensor selectedSensor;
    private Trigger trigger;

    private static LayoutInflater inflater;
    private PopupWindow popupWindow;
    private View popupView;


    public AddTriggerWindow(final Activity activity, int sensorPos) {
        this.root = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        this.overlay = this.root.getOverlay();
        this.trigger = new Trigger();
        this.selectedSensor = SensorDatabase.getInstance().getSensor(sensorPos);

        ArrayList<String> sensorList = new ArrayList<>();
        for (int i = 0; i < SensorDatabase.getInstance().size(); i++) {
            sensorList.add(SensorDatabase.getInstance().getSensor(i).getName());
        }

        if (inflater == null) {
            inflater = (LayoutInflater)
                    activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        popupView = inflater.inflate(R.layout.add_trigger_window, null);

        // Выбор сенсора
        Spinner sensorsListView = popupView.findViewById(R.id.add_trigger_window_sensor);
        sensorsListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("bl", "CLICKED " + i);
                selectedSensor = SensorDatabase.getInstance().getSensor(i);
                trigger.setSensorId(selectedSensor.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getBaseContext(), android.R.layout.simple_spinner_item, sensorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorsListView.setAdapter(adapter);

        sensorsListView.setSelection(sensorPos);
        sensorsListView.setSelected(true);


        // Ввод А
        final EditText editA = popupView.findViewById(R.id.add_trigger_window_a);
        final TextView editALabel = popupView.findViewById(R.id.add_trigger_window_a_label);

        // Ввод B
        final EditText editb = popupView.findViewById(R.id.add_trigger_window_b);
        final TextView editBLabel = popupView.findViewById(R.id.add_trigger_window_b_label);

        final TextView helperText = popupView.findViewById(R.id.helperText);

        final Button button = popupView.findViewById(R.id.add_trigger_window_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null) {
                    String aText = editA.getText().toString();
                    String bText = editb.getText().toString();

                    float a = Float.parseFloat(aText);
                    float b = Float.parseFloat(bText);

                    trigger.setA(a);
                    trigger.setB(b);

                    selectedSensor.addTrigger(trigger);

                    try {
                        SensorDatabase.getInstance().save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    popupWindow.dismiss();
                }
            }
        });


        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)button.getLayoutParams();
        // Выбор типа
        Spinner triggerTypesView = popupView.findViewById(R.id.add_trigger_window_type);
        triggerTypesView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedSensor != null) {

                    trigger.setType(Trigger.Type.values()[i]);

                    helperText.setText(activity.getString(trigger.getHelperText()));

                    if (i < 3) {
                        params.topMargin = 20;
                        editBLabel.setVisibility(View.INVISIBLE);
                        editb.setVisibility(View.INVISIBLE);


                        editALabel.setText(R.string.trigger_single_num);
                    } else {
                        params.topMargin = 170;
                        editALabel.setText(R.string.trigger_double_num);
                        editb.setText("0");
                        editBLabel.setVisibility(VISIBLE);
                        editb.setVisibility(VISIBLE);
                    }


                    button.setLayoutParams(params);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        ArrayAdapter<CharSequence> charSequenceArrayAdapter = ArrayAdapter.createFromResource(activity.getBaseContext(),
                R.array.trigger_type_names, android.R.layout.simple_spinner_item);
        charSequenceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        triggerTypesView.setAdapter(charSequenceArrayAdapter);

        // Создание окна
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height);

        // Closes the popup window when touch outside.
        popupWindow.setOutsideTouchable(true);

        // Set focus true to prevent a touch event to go to a below view (main layout)
        popupWindow.setFocusable(true);

        // Removes default background.
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popupWindow.getBackground().setAlpha(0);
        popupView.setAlpha(0);

        popupWindow.setOnDismissListener(this);

        dim.setBounds(0, 0, root.getWidth(), root.getHeight());

        if (opacityAnimator != null)
            opacityAnimator.end();
        opacityAnimator = ValueAnimator.ofInt(0, 200);
        opacityAnimator.addUpdateListener(new AddTriggerWindowAnimator());
    }

    public void show() {
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(this.root, Gravity.CENTER, 0, 0);
        opacityAnimator.start();
    }

    @Override
    public void onDismiss() {
        if (opacityAnimator != null)
            opacityAnimator.end();
        opacityAnimator = ValueAnimator.ofInt(200, 0);
        opacityAnimator.addUpdateListener(new AddTriggerWindowAnimator());
        opacityAnimator.start();
    }

    class AddTriggerWindowAnimator implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            overlay.clear();
            dim.setAlpha((int) valueAnimator.getAnimatedValue());
            overlay.add(dim);

            popupWindow.getBackground().setAlpha((int) valueAnimator.getAnimatedValue() * 255 / 200);
            popupView.setAlpha((int)valueAnimator.getAnimatedValue()*0.005f);

            popupWindow.update();

            root.invalidate();
        }
    }
}
