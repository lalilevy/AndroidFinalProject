package com.shir.androidfinalproject.Models;

public class EventLocation extends Votes {
    private String country;
    private String city;
    private String street;
    private int houseNo;

    public EventLocation(String strCountry,
                         String strCity,
                         String strStreet,
                         int nHouseNo) {
        this.country = strCountry;
        this.city = strCity;
        this.street = strStreet;
        this.houseNo = nHouseNo;
    }

    public String toString() {
        return this.country + ", " + this.city + " " + this.street + ", " + this.houseNo;
    }
}
