package palace2d.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Palace2D extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture block1, background;
	private State state;


	private void newGame() {
		state = new PlayState(batch);
	}

	@Override
	public void create () {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		block1 = new Texture(Gdx.files.internal("block1.png"));
		background = new Texture(Gdx.files.internal("background.png"));
		newGame();
	}

	public void resize(int width, int height) {
		camera.setToOrtho(false, 800, 600);
	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0);
		state.draw();
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		block1.dispose();
		background.dispose();

	}
}
