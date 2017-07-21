package com.prembros.centr.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public abstract class State {

    OrthographicCamera camera;
    Vector3 mouse;
    GameStateManager gameStateManager;

    State(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        camera = new OrthographicCamera();
        mouse = new Vector3();
    }

    public abstract void handleInput();

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();
}
