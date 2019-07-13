package com.miet.smarthome;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miet.smarthome.models.Sensor;

public class SensorsListAdapter extends RecyclerView.Adapter<SensorsListAdapter.SensorViewHolder> {
    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.sensor_list_item, parent, false);
        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = SensorDatabase.sensors.get(position);
        holder.listItemSensorView.setText(sensor.getName() + " - " + sensor.formatValue(sensor.getValue()));
        if (sensor.isTriggered)
            holder.listItemSensorView.setTextColor(Color.parseColor("#ea410e"));
        else
            holder.listItemSensorView.setTextColor(Color.parseColor("#59e26a"));
    }

    @Override
    public int getItemCount() {
        return SensorDatabase.sensors.size();
    }

    class SensorViewHolder extends RecyclerView.ViewHolder {
        TextView listItemSensorView;


        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);

            listItemSensorView = itemView.findViewById(R.id.sensor_item);
        }

        void bind(String text) {
            listItemSensorView.setText(text);
        }

    }
}
