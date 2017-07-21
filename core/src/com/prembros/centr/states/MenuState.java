package com.prembros.centr.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.prembros.centr.MyGdxGame;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public class MenuState extends State {

    private Texture background;
    private Texture playBtn;

    public MenuState(GameStateManager gameStateManager) {
        super(gameStateManager);
        background = new Texture("bg_home.png");
        playBtn = new Texture("playbtn.png");
        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            gameStateManager.set(new PlayState(gameStateManager));
            dispose();
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        spriteBatch.draw(playBtn, (MyGdxGame.WIDTH / 2) - (playBtn.getWidth() / 2), (MyGdxGame.HEIGHT / 2) - (playBtn.getHeight() / 2));
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
    }
}
