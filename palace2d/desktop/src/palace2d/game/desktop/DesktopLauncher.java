package palace2d.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import palace2d.game.Palace2D;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Palace2D";
        config.width = 800;
        config.height = 600;
        new LwjglApplication(new Palace2D(), config);
    }
}