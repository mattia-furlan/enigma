package com.furlan.mattia.enigma;

public class EnigmaM3 extends EnigmaMachine
{
    public EnigmaM3 ()
    {
        this (Rotors.I, Rotors.II, Rotors.III, Reflectors.UKW_B);
    }

    public EnigmaM3 (Rotors rot1, Rotors rot2, Rotors rot3, Reflectors refl)
    {
        super (3);

        setRotor (0, rot1);
        setRotor (1, rot2);
        setRotor (2, rot3);
        setReflector (refl);
        setEntryWheel (EntryWheels.MILITARY);
    }

}
