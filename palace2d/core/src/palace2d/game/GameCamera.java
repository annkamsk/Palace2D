package palace2d.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class GameCamera {
    private static final float CAMERA_SMOOTH = 1f;

    private OrthographicCamera camera;

    public GameCamera() {
        camera = new OrthographicCamera();
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void setOrtho(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    public Vector3 getPosition() {
        return this.camera.position;
    }

    public void moveBy(float x, float y) {
        Vector3 cameraPosition = this.getPosition();
        Vector3 target = new Vector3(x, y, 0.f);
        target.add(cameraPosition);

        cameraPosition.lerp(target, CAMERA_SMOOTH);
    }
}
