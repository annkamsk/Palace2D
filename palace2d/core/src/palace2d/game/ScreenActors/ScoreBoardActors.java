package palace2d.game.ScreenActors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Json;
import palace2d.game.Palace2D;
import palace2d.game.Screens.GameScreen;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreBoardActors {

    private Table gameScore;

    public ScoreBoardActors(Table gameScore) {
        this.gameScore = gameScore;
        this.gameScore = new Table(new Skin(Gdx.files
            .internal("skins/glassy/skin/glassy-ui.json")));
    }

    public Table createScoreTable(HashMap<String, Integer> map) {

        float scorePanelWidth = Gdx.graphics.getWidth() * 0.8f; // 80 % of screen
        float scorePanelHeight = Gdx.graphics.getHeight() * 0.055f * map.size(); // 20 % of screen

        gameScore.remove();
        gameScore.clearChildren();

        gameScore.setWidth(scorePanelWidth);
        gameScore.setHeight(scorePanelHeight);
        gameScore.setY(Gdx.graphics.getHeight() - (scorePanelHeight + 30.f));
        gameScore.setX((Gdx.graphics.getWidth() - scorePanelWidth) * 0.5f);

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

        return gameScore;
    }

    public TextButton createTop10Button(HashMap<String, Integer> map, String worst, Preferences prefs, int currentScore,
        Stage stage) {
        TextButton saveTop10;
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
                        gameScore = createScoreTable(map);
                        stage.addActor(gameScore);
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
        return saveTop10;
    }

    public TextButton createNewGameButton(Palace2D game) {
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

        return playButton;
    }

    public Label createScoreLabel(int currentScore) {
        String title = "YOUR SCORE: " + String.valueOf(currentScore);
        Label label = new Label(title, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        label.setX(Gdx.graphics.getWidth() / 2 - label.getWidth() / 2);
        label.setY(150);
        return label;
    }

    public Label createTop10Label() {
        String top10Message = "YOU ARE IN TOP 10!";
        Label label = new Label(top10Message, new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        label.setX(Gdx.graphics.getWidth() / 2 - label.getWidth() / 2);
        label.setY(130);
        return label;
    }

}
