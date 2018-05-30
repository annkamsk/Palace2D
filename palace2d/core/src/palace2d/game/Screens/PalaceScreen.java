package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import palace2d.game.GameCamera;
import palace2d.game.Palace2D;
import palace2d.game.ScreenActors.GameScreenActors;
import palace2d.game.Graphics.TextureHandler;


public abstract class PalaceScreen implements Screen {
    private static final int BACK_BUTTON_YPOSITION = 10; // px
    private static final int BACK_BUTTON_HEIGHT = 50; // px
    private static final int NEWGAME_BUTTON_YPOSITION = 10; // px
    private static final int BUTTON_HEIGHT = 50; // px
    private static final float INFO_LABEL_HEIGHT = 500;

    Stage stage;
    Palace2D game;
    GameCamera camera;
    GameScreenActors actors;
    Texture backgroundTexture;
    Image backgroundImg;
    TextureHandler textureHandler;


    public PalaceScreen(Palace2D game,
                        TextureHandler textureHandler) {
        this.game = game;
        this.camera = new GameCamera();
        this.camera.setOrtho(Palace2D.V_WIDTH, Palace2D.V_HEIGHT);
        this.stage = new Stage(new FitViewport(Palace2D.V_WIDTH,
                Palace2D.V_HEIGHT, this.camera.getCamera()));
        this.actors = new GameScreenActors(textureHandler);
        this.textureHandler = textureHandler;
        setBackgroundTexture();

    }

    void setBackgroundTexture() {
        backgroundTexture = textureHandler.getBackgroundTexture();

        backgroundImg = TextureHandler.getActorFromTexture
                (backgroundTexture, 0, 0,
                        Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(backgroundImg);

    }

    void createNewGameButton() {
        TextButton playButton = new TextButton("NEW GAME", new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")), "small");

        playButton.setBounds(Gdx.graphics.getWidth() / 2 - playButton
                                                                   .getWidth () / 2,
                NEWGAME_BUTTON_YPOSITION, playButton.getWidth(),
                BUTTON_HEIGHT);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer,
                                int button) {
                game.setScreen(new LevelSelectScreen(game, textureHandler));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }
        });

        stage.addActor(playButton);
    }

    void createInfoLabel(String title) {
        Label label = new Label(title, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        label.setPosition(Gdx.graphics.getWidth() / 2 - label
                .getWidth() / 2, INFO_LABEL_HEIGHT);
        stage.addActor(label);
    }

    void createBackButton(PalaceScreen screen) {
        TextButton playButton = new TextButton("Back", new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")), "small");

        playButton.setBounds(Gdx.graphics.getWidth() / 2 - playButton
                        .getWidth () / 2,
                BACK_BUTTON_YPOSITION, playButton.getWidth(),
                BACK_BUTTON_HEIGHT);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer,
                                int button) {
                game.setScreen(screen);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }
        });

        stage.addActor(playButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
