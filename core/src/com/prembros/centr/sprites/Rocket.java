package com.prembros.centr.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.prembros.centr.MyGdxGame;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public class Rocket {

    private static final int LEFT_GRAVITY = -35;
    private static final int RIGHT_GRAVITY = 35;
    public static final int ROCKET_WIDTH = 34;

    private Vector3 position;
    private Vector3 velocity;
    private Texture rocket;
    private Animation rocketAnimation;
    private Rectangle rocketBounds;

    private boolean leftPressed, rightPressed, flag;

    public Rocket(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        rocket = new Texture("rocket.png");
        rocketAnimation = new Animation(new TextureRegion(rocket), 3, 0.5f);
        rocketBounds = new Rectangle(x, y, rocket.getWidth(), rocket.getHeight());
        handleInput();
    }

    public void update(float deltaTime) {
        rocketAnimation.update(deltaTime);
        float distance = position.dst(((MyGdxGame.WIDTH / 2) - (ROCKET_WIDTH / 2)), position.y, 0);
            if (leftPressed) {
                velocity.add(LEFT_GRAVITY, 0, 0);
                flag = true;
            } else if (rightPressed) {
                velocity.add(RIGHT_GRAVITY, 0, 0);
                flag = true;
            } else if (position.x < (MyGdxGame.WIDTH / 2) - (ROCKET_WIDTH / 2)) {
                velocity.add(distance, 0, 0);
            } else if (position.x > (MyGdxGame.WIDTH / 2) - (ROCKET_WIDTH / 2)) {
                velocity.add(-distance, 0, 0);
            }

            velocity.scl(deltaTime);
            position.add(velocity.x, 0, 0);

            if (position.x < (MyGdxGame.WIDTH / 4) - (ROCKET_WIDTH / 2)) {
                position.x = (MyGdxGame.WIDTH / 4) - (ROCKET_WIDTH / 2);
            } else if (position.x > MyGdxGame.WIDTH - (MyGdxGame.WIDTH / 4) - (ROCKET_WIDTH / 2)) {
                position.x = MyGdxGame.WIDTH - (MyGdxGame.WIDTH / 4) - (ROCKET_WIDTH / 2);
            }

            velocity.scl(1 / deltaTime);

        rocketBounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return rocketAnimation.getFrame();
    }

    public Rectangle getRocketBounds() {
        return rocketBounds;
    }

    public void sway(boolean isLeft) {
        if (isLeft) {
            velocity.x = -250;
        } else {
            velocity.x = 250;
        }
    }

    private void handleInput() {
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    leftPressed = true;
                    rightPressed = false;
//                    sway(true);
                }
                else if (keycode == Input.Keys.RIGHT) {
                    rightPressed = true;
                    leftPressed = false;
//                    sway(false);
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    leftPressed = false;
                    rightPressed = false;
//                    sway(true);
                }
                else if (keycode == Input.Keys.RIGHT) {
                    rightPressed = false;
                    leftPressed = false;
//                    sway(false);
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (screenX < (MyGdxGame.WIDTH / 2) - (ROCKET_WIDTH / 2)) {
                    leftPressed = true;
                    rightPressed = false;
                }
                else if (screenX > (MyGdxGame.WIDTH / 2) + (ROCKET_WIDTH / 2)) {
                    rightPressed = true;
                    leftPressed = false;
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (screenX < (MyGdxGame.WIDTH / 2) - (ROCKET_WIDTH / 2)) {
                    leftPressed = false;
                    rightPressed = false;
                }
                else if (screenX > (MyGdxGame.WIDTH / 2) + (ROCKET_WIDTH / 2)) {
                    rightPressed = false;
                    leftPressed = false;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        });
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void dispose() {
        rocket.dispose();
    }
}
