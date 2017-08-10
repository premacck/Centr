package com.prembros.centr.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 *
 * Created by Prem $ on 7/22/2017.
 */

public class Asteroid {

    public static final int ASTEROID_WIDTH = 64;
    private static final int[] FLUCTUATION = new int[]{24, 144, 240, 360};

    private Texture asteroid1;
    private Texture asteroid2;
    private Texture asteroid3;
    private Texture asteroid4;
    private Texture asteroid5;

    private Texture asteroid;
    private Vector2 position;
    private Rectangle bounds;

    private Random random;

    public Asteroid(float x) {
        asteroid1 = new Texture("asteroid1.png");
        asteroid2 = new Texture("asteroid2.png");
        asteroid3 = new Texture("asteroid3.png");
        asteroid4 = new Texture("asteroid4.png");
        asteroid5 = new Texture("asteroid5.png");

        random = new Random();

        Texture[] ASTEROIDS = new Texture[]{asteroid1, asteroid2, asteroid3, asteroid4, asteroid5};
        asteroid = getRandom(ASTEROIDS);

        position = new Vector2(x, getRandom(FLUCTUATION));

        bounds = new Rectangle(position.x, position.y, asteroid1.getWidth(), asteroid1.getHeight());
    }

    private int getRandom(int[] array) {
        return array[random.nextInt(array.length)];
    }

    private Texture getRandom(Texture[] array) {
        return array[random.nextInt(array.length)];
    }

    public void reposition(float x) {
        position.set(x, getRandom(FLUCTUATION));
        bounds.setPosition(position.x, position.y);
    }

    public Texture getAsteroid() {
        return asteroid;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(bounds);
    }

    public void dispose() {
        asteroid.dispose();
        asteroid1.dispose();
        asteroid2.dispose();
        asteroid3.dispose();
        asteroid4.dispose();
        asteroid5.dispose();
    }
}
