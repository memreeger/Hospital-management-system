package model.enums;

public enum BloodType {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String label;

    BloodType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static BloodType fromLabel(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Blood type cannot be null or empty");
        }

        String normalized = input.trim().toUpperCase();

        normalized = normalized.replace('0', 'O');

        for (BloodType type : BloodType.values()) {
            if (type.getLabel().equalsIgnoreCase(normalized)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid blood type: " + input);
    }

    @Override
    public String toString() {
        return label;
    }
}