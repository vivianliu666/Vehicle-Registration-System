package model;

import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class to represent a vehicle insurance
 */
public class VehicleInsurance extends Component {
    private Name officialOwner;
    private List<Driver> drivers;
    private Date expirationDate;
    private Vehicle vehicle;

    public VehicleInsurance(Name officialOwner, List<Driver> drivers, Date expirationDate, Vehicle vehicle) {
        this.officialOwner = officialOwner;
        this.drivers = drivers;
        this.expirationDate = expirationDate;
        this.vehicle = vehicle;
    }

    public Name getOfficialOwner() {
        return officialOwner;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
