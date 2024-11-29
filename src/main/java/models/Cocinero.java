package models;

import com.almasb.fxgl.entity.Entity;
import services.BufferService;
import components.MovimientoCocineroComponent;

import java.util.concurrent.ThreadLocalRandom;

public class Cocinero implements Runnable {
    private final String name;
    private final BufferService bufferService;
    private final Entity entity;

    public Cocinero(String name, BufferService bufferService, Entity entity) {
        this.name = name;
        this.bufferService = bufferService;
        this.entity = entity;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = bufferService.getNextOrder();
                if (order != null) {
                    entity.getComponent(MovimientoCocineroComponent.class).setCocinando(true);

                    System.out.println(name + " está cocinando la orden de " + order.getCustomerName());
                    int tiempoCoccion = ThreadLocalRandom.current().nextInt(3000, 8001);
                    Thread.sleep(tiempoCoccion);
              

                    entity.getComponent(MovimientoCocineroComponent.class).setCocinando(false);

                    synchronized (order) {
                        order.setStatus(Order.OrderStatus.LISTA);
                        bufferService.addOrder(order);
                        System.out.println(name + " completó la orden de " + order.getCustomerName());
                    }
                } else {
                    // Desactivar movimiento si no hay órdenes
                    entity.getComponent(MovimientoCocineroComponent.class).setCocinando(false);
                    Thread.sleep(500); 
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}