package com.furlan.mattia.enigma;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    EnigmaMachine enigma;

    public SettingsFragment ()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        addPreferencesFromResource (R.xml.preferences);
        enigma = ((MainActivity) getActivity ()).enigma;
        setSettings ();
        View view = super.onCreateView (inflater, container, savedInstanceState);
        //view.setBackgroundColor (getResources ().getColor (R.color.background));

        Preference button = findPreference ("pref_reset_button");
        button.setOnPreferenceClickListener (new Preference.OnPreferenceClickListener ()
        {
            @Override
            public boolean onPreferenceClick (Preference preference)
            {
                enigma = new EnigmaM3 ();
                ((MainActivity) getActivity ()).enigma = enigma;
                setSettings ();
                return true;
            }
        });

        return view;
    }

    void setSettings ()
    {
        ListPreference rot1 = (ListPreference) findPreference ("rotor_1");
        ListPreference rot2 = (ListPreference) findPreference ("rotor_2");
        ListPreference rot3 = (ListPreference) findPreference ("rotor_3");
        EnigmaMachine.Rotors[] rots = enigma.getRotors ();
        rot1.setValueIndex (rots[0].ordinal ());
        rot2.setValueIndex (rots[1].ordinal ());
        rot3.setValueIndex (rots[2].ordinal ());

        ListPreference ring1 = (ListPreference) findPreference ("rotor_1_ring");
        ListPreference ring2 = (ListPreference) findPreference ("rotor_2_ring");
        ListPreference ring3 = (ListPreference) findPreference ("rotor_3_ring");
        char[] rings = enigma.getRotorsRing ();
        ring1.setValueIndex (rings[0] - 'A');
        ring2.setValueIndex (rings[1] - 'A');
        ring3.setValueIndex (rings[2] - 'A');

        ListPreference pos1 = (ListPreference) findPreference ("rotor_1_pos");
        ListPreference pos2 = (ListPreference) findPreference ("rotor_2_pos");
        ListPreference pos3 = (ListPreference) findPreference ("rotor_3_pos");
        char[] pos = enigma.getRotorsPosition ();
        pos1.setValueIndex (pos[0] - 'A');
        pos2.setValueIndex (pos[1] - 'A');
        pos3.setValueIndex (pos[2] - 'A');
    }

    @Override
    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key)
    {
        ListPreference pref = (ListPreference) findPreference (key);

        Log.d ("SettingsFragment", "key = " + key);
        int index = -1;
        if (key.equals ("rotor_1"))
            enigma.setRotor (0, EnigmaMachine.Rotors.values ()[pref.findIndexOfValue (pref.getValue ())]);
        else if (key.equals ("rotor_2"))
            enigma.setRotor (1, EnigmaMachine.Rotors.values ()[pref.findIndexOfValue (pref.getValue ())]);
        else if (key.equals ("rotor_3"))
            enigma.setRotor (2, EnigmaMachine.Rotors.values ()[pref.findIndexOfValue (pref.getValue ())]);

        else if (key.equals ("rotor_1_ring"))
            enigma.setRotorRing (0, (char) (pref.findIndexOfValue (pref.getValue ()) + 'A'));
        else if (key.equals ("rotor_2_ring"))
            enigma.setRotorRing (1, (char) (pref.findIndexOfValue (pref.getValue ()) + 'A'));
        else if (key.equals ("rotor_3_ring"))
            enigma.setRotorRing (2, (char) (pref.findIndexOfValue (pref.getValue ()) + 'A'));

        else if (key.equals ("rotor_1_pos"))
            enigma.setRotorPosition (0, (char) (pref.findIndexOfValue (pref.getValue ()) + 'A'));
        else if (key.equals ("rotor_2_pos"))
            enigma.setRotorPosition (1, (char) (pref.findIndexOfValue (pref.getValue ()) + 'A'));
        else if (key.equals ("rotor_3_pos"))
            enigma.setRotorPosition (2, (char) (pref.findIndexOfValue (pref.getValue ()) + 'A'));
    }

    @Override
    public void onResume ()
    {
        super.onResume ();
        getPreferenceManager ().getSharedPreferences ().registerOnSharedPreferenceChangeListener (this);

    }

    @Override
    public void onPause ()
    {
        getPreferenceManager ().getSharedPreferences ().unregisterOnSharedPreferenceChangeListener (this);
        super.onPause ();
    }
}
