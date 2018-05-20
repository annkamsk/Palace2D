package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Json;
import palace2d.game.Palace2D;
import palace2d.game.ScreenActors.ScoreBoard;

import java.util.*;

public class EndGameScreen extends PalaceScreen {
    private ScoreBoard scoreBoard;
    private TextButton saveTop10;


    public EndGameScreen(Palace2D game, int currentScore, int isWon) {
        super(game, "background.png");
        createScoreBoard(currentScore);
        createScreenObjects(currentScore);
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

    private void createNewGameButton() {
        TextButton playButton = new TextButton("NEW GAME", new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")), "small");

        playButton.setBounds(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2,
                10, playButton.getWidth(), 50);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(playButton);
    }

    private void createScreenObjects(int currentScore) {
        setBackgroundTexture();
        String title = "YOUR SCORE: " + String.valueOf(currentScore);

        Label text = new Label(title, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        stage.addActor(text);

        if (scoreBoard.isInTop10()) {
            createTop10Button();
        }
        createNewGameButton();
    }

    @Override
    public void show() {
        super.show();
        scoreBoard.display();
    }
}
