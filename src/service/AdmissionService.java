package service;

import model.admission.Admission;
import model.admission.Room;
import model.enums.RoomType;
import model.person.Patient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdmissionService {
    private Map<Integer, Admission> admissions;
    private RoomService roomService;


    //Constructor

    public AdmissionService(RoomService roomService) {
        if (roomService == null) {
            throw new IllegalArgumentException("Room service cannot be null");
        }
        this.admissions = new HashMap<>();
        this.roomService = roomService;
    }

    //Helper

    private void validateString(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank ");
        }
    }

    public void validatteInt(int val, String fieldname) {
        if (val < 0) {
            throw new IllegalArgumentException(fieldname + " cannot be smaller than 0");
        }
    }

    private Admission getAdmissionOrThrow(int id) {
        validatteInt(id, "Id");

        Admission admission = admissions.get(id);
        if (admission == null) {
            throw new IllegalArgumentException("Admission not found.");
        }
        return admission;
    }

    private boolean hasActiveAdmissionByPatient(Patient patient) {
        return admissions.values().stream()
                .anyMatch(a -> a.getPatient().getId() == (patient.getId())
                        && a.isActive());
    }

    //Create
    public Admission admitPatient(Patient patient, Room room) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }

        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is not available.");
        }

        if (hasActiveAdmissionByPatient(patient)) {
            throw new IllegalStateException("Patient already has an active admission.");
        }

        Admission admission = new Admission(patient, room);
        admissions.put(admission.getId(), admission);
        roomService.markRoomOccupied(room.getId());

        return admission;
    }

    public Admission dischargePatient(int id) {
        validatteInt(id, "Id");

        Admission admission = getAdmissionOrThrow(id);
        if (!admission.isActive()) {
            throw new IllegalStateException("Admission must be active to discharge");
        }
        admission.discharge();

        roomService.markRoomAvailable(admission.getRoom().getId());
        return admission;
    }


    //Read
    public Admission getAdmissionById(int id) {
        validatteInt(id, "Id");
        return getAdmissionOrThrow(id);
    }

    public List<Admission> getActiveAdmissions() {
        List<Admission> result = admissions.values().stream()
                .filter(a -> a.isActive())
                .toList();
        return result;
    }

    public List<Admission> getAdmissionsByPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        List<Admission> result = admissions.values().stream()
                .filter(a -> a.getPatient().getId() == (patient.getId()))
                .toList();
        return result;
    }

    public List<Admission> getAdmissionsByRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        List<Admission> result = admissions.values().stream()
                .filter(a -> a.getRoom().getId() == (room.getId()))
                .toList();
        return result;
    }

    public List<Admission> getActiveAdmissionsByRoomType(RoomType roomType) {
        if (roomType == null) throw new IllegalArgumentException("Room type cannot be null");
        return admissions.values().stream()
                .filter(a -> a.isActive() && a.getRoom().getRoomType() == roomType)
                .toList();
    }

    public List<Admission> getDischargedAdmissions() {
        return admissions.values().stream()
                .filter(a -> !a.isActive())
                .toList();
    }


    //Update
    public void transferPatient(int admissionId, Room newRoom) {
        validatteInt(admissionId, "Admission id");

        if (newRoom == null) {
            throw new IllegalArgumentException("New room canno be null");
        }
        Admission admission = getAdmissionOrThrow(admissionId);

        if (!admission.isActive()) {
            throw new IllegalStateException("Cannot transfer discharged patient");
        }

        if (!newRoom.isAvailable()) {
            throw new IllegalStateException("New room is not available");
        }

        Room oldRoom = admission.getRoom();
        roomService.markRoomAvailable(oldRoom.getId());
        roomService.markRoomOccupied(newRoom.getId());

        admission.transferRoom(newRoom);
    }


    //Info
    public int getActiveAdmissionCount() {
        return (int) admissions.values().stream().filter(Admission::isActive).count();
    }

    public int getDischargedAdmissionCount() {
        return (int) admissions.values().stream().filter(a -> !a.isActive()).count();
    }

    public int getTotalAdmissionCount() {
        return admissions.size();
    }


    //Delete
    public void deleteAllAdmissions() {
        admissions.clear();
    }

    public Admission removeAdmissionById(int id) {
        validatteInt(id, "Id");

        Admission admission = getAdmissionOrThrow(id);
        Room room = admission.getRoom();

        if (admission.isActive()) {
            roomService.markRoomAvailable(room.getId());
        }
        admissions.remove(id);
        return admission;
    }

    @Override
    public String toString() {
        return "AdmissionService{" +
                "admissions=" + admissions.values() +
                '}';
    }

}
