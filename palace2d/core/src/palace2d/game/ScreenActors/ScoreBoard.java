package palace2d.game.ScreenActors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

// TODO wczytywanie wejścia: username nie może być ""

public class ScoreBoard extends Actor {
    private Preferences prefs;
    private HashMap<String, Integer> map;
    private String scoresString;
    private Table gameScore;
    private int currentScore;
    final private String noScoresYet = "NO SCORES YET!";
    private String worst;

    public ScoreBoard(int currentScore) {
        this.currentScore = currentScore;
        worst = "";
        prefs = Gdx.app.getPreferences("MyPreferences");
        scoresString = prefs.getString("top10", noScoresYet);
        Json json = new Json();
        map = json.fromJson(HashMap.class, scoresString);
        gameScore = new Table(new Skin(Gdx.files
                .internal("skins/glassy/skin/glassy-ui.json")));
        createScoreTable(map);
    }

    public boolean isInTop10() {
        if (!scoresString.equals(noScoresYet)) {
            Gdx.app.log("info", map.toString());
            return map.size() < 10 ||
                    map.get(findWorstScore(map)) < currentScore;

        }
        return true;
    }

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
    }

    public void addScore(String username) {
        Integer usernameScore = map.get(username);
        if (usernameScore == null || usernameScore < currentScore) {
            removeWorstScore();
            map.put(username, currentScore);
        }
        String jsonString = new Json().toJson(map);
        prefs.putString("top10", jsonString);
        prefs.flush();
        createScoreTable(map);
        updateWorstScore(username);
    }

    private void removeWorstScore() {
        if (map.size() >= 10) {
            if (worst.isEmpty()) {
                worst = findWorstScore(map);
            }
            map.remove(worst);
            worst = findWorstScore(map);
        }
    }

    private void updateWorstScore(String username) {
        if (worst.equals("") || map.get(worst) > currentScore) {
            worst = username;
        }
    }

    public void display() {
        getStage().addActor(gameScore);
    }

    /* change from protected to public */
    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
    }

}
