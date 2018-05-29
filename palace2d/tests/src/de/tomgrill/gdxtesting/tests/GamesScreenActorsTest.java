package de.tomgrill.gdxtesting.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;
import palace2d.game.Graphics.PalaceTextureHandler;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.Palace2D;
import palace2d.game.ScreenActors.Block;
import palace2d.game.ScreenActors.GameScreenActors;

import java.util.Iterator;

@RunWith(GdxTestRunner.class)
public class GamesScreenActorsTest {
    private static final int TEST_BLOCK_WIDTH = 578;
    private static final int TEST_DROP_HEIGHT = 20;

    private GameScreenActors actors;

    @BeforeClass
    public static void setUpTestMod() {
        Palace2D.TEST_MOD = true;
    }

    @AfterClass
    public static void restoreTestMod() {
        Palace2D.TEST_MOD = false;
    }

    @Before
    public void setUp() {
        actors = new GameScreenActors(new PalaceTextureHandler());
        actors.initGameBlocks();
    }

    @Test
    public void createTextureTest() {
        String path = "../core/assets/blocks/block00.png";
        assertTrue("No such a file or directory",
                Gdx.files.internal(path).exists());

        Texture testTexture = TextureHandler.createTexture(path);
        Pixmap result = new Pixmap(new FileHandle(path));

        assertEquals(testTexture.getTextureData().getHeight(), result.getHeight());
        assertEquals(testTexture.getTextureData().getWidth(), result.getWidth());
    }

    @Test
    public void initGameBlocksTest() {
        assertEquals(actors.getActualBlockNumber(), 0);
        assertTrue(actors.hasNextBlock());
    }

    private void testPosition(Block block, int x, int y) {
        actors.setActualBlockPosition(x, y);
        assertEquals(block.getX(), x, 0);
        assertEquals(block.getY(), y, 0);
    }

    @Test
    public void setActualBlockPositionTest() {
        Block block = actors.getActualBlock();

        testPosition(block, -20, -20);
        testPosition(block, 20, 20);
        testPosition(block, 0, 0);
    }

    private void testStackEdges(int width) {
        actors.setStackEdges(width);
        assertEquals(actors.getBlockWidth(), TEST_BLOCK_WIDTH);
    }

    @Test
    public void setStackEdgesTest() {
        testStackEdges(1000);
        testStackEdges(-1000);
        testStackEdges(0);
    }

    private void simulateBlockDrop() {
        Block block = actors.getActualBlock();
        actors.setStackEdges(TEST_BLOCK_WIDTH);

        block.setPosition(block.getX() - 20, block.getY());
        actors.setDroppedBlockSizeAndPosition();
    }

    /*
     * Tests the stack`s edges after cut the previous block.
     */
    @Test
    public void stackSizeTestAfterCutThePrevious() {
        simulateBlockDrop();

        assertEquals(actors.getBlockWidth(), TEST_BLOCK_WIDTH - 20);
    }

    /*
     * Test the size of next block after drop the previous one.
     */
    @Test
    public void blockPositionTestAfterDropThePrevious() {
        simulateBlockDrop();

        actors.prepareNewBlock();

        Block nextBlock = actors.setNewBlock(TEST_DROP_HEIGHT);
        assertEquals(actors.getBlockWidth(), TEST_BLOCK_WIDTH - 20);
    }

    @Test
    public void getActualBlockAndIteratorTest() {
        Iterator<Block> iter = actors.getBlocksIterator();
        assertTrue(actors.hasNextBlock());

        assertEquals(iter.next(), actors.getActualBlock());
    }
}
