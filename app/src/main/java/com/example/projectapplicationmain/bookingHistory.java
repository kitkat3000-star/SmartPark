package com.example.projectapplicationmain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class bookingHistory {
    String Date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    private String Duration = "2 Hrs";
    private String Location = "University of Wollongong in Dubai";
    private String Parking = "";
    private String Type = "Stand";
    private String Zone = "";
    private String level = "Basement 1";


    public bookingHistory() {
        //public no-arg constructor needed
    }

    public bookingHistory(String duration, String location, String parking, String type, String zone, String level, String date, String time) {
        Duration = duration;
        Location = location;
        Parking = parking;
        Type = type;
        Zone = zone;
        this.level = level;
        Date = date;
        Time = time;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getParking() {
        return Parking;
    }

    public void setParking(String parking) {
        Parking = parking;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time =Time;
    }

}
