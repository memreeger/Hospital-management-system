package model.person;

import model.department.Department;

public class Doctor extends Staff {

    private String specialization;

    public Doctor(String firstName, String lastName, String phone, String email,
                  Department department, double salary, String specialization) {
        super(firstName, lastName, phone, email, department, salary);

        if (specialization == null || specialization.isBlank()) { // model içinde validasyon !!!!
            throw new IllegalArgumentException("Specialization cannot be null or blank");
        }
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.isBlank()) {
            throw new IllegalArgumentException("Specialization cannot be null or blank");
        }
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", department='" + getDepartment().getName() + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}