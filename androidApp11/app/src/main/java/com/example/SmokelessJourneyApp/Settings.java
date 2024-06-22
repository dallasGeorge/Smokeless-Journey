package com.example.SmokelessJourneyApp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {

    private MyDBHandler dbHandler;
    private EditText editEurosPerPackage;
    private EditText editPackagesPerDay;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       EdgeToEdge.enable(this);
       setContentView(R.layout.activity_settings);
       //προσθήκη του navigation bar
       BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this);
       BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
       bottomNavigationHandler.setupBottomNavigation(bottomNavigationView);

       editEurosPerPackage = findViewById(R.id.editEurosPerPackage);
       editPackagesPerDay = findViewById(R.id.editPackagesPerDay);
       dbHandler = new MyDBHandler(this);

       //εμφάνηση τιμών παλαιών settings αν υπήρξαν
       loadUserSettings();
       //ρύθμιση για την σωστή εμφάνιση της εφαρμογής στην οθόνη
       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
           Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
           v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
           return insets;
       });


   }
        //μέθοδος που καλείται οταν πατιέται το κουμπί Reset Days Clean
    public void resetDaysClean(View view) {
        User user = dbHandler.getUser();
        //θέτει τις μέρες 0
        if (user != null) {

            user.setDaysClean(0);


            //ενημερώνει τα στοιχεία του χρήστη στην βάση
            dbHandler.updateUser(user);
            //εμφανίζει σχετικό μήνυμα
            Toast.makeText(this, "Days clean reset to 0", Toast.LENGTH_SHORT).show();
        } else {
            //αν δεν υπάρχει χρήστης εμφανίζει σχετικό ενημερωτικό μήνυμα
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }
    }
    //μέθοδος που καλείται οταν πατιέται το κουμπί Submit
    public void submitSettings(View view) {
        try {
            double eurosPerPackage = Double.parseDouble(editEurosPerPackage.getText().toString());
            int packagesPerDay = Integer.parseInt(editPackagesPerDay.getText().toString());


            User user = dbHandler.getUser();
            //αν υπάρχει ήδη χρήστης στην βάση ενημερώνει τις τιμές του
            if (user != null) {

                user.setEurosForTobaccoPackage(eurosPerPackage);
                user.setPackagesPerDay(packagesPerDay);


                dbHandler.updateUser(user);

                Toast.makeText(this, "Settings updated", Toast.LENGTH_SHORT).show();
            } else { //αλλιώς δημιουργεί νέο χρήστη και τον αποθηκεύει στην βάση με τις τιμές αυτές

                user = new User(eurosPerPackage, packagesPerDay, 0);
                dbHandler.addUser(user);
                Toast.makeText(this, "New user created and settings saved", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    //μέθοδος για την εμφάνηση των αποθηκευμένων settings αν αυτά υπάρχουν.
    private void loadUserSettings() {
        User user = dbHandler.getUser();
        if (user != null) {
            editEurosPerPackage.setText(String.valueOf(user.getEurosForTobaccoPackage()));
            editPackagesPerDay.setText(String.valueOf(user.getPackagesPerDay()));
        }
    }
}
