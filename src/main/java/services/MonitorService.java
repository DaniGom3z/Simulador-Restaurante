package services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


public class MonitorService {
    private final int capacidad;
    private int mesasDisponibles;
    private final Queue<String> comensalesEsperando;
    private final boolean[] mesasOcupadas;
    private final int[][] posicionesMesas;
    private final Map<String, Integer> asignacionesMesas; // Relación comensal-mesa

    public MonitorService(int capacidad) {
        this.capacidad = capacidad;
        this.mesasDisponibles = capacidad;
        this.comensalesEsperando = new LinkedList<>();
        this.mesasOcupadas = new boolean[capacidad];
        this.asignacionesMesas = new HashMap<>(); // Inicializar el mapa

        // Definir posiciones de las mesas (puedes personalizar estas coordenadas)
        this.posicionesMesas = new int[][]{
                {450, 40}, {630, 40}, {450, 140}, {630, 140},
                {450, 250}, {630, 250}, {450, 370}, {630, 370},
                {450, 490}, {630, 490}
        };
    }

    public synchronized int ocuparMesa(String comensal) throws InterruptedException {
        while (mesasDisponibles == 0) {
            comensalesEsperando.add(comensal);
            wait(); // Esperar hasta que haya mesas disponibles
        }
        mesasDisponibles--;
        for (int i = 0; i < capacidad; i++) {
            if (!mesasOcupadas[i]) {
                mesasOcupadas[i] = true;
                asignacionesMesas.put(comensal, i); // Asignar la mesa al comensal
                System.out.println(comensal + " ocupa la mesa " + i);
                notifyAll(); // Notificar cambios
                return i;
            }
        }
        return -1; // Nunca debería llegar aquí
    }

    public synchronized void liberarMesa(int mesaId) {
        mesasOcupadas[mesaId] = false; // Marcar mesa como disponible
        mesasDisponibles++;
        asignacionesMesas.entrySet().removeIf(entry -> entry.getValue() == mesaId); // Eliminar asignación
        System.out.println("Mesa " + mesaId + " liberada.");
        notifyAll(); // Notificar a los threads en espera
    }

    public synchronized int obtenerMesaPorNombre(String comensal) {
        // Devuelve el ID de la mesa asignada al comensal
        return asignacionesMesas.getOrDefault(comensal, -1); // -1 si no se encuentra
    }

    public int[] obtenerPosicionMesa(int mesaId) {
        if (mesaId < 0 || mesaId >= posicionesMesas.length) {
            throw new IllegalArgumentException("Mesa ID inválido: " + mesaId);
        }
        return posicionesMesas[mesaId];
    }

    public synchronized int mesasDisponibles() {
        return mesasDisponibles;
    }

}