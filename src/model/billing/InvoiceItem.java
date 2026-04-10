package model.billing;
// DOLDUR get set


public class InvoiceItem {
    private int id;
    private String description;
    private int quantity;
    private double amount;

    private static int nextId = 1;

    public InvoiceItem(String description, int quantity, double amount) {
        this.id = nextId++;
        setDescription(description);
        setQuantity(quantity);
        setAmount(amount);
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
