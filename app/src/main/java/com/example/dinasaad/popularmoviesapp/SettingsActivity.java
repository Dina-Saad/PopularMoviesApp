package com.example.dinasaad.popularmoviesapp;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by DinaSaad on 24/07/2017.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         addPreferencesFromResource(R.xml.pref_general);

      /*  SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for (int i=0;i<count;i++)
        {
            Preference P = prefScreen.getPreference(i);
            if(!(P instanceof CheckBoxPreference))
            {String value = sharedPref.getString(P.getKey(),"");
                PreferenceSummary(P,value);
            }
        }*/

        }
/*    private void PreferenceSummary (Preference preference, String value)
    {
        if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex= listPreference.findIndexOfValue(value);
            if(prefIndex>0)
            {listPreference.setSummary(listPreference.getEntries()[prefIndex]);}
        }
    }*/
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value)  {
        String stringValue = value.toString();

       /* Preference pref = findPreference(stringValue);
        if(null != pref)
        {
            if (!(pref instanceof CheckBoxPreference))
                {String Prefvalue = sharedPreferences.getString(pref.getKey(),"");
                    PreferenceSummary(pref,Prefvalue);
                }


            }*/
       if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;

    }

}
