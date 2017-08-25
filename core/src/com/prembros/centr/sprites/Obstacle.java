package com.prembros.centr.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private static final int COIN_OFFSET = 200;
    public static final int OBSTACLE_WIDTH = 70;

    private Texture coinTexture;
    private CoinAnimation coinAnimation;
    private Vector2 coinPosition;
    private Texture obstacle;
    private Vector2 obstaclePosition;
    private Rectangle obstacleBounds;
    private Rectangle coinBounds;

    private Random random;

    public Obstacle(float x) {
        random = new Random();

        coinTexture = new Texture("coin/coins.png");
        obstacle = new Texture("obstacle.png");
        coinAnimation = new CoinAnimation(coinTexture, 0.5f);

        obstaclePosition = new Vector2(x, random.nextInt(FLUCTUATION));
        coinPosition = new Vector2(x + COIN_OFFSET, random.nextInt(657) + 40);

        obstacleBounds = new Rectangle(obstaclePosition.x, obstaclePosition.y, obstacle.getWidth(), obstacle.getHeight());
        coinBounds = new Rectangle(coinPosition.x, coinPosition.y, 63, coinTexture.getHeight());
    }

    public void updateCoins(float deltaTime) {
        coinAnimation.update(deltaTime);
    }

    public TextureRegion getCoinFrame() {
        return coinAnimation.getCurrentFrame();
    }

    public Texture getTexture() {
        return obstacle;
    }

    public Vector2 getObstaclePosition() {
        return obstaclePosition;
    }

    public Vector2 getCoinPosition() {
        return coinPosition;
    }

    public void reposition(float x) {
        obstaclePosition.set(x, random.nextInt(FLUCTUATION));
        obstacleBounds.setPosition(obstaclePosition.x, obstaclePosition.y);
    }

    public void repositionCoin(float x) {
        coinPosition.set(x, random.nextInt(657) + 40);
        coinBounds.setPosition(coinPosition.x, coinPosition.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(obstacleBounds);
    }

    public boolean coinCaptured(Rectangle player) {
        return player.overlaps(coinBounds);
    }

    public void dispose() {
        obstacle.dispose();
        coinTexture.dispose();
    }
}
