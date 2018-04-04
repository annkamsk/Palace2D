package palace2d.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import palace2d.game.Block;
import palace2d.game.Palace2D;

import java.awt.*;
import java.util.ArrayList;


public class GameScreen implements Screen {
    private Stage stage;
    private Palace2D game;

    private Viewport viewport;
    private ArrayList<Texture> blockTextures;
    private ArrayList<Block> blocks;

    public GameScreen(Palace2D game) {
        this.game = game;
        viewport = new FitViewport(800, 600);
        stage = new Stage(new ScreenViewport());
        createGameObjects();
    }

    private void createGameObjects() {
        /* creating background */
        Texture backgroundTexture = new Texture(Gdx.files.internal
                ("background.png"));
        stage.addActor(getActorFromTexture(backgroundTexture, 0, 0, Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()));


        /* creating block textures */
        blockTextures = new ArrayList<>();
        blockTextures.add(new Texture(Gdx.files.internal("block0.png")));
        blockTextures.add(new Texture(Gdx.files.internal("block1.png")));

        /* creating blocks */
        blocks = new ArrayList<>();
        createBlock(0,111, 10, 578, 60);
        createBlock(1,111, 100, 578, 60);
    }


    private Actor getActorFromTexture(Texture tex, int x, int y, int w, int h) {
        TextureRegion texRegion = new TextureRegion(tex,
                x, y, w, h);
        return new Image(texRegion);
    }

    private void createBlock(int idx, int x, int y, int w, int h) {
        Texture blockTexture = new Texture(Gdx.files.internal("block" + idx  +
                ".png"));
        blockTextures.add(blockTexture);
        Block block = new Block(blockTexture, idx);
        blocks.add(block);
        block.spritePos(x, y);

        if (idx != 0) {
            createBlockActions(block);
        }

        stage.addActor(block);
    }

    private void createBlockActions(Block block) {
        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(Actions.moveTo(0, block.getY(), 2f));
        overallSequence.addAction(Actions.moveTo(800 - block.getWidth(),
                block.getY(),2f));
        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequence);
        block.addAction(infiniteLoop);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
