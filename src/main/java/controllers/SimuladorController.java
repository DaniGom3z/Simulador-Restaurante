package controllers;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.application.Platform;
import javafx.util.Duration;
import models.Cocinero;
import models.Mesero;
import models.Recepcionista;
import services.BufferService;
import services.MonitorService;
import utils.PoissonGenerator;
import com.almasb.fxgl.dsl.FXGL;

public class SimuladorController {

    public void iniciarSimulacion(int capacidadRestaurante, double tasaLlegada, MonitorService monitorService, BufferService bufferService) {
        Platform.runLater(() -> {
            FXGL.getGameScene().setBackgroundRepeat("fondo.png");

            var mesasDisponiblesText = FXGL.getUIFactoryService().newText("", 10);
            mesasDisponiblesText.setTranslateX(230);
            mesasDisponiblesText.setTranslateY(365);
            FXGL.addUINode(mesasDisponiblesText);


            FXGL.run(() -> {
                mesasDisponiblesText.setText("Mesas Disponibles:" + monitorService.mesasDisponibles());
            }, Duration.seconds(0.5)); // Actualizamos cada 0.5 segundos
        });

        FXGL.spawn("recepcionista", 250, 370);

        int numMeseros = (int) Math.ceil(capacidadRestaurante * 0.1);
        int numCocineros = (int) Math.ceil(capacidadRestaurante * 0.15);

        // Creaamos y ejecutamos recepcionista
        Recepcionista recepcionista = new Recepcionista(monitorService);
        new Thread(recepcionista).start();

        for (int i = 0; i < numMeseros; i++) {
            final int meseroIndex = i;

            // Generamos la entidad visual del mesero
            Entity meseroEntity = FXGL.spawn("mesero", 720 + meseroIndex * 50, 250);

            // Creamos el hilo del mesero
            new Thread(new Mesero("Mesero " + (meseroIndex + 1), monitorService, bufferService, meseroEntity)).start();
        }




        for (int i = 0; i < numCocineros; i++) {
            final int cocineroIndex = i;
            Entity chefEntity = FXGL.spawn("chef", 800 + cocineroIndex * 50, 200);
            new Thread(new Cocinero("Cocinero " + (cocineroIndex + 1), bufferService, chefEntity)).start();
        }




        // Creamos comensales
        crearComensales(capacidadRestaurante, tasaLlegada, monitorService, bufferService);
    }

    private void crearComensales(int capacidadRestaurante, double tasaLlegada, MonitorService monitorService, BufferService bufferService) {
        int comensalesTotal = 20;
        double xMin = 200;
        double xMax = 300;
        double yMin = 60;
        double yMax = 250;

        new Thread(() -> {
            for (int i = 0; i < comensalesTotal; i++) {
                final int comensalId = i + 1;

                double xPos = xMin + Math.random() * (xMax - xMin);
                double yPos = yMin + Math.random() * (yMax - yMin);

                Platform.runLater(() -> {
                    FXGL.spawn("cliente", new SpawnData(xPos, yPos).put("id", comensalId));
                    System.out.println("Comensal " + comensalId + " generado.");
                });

                try {
                    long tiempoLlegada = PoissonGenerator.generate(tasaLlegada);
                    System.out.println("Comensal " + comensalId + " llegará en " + tiempoLlegada + " ms");
                    Thread.sleep(tiempoLlegada);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("El hilo de llegada de comensales fue interrumpido.");
                }
            }
        }).start();
    }
}