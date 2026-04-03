package model.person;

import model.department.Department;

public class Doctor extends Staff {

    private String specialization;

    public Doctor(String firstName, String lastName, String phone, String email,
                  Department department, double salary, String specialization) {
        super(firstName, lastName, phone, email, department, salary);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", department='" + (department != null ? department.getName() : "N/A") + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
}