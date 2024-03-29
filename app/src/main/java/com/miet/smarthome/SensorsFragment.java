package com.miet.smarthome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miet.smarthome.adapters.SensorsListAdapter;
import com.miet.smarthome.models.ITitleable;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class SensorsFragment extends Fragment implements ITitleable {
    private RecyclerView sensorsListView;
    private RecyclerView.Adapter sensorsListAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_sensors, container, false);

        sensorsListView = view.findViewById(R.id.sensorsList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sensorsListView.setLayoutManager(layoutManager);
        sensorsListView.setHasFixedSize(true);

        sensorsListAdapter = new SensorsListAdapter();
        sensorsListView.setAdapter(sensorsListAdapter);

        SensorDatabase.getInstance().addDataChangedListener(new EventHandler() {
            @Override
            public void handle() {
                sensorsListAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public String getTitle() {
        return "Датчики";
    }
}
