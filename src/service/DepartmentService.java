package service;

import model.department.Department;
import util.ValidationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentService {
    private Map<Integer, Department> departments;

    public DepartmentService() {
        this.departments = new HashMap<>();
    }


    public Department addDepartment(String name) {
        ValidationUtil.requireNonBlank(name, "Name");
        if (existsByName(name)) {
            throw new IllegalArgumentException("This department already exists");
        }

        Department department = new Department(name);
        departments.put(department.getId(), department);
        return department;
    }

    public List<Department> getAllDepartments() {
        return new ArrayList<>(departments.values());
    }

    public Department getDepartmentById(int id) {
        ValidationUtil.requireNonNegative(id, "ID");

        Department department = departments.get(id);
        if (department == null) {
            throw new IllegalArgumentException("Department not found");
        }
        return department;
    }

    public Department getDepartmentByName(String name) {
        ValidationUtil.requireNonBlank(name, "Name");

        Department department = departments.values().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        return department;
    }

    public Department updateDepartmentNameById(int id, String newName) {
        ValidationUtil.requireNonNegative(id, "ID");
        ValidationUtil.requireNonBlank(newName, "New name");

        Department department = departments.get(id);
        if (department == null) {
            throw new IllegalArgumentException("Department not found");
        }
        //Duplicate kontrol
        boolean exists = departments.values().stream()
                .anyMatch(d -> d.getId() != (id) &&
                        d.getName().equalsIgnoreCase(newName));

        if (exists) {
            throw new IllegalArgumentException("This department already exists");
        }

        department.setName(newName.trim());
        return department;
    }

    public boolean existsByName(String name) {
        ValidationUtil.requireNonBlank(name, "Name");

        return departments.values()
                .stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(name));
    }

    public boolean existsById(int id) {
        ValidationUtil.requirePositive(id, "ID");
        return departments.containsKey(id);
    }

    public Department removeDepartmentById(int id) {
        ValidationUtil.requirePositive(id, "ID");
        Department department = departments.get(id);

        if (department == null) {
            throw new IllegalArgumentException("Department not found");
        }

        departments.remove(id);
        return department;
    }

    public void deleteAllDepartments() {
        departments.clear();
    }

    public int getDepartmentCount() {
        return departments.size();
    }

    public List<Department> searchDepartmentsByName(String departmentName) {
        ValidationUtil.requireNonBlank(departmentName, "Department name");
        return departments.values().stream()
                .filter(d -> d.getName().toLowerCase().contains(departmentName.toLowerCase()))
                .toList();
    }

}
