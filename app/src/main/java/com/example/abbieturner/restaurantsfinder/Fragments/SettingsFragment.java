package com.example.abbieturner.restaurantsfinder.Fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.example.abbieturner.restaurantsfinder.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_pref);
    }

    //boolean GPS = ((SwitchPreferenceCompat) findPreference(PreferenceManager.REMOTE_ACCESS_ACTIVE)).isChecked();



}