package palace2d.game;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import palace2d.game.Screens.MainMenuScreen;


public class Palace2D extends Game {
	public static final String TITLE = "Palace2D";
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 600;

	public OrthographicCamera camera;

	@Override
	public void create () {
        this.setScreen(new MainMenuScreen(this));
	}

	public void resize(int width, int height) {}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
	}
}
