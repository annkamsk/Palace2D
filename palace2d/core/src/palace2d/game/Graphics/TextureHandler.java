package palace2d.game.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public abstract class TextureHandler {
    List<Texture> textures;
    int actualTextureNumber = 0;
    static Map<Integer, Predicate<Integer>> textureChangeHandlers;

    public TextureHandler() {
        textures = new ArrayList<>();
        textureChangeHandlers = new HashMap<>();
        initTextures();
        initTextureChangeHandling();
    }

    public int getActualTextureWidth() {
        return textures.get(actualTextureNumber).getWidth();
    }

    int getNextTextureWidth() {
        return textures.get(actualTextureNumber + 1).getWidth();
    }

    public static Image getActorFromTexture(Texture tex, int x, int y, int w,
                                           int h) {
        TextureRegion texRegion = new TextureRegion(tex,
                x, y, w, h);
        return new Image(texRegion);
    }


    public static Texture createTexture(String filename) {
        return new Texture(Gdx.files.internal(filename)) {
            @Override
            public String toString() {
                return filename;
            }
        };
    }

    public abstract void initTextures();
    abstract void initTextureChangeHandling();
    public abstract Texture getActualTexture(int blockWidth);

}
