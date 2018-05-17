package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import javafx.beans.binding.IntegerBinding;
import palace2d.game.Palace2D;

import java.text.SimpleDateFormat;
import java.util.*;

public class EndGameScreen implements Screen {
    private Stage stage;
    private Palace2D game;
    private Label text;
    private Table gameScore;
    private int currentScore;
    final private String noScoresYet = "NO SCORES YET!";


    private String findWorstScore(HashMap<String, String> map){
        int lowest = Integer.MAX_VALUE;
        String loser = "";
        for (String key : map.keySet()) {
            int current = Integer.parseInt(map.get(key));
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
        stage = new Stage(new FitViewport(Palace2D.V_WIDTH, Palace2D.V_HEIGHT));
        createGameObjects();
    }

    private void createGameObjects() {
        /* creating background */
        String  title = "YOU FINISHED YOUR GAME";
        Preferences prefs = Gdx.app.getPreferences("MyPreferences");
        String scoresString = prefs.getString("top10", noScoresYet);

        boolean isInTop10 = false;
        Json json = new Json();
        HashMap<String, String> map = new HashMap<>();

        if (scoresString != noScoresYet) {
            map = json.fromJson(HashMap.class, scoresString);
            Gdx.app.log("info", map.toString());
            if (map.size() < 10) {
                isInTop10 = true;
            } else {
                String worst = findWorstScore(map);
                if (Integer.parseInt(map.get(worst)) < currentScore) {
                    isInTop10 = true;
                    map.remove(worst);
                }
            }
        }
        else{
            isInTop10 = true;
        }
        if (isInTop10) {
            Date date = new Date();
            SimpleDateFormat ft =
                    new SimpleDateFormat ("hh:mm:ss");
            map.put(ft.format(date), String.valueOf(currentScore));
            title += "! Best score ";
        }

        String jsonString = json.toJson(map);
        prefs.putString("top10", jsonString);
        prefs.flush();

        Texture backgroundTexture = new Texture(Gdx.files.internal
                ("background.png"));
        stage.addActor(getActorFromTexture(backgroundTexture, 0, 0, Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()));
        text = new Label(title, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        text.setBounds(Gdx.graphics.getWidth() / 2 - text.getWidth() / 2,
                Gdx.graphics.getHeight() / 2, 200, 40);
        gameScore = new Table(new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));

        float scorePanelWidth = Gdx.graphics.getWidth() * 0.8f; // 80 % of screen
        float scorePanelHeight = Gdx.graphics.getHeight() * 0.055f * map.size(); // 20 % of screen

        gameScore.setWidth(scorePanelWidth);
        gameScore.setHeight(scorePanelHeight);
        gameScore.setY(Gdx.graphics.getHeight() - (scorePanelHeight + 30.f));
        gameScore.setX((Gdx.graphics.getWidth() - scorePanelWidth) * 0.5f);

//        gameScore.setBounds(Gdx.graphics.getWidth() / 2 - text.getWidth() / 2,
//                Gdx.graphics.getHeight() / 2, 200, 40);
        gameScore.setDebug(true);

//        gameScore.left();
//        gameScore.pad(32.f);

        for (String key : map.keySet()) {
            gameScore.add(key).left().padBottom(10).padRight(50);
            gameScore.add(map.get(key)).right().padBottom(10);
            gameScore.row();
        }

//        stage.addActor(text);
        stage.addActor(gameScore);
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
