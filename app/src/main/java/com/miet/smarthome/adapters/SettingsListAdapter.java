package com.miet.smarthome.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miet.smarthome.AddTriggerWindow;
import com.miet.smarthome.ExpandableLayout;
import com.miet.smarthome.R;
import com.miet.smarthome.SensorDatabase;
import com.miet.smarthome.models.Sensor;

public class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.SettingsViewHolder> {

    private FragmentActivity activity;

    private SettingsViewHolder holderToClose;

    public SettingsListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SettingsListAdapter.SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.settings_list_item, parent, false);


        return new SettingsListAdapter.SettingsViewHolder(view);
    }


     private void onAddTriggerButtonClicked(SettingsViewHolder holder) {
        AddTriggerWindow addTriggerWindow = new AddTriggerWindow(activity, holder.getIndex());
        addTriggerWindow.show();
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        Sensor sensor = SensorDatabase.sensors.get(position);

        holder.setIndex(position);
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


    class SettingsViewHolder extends RecyclerView.ViewHolder {
        View root;
        private TextView sensorName;
        private ExpandableLayout accordion;
        private RecyclerView sensorEditList;
        private TriggersEditListAdapter sensorEditAdapter;
        private Button addTriggerButton;
        private int index;

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView;
            sensorEditAdapter = new TriggersEditListAdapter(itemView.getContext());

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
                    onAddTriggerButtonClicked(self);
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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
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
