import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import model.Application;
import model.Crash;
import model.CrashFactory;
import model.Driver;
import model.DriverHistory;
import model.DriverLicense;
import model.Name;
import model.Vehicle;
import model.VehicleHistory;
import model.VehicleInsurance;
import model.Violation;
import model.ViolationFactory;

/**
 * main controller of the system
 */
public class RideshareDriverValidator {
    private Map<Driver, Application> existingPool;
    private DriverInfoViewer driverInfoViewer;

    public RideshareDriverValidator() {
        existingPool = new HashMap<Driver, Application>();
        driverInfoViewer = new DriverInfoViewer();
    }

    public Map<Driver, Application> getExistingPool() {
        return existingPool;
    }

    /**
     * query by lastname
     * @param lastName
     */
    public void provideDriverInformation(String lastName) {
        if (lastName == null) {
            return;
        }

        List<Application> applications = new ArrayList<>();
        for (Driver driver : existingPool.keySet()) {
            if (driver.getName().getLastname().equals(lastName)) {
                applications.add(existingPool.get(driver));
            }
        }

        driverInfoViewer.show(applications);
    }

    /**
     * load json data from file, each line should be a complete json data
     * @param filename
     */
    public void loadDataFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    JSONObject jsonObject = JSONObject.parseObject(line);
                    Application application = new DataParser(jsonObject).parse();
                    RegistrationValidator validator = new RegistrationValidator(application);
                    Driver driver = application.getDriver();
                    if (validator.validate() && !existingPool.containsKey(driver)) {
                        System.out.println("Registration successfully, add driver " + driver + " to existing pool");
                        existingPool.put(driver, application);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid data json, " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Load from file failed " + filename + ", " + e.getMessage());
        }
    }

    /**
     * main entry
     * @param args
     */
    public static void main(String[] args) {
        RideshareDriverValidator rdv = new RideshareDriverValidator();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to use RideshareDriverValidator");
        while (true) {
            System.out.println("Enter l [filename] to load data, p [lastname] to query driver, enter q to quit");
            String input = scanner.nextLine();
            String[] tmp = input.split(" ");
            if (tmp.length == 2) {
                switch (tmp[0]) {
                    case "l":
                        String filename = tmp[1];
                        rdv.loadDataFromFile(filename);
                        break;
                    case "p":
                        String lastname = tmp[1];
                        rdv.provideDriverInformation(lastname);
                }
            } else if (input.equals("q")) {
                System.out.println("Bye");
                break;
            } else {
                System.out.println("Command not found");
            }
        }
    }

    /**
     * json data parser
     */
    private class DataParser {
        JSONObject jsonObject;
        Application application;

        public DataParser(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
            this.application = new Application();
        }

        /**
         * parse and return application
         * @return
         * @throws Exception
         */
        public Application parse() throws Exception {
            parseDriver();
            parseVehicle();
            System.out.println("Parse application successfully");

            return application;
        }

        /**
         * convert string to Date
         * @param date
         * @return
         * @throws Exception
         */
        private Date stringToDate(String date) throws Exception {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            return sdf.parse(date);
        }

        /**
         * parse Name obj
         * @param jsonObject
         * @return
         * @throws Exception
         */
        private Name parseName(JSONObject jsonObject) throws Exception {
            String firstName = jsonObject.getString("firstname");
            String lastName = jsonObject.getString("lastname");
            return new Name(firstName, lastName);
        }

        /**
         * parse driver license
         * @param jsonObject
         * @throws Exception
         */
        private void parseDriverLicense(JSONObject jsonObject) throws Exception {
            Name name = parseName(jsonObject);
            Date birthDate = stringToDate(jsonObject.getString("birth"));
            String address = jsonObject.getString("address");
            String country = jsonObject.getString("country");
            String state = jsonObject.getString("state");
            Date issuanceDate = stringToDate(jsonObject.getString("issuance"));
            Date expirationDate = stringToDate(jsonObject.getString("expiration"));
            Long number = jsonObject.getLong("number");

            if (birthDate == null || address == null || country == null || state == null
                    || issuanceDate == null || expirationDate == null || number == null) {
                throw new IllegalArgumentException("Create Driver Licence failed, Missing parameters");
            }

            application.add(new DriverLicense(number, address, name, birthDate, country, state, issuanceDate, expirationDate));
        }

        /**
         * parse driver
         * @throws Exception
         */
        private void parseDriver() throws Exception {
            if (jsonObject.containsKey("driver")) {
                JSONObject driverJson = jsonObject.getJSONObject("driver");
                Name name = parseName(driverJson);
                Date birthDate = stringToDate(driverJson.getString("birth"));

                application.add(new Driver(name, birthDate));

                DriverHistory driverHistory = new DriverHistory();
                application.add(driverHistory);

                if (driverJson.containsKey("violation")) {
                    JSONArray driverVioArr = driverJson.getJSONArray("violation");
                    driverHistory.setViolations(parseViolation(driverVioArr, name));
                }

                if (driverJson.containsKey("license")) {
                    JSONObject driverLicenseJson = driverJson.getJSONObject("license");
                    parseDriverLicense(driverLicenseJson);
                } else {
                    throw new IllegalArgumentException("No license specified");
                }
            }

            System.out.println("Driver parse successful");
        }

        /**
         * parse all vehicles
         * @throws Exception
         */
        private void parseVehicle() throws Exception {
            if (jsonObject.containsKey("vehicle")) {
                parseVehicle(jsonObject.getJSONArray("vehicle"));

                System.out.println("Vehicle parse successful");
            }
        }

        /**
         * parse all vehicles
         * @param vehicleArr
         * @throws Exception
         */
        private void parseVehicle(JSONArray vehicleArr) throws Exception {
            for (int i = 0; i < vehicleArr.size(); i++) {
                JSONObject vehicleJson = vehicleArr.getJSONObject(i);
                parseVehicle(vehicleJson);
            }
        }

        /**
         * parse one vehicle
         * @param vehicleJson
         * @throws Exception
         */
        private void parseVehicle(JSONObject vehicleJson) throws Exception {
            String make = vehicleJson.getString("make");
            String model = vehicleJson.getString("model");
            String color = vehicleJson.getString("color");
            String number = vehicleJson.getString("number");
            Integer year = vehicleJson.getInteger("year");
            Name name = parseName(vehicleJson);

            if (make == null || model == null || color == null || number == null || year <= 0) {
                throw new IllegalArgumentException();
            }

            Vehicle vehicle = new Vehicle(make, color, number, model, year, name);
            application.add(vehicle);

            if (vehicleJson.containsKey("insurance")) {
                JSONObject insuranceJson = vehicleJson.getJSONObject("insurance");
                parseVehicleInsurance(vehicle, insuranceJson);
            } else {
                throw new IllegalArgumentException("No insurance specified");
            }

            VehicleHistory vehicleHistory = new VehicleHistory(vehicle);
            if (vehicleJson.containsKey("violation")) {
                JSONArray vioArr = vehicleJson.getJSONArray("violation");
                vehicleHistory.setViolations(parseViolation(vioArr));
            }

            if (vehicleJson.containsKey("crash")) {
                JSONArray crashArr = vehicleJson.getJSONArray("crash");
                vehicleHistory.setCrashes(parseCrash(crashArr));
            }

            application.add(vehicleHistory);
        }

        /**
         * parse vehicle insurance
         * @param vehicle
         * @param jsonObject
         * @throws Exception
         */
        private void parseVehicleInsurance(Vehicle vehicle, JSONObject jsonObject) throws Exception {
            Name officialOwner = parseName(jsonObject);
            List<Driver> coverDrivers = new ArrayList<>();
            if (jsonObject.containsKey("drivers")) {
                JSONArray driverArr = jsonObject.getJSONArray("drivers");
                for (int i = 0; i < driverArr.size(); i++) {
                    JSONObject driverJson = driverArr.getJSONObject(i);
                    Name driverName = parseName(driverJson);
                    Date birth = stringToDate(driverJson.getString("birth"));

                    coverDrivers.add(new Driver(driverName, birth));
                }
            }

            Date expiration = stringToDate(jsonObject.getString("expiration"));
            VehicleInsurance vehicleInsurance = new VehicleInsurance(officialOwner, coverDrivers, expiration, vehicle);

            application.add(vehicleInsurance);
        }

        /**
         * parse violation array
         * @param vioArr
         * @return
         * @throws Exception
         */
        private List<Violation> parseViolation(JSONArray vioArr) throws Exception {
            return parseViolation(vioArr, null);
        }

        /**
         * parse violation array
         * @param vioArr
         * @param name
         * @return
         * @throws Exception
         */
        private List<Violation> parseViolation(JSONArray vioArr, Name name) throws Exception {
            List<Violation> violations = new ArrayList<Violation>();
            for (int i = 0; i < vioArr.size(); i++) {
                JSONObject vioObj = vioArr.getJSONObject(i);

                Date date = stringToDate(vioObj.getString("date"));
                String type = vioObj.getString("type");

                if (name == null) {
                    name = parseName(vioObj);
                }

                violations.add(ViolationFactory.getINSTANCE().create(type, date, name));
            }

            return violations;
        }

        /**
         * parse crash array
         * @param crashArr
         * @return
         * @throws Exception
         */
        private List<Crash> parseCrash(JSONArray crashArr) throws Exception {
            List<Crash> crashes = new ArrayList<Crash>();
            for (int i = 0; i < crashArr.size(); i++) {
                JSONObject vioObj = crashArr.getJSONObject(i);

                Date date = stringToDate(vioObj.getString("date"));
                String type = vioObj.getString("type");
                Name driverName = parseName(vioObj);

                crashes.add(CrashFactory.getINSTANCE().create(type, date, driverName));
            }

            return crashes;
        }
    }
}
