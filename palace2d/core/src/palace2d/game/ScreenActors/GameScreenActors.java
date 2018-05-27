package palace2d.game.ScreenActors;

import palace2d.game.Graphics.PalaceTextureHandler;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.Palace2D;

import java.util.concurrent.ThreadLocalRandom;

import java.util.*;

public class GameScreenActors {
    private static final int MAX_BLOCKS = 200;
    private static final int INIT_BLOCK_WIDTH = 578; // px

    public final int BLOCK_HEIGHT = 60; //px

    private int actualBlockNumber = 0;

    private static int actualStackLeftEdge; // px
    private static int actualStackRightEdge; // px

    private List<Block> blocks;
    private int palaceHeight = 0;
    private int palaceLeftEdge; // px
    private int palaceRightEdge; // px

    private TextureHandler textureHandler;

    public GameScreenActors() {
        blocks = new ArrayList<>();
        textureHandler = new PalaceTextureHandler();
    }

    public void initGameBlocks() {
        Block newBlock = new Block(textureHandler.getActualTexture(getBlockWidth()));
        blocks.add(newBlock);
        palaceHeight = BLOCK_HEIGHT;
        newBlock.spritePos(newBlock.getX(), newBlock.getY());
    }


    public void setActualBlockPosition(float x, float y) {
        getActualBlock().spritePos(x, y);
    }

    public void setStackEdges(int width) {
        actualStackLeftEdge = width / 2 - INIT_BLOCK_WIDTH / 2;
        actualStackRightEdge = actualStackLeftEdge + INIT_BLOCK_WIDTH;
        palaceLeftEdge = actualStackLeftEdge;
        palaceRightEdge = actualStackRightEdge;
    }

    public void prepareNewBlock() {
        actualBlockNumber++;
    }

    private int randBlockPosition() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);

        if (randomNum > 0) {
            return Palace2D.V_WIDTH + getBlockWidth();
        }

        return (-1) * getBlockWidth();
    }

    public Block setNewBlock(int dropHeight) {
        prepareNewBlock();

        Block newBlock = new Block(textureHandler.getActualTexture(getBlockWidth()));
        blocks.add(newBlock);
        palaceHeight += newBlock.getHeight();

        newBlock.trim(getBlockWidth());
        newBlock.spritePos(randBlockPosition(),
                blocks.get(actualBlockNumber - 1).getTop() + dropHeight);

        return newBlock;
    }


    public void setDroppedBlockSizeAndPosition() {
        actualStackLeftEdge = Math.max(actualStackLeftEdge, (int) getActualBlock().getX());
        actualStackRightEdge = Math.min(actualStackRightEdge,
                (int) getActualBlock().getX() + (int) getActualBlock().getWidth());
        palaceLeftEdge = Math.min(actualStackLeftEdge, palaceLeftEdge);
        palaceRightEdge = Math.max(actualStackRightEdge, palaceRightEdge);

        trimActualBlock();
        setActualBlockPosition(actualStackLeftEdge, getActualBlock().getY());
    }

    private void trimActualBlock() {
        if (actualStackLeftEdge < actualStackRightEdge)
            getActualBlock().trim(getBlockWidth());
    }


    public int getActualBlockNumber() {
        return actualBlockNumber;
    }

    public Block getActualBlock() {
        return blocks.get(actualBlockNumber);
    }

    public int getBlockWidth() {
        return Math.min(actualStackRightEdge - actualStackLeftEdge,
                textureHandler.getActualTextureWidth());
    }

    public Iterator<Block> getBlocksIterator() {
        return blocks.iterator();
    }

    public boolean hasNextBlock() {
        return !(actualBlockNumber == MAX_BLOCKS);
    }

    public int getPalaceHeight() {
        return palaceHeight;
    }

    public int getPalaceWidth() {
        return palaceRightEdge - palaceLeftEdge;
    }
}
