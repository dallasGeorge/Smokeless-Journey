package com.example.SmokelessJourneyApp;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHandler {
    private Activity activity;
    //κατασκευαστής της κλάσης για την μπάρα πλοήγησης
    public BottomNavigationHandler(Activity activity) {
        this.activity = activity;
    }

    //listener που απο το επιλεγμένο στοιχείο ανοίγει κατάλληλο activity
    public void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.home: //αν δεν είναι το τρέχον activity ηδη αυτό, δημιούργησε νέο intent με αυτό
                    if (!activity.getClass().equals(MainActivity.class)) {
                        intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                    return true;
                case R.id.journey:
                    if (!activity.getClass().equals(Journey.class)) {
                        intent = new Intent(activity, Journey.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                    return true;
                case R.id.checkIn:
                    if (!activity.getClass().equals(CheckIn.class)) {
                        intent = new Intent(activity, CheckIn.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                    return true;
            }
            return false;
        });
    }
}
