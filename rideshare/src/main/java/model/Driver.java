package model;

import com.alibaba.fastjson2.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class to represent a driver
 */
public class Driver extends Component {
    private Name name;
    private Date birthDate;

    public Driver(Name name, Date birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Name getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
