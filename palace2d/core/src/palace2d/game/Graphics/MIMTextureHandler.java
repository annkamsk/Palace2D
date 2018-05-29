package palace2d.game.Graphics;

import java.util.*;

public class MIMTextureHandler extends TextureHandler {

    public MIMTextureHandler() {
        super("mim");
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


}
