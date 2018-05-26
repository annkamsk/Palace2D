package palace2d.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import palace2d.game.ScreenActors.Block;
import palace2d.game.Palace2D;

import java.util.Iterator;


public class GameScreen extends PalaceScreen {
    private static final int DROP_HEIGHT = 20; // px
    private static final float BLOCK_DROP_DURATION = 0.25f;
    private static final float BLOCK_MOVE_DURATION = 1f;
    private static final float CAMERA_SMOOTH = 1f;

    // TODO przerobić na klasy stan, w których będą przechowywane funkcje
    // wyświetlające komunikaty
    private int WON = 1;
    private int LOST = -1;
    private int GAVEUP = 0;

    private TextButton endButton;

    public GameScreen(Palace2D game) {
        super(game, "background.png");
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

    void createGameObjects() {
        setBackgroundTexture();
        initGameBlocks();
        setActors();
    }

    private void setActors() {
        Iterator<Block> iter = actors.getBlocksIterator();
        while (iter.hasNext()) {
            stage.addActor(iter.next());
        }
    }

    private void setActualActor() {
        stage.addActor(actors.getActualBlock());
    }

    private void initGameBlocks() {
        actors.initGameBlocks();
        actors.setActualBlockPosition(111, 10);
        makeBlockReady(actors.setNewBlock(DROP_HEIGHT));
    }

    private void makeBlockReady(Block block) {
        block.addAction(sideToSideAction(block));
        stage.setKeyboardFocus(block);

        block.addListener(new InputListener() {
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

                    if (myBlock.getY() > 4 * actors.BLOCK_HEIGHT) {
                        moveView(0f, actors.BLOCK_HEIGHT);
                    }

                    return true;
                }
                return false;
            }
        });
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


    private void dropAction() {
        actors.setDroppedBlockSizeAndPosition();
        if (gameContinues()) {
            Gdx.app.log("info",
                    "I DROPPED BLOCK NR " + (actors.getActualBlockNumber()));

            makeBlockReady(actors.setNewBlock(DROP_HEIGHT));
            setActualActor();

        } else {
            if (gameWon()) {
                Gdx.app.log("info", "YOU WIN");
                Gdx.app.log("info", "YOU LOSE");
                game.setScreen(new EndGameScreen(game,actors
                        .getActualBlockNumber() - 1, WON, actors));
            } else {
                Gdx.app.log("info", "YOU LOSE");
                game.setScreen(new EndGameScreen(game,actors
                        .getActualBlockNumber() - 1, LOST, actors));
            }
        }
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
        endButton = new TextButton("END GAME", new Skin(Gdx.files
                .internal
                        ("skins/glassy/skin/glassy-ui.json")), "small");
        endButton.setBounds(endButton.getWidth() / 10,
                Gdx.graphics.getHeight() - endButton.getHeight(), 100, 50);
        endButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new EndGameScreen(game, actors
                        .getActualBlockNumber() - 1, GAVEUP, actors));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(endButton);
    }

    @Override
    void setBackgroundTexture() {
        super.setBackgroundTexture();
        createEndGameButton();
        stageKeyboardPrepare();
        actors.setStackEdges(backgroundTexture.getWidth());
    }

    private void moveView(float x, float y) {
        camera.moveBy(x, y);
        backgroundImg.addAction(Actions.moveBy(x, y));
        endButton.addAction(Actions.moveBy(x, y));
    }


    @Override
    public void show() {
        super.show();
        makeBlockReady(actors.getActualBlock());
    }
}
