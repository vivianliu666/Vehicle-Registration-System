import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import model.Application;
import model.Driver;
import model.DriverHistory;
import model.Name;
import model.Vehicle;
import model.Violation;
import model.ViolationFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestView {
    private DriverInfoViewer viewer;

    @BeforeEach
    void setUp() {
        viewer = new DriverInfoViewer();
    }

    @Test
    public void testEmpty() {
        Assertions.assertEquals("No registered driver found", viewer.show(null));
        Assertions.assertEquals("No registered driver found", viewer.show(new ArrayList<>()));
    }

    @Test
    public void testNoViolation() throws Exception {
        List<Application> applications = new ArrayList();
        Application application = new Application();
        application.add(new Driver(new Name("a", "b"), new Date()));
        Vehicle vehicle = new Vehicle("m", "red", "123", "typeR", 2020, new Name("a", "b"));
        application.add(vehicle);
        Violation violation = ViolationFactory.getINSTANCE().create("Speeding", new Date(), new Name("a", "b"));
        application.add(vehicle);
        DriverHistory history = new DriverHistory();
        history.setViolations(Arrays.asList(violation));
        application.add(history);
        applications.add(application);

        String res = viewer.show(applications);
        Assertions.assertTrue(res.startsWith("b, a"));
        Assertions.assertTrue(res.contains("2020 red m typeR, 123"));
        Assertions.assertTrue(res.contains("Driving violations"));
        Assertions.assertTrue(res.contains("Speeding"));
    }
}
