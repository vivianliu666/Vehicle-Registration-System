import java.util.Iterator;
import java.util.List;
import model.Application;
import model.Driver;
import model.DriverHistory;
import model.Vehicle;
import model.Violation;

/**
 * viewer for driver info display
 */
public class DriverInfoViewer {

    /**
     * show driver information from application list
     * @param applications
     */
    public String show(List<Application> applications) {
        StringBuilder sb = new StringBuilder();
        if (applications != null && applications.size() > 0) {
            for (Application application : applications) {
                Driver driver = application.getDriver();
                sb.append(driver.getName()).append("\n");
                Iterator<Vehicle> vehicleIterator = application.getVehicle().iterator();
                while (vehicleIterator.hasNext()) {
                    sb.append("\t" + vehicleIterator.next()).append("\n");
                }
                DriverHistory driverHistory = application.getDriverHistory();
                if (driverHistory.getViolations() != null && !driverHistory.getViolations().isEmpty()) {
                    sb.append("\tDriving violations:").append("\n");
                    for (Violation violation : driverHistory.getViolations()) {
                        sb.append("\t\t" + violation.getType()).append("\n");
                    }
                }
            }
        } else {
            sb.append("No registered driver found");
        }

        System.out.println(sb);

        return sb.toString();
    }
}
