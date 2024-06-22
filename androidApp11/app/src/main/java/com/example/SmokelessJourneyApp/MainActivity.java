package com.example.SmokelessJourneyApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MyDBHandler dbHandler;
    TextView textViewDaysClean;
    TextView textViewEurosSaved;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //ρύθμιση για την σωστή εμφάνιση της εφαρμογής στην οθόνη
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHandler = new MyDBHandler(this);

        //αν δεν υπαρχει χρηστης εμφανηση του settings σαν πρωτη καρτελα
        if (dbHandler.getUser() == null) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            finish();
            return;
        }
        textViewDaysClean = findViewById(R.id.textView);
        textViewEurosSaved = findViewById(R.id.textView2);
        //προσθήκη του navigation bar
        BottomNavigationHandler bottomNavigationHandler = new BottomNavigationHandler(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationHandler.setupBottomNavigation(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        // Αρχικοποίηση του γραφήματος
        lineChart = findViewById(R.id.lineChart);
        setupLineChart();
        plotCravingsData();
        //κλήση μεθόδου για την ενημέρωση τιμών σχετικά με τον user στην οθόνη
        updateUserValues();
    }

    private void setupLineChart() {
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        //ρυθμίσεις για τον άξονα χ
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(5);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(0xFF000000);
        //ρυθμίσεις για τον άξονα y
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(0xFF000000);
        //απενεργοποίηση του δεξιού άξονα
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }
    //ploting τιμών ημερών απο το database στο γράφιμα
    private void plotCravingsData() {
        List<Day> days = dbHandler.getAllDays();
        ArrayList<Entry> entries = new ArrayList<>();
        int index = 1;
        //δημιουργία λίστας με τα ποσοστά στεριτικών κάθε μέρας
        for (Day day : days) {
            entries.add(new Entry(index++, day.getCravingsPercentage()));
        }
        //εισαγωγή των δεδομένων
        LineDataSet dataSet = new LineDataSet(entries, "Cravings Percentage");
        //ρυθμίσεις για την καλύτερη εμφάνιση των δεδομένων
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(0xFF000000);
        dataSet.setColor(0xFF000000);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // ενημέρωση στοιχείων του γραφήματος
    }

    //μέθοδος για την ενημέρωση του days clean και euros saved
    //σύμφωνα με τις τιμές μεταβλητών του user στο database
    private void updateUserValues() {
        User user = dbHandler.getUser();
        if (user != null) {
            int daysClean = user.getDaysClean();
            double eurosSaved = user.getDaysClean() * user.getPackagesPerDay() * user.getEurosForTobaccoPackage();

            textViewDaysClean.setText(getString(R.string.days_clean, daysClean));
            textViewEurosSaved.setText(getString(R.string.euros_saved, eurosSaved));
        }
    }
    //μέθοδος για άνοιγμα του activity settings όταν πατηθεί το αντίστοιχο κουμπί
    public void openSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
