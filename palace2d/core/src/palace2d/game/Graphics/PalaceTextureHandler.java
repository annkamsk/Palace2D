package palace2d.game.Graphics;

import palace2d.game.Palace2D;

public class PalaceTextureHandler extends TextureHandler {
    private static final int INITIAL_BLOCK_HEIGHT = 60; // px

    public PalaceTextureHandler() {
        super("blocks");
    }

    public int getInitalBlockHeight() {
        return INITIAL_BLOCK_HEIGHT;
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

    void setBackgroundTexture() {
        String filename = "background.png";
        if (Palace2D.TEST_MOD) {
            filename = "../core/assets/" + filename;
        }

        backgroundTexture = createTexture(filename);
    }

}
