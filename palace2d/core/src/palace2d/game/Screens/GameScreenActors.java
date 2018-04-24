package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import palace2d.game.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreenActors {
    private static final int MAX_BLOCKS = 5;
    private static final int INIT_BLOCK_WIDTH = 578; // px

    private int actualBlockNumber = 0;

    private static int actualStackLeftEdge; // px
    private static int actualStackRightEdge; // px

    private List<Block> blocks;

    public GameScreenActors() {
        blocks = new ArrayList<Block>();
    }

    public void setStackEdges(int width) {
        actualStackLeftEdge = width / 2 - INIT_BLOCK_WIDTH / 2;
        actualStackRightEdge = actualStackLeftEdge + INIT_BLOCK_WIDTH;
    }

    public Texture createTexture(String filename) {
        return new Texture(Gdx.files.internal(filename));
    }

    private void addInvisibleBlock(Texture blockTexture, int x, int y) {
        Block block = new Block(blockTexture);
        blocks.add(block);

        block.spritePos(x, y);
        block.setVisible(false);
    }

    public void initGameBlocks(Texture blockTexture) {
        for (int i = 0; i < MAX_BLOCKS; i++) {
            addInvisibleBlock(blockTexture, actualStackLeftEdge, 100);
        }
    }

    public void setDroppedBlockSizeAndPosition() {
        actualStackLeftEdge = Math.max(actualStackLeftEdge, (int) getActualBlock().getX());
        actualStackRightEdge = Math.min(actualStackRightEdge,
                (int) getActualBlock().getX() + (int) getActualBlock().getWidth());

        trimActualBlock();
        setActualBlockPosition(actualStackLeftEdge, getActualBlock().getY());
    }

    public void prepareNewBlock() {
        actualBlockNumber++;
    }

    public Block setNewBlock(int dropHeight) {
        Block newBlock = getActualBlock();

        newBlock.trim(getBlockWidth());
        newBlock.spritePos(actualStackLeftEdge,
                blocks.get(actualBlockNumber - 1).getTop() + dropHeight);

        return newBlock;
    }

    public void setActualBlockPosition(float x, float y) {
        getActualBlock().spritePos(x, y);
    }

    public void trimActualBlock() {
        if (actualStackLeftEdge < actualStackRightEdge)
            getActualBlock().trim(getBlockWidth());
    }

    public void setActualBlockVisible() {
        getActualBlock().setVisible(true);
    }

    public int getBlockWidth() {
        return actualStackRightEdge - actualStackLeftEdge;
    }

    public int getActualBlockNumber() {
        return actualBlockNumber;
    }

    public Block getActualBlock() {
        return blocks.get(actualBlockNumber);
    }

    public Iterator<Block> getBlocksIterator() {
        return blocks.iterator();
    }

    public boolean hasNextBlock() {
        return actualBlockNumber == MAX_BLOCKS;
    }
}
