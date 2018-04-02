package palace2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoseState extends State {
    private Texture tex;

    public LoseState(SpriteBatch batch) {
        super(batch);
        tex = new Texture(Gdx.files.internal("lose.png"));
    }

    public void draw() {
        getBatch().draw(tex,0,0);
    }

}
