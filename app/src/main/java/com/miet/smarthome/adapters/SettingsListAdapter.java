package com.miet.smarthome.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miet.smarthome.ExpandableLayout;
import com.miet.smarthome.R;
import com.miet.smarthome.SensorDatabase;
import com.miet.smarthome.models.Sensor;

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.SettingsViewHolder> implements ValueAnimator.AnimatorUpdateListener {

    Drawable dim = new ColorDrawable(Color.BLACK);
    private ViewGroup root;
    private ViewGroupOverlay overlay;
    private FragmentActivity activity;
    private PopupWindow popupWindow;

    private SettingsViewHolder holderToClose;

    public SettingsListAdapter(FragmentActivity activity) {
        this.activity = activity;
        root = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        overlay = root.getOverlay();
    }

    @NonNull
    @Override
    public SettingsListAdapter.SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.settings_list_item, parent, false);


        return new SettingsListAdapter.SettingsViewHolder(view);
    }


    public void onAddTriggerButtonClicked(View view) {
        LayoutInflater inflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_trigger_window, null);

        assert popupView != null;

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height);

        Spinner sensorsListView = popupView.findViewById(R.id.add_trigger_window_sensor);
        Spinner triggerTypesView = popupView.findViewById(R.id.add_trigger_window_type);
        EditText editA = popupView.findViewById(R.id.add_trigger_window_a);
        EditText editb = popupView.findViewById(R.id.add_trigger_window_b);

        // Closes the popup window when touch outside.
        popupWindow.setOutsideTouchable(true);

        // Set focus true to prevent a touch event to go to a below view (main layout)
        popupWindow.setFocusable(true);

        // Removes default background.
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final SettingsListAdapter self = this;

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ValueAnimator opacityAnimator = ValueAnimator.ofInt(200, 0);
                opacityAnimator.addUpdateListener(self);
                opacityAnimator.start();
            }
        });

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        dim.setBounds(0, 0, root.getWidth(), root.getHeight());

        ValueAnimator opacityAnimator = ValueAnimator.ofInt(0, 200);
        opacityAnimator.addUpdateListener(this);
        opacityAnimator.start();

        System.gc();
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        Sensor sensor = SensorDatabase.sensors.get(position);

        holder.sensorEditList.setHasFixedSize(true);
        holder.sensorEditAdapter.setSensor(sensor);
        holder.sensorName.setText(sensor.getName());
//        if (sensor.isTriggered)
//            holder.sensorName.setTextColor(Color.parseColor("#ea410e"));
//        else
//            holder.sensorName.setTextColor(Color.parseColor("#59e26a"));
    }

    @Override
    public int getItemCount() {
        return SensorDatabase.sensors.size();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        overlay.clear();
        dim.setAlpha((int) valueAnimator.getAnimatedValue());
        overlay.add(dim);

        popupWindow.getBackground().setAlpha((int) valueAnimator.getAnimatedValue()*255/200);
        popupWindow.getContentView().setAlpha(((int) valueAnimator.getAnimatedValue())*0.005f);
        popupWindow.getContentView().invalidate();

        popupWindow.update();

        root.invalidate();
    }


    class SettingsViewHolder extends RecyclerView.ViewHolder {
        View root;
        private TextView sensorName;
        private ExpandableLayout accordion;
        private RecyclerView sensorEditList;
        private SensorEditAdapter sensorEditAdapter;
        private Button addTriggerButton;

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView;
            sensorEditAdapter = new SensorEditAdapter(itemView.getContext());

            addTriggerButton = itemView.findViewById(R.id.add_trigger_button);
            accordion = itemView.findViewById(R.id.accordion);
            sensorName = itemView.findViewById(R.id.setting_item);
            sensorEditList = itemView.findViewById(R.id.sensorEditList);
            sensorEditList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            sensorEditList.setAdapter(sensorEditAdapter);
            accordion.collapse();

            final SettingsViewHolder self = this;

            addTriggerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddTriggerButtonClicked(view);
                }
            });

            sensorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (accordion.isExpanded()) {
                        collapse();
                    } else {
                        if (holderToClose != null) {
                            holderToClose.collapse();
                        }
                        expand();
                        holderToClose = self;
                    }
                }
            });
        }

        void collapse() {
            sensorName.setTextColor(Color.BLACK);
            sensorName.setBackgroundColor(Color.TRANSPARENT);
            accordion.collapse(); // Закрыть
        }

        void expand() {
            sensorName.setBackgroundColor(Color.parseColor("#ececec"));
            sensorName.setTextColor(Color.parseColor("#3ace89"));
            accordion.expand(); // Открыть
        }

    }
}
