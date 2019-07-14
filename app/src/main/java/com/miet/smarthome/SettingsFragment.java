package com.miet.smarthome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miet.smarthome.models.ITitleable;


public class SettingsFragment extends Fragment implements ITitleable {
    private RecyclerView settingsListView;
    private RecyclerView.Adapter settingsListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsListView= view.findViewById(R.id.settingsList);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        settingsListView.setLayoutManager(layoutManager);
        settingsListView.setHasFixedSize(true);

        settingsListAdapter = new SettingsListAdapter();
        settingsListView .setAdapter(settingsListAdapter);

        /*SensorDatabase.getInstance().addDataChangedListener(new EventHandler() {
            @Override
            public void handle() {
                settingsListAdapter.notifyDataSetChanged();
            }
        });*/

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_settings) {
//            Toast.makeText(getActivity(), "Test!" + item.getTitle(), Toast.LENGTH_LONG).show();
//        }
//
//        return true;
//    }

    @Override
    public String getTitle() {
        return "Настройки";
    }
}
