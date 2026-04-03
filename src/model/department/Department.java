package model.department;

import model.person.Staff;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Department {

    private String id;
    private String name;
    private List<Staff> staffList;

    public Department(String id, String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.staffList = new ArrayList<>();
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
        this.name = name;
    }

    public void addStaff(Staff staff) {
        if (staff != null) {
            staffList.add(staff);
        }
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