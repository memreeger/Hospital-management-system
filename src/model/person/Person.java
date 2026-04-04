package model.person;

import java.util.UUID;

public abstract class Person {

    protected String id;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected String email;

    public Person(String firstName, String lastName, String phone, String email) {
        this.id = UUID.randomUUID().toString();

        // VALIDATION
        this.firstName = requireNonBlank(firstName, "First name");
        this.lastName = requireNonBlank(lastName, "Last name");
        this.phone = requireNonBlank(phone, "Phone");
        this.email = requireNonBlank(email, "Email");
    }

    // HELPER METHOD
    protected String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
        return value;
    }

    // GETTERS
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // SETTERS WITH VALIDATION
    public void setFirstName(String firstName) {
        this.firstName = requireNonBlank(firstName, "First name");
    }

    public void setLastName(String lastName) {
        this.lastName = requireNonBlank(lastName, "Last name");
    }

    public void setPhone(String phone) {
        this.phone = requireNonBlank(phone, "Phone");
    }

    public void setEmail(String email) {
        this.email = requireNonBlank(email, "Email");
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + getFullName() + '\'' +
                '}';
    }
}