package service;

import model.admission.Room;
import model.enums.RoomStatus;
import model.enums.RoomType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomService {
    private Map<String, Room> rooms;
    private final int maxCapacity;


    //Constructor
    public RoomService() {
        this.rooms = new HashMap<>();
        this.maxCapacity = 30;
    }

    //Validation
    private void validateRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be positive");
        }
    }

    private void validateRoomType(RoomType roomType) {
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }
    }

    private void validateString(String field, String fieldName) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException(fieldName + "cannot be null or empty");
        }
    }

    private boolean existsByRoomNumber(int roomNumber) {

        return rooms.values().stream()
                .anyMatch(r -> r.getRoomNumber() == roomNumber);
    }


    //Methods

    //Create method

    public Room addRoom(int roomNumber, RoomType roomType) {
        validateRoomNumber(roomNumber);
        validateRoomType(roomType);

        if (rooms.size() >= maxCapacity) {
            throw new IllegalStateException("Rooms are full");
        }

        if (existsByRoomNumber(roomNumber)) {
            throw new IllegalArgumentException("Room number already exists");
        }

        Room room = new Room(roomNumber, roomType);
        rooms.put(room.getId(), room);
        return room;
    }


    //Read methods

    public Room getRoomById(String id) {
        validateString(id, "ID");

        Room room = rooms.get(id);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }

        return room;
    }

    public Room getRoomByRoomNumber(int roomNumber) {
        validateRoomNumber(roomNumber);

        Room room = rooms.values().stream()
                .filter(r -> r.getRoomNumber() == roomNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Room not found"));
        return room;
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> getAvailableRooms() {
        return rooms.values().stream()
                .filter(r -> r.getRoomStatus() == RoomStatus.AVAILABLE)
                .toList();
    }

    public List<Room> getAvailableRoomsByType(RoomType roomType) {
        validateRoomType(roomType);

        return getAvailableRooms().stream()
                .filter(r -> r.getRoomType() == roomType)
                .toList();

    }

    public List<Room> getRoomsByStatus(RoomStatus roomStatus) {
        if (roomStatus == null) {
            throw new IllegalArgumentException("Room status cannot be null");
        }

        List<Room> result = rooms.values().stream()
                .filter(r -> r.getRoomStatus() == roomStatus)
                .toList();
        return result;
    }


    //Update methods

    public void markRoomOccupied(String id) {
        Room room = getRoomById(id);

        if (!room.isAvailable()) {
            throw new IllegalArgumentException("Room is not available");
        }

        room.setRoomStatus(RoomStatus.OCCUPIED);
    }

    public void markRoomAvailable(String id) {
        Room room = getRoomById(id);
        room.setRoomStatus(RoomStatus.AVAILABLE);
    }

    public void updateRoomType(String id, RoomType newType) {
        validateRoomType(newType);

        Room room = getRoomById(id);
        room.setRoomType(newType);
    }


    //Delete

    public Room removeRoomById(String id) {
        Room room = getRoomById(id);
        rooms.remove(id);
        return room;
    }

    public void deleteAllRooms() {
        rooms.clear();
    }


    //Info

    public int getRoomCount() {
        return rooms.size();
    }

    public int getAvailableRoomCount() {
        return (int) rooms.values().stream()
                .filter(r -> r.getRoomStatus() == RoomStatus.AVAILABLE)
                .count();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}