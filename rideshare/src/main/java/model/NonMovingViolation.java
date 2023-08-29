package model;

import java.util.Date;

/**
 * class to represent a non-moving violation
 */
public class NonMovingViolation extends Violation {

    public NonMovingViolation(String type, Date date, Name driverName) {
        super(date, type, driverName);
    }

}