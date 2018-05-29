package palace2d.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import palace2d.game.Graphics.MIMTextureHandler;
import palace2d.game.Graphics.PalaceTextureHandler;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.ScreenActors.Block;
import palace2d.game.Palace2D;

import java.util.Iterator;


public class GameScreen extends PalaceScreen {
    private static final int DROP_HEIGHT = 20; // px
    private static final float BLOCK_DROP_DURATION = 0.25f;
    private static final float CAMERA_SMOOTH = 1f;

    // TODO przerobić na klasy stan, w których będą przechowywane funkcje
    // wyświetlające komunikaty
    private int WON = 1;
    private int LOST = -1;
    private int GAVEUP = 0;

    public enum State
    {
        PAUSE,
        RUN
    }

    private State state = State.RUN;
    private TextButton endButton;
    private TextButton pauseButton;
    private Label scoreLabel;
  
    private int score = 0;

    private Container<Label> bonusBlockLabel;
    private float blockMoveDuration = 0.8f;

    public GameScreen(Palace2D game, TextureHandler textureHandler,
                      float blockMoveDuration) {
        super(game, textureHandler);
        this.blockMoveDuration = blockMoveDuration;
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

    @Override
    public void pause() {
        actors.getActualBlock().clearActions();
    }

    @Override
    public void resume() {
        Block block = actors.getActualBlock();
        block.addAction(sideToSideAction(block));
    }

    void createGameObjects() {
        setBackgroundTexture();
        createBonusBlockLabel();
        initGameBlocks();
        setActors();
        setScoreLabel();
    }

    private void createBonusBlockLabel() {
        String title = "BONUS BLOCK SIZE ACTIVATED!";
        Label label = new Label(title, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        bonusBlockLabel = new Container<>(label);
        bonusBlockLabel.setTransform(true);
        bonusBlockLabel.setSize(100, 60); // TODO jakos ladnie te px zrobic
        bonusBlockLabel.setOrigin(bonusBlockLabel.getWidth() / 2,
                bonusBlockLabel.getHeight() / 2);
        bonusBlockLabel.setPosition(
                Gdx.graphics.getWidth() / 2 - bonusBlockLabel.getWidth() / 2,
                5 * textureHandler.getInitalBlockHeight() + 150);
        bonusBlockLabel.setVisible(false);
        stage.addActor(bonusBlockLabel);
    }

    private void showBonusBlockLabelAction() {
        SequenceAction seq = new SequenceAction(Actions.show(),
                Actions.scaleTo(2f, 2f, 0.75f),
                Actions.scaleTo(1f, 1f, 0.75f), Actions.hide());
        bonusBlockLabel.addAction(seq);
    }

    private void setScoreLabel() {
        scoreLabel = new Label(textureHandler.getScoreLabelText() + score, new
                Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        scoreLabel.setX(5);
        scoreLabel.setFontScale(0.8f);
        stage.addActor(scoreLabel);
    }

    private void increaseScore() {
        ++score;
        scoreLabel.setText(textureHandler.getScoreLabelText() + score);
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
        if (actors.isBonusActive()) {
            System.out.println("bonus activated");
            showBonusBlockLabelAction();
        }
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
                                            myBlock.getY() - DROP_HEIGHT,
                                            BLOCK_DROP_DURATION)
                                    , new Action() {
                                        @Override
                                        public boolean act(float delta) {
                                            Block me = (Block) this.getTarget();
                                            dropAction();
                                            return true;
                                        }
                                    }
                            ));

                    if ((textureHandler instanceof MIMTextureHandler &&
                         myBlock.getY() >
                         10 * textureHandler.getInitalBlockHeight()) ||
                        (textureHandler instanceof PalaceTextureHandler &&
                         myBlock.getY() >
                         4 * textureHandler.getInitalBlockHeight())) {
                        moveView(0f, actors.getBlockHeight());
                    }

                    return true;
                }
                return false;
            }
        });
    }

    private Action sideToSideAction(Block block) {
        SequenceAction overallSequence = new SequenceAction();

        if (!block.getLeft()) {
            overallSequence.addAction(Actions.moveTo(0, block.getY(), BLOCK_MOVE_DURATION));
            overallSequence.addAction(Actions.moveTo(Palace2D.V_WIDTH - block.getWidth(),
                    block.getY(), BLOCK_MOVE_DURATION));
            block.setLeft(true);
        } else {
            overallSequence.addAction(Actions.moveTo(Palace2D.V_WIDTH - block.getWidth(),
                    block.getY(), BLOCK_MOVE_DURATION));
            overallSequence.addAction(Actions.moveTo(0, block.getY(), BLOCK_MOVE_DURATION));
            block.setLeft(false);

        }

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
            increaseScore();
            makeBlockReady(actors.setNewBlock(DROP_HEIGHT));
            setActualActor();

        }
        else {
            if (gameWon()) {
                Gdx.app.log("info", "YOU WIN");
                Gdx.app.log("info", "YOU LOSE");
                game.setScreen(new EndGameScreen(game, score, WON, actors,
                        textureHandler));
            }
            else {
                Gdx.app.log("info", "YOU LOSE");
                game.setScreen(new EndGameScreen(game, score, LOST, actors,
                        textureHandler));
            }
        }
    }


    private void stageKeyboardPrepare() {
        /* make stage catch keyboard events and pass it to block */
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Gdx.app.log("Image ClickListener", "keyDown. keycode=" + keycode + ", state=" + state);

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
            public void touchUp(InputEvent event, float x, float y, int pointer,
                                int button) {
                game.setScreen(new EndGameScreen(game, score, GAVEUP, actors,
                        textureHandler));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                return true;
            }
        });
        stage.addActor(endButton);
    }

    private void createPauseButton() {
        pauseButton = new TextButton("PAUSE", new Skin(Gdx.files
                .internal
                        ("skins/glassy/skin/glassy-ui.json")), "small");
        pauseButton.setBounds(Gdx.graphics.getWidth() - pauseButton.getWidth(),
                Gdx.graphics.getHeight() - pauseButton.getHeight(), 100, 50);
        pauseButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (state == State.RUN) {
                    state = State.PAUSE;
                    game.pause();
                } else if (state == State.PAUSE) {
                    state = State.RUN;
                    game.resume();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(pauseButton);
    }

    @Override
    void setBackgroundTexture() {
        super.setBackgroundTexture();
        createEndGameButton();
        createPauseButton();
        stageKeyboardPrepare();
        actors.setStackEdges(backgroundTexture.getWidth());
    }

    private void moveView(float x, float y) {
        camera.moveBy(x, y);
        backgroundImg.addAction(Actions.moveBy(x, y));
        endButton.addAction(Actions.moveBy(x, y));
        bonusBlockLabel.addAction(Actions.moveBy(x, y));
        scoreLabel.addAction(Actions.moveBy(x, y));
    }


    @Override
    public void show() {
        super.show();
        makeBlockReady(actors.getActualBlock());
    }
}
