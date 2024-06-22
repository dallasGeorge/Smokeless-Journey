package com.example.SmokelessJourneyApp;

public class User {
    private double eurosForTobaccoPackage;
    private int packagesPerDay;
    private int daysClean;

    //κατασκευαστής της κλάσης User
    public User(double eurosForTobaccoPackage, int packagesPerDay, int daysClean) {
        this.eurosForTobaccoPackage = eurosForTobaccoPackage;
        this.packagesPerDay = packagesPerDay;
        this.daysClean = daysClean;
    }



    //getters και setters των μεταβλητών του
    public double getEurosForTobaccoPackage() {
        return eurosForTobaccoPackage;
    }

    public void setEurosForTobaccoPackage(double eurosForTobaccoPackage) {
        this.eurosForTobaccoPackage = eurosForTobaccoPackage;
    }


    public int getPackagesPerDay() {
        return packagesPerDay;
    }

    public void setPackagesPerDay(int packagesPerDay) {
        this.packagesPerDay = packagesPerDay;
    }


    public int getDaysClean() {
        return daysClean;
    }

    public void setDaysClean(int daysClean) {
        this.daysClean = daysClean;
    }
}
