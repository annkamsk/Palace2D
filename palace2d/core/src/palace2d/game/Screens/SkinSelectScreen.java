package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import palace2d.game.Graphics.MIMTextureHandler;
import palace2d.game.Graphics.PalaceTextureHandler;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.Palace2D;

public class SkinSelectScreen extends PalaceScreen {
    private static final int PALACE_BUTT_X = Gdx.graphics.getWidth() / 4 + 50;
    private static final int PALACE_BUTT_Y = Gdx.graphics.getHeight() / 2;
    private static final int MIM_BUTT_X = Gdx.graphics.getWidth() / 4 * 3 - 50;
    private static final int MIM_BUTT_Y = Gdx.graphics.getHeight() / 2;
    private static final String LABEL_TEXT = "Select skin";

    public SkinSelectScreen(Palace2D game, TextureHandler textureHandler, float blockSpeed) {
        super(game, textureHandler);
        createInfoLabel(LABEL_TEXT);
        createBackButton(new LevelSelectScreen(game, textureHandler));
        createImageButton("palace_button.png", new GameScreen(game,
                new PalaceTextureHandler(), blockSpeed), PALACE_BUTT_X, PALACE_BUTT_Y);
        createImageButton("mim_button.png", new GameScreen(game,
                new MIMTextureHandler(), blockSpeed), MIM_BUTT_X, MIM_BUTT_Y);
    }

    private void createImageButton(String texPath, GameScreen screen,
                                   float x, float y) {
        Texture buttonTexture = new Texture(Gdx.files.internal(texPath));
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        ImageButton button = new ImageButton(drawable);

        button.setPosition(x - button.getWidth() / 2, y - button.getHeight() / 2);
        button.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer,
                                int button) {
                game.setScreen(screen);
                screen.initMusic();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }
        });

        stage.addActor(button);
    }

}
