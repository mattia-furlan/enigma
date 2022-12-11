package com.furlan.mattia.enigma;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    EnigmaFragment enigma_fragment;
    DailySettingsFragment daily_settings_fragment;
    SettingsFragment settings_fragment;
    PlugboardFragment plugboard_fragment;

    public EnigmaMachine enigma = new EnigmaM3 ();

    public void clicked (View v)
    {
        switch (v.getId ())
        {
            case R.id.buttonGetDailySettings:
                daily_settings_fragment.getDailySettingsButtonClicked (v);
                break;
            case R.id.buttonApplySettings:
                daily_settings_fragment.setConfiguration ();
                break;
            case R.id.buttonDelete:
                enigma_fragment.deleteLast ();
                break;
            default:
                /*enigma_fragment.*/
                enigma_fragment.keyboardClick (v);
                break;
        }
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        toolbar = (Toolbar) findViewById (R.id.nav_action);
        setSupportActionBar (toolbar);

        drawer_layout = (DrawerLayout) findViewById (R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle (this, drawer_layout, R.string.open, R.string.close);

        drawer_layout.addDrawerListener (toggle);
        toggle.syncState ();

        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);

        enigma_fragment = new EnigmaFragment ();
        setTitle ("Enigma");

        FragmentTransaction ft = getFragmentManager ().beginTransaction ();
        ft.replace (R.id.relativelayout, enigma_fragment, "Enigma");
        ft.commit ();

        NavigationView navigationView = (NavigationView) findViewById (R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (this);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (toggle.onOptionsItemSelected (item))
            return true;
        return super.onOptionsItemSelected (item);
    }

    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item)
    {
        int id = item.getItemId ();

        if (id == R.id.nav_machine)
        {
            Log.d ("Enigma", "Got R.id.nav_machine");
            setTitle ("Enigma");

            enigma_fragment = new EnigmaFragment ();
            FragmentTransaction ft = getFragmentManager ().beginTransaction ();
            ft.replace (R.id.relativelayout, enigma_fragment, "Enigma");
            ft.commit ();
        } else if (id == R.id.nav_settings)
        {
            Log.d ("Enigma", "Got R.id.nav_settings");
            setTitle ("Machine settings");

            settings_fragment = new SettingsFragment ();
            FragmentTransaction ft = getFragmentManager ().beginTransaction ();
            ft.replace (R.id.relativelayout, settings_fragment, "Settings");
            ft.commit ();
        } else if (id == R.id.nav_plugboard)
        {
            Log.d ("Enigma", "Got R.id.nav_plugboard");
            setTitle ("Plugboard");

            plugboard_fragment = new PlugboardFragment ();
            FragmentTransaction ft = getFragmentManager ().beginTransaction ();
            ft.replace (R.id.relativelayout, plugboard_fragment, "Plugboard");
            ft.commit ();
        } else if (id == R.id.nav_dailysettings)
        {
            Log.d ("Enigma", "Got R.id.nav_dailysettings");
            setTitle ("Daily settings");

            daily_settings_fragment = new DailySettingsFragment ();
            FragmentTransaction ft = getFragmentManager ().beginTransaction ();
            ft.replace (R.id.relativelayout, daily_settings_fragment, "Daily settings");
            ft.commit ();
        }

        drawer_layout.closeDrawer (GravityCompat.START);
        return true;
    }
}
