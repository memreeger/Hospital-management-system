package model.department;

import model.person.Staff;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Department {

    private String id;
    private String name;
    private List<Staff> staffList;


    public Department(String name) {
        validateString(name, "Name");

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.staffList = new ArrayList<>();
    }
    //VALIDATION HELPER

    public void validateString(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }

    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Staff> getStaffList() {
        return new ArrayList<>(staffList);
    }

    public void setName(String name) {
        validateString(name, "Name");
        this.name = name;
    }

    public void addStaff(Staff staff) {
        if (staff != null) {
            staffList.add(staff);
        }
    }

    public void removeStaff(Staff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        staffList.remove(staff);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", staffCount=" + staffList.size() +
                '}';
    }
}