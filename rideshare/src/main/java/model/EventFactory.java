package model;

import com.alibaba.fastjson2.JSONObject;
import java.util.Date;

/**
 * an interface for event creation factory, using generic type
 * @param <T>
 */
public interface EventFactory<T extends Event> {

    /**
     * create a T instance
     * @param type
     * @param date
     * @param driverName
     * @return
     * @throws Exception
     */
    T create(String type, Date date, Name driverName) throws Exception;
}
