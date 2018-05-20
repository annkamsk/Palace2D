package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import palace2d.game.Palace2D;
import palace2d.game.ScreenActors.ScoreBoardActors;

import java.util.*;

public class EndGameScreen implements Screen {
    private Stage stage;
    private Palace2D game;
    private int currentScore;
    final private String noScoresYet = "NO SCORES YET!";
    private String worst;
    private Label top10;
    private Label title;
    private ScoreBoardActors actors;
    private Table gameScore;
    private TextButton playButton;
    private TextButton top10Button;

    private String findWorstScore(HashMap<String, Integer> map) {
        int lowest = Integer.MAX_VALUE;
        String loser = "";
        for (String key : map.keySet()) {
            int current = map.get(key);
            if (current < lowest) {
                lowest = current;
                loser = key;
            }
        }
        return loser;
    }

    public EndGameScreen(Palace2D game, int currentScore) {
        this.game = game;
        this.currentScore = currentScore;
        this.actors = new ScoreBoardActors(gameScore);
        stage = new Stage(new FitViewport(Palace2D.V_WIDTH, Palace2D.V_HEIGHT));
        createGameObjects();
    }

    private void createGameObjects() {
        /* creating background */
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        String scoresString = prefs.getString("top10", noScoresYet);

        boolean isInTop10 = false;
        Json json = new Json();
        HashMap<String, Integer> map = new HashMap<>();

        if (scoresString != noScoresYet) {
            map = json.fromJson(HashMap.class, scoresString);
            Gdx.app.log("info", map.toString());
            if (map.size() < 10) {
                isInTop10 = true;
            } else {
                worst = findWorstScore(map);
                if (map.get(worst) < currentScore) {
                    isInTop10 = true;
                }
            }
        } else {
            isInTop10 = true;
        }
        if (isInTop10) {
            top10 = actors.createTop10Label();
        }

        Texture backgroundTexture = new Texture(Gdx.files.internal
                ("background.png"));
        stage.addActor(getActorFromTexture(backgroundTexture, 0, 0, Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()));

        gameScore = actors.createScoreTable(map);
        stage.addActor(gameScore);

        title = actors.createScoreLabel(currentScore);
        stage.addActor(title);

        Gdx.input.setInputProcessor(stage);

        if (isInTop10) {
            stage.addActor(top10);
            top10Button = actors.createTop10Button(map, worst, prefs, currentScore, stage);
            stage.addActor(top10Button);
        }

        playButton = actors.createNewGameButton(game);
        stage.addActor(playButton);
    }

    private Actor getActorFromTexture(Texture tex, int x, int y, int w, int h) {
        TextureRegion texRegion = new TextureRegion(tex,
                x, y, w, h);
        return new Image(texRegion);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
