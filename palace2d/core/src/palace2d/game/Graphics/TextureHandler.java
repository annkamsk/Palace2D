package palace2d.game.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import palace2d.game.Palace2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static palace2d.game.Palace2D.TEST_MOD;

public abstract class TextureHandler {
    List<Texture> textures;
    Texture backgroundTexture;
    int actualTextureNumber = 0;
    static Map<Integer, Predicate<Integer>> textureChangeHandlers;

    public TextureHandler(String directoryName) {
        textures = new ArrayList<>();
        textureChangeHandlers = new HashMap<>();
        initTextures(directoryName);
        initTextureChangeHandling();
        setBackgroundTexture();
    }

    public void initTextures(String directoryName) {
        String path = directoryName;

        if (TEST_MOD) {
            path = "../core/assets/" + directoryName;
        }

        try (Stream<Path> paths = Files.walk(Paths.get(path)
        )) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach((file) -> textures.add(createTexture(file
                            .toString())));

            /* sort textures according to their file name */
            Collections.sort(textures, Comparator.comparing(Object::toString));
        } catch (IOException e) {
            Gdx.app.log("info",
                    "Textures missing from core/assets/" + directoryName +
                    "directory.");
        }
    }

    abstract void setBackgroundTexture();

    public Texture getBackgroundTexture() {
        return backgroundTexture;
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

    /**
     * For each texture creates predicates deciding when the texture should
     * be changed. Puts predicates in textureChangeHandlers map.
     */
    abstract void initTextureChangeHandling();

    public abstract int getInitalBlockHeight();

    public Texture getActualTexture(int blockWidth) {
        if (isTextureChanging(blockWidth)) {
            return textures.get(++actualTextureNumber);
        }
        return textures.get(actualTextureNumber);
    }

    private boolean isTextureChanging(int blockWidth) {
        return textureChangeHandlers.get(actualTextureNumber).test(blockWidth);
    }

    public String getScoreLabelText() {
        return "SCORE: ";
    }

    public String getFinalScoreLabelText() {
        return "YOUR SCORE: ";
    }

}
