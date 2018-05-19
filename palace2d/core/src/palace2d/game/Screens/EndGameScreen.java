package palace2d.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import palace2d.game.Palace2D;

import java.util.*;
import java.util.stream.Collectors;

public class EndGameScreen implements Screen {
    private Stage stage;
    private Palace2D game;
    private Label text;
    private Label top10;
    private TextButton saveTop10;
    private Table gameScore;
    private String username;
    private int currentScore;
    final private String noScoresYet = "NO SCORES YET!";
    String worst;


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

    private void createScoreTable(HashMap<String, Integer> map) {
        float scorePanelWidth = Gdx.graphics.getWidth() * 0.8f; // 80 % of screen
        float scorePanelHeight = Gdx.graphics.getHeight() * 0.055f * map.size(); // 20 % of screen

        gameScore.remove();
        gameScore.clearChildren();

        gameScore.setWidth(scorePanelWidth);
        gameScore.setHeight(scorePanelHeight);
        gameScore.setY(Gdx.graphics.getHeight() - (scorePanelHeight + 30.f));
        gameScore.setX((Gdx.graphics.getWidth() - scorePanelWidth) * 0.5f);
        text.setX(Gdx.graphics.getWidth() / 2 - text.getWidth() / 2);
        text.setY(150);

        if (map.isEmpty()) {
            gameScore.add("NO SCORES YET!").center();
            gameScore.row();
        } else {
            gameScore.add("BEST SCORES:").center().padBottom(10);
            gameScore.row();
        }

        Map<String, Integer> topTen =
                map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        for (String key : topTen.keySet()) {
            gameScore.add(key).left().padBottom(10).padRight(50);
            gameScore.add(String.valueOf(topTen.get(key))).right().padBottom(10);
            gameScore.row();
        }

        stage.addActor(gameScore);
    }

    private void createTop10Button(HashMap<String, Integer> map, String worst, Preferences prefs) {
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
                        map.remove(worst);
                        Gdx.app.log("info", username);
                        map.put(username, currentScore);
                        Json json = new Json();
                        String jsonString = json.toJson(map);
                        prefs.putString("top10", jsonString);
                        prefs.flush();
                        createScoreTable(map);
                        saveTop10.remove();
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

    public EndGameScreen(Palace2D game, int currentScore) {
        this.game = game;
        this.currentScore = currentScore;
        stage = new Stage(new FitViewport(Palace2D.V_WIDTH, Palace2D.V_HEIGHT));
        createGameObjects();
    }

    private void createGameObjects() {
        /* creating background */
        String title = "YOUR SCORE: " + String.valueOf(currentScore);
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
            String top10Message = "YOU ARE IN TOP 10!";
            top10 = new Label(top10Message, new Skin(Gdx.files
                    .internal("skins/glassy/skin/glassy-ui.json")));
            top10.setX(Gdx.graphics.getWidth() / 2 - top10.getWidth() / 2);
            top10.setY(130);
        }

        Texture backgroundTexture = new Texture(Gdx.files.internal
                ("background.png"));
        stage.addActor(getActorFromTexture(backgroundTexture, 0, 0, Gdx
                .graphics.getWidth(), Gdx.graphics.getHeight()));
        text = new Label(title, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        gameScore = new Table(new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        createScoreTable(map);

        stage.addActor(text);

        Gdx.input.setInputProcessor(stage);
        if (isInTop10) {
            stage.addActor(top10);
            createTop10Button(map, worst, prefs);
        }
        createNewGameButton();
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
