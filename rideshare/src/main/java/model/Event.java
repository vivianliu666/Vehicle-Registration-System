package model;

import com.alibaba.fastjson2.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * class to represent an abstract event
 */
public abstract class Event extends Component {
    private Date date;
    private String type;
    private Name driverName;

    public Event(Date date, String type, Name driverName) {
        this.date = date;
        this.type = type;
        this.driverName = driverName;
    }

    public Date getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
