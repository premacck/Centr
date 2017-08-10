package com.prembros.centr.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.prembros.centr.MyGdxGame;
import com.prembros.centr.sprites.Asteroid;
import com.prembros.centr.sprites.Rocket;

import static com.prembros.centr.sprites.Asteroid.ASTEROID_WIDTH;

/**
 *
 * Created by Prem $ on 7/22/2017.
 */

@SuppressWarnings({"Convert2Diamond", "LibGDXUnsafeIterator"})
class AsteroidPlayState extends State {

    private static final int ASTEROID_SPACING = 120;
    private static final int ASTEROID_COUNT = 4;

    private Rocket rocket;
    private Array<Asteroid> asteroids;
    private Sound hitSound;
    private Texture bg;
    private Vector2 bgPosition1;
    private Vector2 bgPosition2;

    AsteroidPlayState(GameStateManager gameStateManager) {
        super(gameStateManager);
        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        rocket = new Rocket(50, gameStateManager, false);
        bg = new Texture("bg_play_asteroid.png");
        bgPosition1 = new Vector2(camera.position.y - camera.viewportHeight, 0);
        bgPosition2 = new Vector2((camera.position.y - camera.viewportHeight) + bg.getWidth(), 0);

        asteroids = new Array<Asteroid>();
        for (int i = 3; i <= ASTEROID_COUNT + 2; i++) {
            asteroids.add(new Asteroid(i * (ASTEROID_SPACING + ASTEROID_WIDTH)));
        }
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx_hit.wav"));
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            gameStateManager.set(new MenuState(gameStateManager));
            dispose();
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
        updateBg();
        rocket.update(deltaTime);
        camera.position.x = rocket.getPosition().x + 120;

        for (Asteroid asteroid : asteroids) {
            if (camera.position.x - camera.viewportWidth > asteroid.getPosition().x + asteroid.getAsteroid().getWidth()) {
                asteroid.reposition(asteroid.getPosition().x + ((ASTEROID_WIDTH + ASTEROID_SPACING) * ASTEROID_COUNT));
            }

            if (asteroid.collides(rocket.getBounds())) {
                hitSound.stop();
                hitSound.play(0.1f);
            }
        }

        camera.update();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(bg, bgPosition1.x, bgPosition1.y);
        spriteBatch.draw(bg, bgPosition2.x, bgPosition2.y);
        for (Asteroid asteroid : asteroids) {
            spriteBatch.draw(asteroid.getAsteroid(), asteroid.getPosition().x, asteroid.getPosition().y);
        }
        spriteBatch.draw(rocket.getTexture(), rocket.getPosition().x, rocket.getPosition().y,
                rocket.getWidth() / 2, rocket.getHeight() / 2, rocket.getWidth(), rocket.getHeight(), 1, 1, rocket.getRotation(),
                0, 0, rocket.getWidth(), rocket.getHeight(), false, false);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        rocket.dispose();
        for (Asteroid asteroid : asteroids) {
            asteroid.dispose();
        }
        hitSound.dispose();
        bg.dispose();
    }

    private void updateBg() {
        if ((camera.position.x - camera.viewportWidth + 240) > bgPosition1.x + bg.getWidth())
            bgPosition1.add(bg.getWidth() * 2, 0);

        if ((camera.position.x - camera.viewportWidth + 240) > bgPosition2.x + bg.getWidth())
            bgPosition2.add(bg.getWidth() * 2, 0);
    }
}