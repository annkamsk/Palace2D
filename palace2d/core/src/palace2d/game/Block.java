package palace2d.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;

public class Block extends Actor {
    private Sprite sprite;

    private final int idx;
    private int width; /* not final because we need to cut it sometimes */
    private final int height;

    public Block(Texture tex, final int idx) {
        sprite = new Sprite(tex);
        this.idx = idx;
        width = tex.getWidth();
        height = tex.getHeight();
        spritePos(sprite.getX(), sprite.getY());
    }

    public void trim(int width) {
        this.sprite.setSize(width, height);
        // TODO tez trzeba 'width' zmienic, pytanie czy w ogole to jest potrzebne
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void spritePos(float x, float y) {
        sprite.setPosition(x, y);
        setBounds(x, y, sprite.getWidth(), sprite.getHeight());
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

    public int getIdx() {
        return idx;
    }
}
