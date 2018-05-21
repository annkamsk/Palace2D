package de.tomgrill.gdxtesting.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import de.tomgrill.gdxtesting.GdxTestRunner;
import palace2d.game.ScreenActors.Block;

@RunWith(GdxTestRunner.class)
public class BlockTest {

    private Block block;
    private Texture blockTexture;

    @Before
    public void setUp() {
        blockTexture = new Texture(Gdx.files.internal("../core/assets/block0.png"));
        block = new Block(blockTexture);
    }

    @Test
    public void blockConstructorTest() {
        assertNotNull(block);
    }

    @Test
    public void spriteTextureTest() {
        assertEquals(block.getSprite().getTexture(), blockTexture);
    }

    private void posTest(float x, float y) {
        block.spritePos(x, y);
        assertEquals(x, block.getSprite().getX(), 0f);
        assertEquals(y, block.getSprite().getY(), 0f);
    }

    @Test
    public void spritePosTest() {
        posTest(20, 20);
        posTest(0, 0);
        posTest(-20, -20);
    }

    private void trimTest(int width) {
        block.trim(width);
        assertEquals(width, block.getSprite().getWidth(), 0f);
    }

    @Test
    public void spriteTrimTest() {
        trimTest(10);
        trimTest(100);
        trimTest(0);
    }
}

