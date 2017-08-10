package com.prembros.centr.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 *
 * Created by Prem $ on 7/22/2017.
 */

@SuppressWarnings("Convert2Diamond")
public class Obstacle {

    private static final int FLUCTUATION = 300;
    public static final int OBSTACLE_WIDTH = 75;

    private Texture obstacle;
    private Vector2 position;
    private Rectangle bounds;

    private Random random;

    public Obstacle(float x) {
        random = new Random();

        obstacle = new Texture("obstacle.png");

        position = new Vector2(x, random.nextInt(FLUCTUATION));

        bounds = new Rectangle(position.x, position.y,
                obstacle.getWidth(), obstacle.getHeight());
    }

    public Texture getTexture() {
        return obstacle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void reposition(float x) {
        position.set(x, random.nextInt(FLUCTUATION));
        bounds.setPosition(position.x, position.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(bounds);
    }

    public void dispose() {
        obstacle.dispose();
    }
}
