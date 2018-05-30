package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import palace2d.game.Graphics.TextureHandler;
import palace2d.game.Palace2D;
import palace2d.game.ScreenActors.Bird;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenuScreen extends PalaceScreen {
    private static final int FRAME_COLS = 2, FRAME_ROWS = 2;
    private static final float BIRD_MOVE_DURATION = 1.8f;

    private List<Bird> birds;
    private Animation<TextureRegion> walkAnimation;
    TextureRegion[] walkFrames;
    SpriteBatch spriteBatch;

    private float stateTime;
    private Random generator;

    public MainMenuScreen(final Palace2D game, TextureHandler textureHandler) {
        super(game, textureHandler);
        generator = new Random();
        birds = new ArrayList<>();
        setTextures();
        setPalaceTitle();
        setBirdsGroup(400);
        setPalaceImage();
        setBirdsGroup(375);
        createNewGameButton();

    }

    private void setBirdsGroup(int y) {
        for (int i = 0; i < 10; ++i) {
            addNewBird(generator.nextInt(500) + 100, y, generator.nextFloat());
        }
    }

    private void setTextures() {
        Texture walkSheet = new Texture(Gdx.files.internal("birds.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet
                                                                       .getWidth() /
                                                               FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; ++i) {
            for (int j = 0; j < FRAME_COLS; ++j) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation<>(0.050f, walkFrames);
        stateTime = 0f;
        spriteBatch = new SpriteBatch();
    }

    private void setPalaceImage() {
        Image palace = new Image(TextureHandler.createTexture
                ("palace_transparent.png"));
        float ratio = 1f * 490 / palace.getHeight();
        palace.setBounds(Palace2D.V_WIDTH / 2 - ratio * palace.getWidth() /
                                                2, 5,
                ratio * palace.getWidth(), ratio * palace.getHeight());
        stage.addActor(palace);
    }

    private void setPalaceTitle() {
        Image title = new Image(TextureHandler.createTexture("palace2d_min" +
                                                             ".png"));
        float ratio = Math.min(1, 1f * Palace2D.V_WIDTH / title.getWidth());
        title.setBounds(Palace2D.V_WIDTH / 2 - ratio * title.getWidth() / 2,
                Palace2D.V_HEIGHT - 150,
                ratio * title.getWidth(), ratio * title.getHeight());
        stage.addActor(title);
    }

    private void addNewBird(int x, int y, float time) {
        Bird bird = new Bird(new Animation<>(0.050f, walkFrames), time);
        bird.setBounds(x, y, bird.getWidth(), bird.getHeight());
        bird.addAction(sideToSideAction(bird));
        birds.add(bird);
        stage.addActor(bird);
    }

    private Action sideToSideAction(Bird bird) {
        SequenceAction overallSequence = new SequenceAction();

        overallSequence.addAction(Actions.moveTo(100, bird.getY(),
                BIRD_MOVE_DURATION * bird.getX() / Palace2D.V_WIDTH));

        SequenceAction overallSequenceSameSpeed = new SequenceAction();


        overallSequenceSameSpeed.addAction(Actions.moveTo(Palace2D.V_WIDTH - 100,
                bird.getY(), BIRD_MOVE_DURATION));
        overallSequenceSameSpeed.addAction(Actions
                .moveTo(100, bird.getY(), BIRD_MOVE_DURATION));


        RepeatAction infiniteLoop = new RepeatAction();
        infiniteLoop.setCount(RepeatAction.FOREVER);
        infiniteLoop.setAction(overallSequenceSameSpeed);

        overallSequence.addAction(infiniteLoop);

        return overallSequence;
    }


}
