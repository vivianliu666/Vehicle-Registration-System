package model;

import java.util.Date;

/**
 * class to represent a moving violation
 */
public class MovingViolation extends Violation {

    public MovingViolation(String type, Date date, Name driverName) {
        super(date, type, driverName);
    }

}
