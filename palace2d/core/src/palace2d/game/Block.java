package palace2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;

import java.util.Iterator;

public class Block extends Actor {
    private Sprite sprite;
    private final int idx;
    private final int width;
    private final int height;

    public Block(Texture tex, final int idx) {
        sprite = new Sprite(tex);
        this.idx = idx;
        width = tex.getWidth();
        height = tex.getHeight();
        spritePos(sprite.getX(), sprite.getY());
    }

    public void spritePos(float x, float y) {
        sprite.setPosition(x, y);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.draw(batch);
    }


}
