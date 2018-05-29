package palace2d.game.ScreenActors;

import palace2d.game.Graphics.PalaceTextureHandler;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.Palace2D;

import java.util.concurrent.ThreadLocalRandom;

import java.util.*;

public class GameScreenActors {
    private static final int MAX_BLOCKS = 20;
    private static final int INIT_BLOCK_WIDTH = 578; // px
    private static final int BONUS_BLOCK_AFTER = 10;
    private static final int BONUS_MAX_PX_DIFF = 100;

    private static int actualStackLeftEdge; // px
    private static int actualStackRightEdge; // px

    private List<Block> blocks;
    private int palaceHeight = 0;
    private int palaceLeftEdge; // px
    private int palaceRightEdge; // px
    private int actualBlockNumber = 0;
    private int lastBonusBlockNumber = 0;
    private float lastBonusBlockWidth = INIT_BLOCK_WIDTH; // default = 578
    private boolean bonusActive = false;

    private TextureHandler textureHandler;

    public GameScreenActors(TextureHandler textureHandler) {
        blocks = new ArrayList<>();
        this.textureHandler = textureHandler;
    }

    public void initGameBlocks() {
        Block newBlock = new Block(textureHandler.getActualTexture(getBlockWidth()));
        blocks.add(newBlock);
        palaceHeight = textureHandler.getInitalBlockHeight();
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

    private int getStackSize() {
        return actualStackRightEdge - actualStackLeftEdge;
    }

    private boolean isActualStackSmallerThanPalaceTexture() {
        return getStackSize() < textureHandler.getActualTextureWidth();
    }

    private boolean canBeBonusObtain() {
        return isActualStackSmallerThanPalaceTexture() &&
                actualBlockNumber - lastBonusBlockNumber >= BONUS_BLOCK_AFTER &&
                lastBonusBlockWidth <=
                        blocks.get(actualBlockNumber - 1).getWidth() + BONUS_MAX_PX_DIFF;
    }

    private float calcBlockWidth() {
        if (canBeBonusObtain()) {
            bonusActive = true;
            lastBonusBlockNumber = actualBlockNumber;
            lastBonusBlockWidth = Math.min(getBlockWidth() + BONUS_MAX_PX_DIFF,
                    textureHandler.getActualTextureWidth());
            return lastBonusBlockWidth;
        } else {
            if (blocks.size() > lastBonusBlockNumber + BONUS_BLOCK_AFTER) {
                lastBonusBlockNumber++;
                lastBonusBlockWidth = blocks.get(lastBonusBlockNumber).getWidth();
            }

            return getBlockWidth();
        }
    }

    public Block setNewBlock(int dropHeight) {
        prepareNewBlock();
        float width = calcBlockWidth();

        Block newBlock = new Block(textureHandler.getActualTexture((int)width));
        blocks.add(newBlock);
        palaceHeight += newBlock.getHeight();

        newBlock.trim((int)width);
        int rand = randBlockPosition();
        Block block = blocks.get(actualBlockNumber - 1);
        if (rand > 0) {
            block.setLeft(false);
        }
        else {
            block.setLeft(true);
        }
        newBlock.spritePos(rand,
                block.getTop() + dropHeight);

        return newBlock;
    }


    public void setDroppedBlockSizeAndPosition() {
        if (!bonusActive) {
            actualStackLeftEdge = Math.max(actualStackLeftEdge, (int) getActualBlock().getX());
            actualStackRightEdge = Math.min(actualStackRightEdge,
                    (int) getActualBlock().getX() + (int) getActualBlock().getWidth());
        } else {
            bonusActive = false;
            actualStackLeftEdge = (int) getActualBlock().getX();
            actualStackRightEdge = (int) getActualBlock().getX() + (int) getActualBlock().getWidth();
        }
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
        return Math.min(getStackSize(), textureHandler.getActualTextureWidth());
    }

    public float getBlockHeight() {
        return blocks.get(actualBlockNumber).getHeight();
    }

    public boolean isBonusActive() {
        return bonusActive;
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
