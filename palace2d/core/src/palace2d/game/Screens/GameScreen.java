package palace2d.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import palace2d.game.Block;
import palace2d.game.Palace2D;

import java.util.ArrayList;


/**
 * TODO TODO TODO
 * poniewaz bylo duzo problemow zeby zgrać akcje spadania klocków
 * zrobilem to tak, ze na poczatku gry generuje MAX_BLOCKS klockow,
 * są ukryte i potem upadający uwidacznia kolejnego.
 * TODO TODO TODO
 */


public class GameScreen implements Screen {
    private static final int MAX_BLOCKS = 50;
    private static final int INIT_BLOCK_WIDTH = 578; // px
    private static final int BLOCK_HEIGHT = 60; // px
    private static final int DROP_HEIGHT = 20; // px
    private static final float BLOCK_DROP_DURATION = 0.25f;
    private static final float BLOCK_MOVE_DURATION = 1f;
    private static final float CAMERA_SMOOTH = 1f; // lower the smoother

    private static int actualBlockNumber = 0;
    private static int actualStackLeftEdge; // px
    private static int actualStackRightEdge; // px

    private Stage stage;
    private Palace2D game;
    private Image backgroundImg;
    private ArrayList<Texture> blockTextures;
    private ArrayList<Block> blocks;


    public GameScreen(Palace2D game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(Palace2D.V_WIDTH, Palace2D.V_HEIGHT, this.game.camera));
        createGameObjects();
    }


    private void SetStackEdges(Texture background) {
        int width = background.getWidth();
        actualStackLeftEdge = width / 2 - INIT_BLOCK_WIDTH / 2;
        actualStackRightEdge = actualStackLeftEdge + INIT_BLOCK_WIDTH;
    }

    private int blockWidth() {
        return actualStackRightEdge - actualStackLeftEdge;
    }

    private boolean gameLost() {
        return blockWidth() <= 0;
    }

    private boolean gameWon() {
        return actualBlockNumber == MAX_BLOCKS;
    }

    private boolean gameContinues() {
        return !gameLost() && !gameWon();
    }


    private void stageKeyboardPrepare() {
        /* make stage process keyboard events */
        Gdx.input.setInputProcessor(stage);

        /* make stage catch keyboard events and pass it to block */
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Gdx.app.log("Image ClickListener", "keyDown. keycode=" + keycode);
                return true;
            }
        });
    }

    private void createGameObjects() {
        /* creating background */
        Texture backgroundTexture = new Texture(Gdx.files.internal
                ("background.png"));

        backgroundImg = getActorFromTexture(backgroundTexture, 0, 0, Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(backgroundImg);

        stageKeyboardPrepare();

        SetStackEdges(backgroundTexture);

        /* creating block textures */
        // TODO bedziemy z tego korzystac?
        blockTextures = new ArrayList<Texture>();
        blockTextures.add(new Texture(Gdx.files.internal("block0.png")));
        blockTextures.add(new Texture(Gdx.files.internal("block1.png")));

        /* creating blocks */
        blocks = new ArrayList<Block>();
        Block first = createBlock(0, 111, 10, 578, 60);

        for (int i = 1; i <= MAX_BLOCKS; i++) {
            createBlock(i, actualStackLeftEdge, 100, blockWidth(), BLOCK_HEIGHT);
        }

        first.setVisible(true); /* first block is already set */

        /* Assign first block ready, after dropping it will cascade */
        makeBlockReady(blocks.get(1));
    }


    private Image getActorFromTexture(Texture tex, int x, int y, int w, int h) {
        TextureRegion texRegion = new TextureRegion(tex,
                x, y, w, h);
        return new Image(texRegion);
    }

    /**
     * Creates invisible block with no actions.
     * Adds it to 'blocks' list, focuses stage on it and assign as an actor.
     *
     * @return Created block
     */
    private Block createBlock(int idx, int x, int y, int w, int h) {
//        Texture blockTexture = new Texture(Gdx.files.internal("block" + idx +
//                ".png"));
        // TODO to trzeba bedzie potem zmienic
        Texture blockTexture = new Texture(Gdx.files.internal("block0.png"));

        // TODO bedziemy z tego korzystac?
        // blockTextures.add(blockTexture);

        Block block = new Block(blockTexture, idx);
        blocks.add(block);
        block.spritePos(x, y);
        block.setVisible(false);
        stage.addActor(block);
        return block;
    }


    private Action SideToSideAction(Block block) {
        SequenceAction overallSequence = new SequenceAction();
        overallSequence.addAction(Actions.moveTo(0, block.getY(), BLOCK_MOVE_DURATION));
        overallSequence.addAction(Actions.moveTo(Palace2D.V_WIDTH - block.getWidth(),
                block.getY(), BLOCK_MOVE_DURATION));
        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequence);
        return infiniteLoop;
    }


    /**
     * Makes existing but hidden block ready to be shown.
     */
    private void makeBlockReady(Block b) {
        Block previous = blocks.get(b.getIdx() - 1);
        b.trim(blockWidth());
        b.spritePos(actualStackLeftEdge, previous.getTop() + DROP_HEIGHT);
        b.addAction(SideToSideAction(b));
        stage.setKeyboardFocus(b);
        b.setVisible(true);
        b.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    Actor myBlock = event.getTarget();
                    myBlock.clear();
                    stage.unfocus(myBlock);
                    myBlock.addAction(
                            Actions.sequence(
                                    Actions.moveTo(myBlock.getX(),
                                            myBlock.getY() - DROP_HEIGHT, BLOCK_DROP_DURATION)
                                    , new Action() {
                                        @Override
                                        public boolean act(float delta) {
                                            Block me = (Block) this.getTarget();
                                            dropAction(me);
                                            return true;
                                        }
                                    }
                            ));

                    if (myBlock.getY() > 4 * BLOCK_HEIGHT) {
                        moveCamera(0f, BLOCK_HEIGHT);
                    }

                    return true;
                }
                return false;
            }
        });
    }

    private void increaseScore() {
        game.score++;
        game.scoreName = "score: " + game.score;
    }

    private void moveCamera(float x, float y) {
        Vector3 cameraPosition = stage.getViewport().getCamera().position;
        Vector3 target = new Vector3(x, y, 0.f);
        target.add(cameraPosition);

        cameraPosition.lerp(target, CAMERA_SMOOTH);
        backgroundImg.addAction(Actions.moveBy(x, y));
    }

    private void dropAction(Block me) {
        ++actualBlockNumber;
        actualStackLeftEdge = Math.max(actualStackLeftEdge, (int) me.getX());
        actualStackRightEdge = Math.min(actualStackRightEdge, (int) me.getX() + (int) me.getWidth());
        me.trim(blockWidth());
        me.spritePos(actualStackLeftEdge, me.getY());
        if (gameContinues()) {
            Gdx.app.log("info",
                    "I DROPPED BLOCK NR " + actualBlockNumber);
            Block next = blocks.get(me.getIdx() + 1);
            makeBlockReady(next);
            increaseScore();
        } else {
            /* KONIEC GRY */
            if (gameWon())
                Gdx.app.log("info", "YOU WIN");
            else
                Gdx.app.log("info", "YOU LOSE");
        }
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        game.batch.begin();
        game.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        game.font.draw(game.batch, game.scoreName, 25, 100);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
