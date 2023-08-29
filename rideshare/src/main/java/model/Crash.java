package model;

import java.util.Date;

/**
 * class to represent a crash event
 */
public class Crash extends Event {

    public Crash(String type, Date date, Name driverName) {
        super(date, type, driverName);
    }
}
