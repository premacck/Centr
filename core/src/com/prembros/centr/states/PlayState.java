package com.prembros.centr.states;

import com.badlogic.gdx.Application;
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
import com.badlogic.gdx.utils.Timer;
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
class PlayState extends State {

    private static final int OBSTACLE_SPACING = 300;
    private static final int OBSTACLE_COUNT = 3;
    private static int SCORE;
    
    private static final String achievement_good_start_is_half_done = "CgkIvMGr3JQfEAIQAg";
    private static final String achievement_5_points = "CgkIvMGr3JQfEAIQAw";
    private static final String achievement_10_points = "CgkIvMGr3JQfEAIQBA";
    private static final String achievement_nice_going = "CgkIvMGr3JQfEAIQBQ";
    private static final String achievement_god_dang = "CgkIvMGr3JQfEAIQBg";
    private static final String achievement_keep_going = "CgkIvMGr3JQfEAIQBw";
    private static final String achievement_youre_on_fire = "CgkIvMGr3JQfEAIQCA";
//    private static final String leaderboard_centr_hall_of_famers = "CgkIvMGr3JQfEAIQAQ";

    private CharSequence scoreString;
    private BitmapFont scoreFont;
    private Music bgMusic;
    private Music deathSound;
    private Music deathMusic;
    private Rocket rocket;
    private Array<Obstacle> obstacles;
    private Texture bg;
    private Texture ckt;
    private Vector2 bgTopPosition1;
    private Vector2 bgTopPosition2;
    private Vector2 bgBottomPosition1;
    private Vector2 bgBottomPosition2;
//    private Toast toast;
    private boolean scoreFlag;

    PlayState(GameStateManager gameStateManager, MyGdxGame game) {
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
        deathSound.setVolume(getMusicVolume());
        deathMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/dead_music.mp3"));
        deathMusic.setVolume(getMusicVolume());

        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        rocket = new Rocket(50, false);
        bg = new Texture("bg_home.png");
        ckt = new Texture("bg_play_ground.png");
        bgTopPosition1 = new Vector2(camera.position.y - camera.viewportHeight, MyGdxGame.HEIGHT - ckt.getHeight());
        bgTopPosition2 = new Vector2((camera.position.y - camera.viewportHeight) + ckt.getWidth(), MyGdxGame.HEIGHT - ckt.getHeight());
        bgBottomPosition1 = new Vector2(camera.position.y - camera.viewportHeight, 0);
        bgBottomPosition2 = new Vector2((camera.position.y - camera.viewportHeight) + ckt.getWidth(), 0);

        obstacles = new Array<Obstacle>();
        for (int i = 1; i <= OBSTACLE_COUNT; i ++) {
            obstacles.add(new Obstacle((i * (OBSTACLE_SPACING + OBSTACLE_WIDTH)) + 200));
        }

//        toast = new Toast();

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
            updatePlayGamesScore();
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
                IS_MENU_LAUNCHED = false;
            }
            else if (IS_SETTINGS_LAUNCHED) {
                gameStateManager.set(new SettingsState(gameStateManager, game));
                IS_SETTINGS_LAUNCHED = false;
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
//        toast.toaster();
    }

    @Override
    public void dispose() {
        rocket.dispose();
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
        deathSound.dispose();
        bgMusic.dispose();
        deathMusic.dispose();
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
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                deathMusic.play();
            }
        }, 1);
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

    private void updatePlayGamesScore() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (game.playServices.isStoreVersion()) {
        /*
          Submit Score
         */
                if (SCORE > 0) {
                    if (game.playServices.isSignedIn()) {
                        game.playServices.submitScore(SCORE);
                    }
//            else {
//                toast.makeText("Sign in to get your score in leaderboards and earn achievements",
//                        COLOR_PREF.RED, STYLE.ROUND, POSITION.middle, POSITION.middle_down, SHORT);
//            }
                }
//        else toast.makeText("Score above zero to be in leaderboards",
//                COLOR_PREF.RED, STYLE.ROUND, POSITION.middle, POSITION.middle_down, SHORT);

        /*
          Unlock Achievements
         */
                if (game.playServices.isSignedIn()) {
                    if (SCORE >= 1) {
                        game.playServices.unlockAchievement(achievement_good_start_is_half_done);
                    }
                    if (SCORE >= 5) {
                        game.playServices.unlockAchievement(achievement_5_points);
                    }
                    if (SCORE >= 10) {
                        game.playServices.unlockAchievement(achievement_10_points);
                    }
                    if (SCORE >= 20) {
                        game.playServices.unlockAchievement(achievement_nice_going);
                    }
                    if (SCORE >= 30) {
                        game.playServices.unlockAchievement(achievement_god_dang);
                    }
                    if (SCORE >= 40) {
                        game.playServices.unlockAchievement(achievement_keep_going);
                    }
                    if (SCORE >= 50) {
                        game.playServices.unlockAchievement(achievement_youre_on_fire);
                    }
                }
            }
        }
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