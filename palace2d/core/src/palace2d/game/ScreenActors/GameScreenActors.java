package palace2d.game.ScreenActors;

import palace2d.game.Graphics.PalaceTextureHandler;
import palace2d.game.Graphics.TextureHandler;

import java.util.*;

public class GameScreenActors {
    private static final int MAX_BLOCKS = 200;
    private static final int INIT_BLOCK_WIDTH = 578; // px

    public final int BLOCK_HEIGHT = 60; //px

    private int actualBlockNumber = 0;

    private static int actualStackLeftEdge; // px
    private static int actualStackRightEdge; // px

    private List<Block> blocks;

    private TextureHandler textureHandler;

    public GameScreenActors() {
        blocks = new ArrayList<>();
        textureHandler = new PalaceTextureHandler();
    }

    public void initGameBlocks() {
        Block newBlock = new Block(textureHandler.getActualTexture(getBlockWidth()));
        blocks.add(newBlock);
        newBlock.spritePos(newBlock.getX(), newBlock.getY());
    }


    public void setActualBlockPosition(float x, float y) {
        getActualBlock().spritePos(x, y);
    }

    public void setStackEdges(int width) {
        actualStackLeftEdge = width / 2 - INIT_BLOCK_WIDTH / 2;
        actualStackRightEdge = actualStackLeftEdge + INIT_BLOCK_WIDTH;
    }

    public void prepareNewBlock() {
        actualBlockNumber++;
    }


    public Block setNewBlock(int dropHeight) {
        prepareNewBlock();

        Block newBlock = new Block(textureHandler.getActualTexture(getBlockWidth()));
        blocks.add(newBlock);

        newBlock.trim(getBlockWidth());
        newBlock.spritePos(actualStackLeftEdge,
                blocks.get(actualBlockNumber - 1).getTop() + dropHeight);

        return newBlock;
    }


    public void setDroppedBlockSizeAndPosition() {
        actualStackLeftEdge = Math.max(actualStackLeftEdge, (int) getActualBlock().getX());
        actualStackRightEdge = Math.min(actualStackRightEdge,
                (int) getActualBlock().getX() + (int) getActualBlock().getWidth());

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
}
