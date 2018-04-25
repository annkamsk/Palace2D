package palace2d.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import palace2d.game.Block;
import palace2d.game.Palace2D;
import palace2d.game.ScreenActors.GameScreenActors;

import java.util.Iterator;

public class GameScreen implements Screen {
    private static final int DROP_HEIGHT = 20; // px
    private static final float BLOCK_DROP_DURATION = 0.25f;
    private static final float BLOCK_MOVE_DURATION = 1f;
    private static final String backgroundTextureFile = "background.png";
    private static final String blockTextureFile = "block0.png";

    private Stage stage;
    private Palace2D game;
    private GameScreenActors actors;

    public GameScreen(Palace2D game) {
        this.game = game;
        stage = new Stage(new FitViewport(Palace2D.V_WIDTH, Palace2D.V_HEIGHT));
        actors = new GameScreenActors();
        createGameObjects();
    }


    private boolean gameLost() {
        return actors.getBlockWidth() <= 0;
    }

    private boolean gameWon() {
        return !actors.hasNextBlock();
    }

    private boolean gameContinues() {
        return !gameLost() && !gameWon();
    }


    private void stageKeyboardPrepare() {
        /* make stage catch keyboard events and pass it to block */
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Gdx.app.log("Image ClickListener", "keyDown. keycode=" + keycode);
                return true;
            }
        });
    }

    private void createEndGameButton() {
        TextButton endButton = new TextButton("END GAME", new Skin(Gdx.files
                .internal
                        ("skins/glassy/skin/glassy-ui.json")), "small");
        endButton.setBounds(endButton.getWidth() / 10, Gdx.graphics.getHeight
                        () - endButton.getHeight(),
                100,
                50);
        endButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new EndGameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(endButton);
    }

    private void setBackgroundTexture() {
        Texture backgroundTexture = actors.createTexture(backgroundTextureFile);

        stage.addActor(getActorFromTexture(backgroundTexture, 0, 0, Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()));

        createEndGameButton();
        stageKeyboardPrepare();
        actors.setStackEdges(backgroundTexture.getWidth());
    }

    private void setActors() {
        Iterator<Block> iter = actors.getBlocksIterator();
        while (iter.hasNext()) {
            stage.addActor(iter.next());
        }
    }

    private void createGameObjects() {
        setBackgroundTexture();
        initGameBlocks();
        setActors();
    }

    private void initGameBlocks() {
        actors.initGameBlocks(actors.createTexture(blockTextureFile));
        actors.setActualBlockPosition(111, 10);
        actors.setActualBlockVisible();
        actors.prepareNewBlock();
    }

    private Actor getActorFromTexture(Texture tex, int x, int y, int w, int h) {
        TextureRegion texRegion = new TextureRegion(tex,
                x, y, w, h);

        return new Image(texRegion);
    }

    private Action sideToSideAction(Block block) {
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
        Block newBlock = actors.setNewBlock(DROP_HEIGHT);

        newBlock.addAction(sideToSideAction(b));

        stage.setKeyboardFocus(b);

        newBlock.setVisible(true);
        newBlock.addListener(new InputListener() {
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
                                            dropAction();
                                            return true;
                                        }
                                    }
                            ));
                    return true;
                }
                return false;
            }
        });
    }

    private void dropAction() {
        actors.setDroppedBlockSizeAndPosition();
        actors.prepareNewBlock();

        if (gameContinues()) {
            Gdx.app.log("info",
                    "I DROPPED BLOCK NR " + (actors.getActualBlockNumber() - 1));

            makeBlockReady(actors.getActualBlock());
        } else {
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
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        /* Assign first block ready, after dropping it will cascade */
        makeBlockReady(actors.getActualBlock());
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
