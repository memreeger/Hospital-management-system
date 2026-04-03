package service;

import model.enums.BloodType;
import model.person.Patient;

import java.util.HashMap;
import java.util.Map;

public class PatientService {
    private Map<String, Patient> patientsById;
    private Map<String, Patient> patientsByNumber;

    public PatientService() {
        this.patientsById = new HashMap<>();
        this.patientsByNumber = new HashMap<>();
    }

    public Patient addPatient(String firstName, String lastName, String phone, String email,
                              String patientNumber, BloodType bloodType, String emergencyContact) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");

        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be null or blank");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail cannot be null or blank");
        }

        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null or blank");
        }

        if (bloodType == null) {
            throw new IllegalArgumentException("Blood type cannot be null");
        }

        if(patientsByNumber.containsKey(patientNumber)){
            throw new IllegalStateException("Patient already exist");
        }
        Patient patient = new Patient(firstName, lastName, phone, email, patientNumber, bloodType, emergencyContact);

        patientsById.put(patient.getId(), patient);
        patientsByNumber.put(patient.getPatientNumber(),patient);
        return patient;
    }
}
