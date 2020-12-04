package com.example.giton2.view.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.giton2.R;
import com.example.giton2.service.AlarmReceiver;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String LANGUAGE;
    private String REMIND_ME;

    Preference settingLanguage;
    SwitchPreferenceCompat settingRemindMe;
    AlarmReceiver alarmReceiver;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        LANGUAGE = getString(R.string.language_key);
        REMIND_ME = getString(R.string.remind_me_key);

        settingLanguage = findPreference(LANGUAGE);
        settingRemindMe = findPreference(REMIND_ME);

        settingLanguage.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            return true;
        });

        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        settingRemindMe.setChecked(sharedPreferences.getBoolean(REMIND_ME, false));

        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(REMIND_ME)){
            if(sharedPreferences.getBoolean(REMIND_ME, false)){
                alarmReceiver.setReminder(getActivity());
            }
            else{
                alarmReceiver.cancelReminder(getActivity());
            }
            settingRemindMe.setChecked(sharedPreferences.getBoolean(REMIND_ME, false));
        }
    }
}