package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import palace2d.game.Palace2D;


public class EndGameScreen implements Screen {
    private Stage stage;
    private Palace2D game;
    private Label text;

    public EndGameScreen(Palace2D game) {
        this.game = game;
        stage = new Stage(new FitViewport(Palace2D.V_WIDTH, Palace2D.V_HEIGHT));
        createGameObjects();
    }

    private void createGameObjects() {
        /* creating background */
        Texture backgroundTexture = new Texture(Gdx.files.internal
                ("background.png"));
        stage.addActor(getActorFromTexture(backgroundTexture, 0, 0, Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()));
        text = new Label("YOU FINISHED YOUR GAME", new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        text.setBounds(Gdx.graphics.getWidth() / 2 - text.getWidth() / 2,
                Gdx.graphics.getHeight() / 2, 200, 40);
        stage.addActor(text);
    }

    private Actor getActorFromTexture(Texture tex, int x, int y, int w, int h) {
        TextureRegion texRegion = new TextureRegion(tex,
                x, y, w, h);
        return new Image(texRegion);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
