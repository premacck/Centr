package com.prembros.centr.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 *
 * Created by Prem $ on 7/19/2017.
 */

@SuppressWarnings("Convert2Diamond")
public class GameStateManager {

    private Stack<State> states;

    public GameStateManager() {
        states = new Stack<State>();
    }

//    public void pop() {
//        states.pop().dispose();
//    }

    public void push(State state) {
        states.push(state);
    }

    public State getState() {
        return states.peek();
    }

    boolean isStackEmpty() {
        return states.empty();
    }

    void set(State state) {
        states.pop().dispose();
        states.push(state);
    }

    public void update(float deltaTime) {
        states.peek().update(deltaTime);
    }

    public void render(SpriteBatch spriteBatch) {
        states.peek().render(spriteBatch);
    }
}
