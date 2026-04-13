package model.admission;

import model.enums.AdmissionStatus;
import model.person.Doctor;
import model.person.Patient;

import java.time.LocalDateTime;

public class Admission {
    private int id;
    private Doctor responsibleDoctor;
    private Patient patient;
    private Room room;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private AdmissionStatus status;
    private String description;

    private static int nextId = 1;

    public Admission(Patient patient, Room room) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }

        this.id = nextId++;
        this.patient = patient;
        this.room = room;
        this.admissionDate = LocalDateTime.now();
        this.dischargeDate = null;
        this.status = AdmissionStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == AdmissionStatus.ACTIVE;
    }

    public void discharge() {
        if (this.status == AdmissionStatus.DISCHARGED) {
            throw new IllegalStateException("Patient already discharged");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.admissionDate)) {
            throw new IllegalStateException("Discharge date cannot be before admission date");
        }

        this.dischargeDate = now;
        this.status = AdmissionStatus.DISCHARGED;
    }

    public int getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Room getRoom() {
        return room;
    }

    public Doctor getResponsibleDoctor() {
        return responsibleDoctor;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public LocalDateTime getDischargeDate() {
        return dischargeDate;
    }

    public AdmissionStatus getStatus() {
        return status;
    }

    public String getDescription(){
        return description;
    }

    public void assignDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        this.responsibleDoctor = doctor;
    }


    public long getLengthOfStayInDays() {
        LocalDateTime endDate = (dischargeDate != null) ? dischargeDate : LocalDateTime.now();
        return java.time.Duration.between(admissionDate, endDate).toDays();
    }

    public void transferRoom(Room newRoom) {
        if (newRoom == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        this.room = newRoom;
    }

    @Override
    public String toString() {
        return "Admission{" +
                "id='" + id + '\'' +
                ", patient='" + patient.getFullName() + '\'' +
                ", roomNumber=" + room.getRoomNumber() +
              //  ", doctor= " + responsibleDoctor.getFullName() +
                ", admissionDate=" + admissionDate +
                ", dischargeDate=" + dischargeDate +
                ", status=" + status +
                '}';
    }

}
