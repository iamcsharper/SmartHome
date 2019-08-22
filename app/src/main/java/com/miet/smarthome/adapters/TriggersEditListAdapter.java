package com.miet.smarthome.adapters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miet.smarthome.R;
import com.miet.smarthome.models.Sensor;
import com.miet.smarthome.models.Trigger;

public class TriggersEditListAdapter extends RecyclerView.Adapter<TriggersEditListAdapter.SensorEditViewHolder> {

    private SparseArray<Boolean> defaultValues = new SparseArray<Boolean>();
    private Sensor sensor;
    private ArrayAdapter<CharSequence> charSequenceArrayAdapter;
    private Context context;

    public TriggersEditListAdapter(Context context) {
        this.context = context;

        charSequenceArrayAdapter = ArrayAdapter.createFromResource(this.context,
                R.array.trigger_type_names, android.R.layout.simple_spinner_item);

        charSequenceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @NonNull
    @Override
    public TriggersEditListAdapter.SensorEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.settings_edit_sensor_item, parent, false);
        return new TriggersEditListAdapter.SensorEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TriggersEditListAdapter.SensorEditViewHolder holder, int position) {
        Trigger trigger = sensor.getTriggerList().get(position);

        holder.setPos(position);
        holder.helperText.setText(context.getString(trigger.getHelperText()));

        holder.listener.setTriggerPos(position);

        if (!defaultValues.get(position, false)) {
            holder.triggerTypeView.setSelection(trigger.getType().ordinal(), false);
            defaultValues.setValueAt(position, true);
        }

        holder.sensor_input_a.setText(String.valueOf(trigger.getA()));
        holder.sensor_input_b.setText(String.valueOf(trigger.getB()));

        if (!trigger.needsB()) {
            holder.sensor_input_b.setVisibility(View.GONE);
        } else {
            holder.sensor_input_b.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return sensor.getTriggerList().size();
    }

    class TriggerTypeChangeListener implements AdapterView.OnItemSelectedListener {
        int triggerPos;

        public int getTriggerPos() {
            return triggerPos;
        }

        public void setTriggerPos(int triggerPos) {
            this.triggerPos = triggerPos;
        }

        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            Trigger t = sensor.getTriggerList().get(triggerPos);
            t.setType(Trigger.Type.values()[position]);
            sensor.getTriggerList().set(triggerPos, t);

            notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
            // your code here
        }

    }

    class SensorEditViewHolder extends RecyclerView.ViewHolder  {

        TextView listItemSensorView;
        EditText sensor_input_a, sensor_input_b;
        Spinner triggerTypeView;
        TriggerTypeChangeListener listener;
        TextView helperText;
        Button buttonRemove;

        int pos;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public SensorEditViewHolder(@NonNull View itemView) {
            super(itemView);

            helperText = itemView.findViewById(R.id.helperText);
            sensor_input_a = itemView.findViewById(R.id.sensor_input_a);

            sensor_input_a.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        sensor.getTriggerList().get(pos).setA(
                                Float.parseFloat(sensor_input_a.getText().toString()));
                    }
                }
            });

            sensor_input_b = itemView.findViewById(R.id.sensor_input_b);
            sensor_input_b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        sensor.getTriggerList().get(pos).setB(
                                Float.parseFloat(sensor_input_b.getText().toString()));
                    }
                }
            });

            listItemSensorView = itemView.findViewById(R.id.settings_edit_label);
            triggerTypeView = itemView.findViewById(R.id.sensor_edit_trigger_type);
            triggerTypeView.setAdapter(charSequenceArrayAdapter);
            listener = new TriggerTypeChangeListener();
            triggerTypeView.setOnItemSelectedListener(listener);

            buttonRemove = itemView.findViewById(R.id.buttonRemove);

            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sensor.removeTrigger(pos);
                    notifyDataSetChanged();
                    view.invalidate();
                }
            });
        }
    }
}
