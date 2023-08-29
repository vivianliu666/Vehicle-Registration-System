package model;

import com.alibaba.fastjson2.JSONObject;
import java.util.Date;

/**
 * class to represent a crash factory, using singleton and simple factory design pattern.
 */
public class CrashFactory implements EventFactory<Crash> {
    private static CrashFactory INSTANCE = new CrashFactory();

    private CrashFactory() {
    }

    /**
     * get instance
     * @return
     */
    public static CrashFactory getINSTANCE() {
        return INSTANCE;
    }

    /**
     * create a crash
     * @param type
     * @param date
     * @param driverName
     * @return
     * @throws Exception
     */
    public Crash create(String type, Date date, Name driverName) throws Exception {
        if (type != null && (type.equals("fender-bender") || type.equals("crash without bodily injuries") || type.equals("crash involving bodily injuries"))) {
            return new Crash(type, date, driverName);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
