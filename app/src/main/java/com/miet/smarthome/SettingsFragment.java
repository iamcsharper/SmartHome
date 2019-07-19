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
import com.miet.smarthome.adapters.SettingsListAdapter;
import com.miet.smarthome.models.ITitleable;


public class SettingsFragment extends Fragment implements ITitleable {
    private RecyclerView settingsListView;
    private SettingsListAdapter settingsListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsListView= view.findViewById(R.id.settingsList);

        FloatingActionButton fab = view.findViewById(R.id.saveButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Настройки сохранены.", Snackbar.LENGTH_SHORT).show();
                SettingsDatabase.getInstance().save();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        settingsListView.setLayoutManager(layoutManager);
        settingsListView.setHasFixedSize(true);

        settingsListAdapter = new SettingsListAdapter(getActivity());
        settingsListView.setAdapter(settingsListAdapter);

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
