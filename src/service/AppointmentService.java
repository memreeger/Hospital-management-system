package service;

import model.appointment.Appointment;
import model.enums.AppointmentStatus;
import model.person.Doctor;
import model.person.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentService {
    private Map<String, Appointment> appointments;

    public AppointmentService() {
        this.appointments = new HashMap<>();
    }

    private boolean isSameSlot(LocalDateTime t1, LocalDateTime t2) {
        return !t1.isBefore(t2.minusMinutes(30)) &&
                !t1.isAfter(t2.plusMinutes(30));
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime appointmentTime, String reason) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        if (appointmentTime == null) {
            throw new IllegalArgumentException("Appointment time cannot be null");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be empty");
        }


        if (appointmentTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("You cannot make appointment in the past");
        }

        boolean isPatientBusy = appointments.values().stream()
                .anyMatch(a ->
                        a.isActive() &&
                                a.getPatient().getId().equals(patient.getId()) &&
                                isSameSlot(a.getAppointmentDateTime(), appointmentTime)
                );

        if (isPatientBusy) {
            throw new IllegalStateException("Patient already has an appointment at this time");
        }

        boolean isDoctorBusy = appointments.values().stream()
                .anyMatch(a ->
                        a.isActive() &&
                                a.getDoctor().getId().equals(doctor.getId()) &&
                                isSameSlot(a.getAppointmentDateTime(), appointmentTime)
                );

        if (isDoctorBusy) {
            throw new IllegalStateException("Doctor already has an appointment at this time");
        }

        Appointment appointment = new Appointment(patient, doctor, appointmentTime, reason);
        appointments.put(appointment.getId(), appointment);

        return appointment;

    }

    public Appointment cancelAppointment(String appointmentId) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new IllegalArgumentException("Appointment id cannot be null or blank");
        }

        Appointment appointment = appointments.get(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }

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
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        List<Appointment> appointmentsByPatient = appointments.values().stream()
                .filter(a -> a.isActive() &&
                        a.getPatient().getId().equals(patient.getId()))
                .toList();
        return appointmentsByPatient;
    }

    public List<Appointment> getActiveAppointmentsByDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }

        List<Appointment> appointmentsByDoctor = appointments.values().stream()
                .filter(a -> a.isActive() &&
                        a.getDoctor().getId().equals(doctor.getId()))
                .toList();

        return appointmentsByDoctor;
    }

    public List<Appointment> getAllAppointmentsByPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        List<Appointment> appointmentsByPatient = appointments.values().stream()
                .filter(a -> a.getPatient().getId().equals(patient.getId()))
                .toList();
        return appointmentsByPatient;
    }

    public List<Appointment> getAllAppointmentsByDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }

        List<Appointment> appointmentsByDoctor = appointments.values().stream()
                .filter(a -> a.getDoctor().getId().equals(doctor.getId()))
                .toList();

        return appointmentsByDoctor;
    }

    public Appointment completeAppointment(String appointmentId) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new IllegalArgumentException("Appointment id cannot be null or blank");
        }
        Appointment appointment = appointments.get(appointmentId);

        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("This appointment is already completed");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled appointment cannot be completed");
        }

        appointment.complete();
        return appointment;
    }

    public Appointment rescheduleAppointment(String appointmentId, LocalDateTime newTime) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new IllegalArgumentException("Appointment id cannot be null or blank");
        }

        if (newTime == null) {
            throw new IllegalArgumentException("Time field cannot be null");
        }

        Appointment appointment = appointments.get(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled appointments can be rescheduled");
        }

        if (newTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reschedule to the past");
        }

        boolean isPatientBusy = appointments.values().stream()
                .anyMatch(a -> a.isActive() &&
                        a.getPatient().getId().equals(appointment.getPatient().getId()) &&
                        isSameSlot(a.getAppointmentDateTime(), newTime) &&
                        !a.getId().equals(appointmentId)
                );
        if (isPatientBusy) {
            throw new IllegalStateException("Patient already has an appointment at this time");
        }

        boolean isDoctorBusy = appointments.values().stream()
                .anyMatch(a -> a.isActive() &&
                        a.getDoctor().getId().equals(appointment.getDoctor().getId()) &&
                        isSameSlot(a.getAppointmentDateTime(), newTime) &&
                        !a.getId().equals(appointmentId)
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
