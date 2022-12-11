package com.furlan.mattia.enigma;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

public class EnigmaFragment extends Fragment
{
    static String ciphertext = null;
    EnigmaMachine enigma;
    MainActivity main_activity;

    public EnigmaFragment ()
    {
        // Required empty public constructor
    }

    public void deleteLast ()
    {
        MainActivity view = (MainActivity) getActivity ();
        TextView et = (TextView) view.findViewById (R.id.textViewEnigmaEnc);
        if (et == null)
            return;
        String s = et.getText ().toString ();
        if (s.equals (""))
            return;
        et.setText (s.substring(0, s.length () - 1));
        ciphertext = et.getText ().toString ();

        enigma.back ();
        int rot_tv[] = new int[] {R.id.textViewRotor1, R.id.textViewRotor2, R.id.textViewRotor3};
        char rotors[] = enigma.getRotorsPosition ();
        for (int i = 0; i < rot_tv.length; i++)
        {
            ((TextView) view.findViewById (rot_tv[i])).setText (String.valueOf (rotors[i]));
        }
    }

    public void keyboardClick (View v)
    {
        MainActivity view = (MainActivity) getActivity ();
        Animation anim = new ScaleAnimation (
                1f, 0.6f, // Start and end values for the X axis scaling
                1f, 0.6f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        /*Animation anim2 = new ScaleAnimation (
                1f, 2f, // Start and end values for the X axis scaling
                1f, 2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling*/
        AnimationSet set = new AnimationSet (true);
        anim.setFillAfter (true); // Needed to keep the result of the animation
        anim.setDuration (150);
        //anim2.setDuration (40);
        set.addAnimation (anim);
        //set.addAnimation (anim2);
        v.startAnimation (set);

        Button sender = (Button) view.findViewById (v.getId ());
        String text = sender.getText ().toString ();
        String enc = enigma.encryptString (text);

        int[] buttons = new int[] {R.id.buttonA, R.id.buttonB, R.id.buttonC, R.id.buttonD, R.id.buttonE, R.id.buttonF,
                R.id.buttonG, R.id.buttonH, R.id.buttonI, R.id.buttonJ, R.id.buttonK, R.id.buttonL, R.id.buttonM,
                R.id.buttonN, R.id.buttonO, R.id.buttonP, R.id.buttonQ, R.id.buttonR, R.id.buttonS, R.id.buttonT,
                R.id.buttonU, R.id.buttonV, R.id.buttonW, R.id.buttonX, R.id.buttonY, R.id.buttonZ};

        for (int button : buttons)
        {
            final Button btn = ((Button) view.findViewById (button));
            if (btn.getText ().toString ().equals (enc))
            {
                btn.setBackgroundResource (R.drawable.enigma_button_light);
                Handler handler = new Handler ();
                handler.postDelayed (new Runnable ()
                {
                    public void run ()
                    {
                        btn.setBackgroundResource (R.drawable.enigma_button);
                    }
                }, 600);
            }
        }

        int rot_tv[] = new int[] {R.id.textViewRotor1, R.id.textViewRotor2, R.id.textViewRotor3};
        char rotors[] = enigma.getRotorsPosition ();
        for (int i = 0; i < rot_tv.length; i++)
        {
            ((TextView) view.findViewById (rot_tv[i])).setText (String.valueOf (rotors[i]));
        }

        TextView et = (TextView) view.findViewById (R.id.textViewEnigmaEnc);
        if (et == null)
            return;
        et.setText (et.getText ().toString () + enc);
        ciphertext = et.getText ().toString ();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        View v = inflater.inflate (R.layout.fragment_enigma, container, false);
        main_activity = (MainActivity) getActivity ();
        enigma = main_activity.enigma;
        int rot_tv[] = new int[] {R.id.textViewRotor1, R.id.textViewRotor2, R.id.textViewRotor3};
        char rotors[] = enigma.getRotorsPosition ();
        for (int i = 0; i < rot_tv.length; i++)
        {
            ((TextView) v.findViewById (rot_tv[i])).setText (String.valueOf (rotors[i]));
        }
        if (ciphertext != null)
        {
            ((TextView) v.findViewById (R.id.textViewEnigmaEnc)).setText (ciphertext);
        }
        return v;
    }
}
