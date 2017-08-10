package com.prembros.centr.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.prembros.centr.MyGdxGame;

import static com.prembros.centr.MyGdxGame.EXIT_STATE;
import static com.prembros.centr.MyGdxGame.HELP_STATE;
import static com.prembros.centr.MyGdxGame.MENU_STATE;
import static com.prembros.centr.MyGdxGame.PLAY_STATE;
import static com.prembros.centr.MyGdxGame.SETTINGS_STATE;
import static com.prembros.centr.states.SettingsState.PREFS_NAME;
import static com.prembros.centr.states.SettingsState.PREF_AUTO_HIDE_BUTTONS;
import static com.prembros.centr.states.SettingsState.PREF_MUSIC_ENABLED;
import static com.prembros.centr.states.SettingsState.PREF_MUSIC_VOLUME;
import static com.prembros.centr.states.SettingsState.PREF_SOUND_ENABLED;
import static com.prembros.centr.states.SettingsState.PREF_SOUND_VOLUME;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public abstract class State {

    static final int BTN_WIDTH = 320;
    static final int BTN_HEIGHT = 80;

    OrthographicCamera camera;
    GameStateManager gameStateManager;
    Stage stage;
    Skin skin;
    Sound btnHover;

    State(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        camera = new OrthographicCamera();
        stage = new Stage(new FillViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera));
        skin = new Skin(Gdx.files.internal("skin/skin_centr.json"));
        btnHover = Gdx.audio.newSound(Gdx.files.internal("sound/click.ogg"));
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    void changeState(int screen) {
        switch(screen) {
            case MENU_STATE:
                if (!gameStateManager.isStackEmpty())
                    gameStateManager.set(new MenuState(gameStateManager));
                else gameStateManager.push(new MenuState(gameStateManager));
                break;
            case PLAY_STATE:
                if (!gameStateManager.isStackEmpty())
                    gameStateManager.set(new ObstaclePlayState(gameStateManager));
                else gameStateManager.push(new ObstaclePlayState(gameStateManager));
                break;
            case SETTINGS_STATE:
                if (!gameStateManager.isStackEmpty())
                    gameStateManager.set(new SettingsState(gameStateManager));
                else gameStateManager.push(new SettingsState(gameStateManager));
                break;
            case HELP_STATE:
                if (!gameStateManager.isStackEmpty())
                    gameStateManager.set(new HelpState(gameStateManager));
                else gameStateManager.push(new HelpState(gameStateManager));
                break;
            case EXIT_STATE:
                quitGameConfirm();
            default:
                break;
        }
    }

    private void quitGameConfirm() {
        TextButton btnYes = new TextButton("Exit", skin);
        TextButton btnNo = new TextButton("Cancel", skin);

        final Dialog dialog = new Dialog("Confirm exit?", skin);
        dialog.align(Align.center);
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);

        btnYes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                Gdx.app.exit();
            }
        });

        btnNo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.cancel();
                dialog.hide();
            }
        });

        Table table = new Table();

        table.row().pad(20, 0, 20, 0);
        table.add(btnNo);
        table.add(btnYes);

        dialog.getButtonTable().add(table).center();
        dialog.show(stage).setPosition((MyGdxGame.WIDTH / 2), (MyGdxGame.HEIGHT / 2), Align.center);

        dialog.setName("quitDialog");
        stage.addActor(dialog);
        stage.draw();
    }

    public abstract void handleInput();

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();

    private Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME);
    }

    float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME);
    }

    boolean ifMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    boolean ifSoundEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    boolean ifAutoHideEnabled() {
        return getPrefs().getBoolean(PREF_AUTO_HIDE_BUTTONS);
    }
}