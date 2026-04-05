package model.enums;

public enum RoomType {
    STANDARD(1000),
    DELUXE(2000),
    PRIVATE(3000),
    SHARED(700);

    private final double defaultRate;

    RoomType(double defaultRate) {
        this.defaultRate = defaultRate;
    }

    public double getDefaultRate() {
        return defaultRate;
    }
}