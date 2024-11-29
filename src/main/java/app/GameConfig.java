package app;

import com.almasb.fxgl.app.GameSettings;

public class GameConfig {
    public static void configureSettings(GameSettings settings) {
        settings.setWidth(1004);
        settings.setHeight(563);
        settings.setTitle("Restaurant Simulator");
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(false);
    }
}