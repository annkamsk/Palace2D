package de.tomgrill.gdxtesting.tests;

import com.badlogic.gdx.math.Vector3;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import palace2d.game.GameCamera;
import palace2d.game.Palace2D;

import static org.junit.Assert.assertTrue;

public class GameCameraTest {
    private GameCamera camera;
    private Vector3 cameraPos;

    @BeforeClass
    public static void setUpTestMod() {
        Palace2D.TEST_MOD = true;
    }

    @AfterClass
    public static void restoreTestMod() {
        Palace2D.TEST_MOD = false;
    }

    @Before
    public void setUp() {
        camera = new GameCamera();
        cameraPos = camera.getPosition();
    }

    private void cameraMoveBy(float x, float y) {
        cameraPos.add(x, y, 0);
        camera.moveBy(x, y);
    }

    @Test
    public void cameraMoveByTest() {
        cameraMoveBy(100, 100);
        assertTrue(cameraPos.epsilonEquals(camera.getPosition()));

        cameraMoveBy(-200, -200);
        assertTrue(cameraPos.epsilonEquals(camera.getPosition()));

        cameraMoveBy(0, 0);
        assertTrue(cameraPos.epsilonEquals(camera.getPosition()));
    }
}
