package model;

import java.util.ArrayList;
import java.util.List;

/**
 * class to represent a vehicle history
 */
public class VehicleHistory extends Component {
    private List<Crash> crashes;
    private List<Violation> violations;
    private Vehicle vehicle;

    public VehicleHistory(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setCrashes(List<Crash> crashes) {
        this.crashes = crashes;
    }

    public void setViolations(List<Violation> violations) {
        this.violations = violations;
    }

    public List<Crash> getCrashes() {
        return crashes;
    }

    public List<Violation> getViolations() {
        return violations;
    }

}
