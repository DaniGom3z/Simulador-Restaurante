package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import controllers.SimuladorController;
import controllers.VisualFactory;
import services.BufferService;
import services.MonitorService;

public class Main extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        GameConfig.configureSettings(settings);
    }

    @Override
    protected void initGame() {
        int capacidadRestaurante = 10;
        double tasaLlegada = 0.5;

        MonitorService monitorService = new MonitorService(capacidadRestaurante);
        BufferService bufferService = new BufferService();

        FXGL.getGameWorld().addEntityFactory(new VisualFactory(monitorService, bufferService));

        // Inicia la simulación desde el controlador
        new SimuladorController().iniciarSimulacion(
                capacidadRestaurante,
                tasaLlegada,
                monitorService,
                bufferService
        );
    }



    public static void main(String[] args) {
        launch(args);
    }
}