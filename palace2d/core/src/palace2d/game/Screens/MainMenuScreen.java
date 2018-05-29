package palace2d.game.Screens;

import palace2d.game.Graphics.MIMTextureHandler;
import palace2d.game.Graphics.PalaceTextureHandler;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.Palace2D;

public class MainMenuScreen extends PalaceScreen {

    public MainMenuScreen(final Palace2D game, TextureHandler textureHandler) {
        super(game, textureHandler);
        createNewGameButton();
    }
}
