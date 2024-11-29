package services;

import models.Order;

import java.util.LinkedList;
import java.util.Queue;

public class BufferService {
    private final Queue<Order> ordenes;

    public BufferService() {
        this.ordenes = new LinkedList<>();
    }

    /**
     * Añade una nueva orden al buffer.
     *
     * @param order La orden a añadir.
     */
    public synchronized void addOrder(Order order) {
        boolean existe = ordenes.stream()
                .anyMatch(o -> o.getCustomerName().equals(order.getCustomerName()));

        if (existe) {
            System.out.println("La orden ya existe en el buffer para el cliente: " + order.getCustomerName());
            return; // Evitar duplicados
        }

        ordenes.add(order);
        notifyAll(); // Notificar a los threads en espera
    }

    /**
     * Retira y retorna la siguiente orden en el buffer.
     *
     * @return La siguiente orden.
     * @throws InterruptedException Si el hilo es interrumpido.
     */
    public synchronized Order getNextOrder() throws InterruptedException {
        while (ordenes.isEmpty() || ordenes.peek().getStatus() != Order.OrderStatus.PENDIENTE) {
            wait(); // Esperar hasta que haya órdenes pendientes
        }
        Order order = ordenes.poll();
        order.setStatus(Order.OrderStatus.EN_PROCESO);
        notifyAll(); // Notificar a los threads en espera
        return order;
    }


    /**
     * Retira y retorna una orden marcada como LISTA.
     *
     * @return Una orden lista.
     * @throws InterruptedException Si el hilo es interrumpido.
     */
    public synchronized Order getOrdenLista() throws InterruptedException {
        while (ordenes.stream().noneMatch(order -> order.getStatus() == Order.OrderStatus.LISTA)) {
            wait(); // Esperar hasta que haya órdenes listas
        }
        Order order = ordenes.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.LISTA)
                .findFirst()
                .orElse(null);
        ordenes.remove(order); // Retirar del buffer
        notifyAll(); // Notificar a los threads en espera
        return order;
    }

}