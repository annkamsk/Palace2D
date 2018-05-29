package palace2d.game.Graphics;

import java.util.*;

public class MIMTextureHandler extends TextureHandler {
    private static final int INITIAL_BLOCK_HEIGHT = 25; // px


    public MIMTextureHandler() {
        super("mim");
    }

    public int getInitalBlockHeight() {
        return INITIAL_BLOCK_HEIGHT;
    }


    void initTextureChangeHandling() {
        // textures used for only one block
        List<Integer> oneTimeTextureIndices = Arrays.asList(0,
                2, 3, 4, 6, 7, 9, 11, 13, 15, 16);

        // textures used for multiple blocks
        List<Integer> multipleTimesTextureIndices = Arrays.asList(1, 5, 8,
                10, 12, 14);

        oneTimeTextureIndices.forEach(i -> textureChangeHandlers.put(i,
                blockWidth -> true));

        multipleTimesTextureIndices.forEach(i -> textureChangeHandlers.put(i,
         blockWidth -> blockWidth < getActualTextureWidth() - 100));

        textureChangeHandlers.put(textures.size() - 1, (blockWidth) -> false);
    }

    void setBackgroundTexture() {
        backgroundTexture = createTexture("background_mim.png");
    }

    @Override
    public String getScoreLabelText() {
        return "ELITARNOSC: ";
    }

    public String getFinalScoreLabelText() {
        return "YOUR ELITARNOSC: ";
    }


}
