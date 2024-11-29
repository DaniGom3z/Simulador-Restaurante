package controllers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import enums.SimulatorTypes;
import models.Comensal;
import services.BufferService;
import services.MonitorService;


public class VisualFactory implements EntityFactory {

    private final MonitorService monitorService;
    private final BufferService bufferService;

    public VisualFactory(MonitorService monitorService, BufferService bufferService) {
        this.monitorService = monitorService;
        this.bufferService = bufferService;
    }

    @Spawns("recepcionista")
    public Entity createRecepcionista(SpawnData data) {
        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.RECEPCIONISTA)
                .viewWithBBox("recepcionista.png")
                .build();
    }

    @Spawns("cliente")
    public Entity createCliente(SpawnData data) {
        String name = "Comensal " + data.get("id");
        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.CLIENTE)
                .with(new Comensal(name, monitorService, bufferService, 0.5))
                .viewWithBBox("comensal1.png")
                .build();
    }

    @Spawns("chef")
    public Entity createChef(SpawnData data) {
        double xMin = 830;
        double xMax = 860;
        double yMin = 150;
        double yMax = 300;

        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.CHEF)
                .viewWithBBox("chef.png")
                .build();
    }


    @Spawns("mesero")
    public Entity createMesero(SpawnData data) {
        return FXGL.entityBuilder()
                .from(data)
                .type(SimulatorTypes.MESERO)
                .viewWithBBox("mesero.png")
                .build();
    }
}