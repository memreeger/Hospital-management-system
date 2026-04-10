package model.billing;

import model.enums.InvoiceStatus;
import model.person.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Invoice {
    private int id;
    private InvoiceStatus status;
    private Patient patient;
    private double totalAmount;
    private double paidAmount;
    private List<InvoiceItem> items;

    private static int nextId = 1;

    public Invoice(Patient patient) {
        validateObject(patient, "Patient");
        this.id = nextId++;
        setPatient(patient);
        setStatus(status);
        setPaidAmount(paidAmount);
        setTotalAmaount(totalAmount);
        this.items = new ArrayList<>();
    }

    //HELPER
    public void validateObject(Object object, String fieldName) {
        if (object == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public void addItem(InvoiceItem invoiceItem) {
        validateObject(invoiceItem, "Invoice item");
        items.add(invoiceItem);
    }

    public void deleteItem(InvoiceItem invoiceItem) {
        Objects.requireNonNull(invoiceItem, "Invoice item cannot be null");
        items.remove(invoiceItem);
    }

    public double getTotalAmount() {
        return totalAmount = items.stream()
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

    public void setStatus(InvoiceStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setTotalAmaount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }




    /*
    create invoice
    creave invoice by admission

     */
}
