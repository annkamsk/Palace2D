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

	public static final String TITLE = "Palace2D";
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 600;

	public int score;
	public String scoreName;

	public OrthographicCamera camera;
	public SpriteBatch batch;

	public BitmapFont font;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
        this.setScreen(new MainMenuScreen(this));

        batch = new SpriteBatch();

        font = new BitmapFont();
        font.setColor(Color.WHITE);

        score = 0;
        scoreName = "score: 0";
	}

	public void resize(int width, int height) {}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
