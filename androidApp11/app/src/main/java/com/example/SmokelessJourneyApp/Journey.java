package com.example.SmokelessJourneyApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Journey extends AppCompatActivity {
    private MyDBHandler dbHandler;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_journey);
        //προσθήκη του navigation bar
        BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationHandler.setupBottomNavigation(bottomNavigationView);
        dbHandler = new MyDBHandler(this);
        listView = findViewById(R.id.listView);

        // Εμφάνιση όλων των αποθηκευμένων ημερών
        displayAllDays();
        //ρύθμιση για την σωστή εμφάνιση της εφαρμογής στην οθόνη
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //μέθοδος για την εμφάνιση των αποθηκευμένων στην βαση (απο check in ) ημερών στην οθόνη
    private void displayAllDays() {
        //λιστα με ημέρες αποθηκευμένη στην βάση
        List<Day> daysList = dbHandler.getAllDays();

        ArrayAdapter<Day> adapter = new ArrayAdapter<Day>(this, R.layout.list_item_day, daysList) {
            @SuppressLint("InflateParams")
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.list_item_day, null);
                }

                Day day = getItem(position);
                if (day != null) { // Εμφάνιση των δεδομένων του αντικειμένου ημέρας
                    TextView textViewDay = view.findViewById(R.id.textViewDay);
                    textViewDay.setText(String.format("Date: %s\nNote: %s\nCravings:%s%%",
                            day.getCheckInDate(), day.getNote(), day.getCravingsPercentage()));
                }

                return view;
            }
        };

        listView.setAdapter(adapter);
    }



}
