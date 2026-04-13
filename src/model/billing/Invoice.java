package model.billing;

import model.enums.InvoiceStatus;
import model.person.Patient;
import util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Invoice {
    private final int id;
    private InvoiceStatus status;
    private Patient patient;
    private double totalAmount;
    private double paidAmount;
    private final List<InvoiceItem> items;

    private static int nextId = 1;

    public Invoice(Patient patient) {
        Objects.requireNonNull(patient, "Patient cannot be null");

        this.id = nextId++;
        this.items = new ArrayList<>();
        this.patient = patient;
        this.status = InvoiceStatus.UNPAID;
        this.paidAmount = 0.0;
        this.totalAmount = getTotalAmount();

    }

    public void addPayment(double amount) {
        ValidationUtil.requirePositive(amount, "Payment amount");

        double remainingAmount = getTotalAmount() - paidAmount;
        if (amount > remainingAmount) {
            throw new IllegalArgumentException("Payment amount cannot exceed remaining balance");
        }

        paidAmount += amount;
        updateStatus();
    }

    private void updateStatus() {
        double totalAmount = getTotalAmount();

        if (paidAmount == 0) {
            status = InvoiceStatus.UNPAID;
        } else if (paidAmount < totalAmount) {
            status = InvoiceStatus.PARTIALLY_PAID;
        } else {
            status = InvoiceStatus.PAID;
        }
    }

    public void addItem(InvoiceItem invoiceItem) {
        Objects.requireNonNull(invoiceItem, "Invoice items cannot be null");
        items.add(invoiceItem);
    }

    public void removeItem(InvoiceItem invoiceItem) {
        Objects.requireNonNull(invoiceItem, "Invoice item cannot be null");
        items.remove(invoiceItem);
    }

    public double getTotalAmount() {
        return items.stream()
                .mapToDouble(InvoiceItem::getTotalPrice)
                .sum();
    }

    public int getId() {
        return id;
    }


    public InvoiceStatus getStatus() {
        return status;
    }

    public Patient getPatient() {
        return patient;
    }


    public double getPaidAmount() {
        return paidAmount;
    }

    public List<InvoiceItem> getItems() {
        return new ArrayList<>(items);
    }

    /*
    public void setStatus(InvoiceStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        this.status = status;
    }
     */

    /*public void setPatient(Patient patient) {
        Objects.requireNonNull(patient, "Patient cannot be null");
        this.patient = patient;
    }
    */


    /*public void setTotalAmount(double totalAmount) {
        ValidationUtil.requireNonNegative(totalAmount, "Total amount");
        this.totalAmount = totalAmount;
    }

     */

    /*public void setPaidAmount(double paidAmount) {
        ValidationUtil.requireNonNegative(paidAmount, "Paid amount");

        this.paidAmount = paidAmount;
    }

     */




    /*
    create invoice
    creave invoice by admission

     */
}
