package model.person;

import model.department.Department;

public class Receptionist extends Staff {

    public Receptionist(String firstName, String lastName, String phone, String email,
                        Department department, double salary) {
        super(firstName, lastName, phone, email, department, salary);
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "id='" + id + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", department='" + (department != null ? department.getName() : "N/A") + '\'' +
                '}';
    }
}