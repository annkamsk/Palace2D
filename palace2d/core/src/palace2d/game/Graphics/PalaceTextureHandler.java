package palace2d.game.Graphics;

import com.badlogic.gdx.graphics.Texture;

public class PalaceTextureHandler extends TextureHandler {

    public PalaceTextureHandler() {
        super("blocks");
    }

    void initTextureChangeHandling() {
        textureChangeHandlers.put(0, (blockWidth) -> true);
        textureChangeHandlers.put(1, (blockWidth) -> true);
        textureChangeHandlers.put(2, (blockWidth) ->
                blockWidth < getActualTextureWidth() - 100);
        textureChangeHandlers.put(3, (blockWidth) ->
                blockWidth < getActualTextureWidth() - 100);
        for (int i = 4; i < textures.size() - 1; ++i) {
            textureChangeHandlers.put(i, (blockWidth) ->
                    blockWidth <= getNextTextureWidth());
        }
        textureChangeHandlers.put(textures.size() - 1, (blockWidth) -> false);
    }

}
