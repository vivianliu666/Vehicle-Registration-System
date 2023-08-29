import com.alibaba.fastjson2.JSONObject;
import java.util.Date;
import model.Application;
import model.Driver;
import model.DriverLicense;
import model.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * unit test
 */
public class TestValidator {
    private Application application;

    @BeforeEach
    public void setup() {
        application = new Application();
    }

    @Test
    public void testInvalidDriver() {
        RegistrationValidator rv = new RegistrationValidator(application);
        Assertions.assertFalse(rv.validate());

        Driver driver = new Driver(new Name("a", "n"), new Date());
        application.add(driver);

        Assertions.assertFalse(rv.validate());
    }

    @Test
    public void testDriverLicense() {
        RegistrationValidator rv = new RegistrationValidator(application);

        Driver driver = new Driver(new Name("a", "n"), new Date());
        DriverLicense license = new DriverLicense(123l, "addr",
                new Name("a", "n"), new Date(), "US", "state", new Date(), new Date());
        application.add(driver);
        application.add(license);

        Assertions.assertFalse(rv.validate());
    }

    @Test
    public void testDataLoadAndValidate() {
        RideshareDriverValidator rdv = new RideshareDriverValidator();
        rdv.loadDataFromFile("data");

        Assertions.assertEquals(3, rdv.getExistingPool().size());

        rdv.provideDriverInformation("Smith");
    }
}
