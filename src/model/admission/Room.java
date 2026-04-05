package model.admission;

import model.enums.RoomStatus;
import model.enums.RoomType;

import java.util.UUID;

public class Room {
    private String id;
    private int roomNumber;
    private RoomType roomType;
    private RoomStatus roomStatus;
    private double dailyRate;

    public Room(int roomNumber, RoomType roomType, double dailyRate) {
        this.id = UUID.randomUUID().toString();
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.dailyRate = dailyRate;
        this.roomStatus = RoomStatus.AVAILABLE;
    }

    //Getter

    public String getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    // Setter
    public void setRoomStatus(RoomStatus roomStatus) {
        if (roomStatus == null) {
            throw new IllegalArgumentException("Room status cannot be null");
        }
        this.roomStatus = roomStatus;
    }

    public void setRoomType(RoomType roomType) {
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }
        this.roomType = roomType;
    }

    public boolean isOccupied() {
        return roomStatus == RoomStatus.OCCUPIED;
    }

    public boolean isAvailable() {
        return roomStatus == RoomStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", roomStatus=" + roomStatus +
                ", dailyRate=" + dailyRate +
                '}';
    }
}
