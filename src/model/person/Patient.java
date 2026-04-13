package model.person;

import model.enums.BloodType;

public class Patient extends Person {

    private int patientNumber;
    private BloodType bloodType;
    private String emergencyContact;

    public Patient(String firstName, String lastName, String phone, String email,
                   int patientNumber, BloodType bloodType, String emergencyContact) {
        super(firstName, lastName, phone, email);

        // VALIDATIONLAR MODEL İÇİNDE !!!!
        if (patientNumber < 0) {
            throw new IllegalArgumentException("Patient number cannot be smaller than 0 ");
        }
        if (bloodType == null) {
            throw new IllegalArgumentException("Blood type cannot be null");
        }

        this.patientNumber = patientNumber;
        this.bloodType = bloodType;
        this.emergencyContact = (emergencyContact != null) ? emergencyContact : "";
    }

    public int getPatientNumber() {
        return patientNumber;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setPatientNumber(int patientNumber) {
        if (patientNumber < 0) {
            throw new IllegalArgumentException("Patient number cannot be smaller than 0");
        }
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
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", patientNumber='" + patientNumber + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", emergencyContact='" + emergencyContact + '\'' +
                '}';
    }
}