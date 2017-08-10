package com.prembros.centr;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prembros.centr.states.GameStateManager;
import com.prembros.centr.states.MenuState;

public class MyGdxGame extends ApplicationAdapter {

	public static final int MENU_STATE = 0;
	public static final int PLAY_STATE = 1;
	public static final int SETTINGS_STATE = 2;
	public static final int HELP_STATE = 3;
	public static final int EXIT_STATE = 4;
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "Centr";

	public PlayServices playServices;
	private SpriteBatch spriteBatch;
	private GameStateManager gameStateManager;

	public MyGdxGame(PlayServices playServices) {
		if (playServices != null)
		this.playServices = playServices;
	}

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		gameStateManager = new GameStateManager();
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
		gameStateManager.push(new MenuState(gameStateManager, this));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.render(spriteBatch);
	}

	@Override
	public void resize(int width, int height) {
		gameStateManager.getState().resize(width, height);
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
	}
}
