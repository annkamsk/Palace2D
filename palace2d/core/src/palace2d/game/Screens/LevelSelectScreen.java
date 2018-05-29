package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.Palace2D;

public class LevelSelectScreen extends PalaceScreen {
    private static final String LABEL_TEXT = "Select difficulty level";

    private static final float EASY_SPEED = 1f;
    private static final float NORMAL_SPEED = 0.8f;
    private static final float HARD_SPEED = 0.5f;

    public LevelSelectScreen(Palace2D game, TextureHandler textureHandler) {
        super(game, textureHandler);
        createInfoLabel(LABEL_TEXT);
        createLevelButton("EASY", 400, EASY_SPEED);
        createLevelButton("NORMAL", 300, NORMAL_SPEED);
        createLevelButton("HARD", 200, HARD_SPEED);
        createBackButton(new MainMenuScreen(game, textureHandler));
    }

    private void createLevelButton(String text, float y_position, float blockSpeed) {
        TextButton playButton = new TextButton(text, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")), "small");

        playButton.setWidth(Gdx.graphics.getWidth() / 2);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - playButton
                .getWidth() / 2, y_position);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer,
                                int button) {
                game.setScreen(new SkinSelectScreen(game, textureHandler, blockSpeed));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }
        });

        stage.addActor(playButton);
    }
}
