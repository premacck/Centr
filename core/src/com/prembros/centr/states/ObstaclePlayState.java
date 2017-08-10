package com.prembros.centr.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.prembros.centr.MyGdxGame;
import com.prembros.centr.sprites.Obstacle;
import com.prembros.centr.sprites.Rocket;

import static com.prembros.centr.MyGdxGame.MENU_STATE;
import static com.prembros.centr.MyGdxGame.PLAY_STATE;
import static com.prembros.centr.MyGdxGame.SETTINGS_STATE;
import static com.prembros.centr.sprites.Obstacle.OBSTACLE_WIDTH;
import static com.prembros.centr.sprites.Rocket.IS_MENU_LAUNCHED;
import static com.prembros.centr.sprites.Rocket.IS_PAUSED;
import static com.prembros.centr.sprites.Rocket.IS_SETTINGS_LAUNCHED;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

@SuppressWarnings({"Convert2Diamond", "LibGDXUnsafeIterator"})
class ObstaclePlayState extends State {

    private static final int OBSTACLE_SPACING = 280;
    private static final int OBSTACLE_COUNT = 3;
    private static int SCORE;

    private CharSequence scoreString;
    private BitmapFont scoreFont;
    private Music bgMusic;
    private Music deathSound;
    private Rocket rocket;
    private Array<Obstacle> obstacles;
    private Texture bg;
    private Texture ckt;
    private Vector2 bgTopPosition1;
    private Vector2 bgTopPosition2;
    private Vector2 bgBottomPosition1;
    private Vector2 bgBottomPosition2;
    private boolean scoreFlag;

    ObstaclePlayState(GameStateManager gameStateManager, MyGdxGame game) {
        super(gameStateManager, game);
        SCORE = 0;
        scoreFlag = true;
        scoreString = String.valueOf(SCORE);
        scoreFont = new BitmapFont(Gdx.files.internal("skin/score_font.fnt"));
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/bg_music.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(getMusicVolume());
        if (ifMusicEnabled()) {
            bgMusic.play();
        }
        deathSound = Gdx.audio.newMusic(Gdx.files.internal("sound/death.ogg"));
        deathSound.setVolume(getSoundVolume());

        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        rocket = new Rocket(50, false);
        bg = new Texture("bg_home.png");
        ckt = new Texture("bg_play_ground.png");
        bgTopPosition1 = new Vector2(camera.position.y - camera.viewportHeight, MyGdxGame.HEIGHT - ckt.getHeight());
        bgTopPosition2 = new Vector2((camera.position.y - camera.viewportHeight) + ckt.getWidth(), MyGdxGame.HEIGHT - ckt.getHeight());
        bgBottomPosition1 = new Vector2(camera.position.y - camera.viewportHeight, 0);
        bgBottomPosition2 = new Vector2((camera.position.y - camera.viewportHeight) + ckt.getWidth(), 0);

        obstacles = new Array<Obstacle>();
        for (int i = 2; i <= OBSTACLE_COUNT + 1; i ++) {
            obstacles.add(new Obstacle(i * (OBSTACLE_SPACING + OBSTACLE_WIDTH)));
        }

        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            showPauseDialog();
    }

    @Override
    public void update(float deltaTime) {
        if (!IS_PAUSED) {
            handleInput();
            updateBg();
            rocket.update(deltaTime);
            camera.position.x = rocket.getPosition().x + 120;

            for (Obstacle obstacle : obstacles) {
                if (camera.position.x - camera.viewportWidth / 2 + 100 > obstacle.getPosition().x + obstacle.getTexture().getWidth()) {
                    if (scoreFlag) {
                        SCORE++;
                        scoreString = String.valueOf(SCORE);
                        scoreFlag = false;

                    }
                }
            }

            for (Obstacle obstacle : obstacles) {
                if (camera.position.x - camera.viewportWidth / 2  > obstacle.getPosition().x + obstacle.getTexture().getWidth()) {
                    obstacle.reposition(obstacle.getPosition().x + ((OBSTACLE_WIDTH + OBSTACLE_SPACING) * OBSTACLE_COUNT));
                    scoreFlag = true;
                }

                if (obstacle.collides(rocket.getBounds()) || rocket.getsOutOfScreen()) {
                    if (!deathSound.isPlaying()) {
                        deathSound.play();
                        if (bgMusic.isPlaying())
                            bgMusic.stop();

                        IS_PAUSED = true;
                        showGameOverDialog();
                    }
                    break;
                }
            }
            camera.update();
        } else {
            if (IS_MENU_LAUNCHED) {
                gameStateManager.set(new MenuState(gameStateManager, game));
            }
            else if (IS_SETTINGS_LAUNCHED) {
                gameStateManager.set(new SettingsState(gameStateManager, game));
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(bg, camera.position.x - camera.viewportWidth + 240, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        spriteBatch.draw(ckt, bgTopPosition1.x, bgTopPosition1.y, 800, 246, 0, 0, 800, 246, false, true);
        spriteBatch.draw(ckt, bgTopPosition2.x, bgTopPosition2.y, 800, 246, 0, 0, 800, 246, false, true);
        spriteBatch.draw(ckt, bgBottomPosition1.x, bgBottomPosition1.y);
        spriteBatch.draw(ckt, bgBottomPosition2.x, bgBottomPosition2.y);
        for (Obstacle obstacle : obstacles) {
            spriteBatch.draw(obstacle.getTexture(), obstacle.getPosition().x, obstacle.getPosition().y);
        }
        spriteBatch.draw(rocket.getTexture(), rocket.getPosition().x, rocket.getPosition().y,
                rocket.getWidth() / 2, rocket.getHeight() / 2, rocket.getWidth(), rocket.getHeight(), 1, 1, rocket.getRotation(),
                0, 0, rocket.getWidth(), rocket.getHeight(), false, false);
        scoreFont.draw(spriteBatch, scoreString, camera.position.x - 40, camera.position.y + 250);
        spriteBatch.end();

        rocket.getStage().act(Math.min(Gdx.graphics.getDeltaTime(), (1 / 30f)));
        rocket.getStage().draw();
    }

    @Override
    public void dispose() {
        rocket.dispose();
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
        deathSound.dispose();
        bgMusic.dispose();
        ckt.dispose();
    }

    private void updateBg() {
        if ((camera.position.x - camera.viewportWidth) + 240 > bgBottomPosition1.x + ckt.getWidth()) {
            bgTopPosition1.add(ckt.getWidth() * 2, 0);
            bgBottomPosition1.add(ckt.getWidth() * 2, 0);
        }
        if ((camera.position.x - camera.viewportWidth) + 240 > bgBottomPosition2.x + ckt.getWidth()) {
            bgTopPosition2.add(ckt.getWidth() * 2, 0);
            bgBottomPosition2.add(ckt.getWidth() * 2, 0);
        }
    }

    private void showGameOverDialog() {
        rocket.getStage().clear();
        Table table = new Table(skin);
        table.setFillParent(true);

        Label message = new Label("SCORE: " + SCORE, skin, "black");
        message.setAlignment(Align.center);
        ImageButton menuBtn = new ImageButton(skin, "menuBtn");
        ImageButton replayBtn = new ImageButton(skin, "replayBtn");


        final Dialog dialog = new Dialog("GAME OVER!", skin);
        dialog.center();
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);

        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                changeState(MENU_STATE);
            }
        });

        replayBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                changeState(PLAY_STATE);
            }
        });

        table.add(message).pad(10).colspan(2);
        table.row().pad(20, 0, 20, 0);
        table.add(menuBtn).size(120, 80).center();
        table.add(replayBtn).size(120, 80).center();

        dialog.getButtonTable().add(table).center();
        dialog.show(rocket.getStage()).setPosition((MyGdxGame.WIDTH / 2), (MyGdxGame.HEIGHT / 2), Align.center);

        dialog.setName("gameOverDialog");
        rocket.getStage().addActor(dialog);
    }

    private void showPauseDialog() {
        IS_PAUSED = true;
        rocket.getStage().clear();
        Table table = new Table(skin);
        table.setFillParent(true);

        ImageButton playBtn = new ImageButton(skin, "playBtn");
        ImageButton settingsBtn = new ImageButton(skin, "settingsBtn");
        ImageButton menuBtn = new ImageButton(skin, "menuBtn");

        final Dialog dialog = new Dialog("GAME OVER!", skin);
        dialog.center();
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                IS_PAUSED = false;
            }
        });

        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                changeState(MENU_STATE);
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                changeState(SETTINGS_STATE);
            }
        });

        table.row().pad(20, 0, 20, 0);
        table.add(playBtn).size(120, 80).center();
        table.row().pad(0, 0, 20, 0);
        table.add(settingsBtn).size(120, 80).center();
        table.row().pad(0, 0, 20, 0);
        table.add(settingsBtn).size(120, 80).center();

        dialog.getButtonTable().add(table).center();
        dialog.show(rocket.getStage()).setPosition((MyGdxGame.WIDTH / 2), (MyGdxGame.HEIGHT / 2), Align.center);

        dialog.setName("gameOverDialog");
        rocket.getStage().addActor(dialog);
    }
}