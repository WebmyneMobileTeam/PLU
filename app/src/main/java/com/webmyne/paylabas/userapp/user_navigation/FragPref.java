package com.webmyne.paylabas.userapp.user_navigation;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;


import com.webmyne.paylabas.userapp.custom_components.PreferenceFragment;
import com.webmyne.paylabas_user.R;


public class FragPref extends PreferenceFragment {

    EditTextPreference edEmail;
    EditTextPreference edMobile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);





        edEmail = (EditTextPreference)findPreference("ed_pref_email");
        edEmail.setText("");
        edEmail.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                edEmail.setTitle(newValue.toString());
                return false;
            }
        });







    }

}
