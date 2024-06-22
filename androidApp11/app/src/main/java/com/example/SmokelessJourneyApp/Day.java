package com.example.SmokelessJourneyApp;

import java.util.Date;

public class Day {
    private Date checkInDate;
    private String note;
    private int cravingsPercentage;

    //κατασκευαστής του Day
    public Day(Date checkInDate, String note, int cravingsPercentage) {
        this.checkInDate = checkInDate;
        this.note = note;
        this.cravingsPercentage = cravingsPercentage;
    }

    //getters των τιμών της
    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getNote() {
        return note;
    }
    //setters των τιμών της
    public void setNote(String note) {
        this.note = note;
    }

    public int getCravingsPercentage() {
        return cravingsPercentage;
    }

    public void setCravingsPercentage(int cravingsPercentage) {
        this.cravingsPercentage = cravingsPercentage;
    }
}
