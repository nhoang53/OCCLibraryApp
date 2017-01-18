package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.preference.PreferenceFragment;
import android.os.Bundle;


public class SettingActivityFragment extends PreferenceFragment {
    /**
     * Create option menu from XML preferences file
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
