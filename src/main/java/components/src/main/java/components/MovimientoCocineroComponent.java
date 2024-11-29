package components;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class MovimientoCocineroComponent extends Component {
    private final double xMin, xMax, yMin, yMax; 
    private boolean cocinando = false;

    public MovimientoCocineroComponent(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    @Override
    public void onUpdate(double tpf) {
        if (cocinando) {
            double nuevoX = entity.getX() + (Math.random() - 0.5) * 2;
            double nuevoY = entity.getY() + (Math.random() - 0.5) * 2;  //movimiento aleatorio añadido

            if (nuevoX < xMin) nuevoX = xMin;
            if (nuevoX > xMax) nuevoX = xMax;
            if (nuevoY < yMin) nuevoY = yMin;
            if (nuevoY > yMax) nuevoY = yMax;
            entity.setPosition(new Point2D(nuevoX, nuevoY));
        }
    }

    public void setCocinando(boolean cocinando) {
        this.cocinando = cocinando;
    }
}