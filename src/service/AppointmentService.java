package service;

import model.appointment.Appointment;
import model.enums.AppointmentStatus;
import model.person.Doctor;
import model.person.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Appointment management service !!! .
 * * @author Muhammed Emre Eğer
 *
 * @version 1.0
 */

public class AppointmentService {
    private Map<Integer, Appointment> appointments;

    //Helper
    public void validateIntId(int id, String fieldName) {
        if (id < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be smaller than zero");
        }
    }

    public void validateString(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    public AppointmentService() {
        this.appointments = new HashMap<>();
    }

    private boolean isSameSlot(LocalDateTime t1, LocalDateTime t2) {
        return !t1.isBefore(t2.minusMinutes(30)) &&
                !t1.isAfter(t2.plusMinutes(30));
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime, String reason) {
        Objects.requireNonNull(patient, "Patient cannot be null");
        Objects.requireNonNull(doctor, "Doctor cannot be null");

        if (appointmentTime == null) {
            throw new IllegalArgumentException("Appointment time cannot be null");
        }
        validateString(reason, "Reason");


        if (appointmentTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("You cannot make appointment in the past");
        }

        boolean isPatientBusy = appointments.values().stream()
                .anyMatch(a ->
                        a.isActive() &&
                                a.getPatient().getId() == (patient.getId()) &&
                                isSameSlot(a.getAppointmentDateTime(), appointmentTime)
                );

        if (isPatientBusy) {
            throw new IllegalStateException("Patient already has an appointment at this time");
        }

        boolean isDoctorBusy = appointments.values().stream()
                .anyMatch(a ->
                        a.isActive() &&
                                a.getDoctor().getId() == (doctor.getId()) &&
                                isSameSlot(a.getAppointmentDateTime(), appointmentTime)
                );

        if (isDoctorBusy) {
            throw new IllegalStateException("Doctor already has an appointment at this time");
        }

        Appointment appointment = new Appointment(patient, doctor, appointmentTime, reason);
        appointments.put(appointment.getId(), appointment);

        return appointment;

    }

    /**
     * Verilen ID'ye göre randevu iptal eder.
     * * @appointmentId id Kullanıcının benzersiz numarası
     *
     * @return Kullanıcı adı, bulunamazsa null
     */
    public Appointment cancelAppointment(int appointmentId) {
        validateIntId(appointmentId, "Appointment id");

        Appointment appointment = appointments.get(appointmentId);

        Objects.requireNonNull(appointment, "Appointment cannot be null");

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("This appointment is already cancelled");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("This appointment is already completed");
        }

        appointment.cancel();
        return appointment;
    }

    public List<Appointment> getActiveAppointmentsByPatient(Patient patient) {
        Objects.requireNonNull(patient, "Patient cannot be null");

        List<Appointment> appointmentsByPatient = appointments.values().stream()
                .filter(a -> a.isActive() &&
                        a.getPatient().getId() == (patient.getId()))
                .toList();
        return appointmentsByPatient;
    }

    public List<Appointment> getActiveAppointmentsByDoctor(Doctor doctor) {
        Objects.requireNonNull(doctor, "Doctor cannot be null");

        List<Appointment> appointmentsByDoctor = appointments.values().stream()
                .filter(a -> a.isActive() &&
                        a.getDoctor().getId() == (doctor.getId()))
                .toList();

        return appointmentsByDoctor;
    }

    public List<Appointment> getAllAppointmentsByPatient(Patient patient) {
        Objects.requireNonNull(patient, "Patient cannot be null");

        List<Appointment> appointmentsByPatient = appointments.values().stream()
                .filter(a -> a.getPatient().getId() == (patient.getId()))
                .toList();
        return appointmentsByPatient;
    }

    public List<Appointment> getAllAppointmentsByDoctor(Doctor doctor) {
        Objects.requireNonNull(doctor, "Doctor cannot be null");

        List<Appointment> appointmentsByDoctor = appointments.values().stream()
                .filter(a -> a.getDoctor().getId() == (doctor.getId()))
                .toList();

        return appointmentsByDoctor;
    }

    public Appointment completeAppointment(int appointmentId) {
        validateIntId(appointmentId, "Appointment id");
        Appointment appointment = appointments.get(appointmentId);

        Objects.requireNonNull(appointment, "Appointment cannot be null");

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("This appointment is already completed");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled appointment cannot be completed");
        }

        appointment.complete();
        return appointment;
    }

    public Appointment rescheduleAppointment(int appointmentId, LocalDateTime newTime) {
        validateIntId(appointmentId, "Appointment id");

        if (newTime == null) {
            throw new IllegalArgumentException("Time field cannot be null");
        }

        Appointment appointment = appointments.get(appointmentId);
        Objects.requireNonNull(appointment, "Appointment cannot be null");

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled appointments can be rescheduled");
        }

        if (newTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reschedule to the past");
        }

        boolean isPatientBusy = appointments.values().stream()
                .anyMatch(a ->
                        a.isActive() &&
                                a.getPatient().getId() == appointment.getPatient().getId() &&
                                isSameSlot(a.getAppointmentDateTime(), newTime) &&
                                a.getId() != appointmentId
                );
        if (isPatientBusy) {
            throw new IllegalStateException("Patient already has an appointment at this time");
        }

        boolean isDoctorBusy = appointments.values().stream()
                .anyMatch(a -> a.isActive() &&
                        a.getDoctor().getId() == (appointment.getDoctor().getId()) &&
                        isSameSlot(a.getAppointmentDateTime(), newTime) &&
                        a.getId() != (appointmentId)
                );
        if (isDoctorBusy) {
            throw new IllegalStateException("Doctor already has an appointment at this time");
        }

        appointment.setAppointmentDateTime(newTime);
        return appointment;

    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return appointments.values().stream()
                .filter(a -> a.getStatus() == status)
                .toList();
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return appointments.values().stream()
                .filter(a -> a.getAppointmentDateTime().toLocalDate().equals(date))
                .toList();
    }

    public List<Appointment> findAppointmentsByReason(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }

        return appointments.values().stream()
                .filter(a -> a.getReason() != null &&
                        a.getReason().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public List<Appointment> getActiveAppointments() {
        return appointments.values().stream()
                .filter(Appointment::isActive)
                .toList();
    }

    public List<Appointment> getAllAppointmentsSorted() {
        return appointments.values().stream()
                .sorted((a1, a2) -> a1.getAppointmentDateTime()
                        .compareTo(a2.getAppointmentDateTime()))
                .toList();
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }
}
