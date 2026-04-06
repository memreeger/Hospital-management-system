package model.admission;

import model.enums.AdmissionStatus;
import model.person.Patient;

import java.time.LocalDateTime;
import java.util.UUID;

public class Admission {
    private String id;
    private Patient patient;
    private Room room;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargedDate;
    private AdmissionStatus status;

    public Admission(Patient patient, Room room) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        this.id = UUID.randomUUID().toString();
        this.patient = patient;
        this.room = room;
        this.admissionDate = LocalDateTime.now();
        this.dischargedDate = null;
        this.status = AdmissionStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == AdmissionStatus.ACTIVE;
    }

    public void discharge() {
        if (this.status == AdmissionStatus.DISCHARGED) {
            throw new IllegalStateException("Patient already discharged");
        }

        this.dischargedDate = LocalDateTime.now();
        this.status = AdmissionStatus.DISCHARGED;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public LocalDateTime getDischargedDate() {
        return dischargedDate;
    }

    public AdmissionStatus getStatus() {
        return status;
    }


}
