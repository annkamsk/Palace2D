package palace2d.game.ScreenActors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bird extends Actor {
    private Animation<TextureRegion> animation;
    private TextureRegion currentRegion;


    private float time;

    private boolean left = false;


    public Bird(Animation<TextureRegion> animation, float time) {
        this.animation = animation;
        this.time = time;

    }


    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
        currentRegion = animation.getKeyFrame(time, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(currentRegion, getX(), getY());
    }

}
