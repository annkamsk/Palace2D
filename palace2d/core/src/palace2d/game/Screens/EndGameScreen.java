package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import palace2d.game.Palace2D;
import palace2d.game.ScreenActors.ScoreBoard;

import java.util.*;

public class EndGameScreen extends PalaceScreen {
    private static final int TOP10_LABEL_YPOSITION = 130; // px

    private ScoreBoard scoreBoard;
    private TextButton saveTop10;


    public EndGameScreen(Palace2D game, int currentScore, int isWon) {
        super(game, "background.png");
        createScoreBoard(currentScore);
        createScoreLabel(currentScore);
        createTop10Label();
        if (scoreBoard.isInTop10()) {
            createTop10Button();
        }
        createNewGameButton();
    }

    private void createScoreBoard(int currentScore) {
        scoreBoard = new ScoreBoard(currentScore);
        scoreBoard.setStage(stage);
    }


    private void createTop10Button() {
        saveTop10 = new TextButton("SAVE YOUR SCORE", new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")), "small");

        saveTop10.setBounds(Gdx.graphics.getWidth() / 2 - saveTop10.getWidth() / 2,
                70, saveTop10.getWidth(), 50);

        saveTop10.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String username) {
                        scoreBoard.addScore(username);
                        Gdx.app.log("info", username);
                        saveTop10.remove();
                        scoreBoard.display();
                    }

                    @Override
                    public void canceled() {
                    }
                }, "Your name", "", "");

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(saveTop10);
    }

    private void createScoreLabel(int currentScore) {
        String title = "YOUR SCORE: " + String.valueOf(currentScore);
        Label label = new Label(title, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        stage.addActor(label);
    }

    private void createTop10Label() {
        String top10Message;
        if (scoreBoard.isInTop10()) {
            top10Message = "YOU ARE IN TOP 10!";
        } else {
            top10Message = "SORRY, YOU'RE NOT IN TOP 10";
        }
        Label top10 = new Label(top10Message, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        top10.setX(Gdx.graphics.getWidth() / 2 - top10.getWidth() / 2);
        top10.setY(TOP10_LABEL_YPOSITION);
        stage.addActor(top10);

    }

    @Override
    public void show() {
        super.show();
        scoreBoard.display();
    }
}
