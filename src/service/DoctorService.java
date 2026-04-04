package service;

import model.department.Department;
import model.person.Doctor;

import java.util.HashMap;
import java.util.Map;

public class DoctorService {
    private Map<String, Doctor> doctors;

    public DoctorService() {
        this.doctors = new HashMap<>();
    }

    public Doctor addDoctor(String firstName, String lastName, String phone, String email,
                            Department department, double salary, String specialization) {
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
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }

        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization cannot be null or blank");
        }

        Doctor doctor = new Doctor(firstName, lastName, phone, email, department, salary, specialization);
        doctors.put(doctor.getId(), doctor);
        return doctor;

    }
}

