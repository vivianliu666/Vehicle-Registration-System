import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import model.Application;
import model.Crash;
import model.Driver;
import model.DriverHistory;
import model.DriverLicense;
import model.Event;
import model.Vehicle;
import model.VehicleHistory;
import model.VehicleInsurance;
import model.Violation;

/**
 * class to valid a registration
 */
public class RegistrationValidator {
    private DriverValidator driverValidator;
    private DriverLicenseValidator driverLicenseValidator;
    private VehicleValidator vehicleValidator;
    private VehicleInsuranceValidator vehicleInsuranceValidator;
    private DriverHistoryValidator driverHistoryValidator;
    private VehicleHistoryValidator vehicleHistoryValidator;
    private Application application;

    public RegistrationValidator(Application application) {
        this.application = application;
        driverValidator = new DriverValidator();
        driverLicenseValidator = new DriverLicenseValidator();
        vehicleValidator = new VehicleValidator();
        vehicleInsuranceValidator = new VehicleInsuranceValidator();
        driverHistoryValidator = new DriverHistoryValidator();
        vehicleHistoryValidator = new VehicleHistoryValidator();
    }

    /**
     * check if the application is valid
     * @return
     */
    public boolean validate() {
        return driverValidator.validate()
                && driverLicenseValidator.validate()
                && vehicleValidator.validate()
                && vehicleInsuranceValidator.validate()
                && driverHistoryValidator.validate()
                && vehicleHistoryValidator.validate();
    }

    /**
     * abstract class for a validator
     */
    public abstract class Validator {

        /**
         * check if is valid
         * @return true if valid
         */
        boolean validate() {
            return true;
        }
    }

    /**
     * driver validator
     */
    private class DriverValidator extends Validator {

        /**
         * check driver
         * @return
         */
        @Override
        public boolean validate() {
            Driver driver = application.getDriver();
            if (driver == null || driver.getBirthDate() == null) {
                System.out.println("Drive valid failed, missing fields");
                return false;
            }

            LocalDate now = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate birth = driver.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int age = Period.between(birth, now).getYears();

            if (age < 21) {
                System.out.println("Drive valid failed, age");
                return false;
            }

            return true;
        }
    }

    /**
     * driver license validator
     */
    private class DriverLicenseValidator extends Validator {

        /**
         * check driver license
         * @return
         */
        @Override
        public boolean validate() {
            Driver driver = application.getDriver();
            DriverLicense licence = application.getDriverLicense();
            if (licence == null || driver == null) {
                System.out.println("Drive license valid failed, missing fields");

                return false;
            }

            if (!licence.getName().equals(driver.getName())
                || !licence.getBirthDate().equals(driver.getBirthDate())
                || (!licence.getCountry().equals("US") && !licence.getCountry().equals("Canada"))) {
                System.out.println("Drive license valid failed, owner or country");

                return false;
            }

            LocalDate issuanceDate = licence.getIssuanceDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            if (Period.between(issuanceDate, now).getMonths() < 6) {
                System.out.println("Drive license valid failed, issuance");

                return false;
            }

            LocalDate expirationDate = licence.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (expirationDate.isBefore(now)) {
                System.out.println("Drive license valid failed, expiration");
                return false;
            }

            return true;
        }
    }

    /**
     * vehicle validator
     */
    private class VehicleValidator extends Validator {

        /**
         * check vehicle
         * @return
         */
        @Override
        public boolean validate() {
            List<Vehicle> vehicles = application.getVehicle();
            if (vehicles.size() == 0) {
                System.out.println("Vehicle valid failed, none");

                return false;
            }
            Iterator<Vehicle> vehicleIterator = vehicles.iterator();
            while (vehicleIterator.hasNext()) {
                Vehicle vehicle = vehicleIterator.next();
                int yearNow = Calendar.getInstance().get(Calendar.YEAR);
                if (vehicle == null || yearNow - vehicle.getYear() > 15) {
                    System.out.println("Vehicle valid failed, year 15");

                    return false;
                }
            }

            return true;
        }
    }

    /**
     * vehicle insurance validator
     */
    private class VehicleInsuranceValidator extends Validator {

        /**
         * check vehicle insurance
         * @return
         */
        @Override
        public boolean validate() {
            Driver driver = application.getDriver();
            VehicleInsurance vehicleInsurance = application.getVehicleInsurance();
            if (driver == null || vehicleInsurance == null) {
                System.out.println("Vehicle insurance valid failed, none");

                return false;
            }

            if (!driver.getName().equals(vehicleInsurance.getOfficialOwner())
                    && !vehicleInsurance.getDrivers().contains(driver)) {
                System.out.println("Vehicle insurance valid failed, owner");

                return false;
            }

            LocalDate expirationDate = vehicleInsurance.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (expirationDate.isBefore(LocalDate.now())) {
                System.out.println("Vehicle insurance valid failed, expiration date");

                return false;
            }

            return true;
        }
    }

    /**
     * driver history validator
     */
    private class DriverHistoryValidator extends Validator {

        /**
         * check driver violations
         * @return
         */
        @Override
        public boolean validate() {
            DriverHistory driverHistory = application.getDriverHistory();
            if (driverHistory.getViolations() != null && !driverHistory.getViolations().isEmpty()) {
                Iterator<Violation> violationIterator = driverHistory.getViolations().iterator();
                while (violationIterator.hasNext()) {
                    Violation violation = violationIterator.next();
                    if (violation.getType().equals("Reckless") || violation.getType().equals("Speeding") || violation.getType().equals("DUI")
                            || violation.getType().equals("NoLicenseOrInsurance")) {
                        System.out.println("Driver history valid failed, type");

                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * vehicle history validator
     */
    private class VehicleHistoryValidator extends Validator {

        /**
         * check vehicle history
         * @return
         */
        @Override
        public boolean validate() {
            VehicleHistory vehicleHistory = application.getVehicleHistory();
            if (vehicleHistory.getViolations() != null && !vehicleHistory.getViolations().isEmpty()) {
                Iterator<Violation> violationIterator = vehicleHistory.getViolations().iterator();
                while (violationIterator.hasNext()) {
                    if (!eventDateValid(violationIterator.next())) {
                        System.out.println("Vehicle history valid failed, violation date");

                        return false;
                    }
                }

                Iterator<Crash> crashIterator = vehicleHistory.getCrashes().iterator();
                while (crashIterator.hasNext()) {
                    if (!eventDateValid(crashIterator.next())) {
                        System.out.println("Vehicle history valid failed, crash date");

                        return false;
                    }
                }
            }

            return true;
        }

        /**
         * check if event date is valid
         * @param event
         * @return
         */
        private boolean eventDateValid(Event event) {
            LocalDate issuanceDate = event.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();

            Period period = Period.between(issuanceDate, now);
            if (period.getYears() < 1 && period.getMonths() < 6) {
                return false;
            }

            return true;
        }
    }
}
