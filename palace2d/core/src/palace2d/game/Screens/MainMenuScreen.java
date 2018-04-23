package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import palace2d.game.Palace2D;

public class MainMenuScreen implements Screen {
    final Palace2D game;
    private Stage stage;

    public MainMenuScreen(final Palace2D game) {
        this.game = game;
        stage = new Stage(new FitViewport(Palace2D.V_WIDTH, Palace2D.V_HEIGHT));

        /* creating menu background */
        Texture backgroundTexture = new Texture(Gdx.files.internal
                ("background.png"));
        TextureRegion backgroundRegion = new TextureRegion(backgroundTexture,
                0, 0, 800, 600);
        Image backgroundActor = new Image(backgroundRegion);
        stage.addActor(backgroundActor);

        /* creating new game button */
        TextButton playButton = new TextButton("NEW GAME", new Skin(Gdx.files.internal
                ("skins/glassy/skin/glassy-ui.json")));
        playButton.setWidth(Gdx.graphics.getWidth() / 2);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - playButton
                .getWidth() / 2, Gdx.graphics.getHeight() / 2 - playButton
                .getHeight() / 2);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void resize(int x, int y) {

    }

    public void resume() {

    }

    public void pause() {

    }

    public void hide() {
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void dispose() {
        stage.dispose();
    }


}
