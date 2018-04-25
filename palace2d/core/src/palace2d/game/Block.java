package palace2d.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;

public class Block extends Actor {
    private Sprite sprite;

    private final int height;

    public Block(Texture tex) {
        sprite = new Sprite(tex);
        height = tex.getHeight();
        spritePos(sprite.getX(), sprite.getY());
    }

    public void trim(int width) {
        this.sprite.setSize(width, height);
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
}
