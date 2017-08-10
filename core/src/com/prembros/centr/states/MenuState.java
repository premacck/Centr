package com.prembros.centr.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.prembros.centr.MyGdxGame;

import static com.prembros.centr.MyGdxGame.EXIT_STATE;
import static com.prembros.centr.MyGdxGame.HELP_STATE;
import static com.prembros.centr.MyGdxGame.PLAY_STATE;
import static com.prembros.centr.MyGdxGame.SETTINGS_STATE;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public class MenuState extends State implements InputProcessor {

    private Texture background;
    private Music welcomeMusic;

    public MenuState(GameStateManager gameStateManager) {
        super(gameStateManager);
        background = new Texture("bg_home.png");
        welcomeMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/welcome_music.mp3"));
        welcomeMusic.setLooping(true);
        welcomeMusic.setVolume(getMusicVolume());
        if (ifMusicEnabled()) {
            welcomeMusic.play();
        }
        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);

        Table table = new Table(skin);
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);

        Label appTitle = new Label("CENTR", skin, "title_white");
        appTitle.setAlignment(Align.center);
        ImageButton playBtn = new ImageButton(skin, "playBtn");
        ImageButton settingsBtn = new ImageButton(skin, "settingsBtn");
        ImageButton helpBtn = new ImageButton(skin, "helpBtn");
        ImageButton exitBtn = new ImageButton(skin, "exitBtn");

        setListeners(playBtn, settingsBtn, helpBtn, exitBtn);

        table.row().pad(80, 0, 120, 0);
        table.add(appTitle).fill().uniformX();
        table.row();
        table.add(playBtn).size(BTN_WIDTH, BTN_HEIGHT).fill().uniformX();
        table.row();
        table.add(settingsBtn).size(BTN_WIDTH, BTN_HEIGHT).fill().uniformX();
        table.row();
        table.add(helpBtn).size(BTN_WIDTH, BTN_HEIGHT).fill().uniformX();
        table.row();
        table.add(exitBtn).size(BTN_WIDTH, BTN_HEIGHT).fill().uniformX();

        stage.addActor(table);
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float deltaTime) {
//        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        spriteBatch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), (1 / 30f)));
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        skin.dispose();
        btnHover.dispose();
        welcomeMusic.dispose();
    }

    private void setListeners(Button playBtn, Button settingsBtn, Button helpBtn, Button exitBtn) {
        playBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeState(PLAY_STATE);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                btnHover.play(getSoundVolume());
            }
        });

        settingsBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeState(SETTINGS_STATE);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                btnHover.play(getSoundVolume());
            }
        });

        helpBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeState(HELP_STATE);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                btnHover.play(getSoundVolume());
            }
        });

        exitBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeState(EXIT_STATE);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                btnHover.play(getSoundVolume());
            }
        });
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            changeState(EXIT_STATE);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenY > (MyGdxGame.HEIGHT / 2)) {
            gameStateManager.set(new ObstaclePlayState(gameStateManager));
            dispose();
        }
        else if (screenY < (MyGdxGame.HEIGHT / 2)) {
            gameStateManager.set(new AsteroidPlayState(gameStateManager));
            dispose();
        }
        return true;
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
}
