package com.furlan.mattia.enigma;


import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import android.support.v4.app.Fragment;

public class DailySettingsFragment extends Fragment
{
    static String last_settings_text = null;
    static String[] last_settings = null;
    EnigmaMachine enigma;
    String[] config;

    public DailySettingsFragment ()
    {
        //Costruttore
    }

    //Ritorna 'true' se il dispositivo è connesso ad Internet
    public static boolean isInternetConnected (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo ();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting ();
    }

    //Funzione che viene richiamata quando si preme il pulsante 'getDailySettings'
    public boolean getDailySettingsButtonClicked (View view)
    {
        //Se il dispositivo non è connesso ad Internet
        if (!isInternetConnected (getActivity ()))
        {
            //Creo un Toast
            Toast.makeText (getActivity (), "No Internet connection!", Toast.LENGTH_SHORT).show ();
            return false;
        }
        //Altrimenti creo un oggetto ConnectTask per ottenere la configurazione giornaliera
        ConnectTask ct = new ConnectTask ();
        new ConnectTask ().execute ("");
        return true;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        View view =  inflater.inflate (R.layout.fragment_daily_settings, container, false);
        //Ottengo l'oggetto che rappresenta la macchina Enigma dall'Activity
        enigma = ((MainActivity) getActivity ()).enigma;
        if (last_settings_text != null)
            ((TextView)  view.findViewById (R.id.textViewDailySettings)).setText (last_settings_text);
        if (last_settings != null)
            config = last_settings;
        return view;
    }

    //Sottoclasse che si occupa di ottenere la configurazione giornaliera (in maniera asincrona)
    public class ConnectTask extends AsyncTask<Object, Object, String>
    {

        @Override
        protected String doInBackground (Object... message)
        {
            try
            {
                URL url = new URL ("http://www.lastaccionata.it/other/index2.php?type=I");//("http://10.0.2.2/EnigmaDailyKey/index.php?type=I");

                //Apro un connessione HTTP verso l'URL specificato
                HttpURLConnection conn = (HttpURLConnection) url.openConnection ();
                //Timeout: 5 secondi
                conn.setConnectTimeout (5000);
                //
                BufferedReader in = new BufferedReader (new InputStreamReader (conn.getInputStream ()));

                String str;

                if ((str = in.readLine ()) != null)
                {
                    String[] csv = str.split (",");

                    for (String s : csv)
                        Log.d ("Enigma", s);

                    config = csv;
                    publishProgress ();
                }
                in.close ();
                conn.disconnect (); //Chiudo la connessione HTTP
            }
            catch (Exception e)
            {
                //In caso di un'eccezione, ne stampo il testo su un log
                Log.d ("Enigma", "Exception: " + e.toString ());
            }
            return "";
        }

        @Override
        protected void onProgressUpdate (Object... values)
        {
            TextView tv = (TextView) getView ().findViewById (R.id.textViewDailySettings);
            String s = "Datum: " + config[0]
                    + "\n\nWalzenlage: " + config[1]
            + "\n\nRingstellung: " + config[2]
            + "\n\nGrundstellung: " + config[3]
            + "\n\nSteckerverbindungen: " + config[4];
            tv.setText (s);
            ((MainActivity) getActivity ()).findViewById (R.id.buttonApplySettings).setEnabled (true);
            last_settings_text = s;
            last_settings = config.clone ();
        }
    }

    void setConfiguration ()
    {
        // Reflector
        /*if (config[0].equals ("A"))
            enigma.setReflector (EnigmaMachine.Reflectors.UKW_A);
        else if (config[0].equals ("B"))
            enigma.setReflector (EnigmaMachine.Reflectors.UKW_B);
        else if (config[0].equals ("C"))
            enigma.setReflector (EnigmaMachine.Reflectors.UKW_C);*/

        if (config == null)
            return;

        // Rotors
        String rotors[] = config[1].split (" ", 3);
        for (String r : rotors)
            Log.d ("Enigma", "r = " + r);
        for (int i = 0; i < 3; i++)
            enigma.setRotor (i, EnigmaMachine.Rotors.valueOf (rotors[i]));

        // Ring settings
        String rings = config[2];
        Log.d ("Enigma", "rings = " + rings);
        for (int i = 0; i < 3; i++)
            enigma.setRotorRing (i, rings.charAt (i));

        // Ground settings
        String pos = config[3];
        Log.d ("Enigma", "pos = " + pos);
        for (int i = 0; i < 3; i++)
            enigma.setRotorPosition (i, pos.charAt (i));

        // Plugboard
        String[] plugs = config[4].split (" ", 10);
        for (String plug : plugs)
            Log.d ("Enigma", "plug = " + plug);
        enigma.resetPlugboard ();
        for (String plug : plugs)
            enigma.makePlugging (new EnigmaMachine.Plug (plug.charAt (0), plug.charAt (1)));
    }

}
