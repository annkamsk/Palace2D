package palace2d.game;

import com.badlogic.gdx.Game;

import palace2d.game.Screens.MainMenuScreen;


public class Palace2D extends Game {


	@Override
	public void create () {
        this.setScreen(new MainMenuScreen(this));
	}

	public void resize(int width, int height) {
	}


	@Override
	public void render () {
	    super.render();
	}

	@Override
	public void dispose () {
	}
}
