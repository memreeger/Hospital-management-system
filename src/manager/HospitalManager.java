package manager;

import service.AppointmentService;
import service.AuthService;
import service.BillingService;
import service.PatientService;

public class HospitalManager {
    private PatientService patientService;
    private AppointmentService appointmentService;
    private AuthService authService;
    private BillingService billingService;


    public HospitalManager() {
        this.patientService = new PatientService();
        this.appointmentService = new AppointmentService();
        this.authService = new AuthService();
        this.billingService = new BillingService();
    }
}
