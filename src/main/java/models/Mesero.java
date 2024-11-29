package models;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import services.BufferService;
import services.MonitorService;

public class Mesero implements Runnable {
    private final String name;
    private final MonitorService monitorService;
    private final BufferService bufferService;
    private final Entity entity; 

    public Mesero(String name, MonitorService monitorService, BufferService bufferService, Entity entity) {
        this.name = name;
        this.monitorService = monitorService;
        this.bufferService = bufferService;
        this.entity = entity;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // mesero debe de recoger la comida , ya funcia 
                Order comidaLista = bufferService.getOrdenLista();
                if (comidaLista != null) {
                    System.out.println(name + " está entregando la comida de " + comidaLista.getCustomerName());

                    moverHaciaComensal(comidaLista);
                    Thread.sleep(1000);

                    System.out.println(name + " entregó la comida de " + comidaLista.getCustomerName());
                    synchronized (comidaLista) {
                        comidaLista.notifyAll();
                    }
                    regresarAPosicionInicial();
                } else {
                    Thread.sleep(500); 
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void moverHaciaComensal(Order order) {
        Point2D posicionComensal = obtenerPosicionComensal(order.getCustomerName());

        if (posicionComensal != null) {
            while (entity.getPosition().distance(posicionComensal) > 5) {
                entity.translateTowards(posicionComensal, 3);
                try {
                    Thread.sleep(50); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void regresarAPosicionInicial() {
        Point2D posicionInicial = new Point2D(720, 250);

        while (entity.getPosition().distance(posicionInicial) > 5) {
            entity.translateTowards(posicionInicial, 3);
            try {
                Thread.sleep(50); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Point2D obtenerPosicionComensal(String customerName) {
        int mesaId = monitorService.obtenerMesaPorNombre(customerName);
        int[] posicionMesa = monitorService.obtenerPosicionMesa(mesaId);
        return new Point2D(posicionMesa[0], posicionMesa[1]);
    }
}