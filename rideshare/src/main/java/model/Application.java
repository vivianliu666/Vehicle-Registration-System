package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class to represent an application, using composite design pattern
 */
public class Application extends Component {
    private Map<Class, List<Component>> components = new HashMap<Class, List<Component>>();
    private LocalDate lastUpdated;

    /**
     * add a component
     * @param component
     */
    public void add(Component component) {
        if (!components.containsKey(component.getClass())) {
            components.put(component.getClass(), new ArrayList<Component>());
        }

        components.get(component.getClass()).add(component);
        lastUpdated = LocalDate.now();
    }

    /**
     * get driver
     * @return
     */
    public Driver getDriver() {
        if (components.containsKey(Driver.class) && components.get(Driver.class).size() > 0) {
            return (Driver) components.get(Driver.class).get(0);
        }

        return null;
    }

    /**
     * get driver history
     * @return
     */
    public DriverHistory getDriverHistory() {
        if (components.containsKey(DriverHistory.class) && components.get(DriverHistory.class).size() > 0) {
            return (DriverHistory) components.get(DriverHistory.class).get(0);
        }

        return null;
    }

    /**
     * get driver license
     * @return
     */
    public DriverLicense getDriverLicense() {
        if (components.containsKey(DriverLicense.class) && components.get(DriverLicense.class).size() > 0) {
            return (DriverLicense) components.get(DriverLicense.class).get(0);
        }

        return null;
    }

    /**
     * get vehicle list
     * @return
     */
    public List<Vehicle> getVehicle() {
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        if (components.containsKey(Vehicle.class)) {
            for (Component component : components.get(Vehicle.class)) {
                vehicleList.add((Vehicle) component);
            }
        }

        Collections.sort(vehicleList, Comparator.comparingInt(Vehicle::getYear));

        return vehicleList;
    }

    /**
     * get vehicle insurance
     * @return
     */
    public VehicleInsurance getVehicleInsurance() {
        if (components.containsKey(VehicleInsurance.class) && components.get(VehicleInsurance.class).size() > 0) {
            return (VehicleInsurance) components.get(VehicleInsurance.class).get(0);
        }

        return null;
    }

    /**
     * get vehicle history
     * @return
     */
    public VehicleHistory getVehicleHistory() {
        if (components.containsKey(VehicleHistory.class) && components.get(VehicleHistory.class).size() > 0) {
            return (VehicleHistory) components.get(VehicleHistory.class).get(0);
        }

        return null;
    }
}
