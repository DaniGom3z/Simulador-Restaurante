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
                    // Esperar notificación de cambio de estado
                    monitorService.wait();
                    // Después de ser notificada, obtiene el estado actualizado de las mesas
                    int mesasDisponibles = monitorService.mesasDisponibles();
                    System.out.println("Recepcionista: Hay " + mesasDisponibles + " mesas disponibles.");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Recepcionista interrumpido.");
        }
    }
}
