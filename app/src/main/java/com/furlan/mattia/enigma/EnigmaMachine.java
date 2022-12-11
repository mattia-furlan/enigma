package com.furlan.mattia.enigma;

import java.util.ArrayList;

/**
 * Abstract class which simulates a generic Enigma machine.
 *
 * @author Mattia Furlan
 */
public abstract class EnigmaMachine
{
    String rotorI = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
    char l = rotorI.charAt ('K' - 'A');
    /**
     *
     */
    static public class Plug implements Cloneable
    {
        private char from;
        private char to;

        /**
         * Plug constructor
         * @param from First letter.
         * @param to Second letter.
         */
        Plug (char from, char to)
        {
            this.from = (Character.isUpperCase (from) ? from : '#');
            this.to = (Character.isUpperCase (to) ? to : '#');
        }

        /**
         * Returns the first letter of the plug.
         * @return The first letter.
         */
        public char getFrom ()
        {
            return this.from;
        }

        /**
         * Returns the second letter of the plug.
         * @return The second letter.
         */
        public char getTo ()
        {
            return this.to;
        }

        /**
         * Returns a clone of the plug.
         * @return Clone of a plug object.
         */
        public Plug clone ()
        {
            return new Plug (from, to);
        }

        /**
         * Returns the plug in a printable format.
         * @return String value made up of the first letter followed by the second one.
         */
        @Override public String toString ()
        {
            return String.valueOf (from) + String.valueOf (to);
        }
    }
    private ArrayList<Plug> plugboard;
    private String entry_wheel;
    private String reflector;
    private String[] rotors;
    private String[] notches;
    private int[] pos;
    private int[] rings;
    private String letters;

    private final int N_LETTERS = 26;
    private int N_ROTORS = 3;

    /**
     * Enum containing the eight rotors of the Enigma M3.
     */
    public enum Rotors
    {
        //    ABCDEFGHIJKLMNOPQRSTUVWXYZ
        /**
         * Rotor I: EKMFLGDQVZNTOWYHXUSPAIBRCJ
         */
        I ("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "Q"),
        /**
         * Rotor II: AJDKSIRUXBLHWTMCQGZNPYFVOE
         */
        II ("AJDKSIRUXBLHWTMCQGZNPYFVOE", "E"),
        /**
         * Rotor III: BDFHJLCPRTXVZNYEIWGAKMUSQO
         */
        III ("BDFHJLCPRTXVZNYEIWGAKMUSQO", "V"),
        /**
         * Rotor IV: ESOVPZJAYQUIRHXLNFTGKDCMWB
         */
        IV ("ESOVPZJAYQUIRHXLNFTGKDCMWB", "J"),
        /**
         * Rotor V: VZBRGITYUPSDNHLXAWMJQOFECK
         */
        V ("VZBRGITYUPSDNHLXAWMJQOFECK", "Z"),
        /**
         * Rotor VI: JPGVOUMFYQBENHZRDKASXLICTW
         */
        VI ("JPGVOUMFYQBENHZRDKASXLICTW", "ZM"),
        /**
         * Rotor VII: NZJHGRCXMYSWBOUFAIVLPEKQDT
         */
        VII ("NZJHGRCXMYSWBOUFAIVLPEKQDT", "ZM"),
        /**
         * Rotor VIII: FKQHTLXOCBJSPDZRAMEWNIUYGV
         */
        VIII ("FKQHTLXOCBJSPDZRAMEWNIUYGV", "ZM");

        String s;
        String notches;

        Rotors (String s, String notches)
        {
            this.s = s;
            this.notches = notches;
        }

        /**
         * Returns a <i>Rotors</i> object from its encoding.
         * @param enc
         * @return
         */
        static Rotors fromString (String enc)
        {
            for (Rotors r : Rotors.values ())
                if (r.s.equals (enc))
                    return r;
            return null;
        }
    }

    /**
     * Enum containing the three reflectors of the Enigma M3 and thw two thin reflectors of the M4.
     */
    public enum Reflectors
    {
        //      ABCDEFGHIJKLMNOPQRSTUVWXYZ
        /**
         * Reflector UKW-A: EJMZALYXVBWFCRQUONTSPIKHGD
         */
        UKW_A ("EJMZALYXVBWFCRQUONTSPIKHGD"),
        /**
         * Reflector UKW-B: YRUHQSLDPXNGOKMIEBFZCWVJAT
         */
        UKW_B ("YRUHQSLDPXNGOKMIEBFZCWVJAT"),
        /**
         * Reflector UKW-C: FVPJIAOYEDRZXWGCTKUQSBNMHL
         */
        UKW_C ("FVPJIAOYEDRZXWGCTKUQSBNMHL"),
        /**
         * Reflector UKW-b: ENKQAUYWJICOPBLMDXZVFTHRGS (Enigma M4 only)
         */
        UWK_b ("ENKQAUYWJICOPBLMDXZVFTHRGS"),
        /**
         * Reflector UKW-c: RDOBJNTKVEHMLFCWZAXGYIPSUQ (Enigma M4 only)
         */
        UWB_c ("RDOBJNTKVEHMLFCWZAXGYIPSUQ");
        String s;

        Reflectors (String s)
        {
            this.s = s;
        }

        static Reflectors fromString (String enc)
        {
            for (Reflectors r : Reflectors.values ())
                if (r.s.equals (enc))
                    return r;
            return null;
        }
    }

    /**
     * Enum containing the two types of entry wheels, for commercial and military Enigma-s.
     */
    public enum EntryWheels
    {
        //    ABCDEFGHIJKLMNOPQRSTUVWXYZ
        /**
         * Alphabetical order (military Enigma).
         */
        MILITARY ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
        /**
         * QWERTZ keyboard order (commercial Enigma).
         */
        COMMERCIAL ("QWERTZUIOASDFGHJKPYXCVBNML");
        String s;

        EntryWheels (String s)
        {
            this.s = s;
        }
    }

    Rotors[] getRotors ()
    {
        Rotors[] rots = new Rotors[N_ROTORS];
        for (int i = 0; i < N_ROTORS; i++)
            rots[i] = Rotors.fromString (rotors[i]);
        return rots;
    }

    Reflectors getReflector ()
    {
        return Reflectors.fromString (reflector);
    }

    void resetPlugboard ()
    {
        plugboard.clear ();
    }

    ArrayList<Plug> getPlugboard ()
    {
        ArrayList<Plug> copy = new ArrayList<> ();
        for (Plug plug : plugboard)
        {
            copy.add (plug.clone ());
        }
        return copy;
    }

    /**
     * Changes a rotor.
     *
     * @param index Numeric index of the rotor we want to change.
     * @param rotor The rotor to be set.
     */
    public void setRotor (int index, Rotors rotor)
    {
        if (index < 0 || index >= N_ROTORS)
            return;
        rotors[index] = rotor.s;
        notches[index] = rotor.notches;
    }

    /**
     * Changes the position of a rotor.
     *
     * @param index Numeric index of the rotor we want to change the state.
     * @param state New position to set the rotor to.<br>Must be a A-Z value.
     */
    public void setRotorPosition (int index, char state)
    {
        if (index < 0 || index >= N_ROTORS)
            return;
        if (!Character.isUpperCase (state))
            return;
        pos[index] = li (state);
    }

    /**
     * Changes the ring of a rotor.
     *
     * @param index Numeric index of the rotor we want to change the ring.
     * @param state New position to set the ring to.<br>Must be a A-Z value.
     */
    public void setRotorRing (int index, char state)
    {
        if (index < 0 || index >= N_ROTORS)
            return;
        if (!Character.isUpperCase (state))
            return;
        rings[index] = li (state);
    }

    /**
     * Changes the reflector.
     *
     * @param reflector The reflector to be set.
     */
    public void setReflector (Reflectors reflector)
    {
        this.reflector = reflector.s;
    }

    /**
     * Changes the entry wheel.
     *
     * @param entry_wheel The entry wheel to be set.
     */
    public void setEntryWheel (EntryWheels entry_wheel)
    {
        this.entry_wheel = entry_wheel.s;
    }

    /**
     * Constructor of the class.
     *
     * @param rotors_number Number of rotors used by the Enigma machine.
     */
    public EnigmaMachine (int rotors_number)
    {
        letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        //plugboard = letters;
        plugboard = new ArrayList<> ();

        this.N_ROTORS = rotors_number;

        rotors = new String[N_ROTORS];
        pos = new int[N_ROTORS];
        rings = new int[N_ROTORS];
        notches = new String[N_ROTORS];
    }

    /**
     * Adds a cable to the plugboard which maps the letter <i>from</i> to the letter <i>to</i> and viceversa.
     *
     * @param plug Plug between two letters.
     */
    public void makePlugging (Plug plug)
    {
        plugboard.add (plug);
    }


    private int mod (int i)
    {
        return (i % N_LETTERS + N_LETTERS) % N_LETTERS;
    }

    private int li (char ch)
    {
        return ch - 'A';
    }

    private char doPlugboard (char ch)
    {
        for (Plug plug : plugboard)
        {
            if (plug.getFrom () == ch)
                return plug.getTo ();
            if (plug.getTo () == ch)
                return plug.getFrom ();
        }
        return ch;
    }

    /**
     * Encrpyts a single character.
     *
     * @param ch Character to encrypt.<br>Must be a A-Z value.
     * @return
     */
    private char encryptCharacter (char ch)
    {
        Character res = ch;

        stepRotor (0);
        //debug ();
        //System.out.print (res + " => (PL) => ");

        //res = plugboard.charAt (li (res));
        res = doPlugboard (res);

        res = entry_wheel.charAt (li (res));

        //int prev = 0;
        //int prevr = 0;
        for (int i = 0; i < N_ROTORS; i++)
        {
            //System.out.print (res + " => (R" + (i + 1) + ") => ");
            String rotor = rotors[i];
            res = rotor.charAt (mod (li (res) + pos[i] - rings[i]));
            res = letters.charAt (mod (li (res) - pos[i] + rings[i]));
            //res = rotor.charAt (mod (pos[i] + li (res) - prev - rings[i] + prevr));
            //prev = pos[i];
            //prevr = rings[i];
        }

        //System.out.print (res + " => (RF) => ");
        res = reflector.charAt (mod (li (res)));

        for (int i = N_ROTORS - 1; i >= 0; i--)
        {
            //System.out.print (res + " => (R" + (i + 1) + ") => ");
            String rotor = rotors[i];
            /*int p2 = mod (li (res) + pos[i]);
        	int p3 = rotor.indexOf (letters.charAt (p2));
        	int p = mod (p3 - pos[i] + rings[i]);
        	res = letters.charAt (p);*/
            int p = mod (li (res) + pos[i] - rings[i]);
            int p2 = rotor.indexOf (letters.charAt (p));
            res = letters.charAt (mod (p2 - pos[i] + rings[i]));
        }

        //System.out.print (res + " => (PL) => ");
        res = entry_wheel.charAt (li (res));
        //res = plugboard.charAt (li (res));
        res = doPlugboard (res);
        //System.out.println (res);

        return res;
    }

    /**
     * Encrypts a string.<br>Non-uppercase letters of the string (including numbers and symbols) are ignored.
     *
     * @param str String to encrypt.
     * @return The encrypted string.
     */
    public String encryptString (String str)
    {
        System.out.flush ();
        String res = "";
        for (int i = 0; i < str.length (); i++)
        {
            char c = str.charAt (i);
            if (!Character.isUpperCase (c))
                continue;
            res += encryptCharacter (c);
        }
        return res;
    }

    /**
     * Increments the position of a rotor.
     *
     * @param index Numeric index of the rotor to rotate.
     */
    private void stepRotor (int index)
    {
        String ch = String.valueOf ((char) (pos[index] + 'A'));
        if (notches[index].contains (ch))
        {
            if (index < N_ROTORS - 1)
                stepRotor (index + 1);
        }

        pos[index] = mod (pos[index] + 1);
    }

    private void stepBackRotor (int index)
    {
        String ch = String.valueOf ((char) (mod (pos[index] - 1) + 'A'));
        if (notches[index].contains (ch))
        {
            if (index < N_ROTORS - 1)
                stepBackRotor (index + 1);
        }
        pos[index] = mod (pos[index] - 1);
    }

    public void back ()
    {
        stepBackRotor (0);
    }

    /**
     * Returns the positions of the rotors.<br>Index 0 is the right-most rotor, index (N_ROTORS - 1) is the left-most rotor.
     *
     * @return Character array with the positions of the rotors.
     */
    public char[] getRotorsPosition ()
    {
        char[] positions = new char[N_ROTORS];
        for (int i = 0; i < N_ROTORS; i++)
            positions[i] = (char) (pos[i] + 'A');
        return positions;
    }

    /**
     * Returns the positions of the rotors.<br>Index 0 is the right-most rotor, index (N_ROTORS - 1) is the left-most rotor.
     *
     * @return Character array with the positions of the rotors.
     */
    public char[] getRotorsRing ()
    {
        char[] rings = new char[N_ROTORS];
        for (int i = 0; i < N_ROTORS; i++)
            rings[i] = (char) (this.rings[i] + 'A');
        return rings;
    }

    /**
     * Sets the positions of the rotors as specified.<br>Index 0 is the right-most rotor, index (N_ROTORS - 1) is the left-most rotor.
     *
     * @param positions Character array with the positions of the rotors.
     */
    public void setRotorsPosition (char[] positions)
    {
        if (positions.length != N_ROTORS)
            return;
        for (int i = 0; i < N_ROTORS; i++)
            setRotorPosition (i, positions[i]);
    }

    /**
     * Sets the rings of the rotors as specified.<br>Index 0 is the left-most rotor, index (N_ROTORS - 1) is the right-most rotor.
     *
     * @param positions Character array with the positions of the rings.
     */
    public void setRotorsRing (char[] positions)
    {
        if (positions.length != N_ROTORS)
            return;
        for (int i = 0; i < N_ROTORS; i++)
            setRotorRing (i, positions[i]);
    }

    private void debug ()
    {
        System.out.println ("    " + letters);
        System.out.println ("PL: " + letters);
        for (int i = 0; i < N_ROTORS; i++)
            System.out.println ("R" + (i + 1) + ": " + rotors[i] + " (" + (char) (pos[i] + 'A') + ")");
        System.out.println ("RF: " + reflector);
    }
}
