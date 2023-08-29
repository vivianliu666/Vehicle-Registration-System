package model;

import com.alibaba.fastjson2.JSONObject;
import java.util.Date;

/**
 * class to represent a violation factory, using singleton and simple factory design pattern.
 */
public class ViolationFactory implements EventFactory<Violation> {
    private static ViolationFactory INSTANCE = new ViolationFactory();

    private ViolationFactory() {
    }

    /**
     * get instance
     * @return
     */
    public static ViolationFactory getINSTANCE() {
        return INSTANCE;
    }

    /**
     * create a violation
     * @param type
     * @param date
     * @param driverName
     * @return
     * @throws Exception
     */
    @Override
    public Violation create(String type, Date date, Name driverName) throws Exception {
        if (type == null) {
            throw new IllegalArgumentException("Create violation failed, Missing type");
        }

        Violation violation;
        switch (type) {
            case "Distracted driving":
            case "Reckless driving":
            case "Speeding":
            case "Driving under influence":
            case "Failure to respect traffic signs":
            case "Driving without a valid license and/or insurance":
                violation = new MovingViolation(type, date, driverName);
                break;
            case "Parking violation":
            case "Paperwork issues":
            case "Problems with the vehicle":
                violation = new NonMovingViolation(type, date, driverName);
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }

        return violation;
    }
}
