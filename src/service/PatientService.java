package service;

import model.enums.BloodType;
import model.person.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientService {
    private Map<Integer, Patient> patientsById;
    private Map<String, Patient> patientsByNumber;

    public PatientService() {
        this.patientsById = new HashMap<>();
        this.patientsByNumber = new HashMap<>();
    }


    public  int idd = 1;

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

        if (patientsByNumber.containsKey(patientNumber)) {
            throw new IllegalStateException("Patient already exists");
        }
        Patient patient = new Patient(firstName, lastName, phone, email, patientNumber, bloodType, emergencyContact);

        patientsById.put(patient.getId(), patient);
        patientsByNumber.put(patient.getPatientNumber(), patient);
        return patient;
    }



    public Patient deletePatient(String patientNumber) {
        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null");
        }

        Patient patient = patientsByNumber.get(patientNumber);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        patientsByNumber.remove(patientNumber);
        patientsById.remove(patient.getId());
        return patient;
    }

    public Patient getPatientById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be null or blank");
        }
        Patient patient = patientsById.get(id);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        return patient;
    }

    public Patient getPatientByNumber(String patientNumber) {
        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null or blank");
        }

        Patient patient = patientsByNumber.get(patientNumber);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }
        return patient;
    }

    public Patient updatePatient(String firstName, String lastName, String phone, String email,
                                 String patientNumber, BloodType bloodType, String emergencyContact, String newPatientNumber) {

        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null or blank");
        }

        Patient patient = patientsByNumber.get(patientNumber);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        // optional alanlar
        if (firstName != null && !firstName.isBlank()) {
            patient.setFirstName(firstName);
        }

        if (lastName != null && !lastName.isBlank()) {
            patient.setLastName(lastName);
        }

        if (phone != null && !phone.isBlank()) {
            patient.setPhone(phone);
        }

        if (email != null && !email.isBlank()) {
            patient.setEmail(email);
        }

        if (bloodType != null) {
            patient.setBloodType(bloodType);
        }

        if (emergencyContact != null) {
            patient.setEmergencyContact(emergencyContact);
        }

        // key change
        if (newPatientNumber != null && !newPatientNumber.isBlank()
                && !patientNumber.equals(newPatientNumber)) {

            if (patientsByNumber.containsKey(newPatientNumber)) {
                throw new IllegalStateException("New patient number already exists");
            }

            patientsByNumber.remove(patientNumber);
            patient.setPatientNumber(newPatientNumber);
            patientsByNumber.put(newPatientNumber, patient);
        }

        return patient;
    }

    public Patient updatePatientPhone(String patientNumber, String newPhone) {
        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null or blank");
        }
        if (newPhone == null || newPhone.isBlank()) {
            throw new IllegalArgumentException("New phone cannot be null or blank");
        }

        Patient patient = patientsByNumber.get(patientNumber);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        patient.setPhone(newPhone);
        return patient;
    }

    public Patient updatePatientEmail(String patientNumber, String newEmail) {
        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null or blank");
        }
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("New email cannot be null or blank");
        }

        Patient patient = patientsByNumber.get(patientNumber);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        patient.setEmail(newEmail);
        return patient;
    }

    public Patient updateEmergencyContact(String patientNumber, String newContact) {
        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null or blank");
        }
        if (newContact == null) {
            newContact = ""; // null-safe
        }

        Patient patient = patientsByNumber.get(patientNumber);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        patient.setEmergencyContact(newContact);
        return patient;
    }

    public Patient updateBloodType(String patientNumber, BloodType newBloodType) {
        if (patientNumber == null || patientNumber.isBlank()) {
            throw new IllegalArgumentException("Patient number cannot be null or blank");
        }
        if (newBloodType == null) {
            throw new IllegalArgumentException("Blood type cannot be null");
        }

        Patient patient = patientsByNumber.get(patientNumber);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        patient.setBloodType(newBloodType);
        return patient;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientsByNumber.values());
    }

    public Patient getPatientByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be null or blank");
        }
        Patient patient = patientsById.values().stream()
                .filter(p -> p.getPhone().equals(phone))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        return patient;
    }

    public List<Patient> getPatientByBloodType(BloodType bloodType) {
        if (bloodType == null) {
            throw new IllegalArgumentException("Blood type cannot be null");
        }

        List<Patient> patients = patientsByNumber.values().stream()
                .filter(patient -> patient.getBloodType() == (bloodType)) // enumlar == operatörü ile karşılaştırılır!!!
                .toList();

        return patients;
    }

    public List<Patient> findPatientsByFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }

        String search = fullName.trim().toLowerCase();

        return patientsByNumber.values().stream()
                .filter(p -> (p.getFirstName() + " " + p.getLastName()).toLowerCase().contains(search))
                .toList();
    }

    public List<Patient> findPatientsByFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }

        return patientsByNumber.values().stream()
                .filter(p -> p.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .toList();
    }

    public List<Patient> findPatientsByLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }

        return patientsByNumber.values().stream()
                .filter(p -> p.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .toList();
    }

    public List<Patient> findPatientsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }

        return patientsByNumber.values().stream()
                .filter(p -> p.getEmail().toLowerCase().contains(email.toLowerCase()))
                .toList();
    }

    public void deleteAllPatients() {
        patientsByNumber.clear();
        patientsById.clear();
    }
}
