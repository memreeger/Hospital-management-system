package model.person;

import model.department.Department;

public abstract class Staff extends Person {

    protected Department department;
    protected double salary;

    public Staff(String firstName, String lastName, String phone, String email,
                 Department department, double salary) {
        super(firstName, lastName, phone, email);
        this.department = department;
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        this.department = department;
    }

    public void setSalary(Double salary) { // null kontrolü yapmak için Double Wrap giydirdim.
        if (salary == null) {
            throw new IllegalArgumentException("Salary cannot be null");
        }

        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id='" + getId() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", department='" + department.getName() + '\'' +
                ", salary=" + salary +
                '}';
    }
}