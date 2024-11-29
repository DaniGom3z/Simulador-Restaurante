package models;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.application.Platform;
import services.BufferService;
import services.MonitorService;
import java.util.concurrent.ThreadLocalRandom;

public class Comensal extends Component implements Runnable {
    private final String name;
    private final MonitorService monitorService;
    private final BufferService bufferService;
    private final double tasaLlegada;
    private Order order;
    private boolean ordenAgregada = false;

    public Comensal(String name, MonitorService monitorService, BufferService bufferService, double tasaLlegada) {
        this.name = name;
        this.monitorService = monitorService;
        this.bufferService = bufferService;
        this.tasaLlegada = tasaLlegada;
        this.order = null;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            System.out.println(name + " llegó al restaurante.");
            int mesaId = monitorService.ocuparMesa(name);
            System.out.println(name + " fue asignado a la mesa " + mesaId);

            int[] posicionMesa = monitorService.obtenerPosicionMesa(mesaId);
            Platform.runLater(() -> entity.setPosition(new Point2D(posicionMesa[0], posicionMesa[1])));
            Thread.sleep(1000);
            System.out.println(name + " está ordenando.");
            Order order = new Order(name);
            bufferService.addOrder(order);

            synchronized (order) {
                while (order.getStatus() != Order.OrderStatus.LISTA) {
                    order.wait(); 
                }
            }

            System.out.println(name + " está comiendo.");
            int tiempoComer = ThreadLocalRandom.current().nextInt(3000, 8001); 
            Thread.sleep(tiempoComer);

            // liberar la mesa 
            System.out.println(name + " terminó y se va del restaurante.");
            monitorService.liberarMesa(mesaId);
            Platform.runLater(() -> entity.removeFromWorld());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(name + " fue interrumpido.");
        }
    }

}