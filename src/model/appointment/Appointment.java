package model.appointment;

import model.enums.AppointmentStatus;
import model.person.Doctor;
import model.person.Patient;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {

    private String id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String reason;

    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDateTime, String reason) {
        validateString(reason, "Reason");
        if (patient == null){
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (doctor == null){
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        if (appointmentDateTime == null){
            throw new IllegalArgumentException("Appointment date time cannot be null");
        }
        this.id = UUID.randomUUID().toString();
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = AppointmentStatus.SCHEDULED;
    }

    private void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public void setReason(String reason) {
        validateString(reason, "Reason");
        this.reason = reason;
    }

    public void cancel() {
        if (status == AppointmentStatus.SCHEDULED) {
            this.status = AppointmentStatus.CANCELLED;
        }
    }

    public void complete() {
        if (status == AppointmentStatus.SCHEDULED) {
            this.status = AppointmentStatus.COMPLETED;
        }
    }

    public boolean isActive() {
        return this.status == AppointmentStatus.SCHEDULED;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patient='" + patient.getFullName() + '\'' +
                ", doctor='" + doctor.getFullName() + '\'' +
                ", appointmentDateTime=" + appointmentDateTime +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}