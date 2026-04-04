package main;

import manager.HospitalManager;
import manager.MenuManager;

public class Main {
    public static void main(String[] args) {
        //MenuManager menuManager = new MenuManager();
        //menuManager.start();

        HospitalManager hospitalManager = new HospitalManager();
        hospitalManager.begin();
    }
}