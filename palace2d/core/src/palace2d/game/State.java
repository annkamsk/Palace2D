package palace2d.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {
    private SpriteBatch batch;

    public State(SpriteBatch batch) {
        this.batch = batch;
    }

    protected SpriteBatch getBatch() {
        return batch;
    }

    public abstract void draw();

}
