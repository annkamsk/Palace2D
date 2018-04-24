package de.tomgrill.gdxtesting.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import de.tomgrill.gdxtesting.GdxTestRunner;
import palace2d.game.Block;

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

    @Test
    public void spritePosTest() {
        block.spritePos(10f, 10f);
        assertEquals(10f, block.getSprite().getX(), 0f);
        assertEquals(10f, block.getSprite().getY(), 0f);

        block.spritePos(0f, 0f);
        assertEquals(0f, block.getSprite().getY(), 0f);
        assertEquals(0f, block.getSprite().getY(), 0f);
    }

    @Test
    public void spriteTrimTest() {
        block.trim(10);
        assertEquals(10f, block.getSprite().getWidth(), 0f);

        block.trim(100);
        assertEquals(100f, block.getSprite().getWidth(), 0f);
    }
}

