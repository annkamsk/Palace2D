package palace2d.game.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

public class PalaceTextureHandler extends TextureHandler {

    public PalaceTextureHandler() {
        super();
    }

    public void initTextures() {
        try (Stream<Path> paths = Files.walk(Paths.get("blocks")
        )) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach((file) -> textures.add(createTexture(file
                            .toString())));

            /* sort textures according to their file name */
            Collections.sort(textures, Comparator.comparing(Object::toString));
        } catch (IOException e) {
            Gdx.app.log("info",
                    "Block textures missing from core/assets/blocks directory" +
                    ".");
        }
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


    public Texture getActualTexture(int blockWidth) {
        if (isTextureChanging(blockWidth)) {
            return textures.get(++actualTextureNumber);
        }
        return textures.get(actualTextureNumber);
    }

    private boolean isTextureChanging(int blockWidth) {
        return textureChangeHandlers.get(actualTextureNumber).test(blockWidth);
    }
}
