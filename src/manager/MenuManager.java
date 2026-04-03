package manager;

import model.appointment.Appointment;
import model.department.Department;
import model.enums.BloodType;
import model.person.Doctor;
import model.person.Patient;
import service.AppointmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuManager {

    private final Scanner scanner;
    private final AppointmentService appointmentService;

    // test verileri
    private final Patient patient1;
    private final Patient patient2;
    private final Doctor doctor1;
    private final Doctor doctor2;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MenuManager() {
        this.scanner = new Scanner(System.in);
        this.appointmentService = new AppointmentService();

        Department cardiology = new Department("D001", "Cardiology");
        Department neurology = new Department("D002", "Neurology");

        this.patient1 = new Patient("Cemre", "Tekin", "1111111111", "cemre@mail.com",
                "P001", BloodType.A_POSITIVE, "Emmi");
        this.patient2 = new Patient("Ayse", "Yilmaz", "2222222222", "ayse@mail.com",
                "P002", BloodType.A_NEGATIVE, "Fatma");

        this.doctor1 = new Doctor("Ahmet", "Kaya", "3333333333", "ahmet@mail.com",
                cardiology, 80000, "Cardiologist");
        this.doctor2 = new Doctor("Mehmet", "Demir", "4444444444", "mehmet@mail.com",
                neurology, 85000, "Neurologist");
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> createAppointment();
                    case "2" -> cancelAppointment();
                    case "3" -> completeAppointment();
                    case "4" -> rescheduleAppointment();
                    case "5" -> listAllAppointments();
                    case "6" -> listActiveAppointments();
                    case "7" -> listAppointmentsByDate();
                    case "8" -> searchAppointmentsByReason();
                    case "9" -> listAppointmentsByPatient();
                    case "10" -> listAppointmentsByDoctor();
                    case "0" -> {
                        running = false;
                        System.out.println("Exiting system...");
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("===== HOSPITAL APPOINTMENT MENU =====");
        System.out.println("1 - Create Appointment");
        System.out.println("2 - Cancel Appointment");
        System.out.println("3 - Complete Appointment");
        System.out.println("4 - Reschedule Appointment");
        System.out.println("5 - List All Appointments");
        System.out.println("6 - List Active Appointments");
        System.out.println("7 - List Appointments By Date");
        System.out.println("8 - Search Appointments By Reason");
        System.out.println("9 - List Appointments By Patient");
        System.out.println("10 - List Appointments By Doctor");
        System.out.println("0 - Exit");
        System.out.print("Choose: ");
    }

    private void createAppointment() {
        Patient patient = selectPatient();
        Doctor doctor = selectDoctor();

        System.out.print("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
        String dateTimeInput = scanner.nextLine();
        LocalDateTime appointmentTime = LocalDateTime.parse(dateTimeInput, dateTimeFormatter);

        System.out.print("Enter reason: ");
        String reason = scanner.nextLine();

        Appointment appointment = appointmentService.createAppointment(patient, doctor, appointmentTime, reason);
        System.out.println("Appointment created successfully:");
        System.out.println(appointment);
    }

    private void cancelAppointment() {
        listAllAppointments();

        System.out.print("Enter appointment id to cancel: ");
        String appointmentId = scanner.nextLine();

        Appointment appointment = appointmentService.cancelAppointment(appointmentId);
        System.out.println("Appointment cancelled successfully:");
        System.out.println(appointment);
    }

    private void completeAppointment() {
        listAllAppointments();

        System.out.print("Enter appointment id to complete: ");
        String appointmentId = scanner.nextLine();

        Appointment appointment = appointmentService.completeAppointment(appointmentId);
        System.out.println("Appointment completed successfully:");
        System.out.println(appointment);
    }

    private void rescheduleAppointment() {
        listAllAppointments();

        System.out.print("Enter appointment id to reschedule: ");
        String appointmentId = scanner.nextLine();

        System.out.print("Enter new date and time (yyyy-MM-dd HH:mm): ");
        String newTimeInput = scanner.nextLine();
        LocalDateTime newTime = LocalDateTime.parse(newTimeInput, dateTimeFormatter);

        Appointment appointment = appointmentService.rescheduleAppointment(appointmentId, newTime);
        System.out.println("Appointment rescheduled successfully:");
        System.out.println(appointment);
    }

    private void listAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointmentsSorted();

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        System.out.println("===== ALL APPOINTMENTS =====");
        appointments.forEach(System.out::println);
    }

    private void listActiveAppointments() {
        List<Appointment> appointments = appointmentService.getActiveAppointments();

        if (appointments.isEmpty()) {
            System.out.println("No active appointments found.");
            return;
        }

        System.out.println("===== ACTIVE APPOINTMENTS =====");
        appointments.forEach(System.out::println);
    }

    private void listAppointmentsByDate() {
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateInput, dateFormatter);

        List<Appointment> appointments = appointmentService.getAppointmentsByDate(date);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found for this date.");
            return;
        }

        System.out.println("===== APPOINTMENTS ON " + date + " =====");
        appointments.forEach(System.out::println);
    }

    private void searchAppointmentsByReason() {
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();

        List<Appointment> appointments = appointmentService.findAppointmentsByReason(keyword);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found for this keyword.");
            return;
        }

        System.out.println("===== SEARCH RESULTS =====");
        appointments.forEach(System.out::println);
    }

    private void listAppointmentsByPatient() {
        Patient patient = selectPatient();
        List<Appointment> appointments = appointmentService.getAllAppointmentsByPatient(patient);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found for this patient.");
            return;
        }

        System.out.println("===== APPOINTMENTS OF PATIENT =====");
        appointments.forEach(System.out::println);
    }

    private void listAppointmentsByDoctor() {
        Doctor doctor = selectDoctor();
        List<Appointment> appointments = appointmentService.getAllAppointmentsByDoctor(doctor);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found for this doctor.");
            return;
        }

        System.out.println("===== APPOINTMENTS OF DOCTOR =====");
        appointments.forEach(System.out::println);
    }

    private Patient selectPatient() {
        System.out.println("Select patient:");
        System.out.println("1 - " + patient1.getFullName());
        System.out.println("2 - " + patient2.getFullName());
        System.out.print("Choose: ");

        String choice = scanner.nextLine();

        return switch (choice) {
            case "1" -> patient1;
            case "2" -> patient2;
            default -> throw new IllegalArgumentException("Invalid patient choice");
        };
    }

    private Doctor selectDoctor() {
        System.out.println("Select doctor:");
        System.out.println("1 - " + doctor1.getFullName());
        System.out.println("2 - " + doctor2.getFullName());
        System.out.print("Choose: ");

        String choice = scanner.nextLine();

        return switch (choice) {
            case "1" -> doctor1;
            case "2" -> doctor2;
            default -> throw new IllegalArgumentException("Invalid doctor choice");
        };
    }
}