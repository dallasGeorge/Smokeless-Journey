package com.example.SmokelessJourneyApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "smokelessJourneyDB.db";


    private static final String TABLE_USER = "user"; // Όνομα πίνακα χρηστη
    private static final String TABLE_DAY = "day"; // Όνομα πίνακα ημερων

    //στήλες χρήστη
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_EUROS_FOR_TOBACCO_PACKAGE = "eurosForTobaccoPackage";
    private static final String COLUMN_USER_PACKAGES_PER_DAY = "packagesPerDay";
    private static final String COLUMN_USER_DAYS_CLEAN = "daysClean";

    //στήλες ημερών
    private static final String COLUMN_DAY_ID = "id";
    private static final String COLUMN_DAY_CHECKIN_DATE = "checkInDate";
    private static final String COLUMN_DAY_NOTE = "note";
    private static final String COLUMN_DAY_CRAVINGS_PERCENTAGE = "cravingsPercentage";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {// Δημιουργία πινάκων όταν δημιουργείται η βάση δεδομένων
        // Δημιουργία πίνακα χρήστη
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY," +
                COLUMN_USER_EUROS_FOR_TOBACCO_PACKAGE + " REAL," +
                COLUMN_USER_PACKAGES_PER_DAY + " INTEGER," +
                COLUMN_USER_DAYS_CLEAN + " INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);
        // Δημιουργία πίνακα ημέρας
        String CREATE_DAY_TABLE = "CREATE TABLE " + TABLE_DAY + "(" +
                COLUMN_DAY_ID + " INTEGER PRIMARY KEY," +
                COLUMN_DAY_CHECKIN_DATE + " INTEGER," +
                COLUMN_DAY_NOTE + " TEXT," +
                COLUMN_DAY_CRAVINGS_PERCENTAGE + " INTEGER" + ")";
        db.execSQL(CREATE_DAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);// Αναβάθμιση πίνακα χρήστη
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY); // Αναβάθμιση πίνακα ημέρας
        onCreate(db);
    }


    //επιστρέφει το τελευταίο timestamp εισόδου στην βάση
    public long getLastCheckInTimestamp() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DAY,
                new String[]{COLUMN_DAY_CHECKIN_DATE},
                null,
                null,
                null,
                null,
                COLUMN_DAY_CHECKIN_DATE + " DESC",
                "1");

        long lastCheckInTimestamp = 0;

        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(COLUMN_DAY_CHECKIN_DATE);
            if (index != -1) {
                lastCheckInTimestamp = cursor.getLong(index);
            }
        }

        cursor.close();
        db.close();

        return lastCheckInTimestamp;
    }

    //προσθήκη χρήστη στον πίνακα χρηστών στην βάση
    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EUROS_FOR_TOBACCO_PACKAGE, user.getEurosForTobaccoPackage());
        values.put(COLUMN_USER_PACKAGES_PER_DAY, user.getPackagesPerDay());
        values.put(COLUMN_USER_DAYS_CLEAN, user.getDaysClean());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    //εμφάνηση του πρώτου χρήστη στον πίνακα
    public User getUser() {
        String query = "SELECT * FROM " + TABLE_USER + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getDouble(1),
                    cursor.getInt(2),
                    cursor.getInt(3)
            );
            cursor.close();
            db.close();
            return user;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    //προσθήκη νέας μέρας στον πίνακα μερών
    public void addDay(Day day) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_CHECKIN_DATE, day.getCheckInDate().getTime());
        values.put(COLUMN_DAY_NOTE, day.getNote());
        values.put(COLUMN_DAY_CRAVINGS_PERCENTAGE, day.getCravingsPercentage());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_DAY, null, values);
        db.close();
    }

    //επιστροφή λίστας ημερών από των πίνακα με μέρς στην βάση
    public List<Day> getAllDays() {
        List<Day> daysList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_DAY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Day day = new Day(
                        new Date(cursor.getLong(1)),
                        cursor.getString(2),
                        cursor.getInt(3)
                );
                daysList.add(day);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return daysList;
    }


    //ενημερώνει τα δεδομένα του χρήστη στην βάση
    public void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EUROS_FOR_TOBACCO_PACKAGE, user.getEurosForTobaccoPackage());
        values.put(COLUMN_USER_PACKAGES_PER_DAY, user.getPackagesPerDay());
        values.put(COLUMN_USER_DAYS_CLEAN, user.getDaysClean());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(1)});
        db.close();
    }








}
