package com.example.SmokelessJourneyApp;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

public class CheckIn extends AppCompatActivity {

    private MyDBHandler dbHandler;
    private EditText editTextExplanation;
    private SeekBar seekBarCravingsStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_in);
        //προσθήκη του navigation bar
        BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationHandler.setupBottomNavigation(bottomNavigationView);

        dbHandler = new MyDBHandler(this);
        editTextExplanation = findViewById(R.id.editTextText2);
            seekBarCravingsStrength = findViewById(R.id.seekBar2);
        //ρύθμιση για την σωστή εμφάνιση της εφαρμογής στην οθόνη
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    //μέθοδος για όταν πατιέται το κουμπί submit στο check in
    public void submitCheckIn(View view) {
        //παίρνει τα inputs
        String explanation = editTextExplanation.getText().toString();
        int cravingsStrength = seekBarCravingsStrength.getProgress() * 10;

        //λήψη χρόνου τελευταίου check in απο την βάση
        long lastCheckInTimestamp = dbHandler.getLastCheckInTimestamp();


        long currentTimestamp = System.currentTimeMillis();


        long timeDifference = currentTimestamp - lastCheckInTimestamp;

        //έλεγχος για το αν εχει περάσει 24ωρο απο το τελευταίο check in
        //και σε περίπτωση που έχει προσθήκη νέου check in στην βαση update(μερα, χρήστης)
        if (timeDifference >= 24 * 60 * 60 * 1000) {

            Day newDay = new Day(new Date(), explanation, cravingsStrength);


            dbHandler.addDay(newDay);

            User user = dbHandler.getUser();
            if (user != null) {
                int newDaysClean = user.getDaysClean() + 1;
                user.setDaysClean(newDaysClean);
                dbHandler.updateUser(user);
            }




            Toast.makeText(this, "Check-in submitted", Toast.LENGTH_SHORT).show();
        } else {
            //αλλιώς εμφάνιση ενημερωτικού μηνύματος στην οθόνη οτι δεν μπορει να κανει check in αν δεν εχει περάσει 24ωρο.
            Toast.makeText(this, "You can only check-in once per day", Toast.LENGTH_SHORT).show();
        }

        //έπειτα μετάβαση στην main
        Intent intent = new Intent(CheckIn.this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }


}
