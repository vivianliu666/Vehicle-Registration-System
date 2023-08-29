package model;

import java.util.Date;
import java.util.Objects;

/**
 * class to represent a driver license
 */
public class DriverLicense extends Component {
    private Long uniqueNumber;
    private String address;
    private Name name;
    private Date birthDate;
    private String country;
    private String state;
    private Date issuanceDate;
    private Date expirationDate;

    public DriverLicense(Long uniqueNumber, String address, Name name, Date birthDate,
            String country, String state, Date issuanceDate, Date expirationDate) {
        this.uniqueNumber = uniqueNumber;
        this.address = address;
        this.name = name;
        this.birthDate = birthDate;
        this.country = country;
        this.state = state;
        this.issuanceDate = issuanceDate;
        this.expirationDate = expirationDate;
    }

    public Name getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getCountry() {
        return country;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
