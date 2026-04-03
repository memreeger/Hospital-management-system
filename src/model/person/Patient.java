package model.person;

import model.enums.BloodType;

public class Patient extends Person {

    private String patientNumber;
    private BloodType bloodType;
    private String emergencyContact;

    public Patient(String firstName, String lastName, String phone, String email,
                   String patientNumber, BloodType bloodType, String emergencyContact) {
        super(firstName, lastName, phone, email);
        this.patientNumber = patientNumber;
        this.bloodType = bloodType;
        this.emergencyContact = (emergencyContact != null) ? emergencyContact : "";
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public void setBloodType(BloodType bloodType) {
        if (bloodType == null) {
            throw new IllegalArgumentException("Blood type cannot be null");
        }
        this.bloodType = bloodType;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = (emergencyContact != null) ? emergencyContact : "";
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", patientNumber='" + patientNumber + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                '}';
    }
}