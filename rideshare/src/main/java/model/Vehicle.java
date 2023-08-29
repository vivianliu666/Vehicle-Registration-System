package model;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

/**
 * class to represent a vehicle
 */
public class Vehicle extends Component {
    private String make;
    private String color;
    private String number;
    private String model;
    private int year;
    private Name officialOwner;

    public Vehicle(String make, String color, String number, String model, int year, Name officialOwner) {
        this.make = make;
        this.color = color;
        this.number = number;
        this.model = model;
        this.year = year;
        this.officialOwner = officialOwner;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(make, model, year, officialOwner);
    }

    @Override
    public String toString() {
        return year + " " + color + " " + make + " " +  model + ", " + number;
    }
}
