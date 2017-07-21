package com.prembros.centr.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.prembros.centr.MyGdxGame;
import com.prembros.centr.sprites.Rocket;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public class PlayState extends State{

    private Texture background;
    private Rocket rocket;
    private Vector2 groundPosition1;
    private Vector2 groundPosition2;

    PlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        background = new Texture("bg_play.png");
        rocket = new Rocket((MyGdxGame.WIDTH / 2) - (Rocket.ROCKET_WIDTH / 2), 100);
        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        groundPosition1 = new Vector2(0, camera.position.y);
        groundPosition2 = new Vector2(0, camera.position.y + background.getHeight());
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rocket.sway(true);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rocket.sway(false);
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
        updateBackground();
        rocket.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(rocket.getTexture(), rocket.getPosition().x, rocket.getPosition().y);
//        spriteBatch.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
//        spriteBatch.draw(background, groundPosition1.x, groundPosition1.y, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
//        spriteBatch.draw(background, groundPosition2.x, groundPosition2.y, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
//        background.dispose();
        rocket.dispose();
    }

    private void updateBackground() {
        if (camera.position.y - (camera.viewportHeight / 2) > groundPosition1.y + background.getHeight()) {
            groundPosition1.add(background.getHeight() * 2, 0);
        }
        if (camera.position.x - (camera.viewportWidth / 2) > groundPosition2.x + background.getHeight()) {
            groundPosition2.add(background.getHeight() * 2, 0);
        }
    }
}
