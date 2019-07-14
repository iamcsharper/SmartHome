package com.miet.smarthome;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miet.smarthome.models.Sensor;

class SettingsListAdapter extends RecyclerView.Adapter<SettingsListAdapter.SettingsViewHolder> {

    @Override
    public SettingsListAdapter.SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.settings_list_item, parent, false);


        return new SettingsListAdapter.SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        Sensor sensor = SensorDatabase.sensors.get(position);
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
        private TextView sensorName;
        private ExpandableLayout accordion;


        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);

            accordion = itemView.findViewById(R.id.accordion);
            sensorName = itemView.findViewById(R.id.setting_item);
            accordion.collapse();

            sensorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (accordion.isExpanded()) {
                        sensorName.setTextColor(Color.BLACK);
                        accordion.collapse();
                    } else {
                        sensorName.setTextColor(Color.GREEN);
                        accordion.expand();
                    }
                }
            });
        }

    }
}
