package service;

import model.department.Department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentService {
    private Map<String, Department> departments;

    public DepartmentService() {
        this.departments = new HashMap<>();
    }

    public void validateString(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    public Department addDepartment(String name) {
        validateString(name, "Department name");
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

    public Department getDepartmentById(String id) {
        validateString(id, "ID");

        Department department = departments.get(id);
        if (department == null) {
            throw new IllegalArgumentException("Department not found");
        }
        return department;
    }

    public Department getDepartmentByName(String name) {
        validateString(name, "Department name");

        Department department = departments.values().stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        return department;
    }

    public Department updateDepartmentNameById(String id, String newName) {
        validateString(id, "ID");
        validateString(newName, "New name");

        Department department = departments.get(id);
        if (department == null) {
            throw new IllegalArgumentException("Department not found");
        }
        //Duplicate kontrol
        boolean exists = departments.values().stream()
                .anyMatch(d -> !d.getId().equals(id) &&
                        d.getName().equalsIgnoreCase(newName));

        if (exists) {
            throw new IllegalArgumentException("This department already exists");
        }

        department.setName(newName.trim());
        return department;
    }

    private boolean existsByName(String name) {
        validateString(name, "Department name");

        return departments.values()
                .stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(name));
    }

    public boolean existsById(String id) {
        validateString(id, "ID");
        return departments.containsKey(id);
    }

    public Department removeDepartmentById(String id) {
        validateString(id, "ID");

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

    public List<Department> searchDepartmentsByName(String keyword) {
        validateString(keyword, "Keyword");

        return departments.values().stream()
                .filter(d -> d.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

}
