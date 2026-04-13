package service;

import model.department.Department;
import model.person.Doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorService {
    private Map<Integer, Doctor> doctors;

    public DoctorService() {
        this.doctors = new HashMap<>();
    }

    //HELPER METHOD
    private void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    private void validateInt(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be smaller than 0");
        }
    }

    public Doctor addDoctor(String firstName, String lastName, String phone, String email,
                            Department department, double salary, String specialization, double fee) {
        validateString(firstName, "First name");
        validateString(lastName, "Last name");
        validateString(phone, "Phone");
        validateString(email, "Email");

        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }

        validateString(specialization, "Specialization");

        Doctor doctor = new Doctor(firstName, lastName, phone, email, department, salary, specialization, fee);
        doctors.put(doctor.getId(), doctor);
        department.addStaff(doctor);
        return doctor;

    }

    public Doctor getDoctorById(int id) {
        validateInt(id, "ID");
        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor who has this ID not found");
        }

        return doctor;
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors.values());
    }

    public Doctor getDoctorByPhone(String phone) {
        validateString(phone, "Phone");

        Doctor doctor = doctors.values().stream()
                .filter(d -> d.getPhone().equalsIgnoreCase(phone))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        return doctor;
    }

    public List<Doctor> findDoctorBySpecialization(String specialization) {
        validateString(specialization, "Specialization");

        List<Doctor> result = doctors.values().stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .toList();
        return result;

    }

    public List<Doctor> findDoctorByDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        List<Doctor> result = doctors.values().stream()
                .filter(d -> d.getDepartment().equals(department))
                .toList();
        return result;
    }

    public List<Doctor> findDoctorByFirstName(String firstName) {
        validateString(firstName, "First name");

        List<Doctor> result = doctors.values().stream()
                .filter(d -> d.getFirstName().equalsIgnoreCase(firstName))
                .toList();

        return result;
    }

    public List<Doctor> findDoctorByLastName(String lastName) {
        validateString(lastName, "Last name");

        List<Doctor> result = doctors.values().stream()
                .filter(d -> d.getLastName().equalsIgnoreCase(lastName))
                .toList();

        return result;
    }

    public List<Doctor> findDoctorsByFullName(String fullName) {
        validateString(fullName, "Full name");
        String search = fullName.trim().toLowerCase();

        return doctors.values().stream()
                .filter(d -> (d.getFirstName() + " " + d.getLastName()).toLowerCase().contains(search))
                .toList();
    }

    public Doctor updateDoctor(int id, String lastName, String phone, String email, Department department,
                               Double salary, String specialization) {
        validateInt(id, "ID");
        validateString(lastName, "Last name");
        validateString(phone, "Phone");
        validateString(email, "E-mail");
        validateString(specialization, "Specialization");

        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        if (salary == null) {
            throw new IllegalArgumentException("Salary cannot be null");
        }


        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        doctor.setLastName(lastName);
        doctor.setPhone(phone);
        doctor.setEmail(email);
        doctor.setSpecialization(specialization);
        doctor.setDepartment(department);
        doctor.setSalary(salary);
        return doctor;

    }

    public Doctor updateDoctorPhone(int id, String phone) {
        validateInt(id, "ID");
        validateString(phone, "Phone");

        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        doctor.setPhone(phone);
        return doctor;
    }

    public Doctor updateDoctorEmail(int id, String email) {
        validateInt(id, "ID");
        validateString(email, "E-mail");

        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        doctor.setEmail(email);
        return doctor;
    }

    public Doctor removeDoctorById(int id) {
        validateInt(id, "ID");

        Doctor doctor = doctors.get(id);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }

        doctors.remove(id);
        Department department = doctor.getDepartment();
        department.removeStaff(doctor);
        return doctor;
    }

    public void deleteAllDoctors() {
        doctors.clear();

    }

    public boolean existsById(int id) {
        validateInt(id, "ID");
        return doctors.containsKey(id);
    }

    public int getDoctorCount() {
        return doctors.size();
    }
}