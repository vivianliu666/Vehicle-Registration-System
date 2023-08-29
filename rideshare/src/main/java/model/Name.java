package model;

import java.util.Objects;

/**
 * class to represent a name
 */
public class Name {
    private String firstname;
    private String lastname;

    public Name(String firstname, String lastname) {
        if (firstname == null || lastname == null) {
            throw new IllegalArgumentException("Create name failed, missing parameters");
        }
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name = (Name) o;
        return Objects.equals(firstname, name.firstname) && Objects.equals(lastname, name.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname);
    }

    public String toString() {
        return lastname + ", " + firstname;
    }
}
