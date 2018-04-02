package palace2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayState extends State {
    Texture tex;

    public PlayState(SpriteBatch batch) {
        super(batch);
        tex = new Texture(Gdx.files.internal("block1.png"));
    }

    public void draw() {
        getBatch().draw(tex,111,10);
    }
}
