package manager;

import model.appointment.Appointment;
import model.department.Department;
import model.enums.BloodType;
import model.person.Doctor;
import model.person.Patient;
import service.AppointmentService;
import service.DepartmentService;
import service.DoctorService;
import service.PatientService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class HospitalManager {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DepartmentService departmentService;
    private final Scanner scanner;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HospitalManager() {
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
        this.appointmentService = new AppointmentService();
        this.departmentService = new DepartmentService();
        this.scanner = new Scanner(System.in);
    }

    public void begin() {
        while (true) {
            System.out.println("\n===== HOSPITAL SYSTEM =====");
            System.out.println("1 - Add Patient");
            System.out.println("2 - Update Patient");
            System.out.println("3 - Delete Patient");
            System.out.println("4 - List Patients");
            System.out.println("5 - Add Doctor");
            System.out.println("6 - Update Doctor");
            System.out.println("7 - Delete Doctor");
            System.out.println("8 - List Doctors");
            System.out.println("9 - Create Appointment");
            System.out.println("10 - Cancel Appointment");
            System.out.println("11 - Complete Appointment");
            System.out.println("12 - List Appointments");
            System.out.println("13 - List Departments");
            System.out.println("0 - Exit");

            System.out.print("Choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
                continue;
            }

            try {
                switch (choice) {
                    case 1 -> addPatientFlow();
                    case 2 -> updatePatientFlow();
                    case 3 -> deletePatientFlow();
                    case 4 -> listPatients();
                    case 5 -> addDoctorFlow();
                    case 6 -> updateDoctorFlow();
                    case 7 -> deleteDoctorFlow();
                    case 8 -> listDoctors();
                    case 9 -> createAppointmentFlow();
                    case 10 -> cancelAppointmentFlow();
                    case 11 -> completeAppointmentFlow();
                    case 12 -> listAppointments();
                    case 13 -> listDepartments();
                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ================== HELPER METHODS ==================

    private int readChoice(int min, int max) {
        while (true) {
            System.out.print("Choose (" + min + "-" + max + "): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid selection. Try again.");
        }
    }

    private Patient selectPatient() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            throw new IllegalStateException("No patients found.");
        }

        System.out.println("\n===== PATIENTS =====");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + " - " + p.getFirstName() + " " + p.getLastName()
                    + " | Number: " + p.getPatientNumber());
        }

        int choice = readChoice(1, patients.size());
        return patients.get(choice - 1);
    }

    private Doctor selectDoctor() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            throw new IllegalStateException("No doctors found.");
        }

        System.out.println("\n===== DOCTORS =====");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            System.out.println((i + 1) + " - " + d.getFirstName() + " " + d.getLastName()
                    + " | ID: " + d.getId()
                    + " | Department: " + d.getDepartment().getName());
        }

        int choice = readChoice(1, doctors.size());
        return doctors.get(choice - 1);
    }

    private Appointment selectAppointment() {
        List<Appointment> appointments = appointmentService.getAllAppointmentsSorted();
        if (appointments.isEmpty()) {
            throw new IllegalStateException("No appointments found.");
        }

        System.out.println("\n===== APPOINTMENTS =====");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            System.out.println((i + 1) + " - "
                    + a.getPatient().getFirstName() + " " + a.getPatient().getLastName()
                    + " | Doctor: " + a.getDoctor().getFirstName() + " " + a.getDoctor().getLastName()
                    + " | Time: " + a.getAppointmentDateTime()
                    + " | Status: " + a.getStatus()
                    + " | ID: " + a.getId());
        }

        int choice = readChoice(1, appointments.size());
        return appointments.get(choice - 1);
    }

    private Department findOrCreateDepartment(String departmentName) {
        if (departmentService.getAllDepartments().isEmpty()) {
            return departmentService.addDepartment(departmentName);
        }

        List<Department> matches = departmentService.searchDepartmentsByName(departmentName);

        for (Department department : matches) {
            if (department.getName().equalsIgnoreCase(departmentName.trim())) {
                return department;
            }
        }

        return departmentService.addDepartment(departmentName);
    }

    // ================== PATIENT FLOWS ==================

    private void addPatientFlow() {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Patient Number: ");
        String patientNumber = scanner.nextLine();
        System.out.print("Blood Type (A+, O-, etc.): ");
        BloodType bloodType = BloodType.fromLabel(scanner.nextLine());
        System.out.print("Emergency Contact: ");
        String emergencyContact = scanner.nextLine();

        Patient patient = patientService.addPatient(
                firstName, lastName, phone, email,
                patientNumber, bloodType, emergencyContact
        );
        System.out.println("Added Patient: " + patient);
    }

    private void updatePatientFlow() {
        Patient patient = selectPatient();

        System.out.print("New First Name (" + patient.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        System.out.print("New Last Name (" + patient.getLastName() + "): ");
        String lastName = scanner.nextLine();
        System.out.print("New Phone (" + patient.getPhone() + "): ");
        String phone = scanner.nextLine();
        System.out.print("New Email (" + patient.getEmail() + "): ");
        String email = scanner.nextLine();
        System.out.print("New Blood Type (" + patient.getBloodType() + "): ");
        String blood = scanner.nextLine();
        BloodType bloodType = blood.isBlank() ? patient.getBloodType() : BloodType.fromLabel(blood);
        System.out.print("New Emergency Contact (" + patient.getEmergencyContact() + "): ");
        String emergencyContact = scanner.nextLine();

        patientService.updatePatient(
                firstName,
                lastName,
                phone,
                email,
                patient.getPatientNumber(),
                bloodType,
                emergencyContact,
                patient.getPatientNumber()
        );

        System.out.println("Updated Patient: " + patient);
    }

    private void deletePatientFlow() {
        Patient patient = selectPatient();
        Patient removed = patientService.deletePatient(patient.getPatientNumber());
        System.out.println("Deleted Patient: " + removed);
    }

    private void listPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            patients.forEach(System.out::println);
        }
    }

    // ================== DOCTOR FLOWS ==================

    private void addDoctorFlow() {
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Department Name: ");
        String depName = scanner.nextLine();

        Department department = findOrCreateDepartment(depName);

        System.out.print("Salary: ");
        double salary = Double.parseDouble(scanner.nextLine());
        System.out.print("Specialization: ");
        String specialization = scanner.nextLine();

        Doctor doctor = doctorService.addDoctor(
                firstName, lastName, phone, email,
                department, salary, specialization
        );
        System.out.println("Added Doctor: " + doctor);
    }

    private void updateDoctorFlow() {
        Doctor doctor = selectDoctor();

        System.out.print("New Last Name (" + doctor.getLastName() + "): ");
        String lastName = scanner.nextLine();
        System.out.print("New Phone (" + doctor.getPhone() + "): ");
        String phone = scanner.nextLine();
        System.out.print("New Email (" + doctor.getEmail() + "): ");
        String email = scanner.nextLine();
        System.out.print("New Department Name (" + doctor.getDepartment().getName() + "): ");
        String depName = scanner.nextLine();

        Department department = depName.isBlank()
                ? doctor.getDepartment()
                : findOrCreateDepartment(depName);

        System.out.print("New Salary (" + doctor.getSalary() + "): ");
        String salaryInput = scanner.nextLine();
        double salary = salaryInput.isBlank() ? doctor.getSalary() : Double.parseDouble(salaryInput);

        System.out.print("New Specialization (" + doctor.getSpecialization() + "): ");
        String specialization = scanner.nextLine();

        doctorService.updateDoctor(
                doctor.getId(),
                lastName.isBlank() ? doctor.getLastName() : lastName,
                phone.isBlank() ? doctor.getPhone() : phone,
                email.isBlank() ? doctor.getEmail() : email,
                department,
                salary,
                specialization.isBlank() ? doctor.getSpecialization() : specialization
        );

        System.out.println("Updated Doctor: " + doctor);
    }

    private void deleteDoctorFlow() {
        Doctor doctor = selectDoctor();
        Doctor removed = doctorService.removeDoctorById(doctor.getId());
        System.out.println("Deleted Doctor: " + removed);
    }

    private void listDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            doctors.forEach(System.out::println);
        }
    }

    private void listDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
        } else {
            departments.forEach(System.out::println);
        }
    }

    // ================== APPOINTMENT FLOWS ==================

    private void createAppointmentFlow() {
        Patient patient = selectPatient();
        Doctor doctor = selectDoctor();

        System.out.print("Reason: ");
        String reason = scanner.nextLine();
        System.out.print("Appointment Date & Time (yyyy-MM-dd HH:mm): ");
        String timeStr = scanner.nextLine();

        LocalDateTime time;
        try {
            time = LocalDateTime.parse(timeStr, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        Appointment appointment = appointmentService.createAppointment(
                patient,
                doctor,
                time,
                reason
        );

        System.out.println("Created Appointment: " + appointment);
    }

    private void cancelAppointmentFlow() {
        Appointment appointment = selectAppointment();
        Appointment cancelled = appointmentService.cancelAppointment(appointment.getId());
        System.out.println("Cancelled Appointment: " + cancelled);
    }

    private void completeAppointmentFlow() {
        Appointment appointment = selectAppointment();
        Appointment completed = appointmentService.completeAppointment(appointment.getId());
        System.out.println("Completed Appointment: " + completed);
    }

    private void listAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointmentsSorted();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            appointments.forEach(System.out::println);
        }
    }
}