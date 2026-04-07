package manager;

import model.admission.Admission;
import model.admission.Room;
import model.department.Department;
import model.enums.BloodType;
import model.enums.RoomStatus;
import model.enums.RoomType;
import model.person.Doctor;
import model.person.Patient;
import model.appointment.Appointment;
import service.AdmissionService;
import service.AppointmentService;
import service.DepartmentService;
import service.DoctorService;
import service.PatientService;
import service.RoomService;

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
    private final RoomService roomService;
    private final AdmissionService admissionService;

    private final Scanner scanner;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public HospitalManager() {
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
        this.appointmentService = new AppointmentService();
        this.departmentService = new DepartmentService();
        this.roomService = new RoomService();
        this.admissionService = new AdmissionService(roomService);
        this.scanner = new Scanner(System.in);

        seedRooms();
    }

    public void begin() {
        while (true) {
            System.out.println("\n===== HOSPITAL SYSTEM =====");
            System.out.println("1 - Manage Patients");
            System.out.println("2 - Manage Doctors");
            System.out.println("3 - Manage Appointments");
            System.out.println("4 - Manage Rooms");
            System.out.println("5 - Manage Admissions");
            System.out.println("6 - List Departments");
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
                    case 1 -> patientMenu();
                    case 2 -> doctorMenu();
                    case 3 -> appointmentMenu();
                    case 4 -> roomMenu();
                    case 5 -> admissionMenu();
                    case 6 -> listDepartments();
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

    // ===================== SEED =====================

    private void seedRooms() {
        if (roomService.getRoomCount() > 0) {
            return;
        }

        roomService.addRoom(101, RoomType.STANDARD);
        roomService.addRoom(102, RoomType.STANDARD);
        roomService.addRoom(201, RoomType.DELUXE);
        roomService.addRoom(202, RoomType.DELUXE);
        roomService.addRoom(301, RoomType.PRIVATE);
        roomService.addRoom(401, RoomType.SHARED);
    }

    // ===================== COMMON HELPERS =====================

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

    private Department findOrCreateDepartment(String departmentName) {
        List<Department> matches = departmentService.searchDepartmentsByName(departmentName);

        for (Department department : matches) {
            if (department.getName().equalsIgnoreCase(departmentName.trim())) {
                return department;
            }
        }

        return departmentService.addDepartment(departmentName);
    }

    // ===================== PATIENT HELPERS =====================

    private Patient selectPatient() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            throw new IllegalStateException("No patients found.");
        }

        System.out.println("\n===== PATIENTS =====");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            System.out.println((i + 1) + " - " + p.getFullName() + " | Patient No: " + p.getPatientNumber());
        }

        int choice = readChoice(1, patients.size());
        return patients.get(choice - 1);
    }

    // ===================== DOCTOR HELPERS =====================

    private Doctor selectDoctor() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            throw new IllegalStateException("No doctors found.");
        }

        System.out.println("\n===== DOCTORS =====");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            System.out.println((i + 1) + " - " + d.getFullName()
                    + " | ID: " + d.getId()
                    + " | Department: " + d.getDepartment().getName());
        }

        int choice = readChoice(1, doctors.size());
        return doctors.get(choice - 1);
    }

    // ===================== APPOINTMENT HELPERS =====================

    private Appointment selectAppointment() {
        List<Appointment> appointments = appointmentService.getAllAppointmentsSorted();
        if (appointments.isEmpty()) {
            throw new IllegalStateException("No appointments found.");
        }

        System.out.println("\n===== APPOINTMENTS =====");
        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            System.out.println((i + 1) + " - "
                    + a.getPatient().getFullName()
                    + " | Doctor: " + a.getDoctor().getFullName()
                    + " | Time: " + a.getAppointmentDateTime()
                    + " | Status: " + a.getStatus());
        }

        int choice = readChoice(1, appointments.size());
        return appointments.get(choice - 1);
    }

    // ===================== ROOM HELPERS =====================

    private Room selectRoom() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            throw new IllegalStateException("No rooms found.");
        }

        System.out.println("\n===== ROOMS =====");
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.println((i + 1) + " - Room " + room.getRoomNumber()
                    + " | Type: " + room.getRoomType()
                    + " | Status: " + room.getRoomStatus()
                    + " | Daily Rate: " + room.getDailyRate());
        }

        int choice = readChoice(1, rooms.size());
        return rooms.get(choice - 1);
    }

    private Room selectAvailableRoomByType(RoomType roomType) {
        List<Room> rooms = roomService.getAvailableRoomsByType(roomType);
        if (rooms.isEmpty()) {
            throw new IllegalStateException("No available rooms found for type: " + roomType);
        }

        System.out.println("\n===== AVAILABLE " + roomType + " ROOMS =====");
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.println((i + 1) + " - Room " + room.getRoomNumber()
                    + " | Status: " + room.getRoomStatus()
                    + " | Daily Rate: " + room.getDailyRate());
        }

        int choice = readChoice(1, rooms.size());
        return rooms.get(choice - 1);
    }

    // ===================== ADMISSION HELPERS =====================

    private Admission selectActiveAdmission() {
        List<Admission> admissions = admissionService.getActiveAdmissions();
        if (admissions.isEmpty()) {
            throw new IllegalStateException("No active admissions found.");
        }

        System.out.println("\n===== ACTIVE ADMISSIONS =====");
        for (int i = 0; i < admissions.size(); i++) {
            Admission admission = admissions.get(i);
            System.out.println((i + 1) + " - "
                    + admission.getPatient().getFullName()
                    + " | Room: " + admission.getRoom().getRoomNumber()
                    + " | Date: " + admission.getAdmissionDate()
                    + " | Status: " + admission.getStatus());
        }

        int choice = readChoice(1, admissions.size());
        return admissions.get(choice - 1);
    }

    // ===================== PATIENT MENU =====================

    private void patientMenu() {
        System.out.println("\n===== PATIENT MENU =====");
        System.out.println("1 - Add Patient");
        System.out.println("2 - Update Patient");
        System.out.println("3 - Delete Patient");
        System.out.println("4 - List Patients");
        System.out.println("0 - Back");

        int choice = readChoice(0, 4);

        switch (choice) {
            case 1 -> addPatientFlow();
            case 2 -> updatePatientFlow();
            case 3 -> deletePatientFlow();
            case 4 -> listPatients();
            case 0 -> { }
        }
    }

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

        System.out.print("Blood Type (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
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

    // ===================== DOCTOR MENU =====================

    private void doctorMenu() {
        System.out.println("\n===== DOCTOR MENU =====");
        System.out.println("1 - Add Doctor");
        System.out.println("2 - Update Doctor");
        System.out.println("3 - Delete Doctor");
        System.out.println("4 - List Doctors");
        System.out.println("0 - Back");

        int choice = readChoice(0, 4);

        switch (choice) {
            case 1 -> addDoctorFlow();
            case 2 -> updateDoctorFlow();
            case 3 -> deleteDoctorFlow();
            case 4 -> listDoctors();
            case 0 -> { }
        }
    }

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

    // ===================== APPOINTMENT MENU =====================

    private void appointmentMenu() {
        System.out.println("\n===== APPOINTMENT MENU =====");
        System.out.println("1 - Create Appointment");
        System.out.println("2 - Cancel Appointment");
        System.out.println("3 - Complete Appointment");
        System.out.println("4 - Reschedule Appointment");
        System.out.println("5 - List All Appointments");
        System.out.println("6 - List Active Appointments");
        System.out.println("0 - Back");

        int choice = readChoice(0, 6);

        switch (choice) {
            case 1 -> createAppointmentFlow();
            case 2 -> cancelAppointmentFlow();
            case 3 -> completeAppointmentFlow();
            case 4 -> rescheduleAppointmentFlow();
            case 5 -> listAppointments();
            case 6 -> listActiveAppointments();
            case 0 -> { }
        }
    }

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

        Appointment appointment = appointmentService.createAppointment(patient, doctor, time, reason);
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

    private void rescheduleAppointmentFlow() {
        Appointment appointment = selectAppointment();

        System.out.print("New Appointment Date & Time (yyyy-MM-dd HH:mm): ");
        String timeStr = scanner.nextLine();

        LocalDateTime newTime;
        try {
            newTime = LocalDateTime.parse(timeStr, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        Appointment updated = appointmentService.rescheduleAppointment(appointment.getId(), newTime);
        System.out.println("Rescheduled Appointment: " + updated);
    }

    private void listAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointmentsSorted();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            appointments.forEach(System.out::println);
        }
    }

    private void listActiveAppointments() {
        List<Appointment> appointments = appointmentService.getActiveAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No active appointments found.");
        } else {
            appointments.forEach(System.out::println);
        }
    }

    // ===================== ROOM MENU =====================

    private void roomMenu() {
        System.out.println("\n===== ROOM MENU =====");
        System.out.println("1 - Add Room");
        System.out.println("2 - List All Rooms");
        System.out.println("3 - List Available Rooms");
        System.out.println("4 - List Rooms By Status");
        System.out.println("5 - Remove Room");
        System.out.println("6 - Show Room Capacity Info");
        System.out.println("0 - Back");

        int choice = readChoice(0, 6);

        switch (choice) {
            case 1 -> addRoomFlow();
            case 2 -> listAllRoomsFlow();
            case 3 -> listAvailableRoomsFlow();
            case 4 -> listRoomsByStatusFlow();
            case 5 -> removeRoomFlow();
            case 6 -> showRoomCapacityInfo();
            case 0 -> { }
        }
    }

    private void addRoomFlow() {
        System.out.print("Room Number: ");
        int roomNumber = Integer.parseInt(scanner.nextLine());

        System.out.println("Room Type Options:");
        RoomType[] roomTypes = RoomType.values();
        for (int i = 0; i < roomTypes.length; i++) {
            System.out.println((i + 1) + " - " + roomTypes[i] + " | Default Rate: " + roomTypes[i].getDefaultRate());
        }

        int choice = readChoice(1, roomTypes.length);
        RoomType roomType = roomTypes[choice - 1];

        Room room = roomService.addRoom(roomNumber, roomType);
        System.out.println("Added Room: " + room);
    }

    private void listAllRoomsFlow() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private void listAvailableRoomsFlow() {
        List<Room> rooms = roomService.getAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("No available rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private void listRoomsByStatusFlow() {
        RoomStatus[] statuses = RoomStatus.values();

        System.out.println("Room Status Options:");
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + " - " + statuses[i]);
        }

        int choice = readChoice(1, statuses.length);
        RoomStatus selectedStatus = statuses[choice - 1];

        List<Room> rooms = roomService.getRoomsByStatus(selectedStatus);
        if (rooms.isEmpty()) {
            System.out.println("No rooms found with status: " + selectedStatus);
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private void removeRoomFlow() {
        Room room = selectRoom();
        Room removed = roomService.removeRoomById(room.getId());
        System.out.println("Removed Room: " + removed);
    }

    private void showRoomCapacityInfo() {
        System.out.println("Max Capacity: " + roomService.getMaxCapacity());
        System.out.println("Total Rooms: " + roomService.getRoomCount());
        System.out.println("Available Rooms: " + roomService.getAvailableRoomCount());
    }

    // ===================== ADMISSION MENU =====================

    private void admissionMenu() {
        System.out.println("\n===== ADMISSION MENU =====");
        System.out.println("1 - Admit Patient");
        System.out.println("2 - Discharge Patient");
        System.out.println("3 - Transfer Patient");
        System.out.println("4 - List Active Admissions");
        System.out.println("5 - List Discharged Admissions");
        System.out.println("6 - List Admissions By Patient");
        System.out.println("7 - List Admissions By Room");
        System.out.println("8 - List Active Admissions By Room Type");
        System.out.println("9 - Admission Statistics");
        System.out.println("0 - Back");

        int choice = readChoice(0, 9);

        switch (choice) {
            case 1 -> admitPatientFlow();
            case 2 -> dischargePatientFlow();
            case 3 -> transferPatientFlow();
            case 4 -> listActiveAdmissionsFlow();
            case 5 -> listDischargedAdmissionsFlow();
            case 6 -> listAdmissionsByPatientFlow();
            case 7 -> listAdmissionsByRoomFlow();
            case 8 -> listActiveAdmissionsByRoomTypeFlow();
            case 9 -> showAdmissionStats();
            case 0 -> { }
        }
    }

    private void admitPatientFlow() {
        Patient patient = selectPatient();

        System.out.println("Room Type Options:");
        RoomType[] roomTypes = RoomType.values();
        for (int i = 0; i < roomTypes.length; i++) {
            System.out.println((i + 1) + " - " + roomTypes[i]);
        }

        int typeChoice = readChoice(1, roomTypes.length);
        RoomType selectedType = roomTypes[typeChoice - 1];

        Room room = selectAvailableRoomByType(selectedType);

        Admission admission = admissionService.admitPatient(patient, room);
        System.out.println("Patient admitted: " + admission);
    }

    private void dischargePatientFlow() {
        Admission admission = selectActiveAdmission();
        Admission discharged = admissionService.dischargePatient(admission.getId());
        System.out.println("Patient discharged: " + discharged);
    }

    private void transferPatientFlow() {
        Admission admission = selectActiveAdmission();

        System.out.println("Room Type Options:");
        RoomType[] roomTypes = RoomType.values();
        for (int i = 0; i < roomTypes.length; i++) {
            System.out.println((i + 1) + " - " + roomTypes[i]);
        }

        int typeChoice = readChoice(1, roomTypes.length);
        RoomType selectedType = roomTypes[typeChoice - 1];

        Room newRoom = selectAvailableRoomByType(selectedType);

        admissionService.transferPatient(admission.getId(), newRoom);
        System.out.println("Patient transferred successfully.");
    }

    private void listActiveAdmissionsFlow() {
        List<Admission> admissions = admissionService.getActiveAdmissions();
        if (admissions.isEmpty()) {
            System.out.println("No active admissions found.");
        } else {
            admissions.forEach(System.out::println);
        }
    }

    private void listDischargedAdmissionsFlow() {
        List<Admission> admissions = admissionService.getDischargedAdmissions();
        if (admissions.isEmpty()) {
            System.out.println("No discharged admissions found.");
        } else {
            admissions.forEach(System.out::println);
        }
    }

    private void listAdmissionsByPatientFlow() {
        Patient patient = selectPatient();
        List<Admission> admissions = admissionService.getAdmissionsByPatient(patient);

        if (admissions.isEmpty()) {
            System.out.println("No admissions found for patient: " + patient.getFullName());
        } else {
            admissions.forEach(System.out::println);
        }
    }

    private void listAdmissionsByRoomFlow() {
        Room room = selectRoom();
        List<Admission> admissions = admissionService.getAdmissionsByRoom(room);

        if (admissions.isEmpty()) {
            System.out.println("No admissions found for room: " + room.getRoomNumber());
        } else {
            admissions.forEach(System.out::println);
        }
    }

    private void listActiveAdmissionsByRoomTypeFlow() {
        RoomType[] roomTypes = RoomType.values();

        System.out.println("Room Type Options:");
        for (int i = 0; i < roomTypes.length; i++) {
            System.out.println((i + 1) + " - " + roomTypes[i]);
        }

        int choice = readChoice(1, roomTypes.length);
        RoomType selectedType = roomTypes[choice - 1];

        List<Admission> admissions = admissionService.getActiveAdmissionsByRoomType(selectedType);

        if (admissions.isEmpty()) {
            System.out.println("No active admissions found for room type: " + selectedType);
        } else {
            admissions.forEach(System.out::println);
        }
    }

    private void showAdmissionStats() {
        System.out.println("Total Admissions: " + admissionService.getTotalAdmissionCount());
        System.out.println("Active Admissions: " + admissionService.getActiveAdmissionCount());
        System.out.println("Discharged Admissions: " + admissionService.getDischargedAdmissionCount());
    }

    // ===================== DEPARTMENT =====================

    private void listDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
        } else {
            departments.forEach(System.out::println);
        }
    }
}