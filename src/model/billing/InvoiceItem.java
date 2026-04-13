package model.billing;
// DOLDUR get set


import java.util.Objects;

public class InvoiceItem {
    private int id;
    private String description;
    private int quantity;
    private double amount;

    private static int nextId = 1;

    public InvoiceItem(String description, int quantity, double amount) {

        this.id = nextId++;

        validateString(description, "Description");
        validateInt(quantity, "Quantity");
        validateDouble(amount, "Amount");

        setDescription(description);
        setQuantity(quantity);
        setAmount(amount);
    }

    // Helper
    public void validateString(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    public void validateInt(int val, String fieldName) {
        if (val < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be smaller than 0");
        }

    }

    public void validateDouble(double val, String fieldName) {
        if (val < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be smaller than 0");
        }

    }


    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }

    public double getTotalPrice() {
        return amount * quantity;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
