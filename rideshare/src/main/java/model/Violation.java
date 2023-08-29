package model;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * class to represent an abstract violation, implements comparator, use type to compare
 */
public abstract class Violation extends Event implements Comparator<Violation> {

    public Violation(Date date, String type, Name driverName) {
        super(date, type, driverName);
    }

    @Override
    public int compare(Violation o1, Violation o2) {
        return o1.getType().compareTo(o2.getType());
    }

}
