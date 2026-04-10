package service;

import model.admission.Admission;
import model.appointment.Appointment;
import model.billing.Invoice;
import model.billing.InvoiceItem;
import model.person.Patient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvoiceService {
    //private int id;
    //private Admission admission;
    //private Appointment appointment;
    private List<Invoice> invoiceList;

    //private static int nextId = 1;

    public InvoiceService() {
        //this.id = nextId++;
        this.invoiceList = new ArrayList<>();
    }


    public void createInvoice(Patient patient, Appointment appointment) {
        // voidde olabilir
        // üstünde işlem yapabilir olduğu zaman da tür döner

        Objects.requireNonNull(patient);

        Invoice invoice = new Invoice(patient);

        if (appointment != null) {
            addAppointmentFeeToInvoice(invoice, appointment);
        }

        invoiceList.add(invoice);


    }

    public void createInvoiceFromAdmission(Admission admission, Appointment appointment) {
        Objects.requireNonNull(admission, "Admission cannot be null");
        if (admission.getDischargeDate() == null) {
            throw new IllegalStateException("Patient not discharged yet");
        }

        long days = Duration.between(admission.getAdmissionDate(), admission.getDischargeDate()).toDays();

        days = Math.max(1, days);
        Invoice invoice = new Invoice(admission.getPatient());

        double dailyPrice = admission.getRoom().getRoomType().getDefaultRate();

        InvoiceItem invoiceItem = new InvoiceItem(
                "Room price",
                (int) days,
                dailyPrice);

        invoice.addItem(invoiceItem);


        if (appointment != null) {
            addAppointmentFeeToInvoice(invoice, appointment);
        }

        invoiceList.add(invoice);

    }


    public void addAppointmentFeeToInvoice(Invoice invoice, Appointment appointment) {
        Objects.requireNonNull(appointment, "Appointment cannot be null");

        if (appointment.getDoctor() == null) {
            throw new IllegalStateException("Doctor cannot be null");
        }

        String doctorName = appointment.getDoctor().getFullName();
        int quantity = 1;
        double fee = appointment.getDoctor().getFee();


        InvoiceItem invoiceItem = new InvoiceItem(doctorName, quantity, fee);
        invoice.addItem(invoiceItem);


    }

    private Invoice findInvoiceById(int invoiceId) {
        return invoiceList.stream()
                .filter(inv -> inv.getId() == invoiceId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
    }




    /*
    public Admission getAdmission() {

        return admission;
    }

    public Appointment getAppointment() {
        return appointment;
    }
    */


    public List<Invoice> getInvoiceList() {
        return new ArrayList<>(invoiceList);
    }


}

// create invoice

