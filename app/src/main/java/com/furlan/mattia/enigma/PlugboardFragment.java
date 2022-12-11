package com.furlan.mattia.enigma;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.annotation.StringDef;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlugboardFragment extends Fragment implements AdapterView.OnItemSelectedListener
{
    View v;
    EnigmaMachine enigma;
    int[] spinnersA, spinnersB;

    public PlugboardFragment ()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        enigma = ((MainActivity) getActivity ()).enigma;
        v = inflater.inflate (R.layout.fragment_plugboard, container, false);
        spinnersA = new int[] {R.id.spinnerPL1A, R.id.spinnerPL2A, R.id.spinnerPL3A, R.id.spinnerPL4A, R.id.spinnerPL5A, R.id.spinnerPL6A, R.id.spinnerPL7A, R.id.spinnerPL8A, R.id.spinnerPL9A, R.id.spinnerPL10A};
        spinnersB = new int[] {R.id.spinnerPL1B, R.id.spinnerPL2B, R.id.spinnerPL3B, R.id.spinnerPL4B, R.id.spinnerPL5B, R.id.spinnerPL6B, R.id.spinnerPL7B, R.id.spinnerPL8B, R.id.spinnerPL9B, R.id.spinnerPL10B};
        for (int id : spinnersA)
            ((Spinner)v.findViewById (id)).setOnItemSelectedListener (this);
        for (int id : spinnersB)
            ((Spinner)v.findViewById (id)).setOnItemSelectedListener (this);
        setPlugboard ();
        return v;
    }

    void setPlugboard ()
    {
        ArrayList<EnigmaMachine.Plug> plugboard = enigma.getPlugboard ();
        for (int i = 0; i < plugboard.size (); i++)
        {
            EnigmaMachine.Plug plug = plugboard.get (i);
            char from = plug.getFrom ();
            char to = plug.getTo ();
            Log.d ("Enigma", String.valueOf (from)  + " -> " + String.valueOf (to));
            Log.d ("Enigma", String.valueOf ((from - 'A') + 1));
            ((Spinner)v.findViewById (spinnersA[i])).setSelection ((from - 'A') + 1, false);
            ((Spinner)v.findViewById (spinnersB[i])).setSelection ((to - 'A') + 1, false);
        }
    }


    Spinner getSibling (@IdRes int id)
    {
        for (int i = 0; i < spinnersA.length; i++)
            if (id == spinnersA[i])
                return ((Spinner)v.findViewById (spinnersB[i]));
            else if (id == spinnersB[i])
                return ((Spinner)v.findViewById (spinnersA[i]));
        return null;
    }

    @Override
    public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
    {
        StringBuilder newplug = new StringBuilder ();

        enigma.resetPlugboard ();

        for (int spinnerId : spinnersA)
        {
            Spinner spinner1 = ((Spinner)v.findViewById (spinnerId));
            Spinner spinner2 = getSibling (spinnerId);
            char from = spinner1.getSelectedItem ().toString ().charAt (0);
            char to = spinner2.getSelectedItem ().toString ().charAt (0);
            if (from != '-' && newplug.toString ().indexOf (from) != -1)
            {
                spinner1.setSelection (0);
                from = '-';
            }
            if (to != '-' && newplug.toString ().indexOf (to) != -1)
            {
                spinner2.setSelection (0);
                to = '-';
            }
            if (from == to)
            {
                spinner1.setSelection (0);
                from = '-';
            }
            newplug.append (from);
            newplug.append (to);
        }

        String plug = newplug.toString ();

        for (int i = 0; i < plug.length ();)
        {
            char from = plug.charAt (i++);
            char to = plug.charAt (i++);
            if (from != '-' && to != '-')
            {
                enigma.makePlugging (new EnigmaMachine.Plug (from, to));
            }
        }

        for (EnigmaMachine.Plug plugging : enigma.getPlugboard ())
        {
            Log.d ("Enigma", "plug = (" + plugging.getFrom () + ", " + plugging.getTo () + ")");
        }
    }

    @Override
    public void onNothingSelected (AdapterView<?> parent)
    {

    }
}
