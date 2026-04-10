package model.billing;

import java.time.LocalDateTime;

public class Payment {
    /*
    1 - yatış süresi
    2 - oda tipi
    3 - patient
    4 - appointment


    core model olarak payment olmalı,
     */
    private int id;
    private double amount;
    private LocalDateTime checkOutTime;
    private Invoice invoice;

    /*
    Payment method
     */
    private static int nextId = 1;

}
