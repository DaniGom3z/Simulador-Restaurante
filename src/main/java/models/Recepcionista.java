package models;

import services.MonitorService;

public class Recepcionista implements Runnable {
    private final MonitorService monitorService;

    public Recepcionista(MonitorService monitorService) {
        this.monitorService = monitorService;
    }
    @Override
    public void run() {
        try {
            while (true) {
                synchronized (monitorService) {
                    int mesasDisponibles = monitorService.mesasDisponibles();
                    System.out.println("Recepcionista: Hay " + mesasDisponibles + " mesas disponibles.");
                }
                Thread.sleep(2000); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Recepcionista interrumpido.");
        }
    }

}