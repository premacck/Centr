package com.prembros.centr.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.prembros.centr.MyGdxGame;

/**
 *
 * Created by Prem $ on 7/24/2017.
 */

public class SettingsState extends State {

    static final String PREF_MUSIC_VOLUME = "music";
    public static final String PREF_SOUND_VOLUME = "sound";
    static final String PREF_MUSIC_ENABLED = "music.enabled";
    static final String PREF_SOUND_ENABLED = "sound.enabled";
    public static final String PREF_AUTO_HIDE_BUTTONS = "btn_auto_hide";
    public static final String PREFS_NAME = "CentrPrefs";
    public static final String PREF_COIN_COUNT = "CoinCount";

    private Slider musicVolumeSlider;
    private Slider soundVolumeSlider;
    private CheckBox musicCheckBox;
    private CheckBox soundCheckBox;
    private CheckBox autoHideCheckBox;
    private boolean flag = true;
    private float previousSoundVolume;
    private float previousMusicVolume;
    private Music welcomeMusic;
    private Sound sampleSound;
    private Texture background;

    private Preferences preferences;

    SettingsState(GameStateManager gameStateManager, MyGdxGame game) {
        super(gameStateManager, game);
        background = new Texture("bg_home.png");
        welcomeMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/welcome_music.mp3"));
        welcomeMusic.setLooping(true);
        welcomeMusic.setVolume(getMusicVolume());
        if (ifMusicEnabled()) {
            welcomeMusic.play();
        }
        sampleSound = Gdx.audio.newSound(Gdx.files.internal("sound/blop.ogg"));
        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        Table table = new Table(skin);
        table.setFillParent(true);
        table.center().top();
        previousSoundVolume = getSoundVolume();
        previousMusicVolume = getMusicVolume();

        musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicCheckBox = new CheckBox(" Music\t", skin);
        soundCheckBox = new CheckBox(" Sound\t", skin);
        autoHideCheckBox = new CheckBox("    Auto hide buttons", skin);

        Label titleLabel = new Label("Settings", skin, "title_white_small");
        Label label = new Label("(If enabled, the buttons will appear whenever the rocket is in the center)" +
                "\n\nYou can tap anywhere to make the rocket stay away from the center.", skin, "white");
        label.setWrap(true);
        label.setFontScale(0.6f);
        label.setAlignment(Align.center);
        ImageButton backBtn = new ImageButton(skin, "backBtn");
        TextButton signInBtn = new TextButton("", skin);

        setInitialValues(musicCheckBox, musicVolumeSlider, soundCheckBox, soundVolumeSlider, autoHideCheckBox);

        setValuesAndListeners(musicVolumeSlider, soundVolumeSlider, musicCheckBox, soundCheckBox, autoHideCheckBox, backBtn, signInBtn);

        prepareTable(table, titleLabel, label, backBtn, signInBtn);
    }

    private void setInitialValues(CheckBox musicCheckBox, Slider musicVolumeSlider,
                                  CheckBox soundCheckBox, Slider soundVolumeSlider, CheckBox autoHideCheckBox) {
        musicCheckBox.setChecked(ifMusicEnabled());
        musicVolumeSlider.setValue(getMusicVolume());
        soundCheckBox.setChecked(ifSoundEnabled());
        soundVolumeSlider.setValue(getSoundVolume());
        autoHideCheckBox.setChecked(ifAutoHideEnabled());
    }

    private void setValuesAndListeners(final Slider musicVolumeSlider, final Slider soundVolumeSlider,
                                       final CheckBox musicCheckBox, final CheckBox soundCheckBox,
                                       final CheckBox autoHideCheckBox, ImageButton back_btn,
                                       final TextButton signInBtn) {
        musicVolumeSlider.setValue(getMusicVolume());
        soundVolumeSlider.setValue(getSoundVolume());
        musicCheckBox.setChecked(ifMusicEnabled());
        soundCheckBox.setChecked(ifSoundEnabled());
        autoHideCheckBox.setChecked(ifAutoHideEnabled());

//        musicCheckBox.setName("MusicCheckBox");
//        soundCheckBox.setName("SoundCheckBox");
//        musicVolumeSlider.setName("MusicVolumeSlider");
//        soundVolumeSlider.setName("SoundVolumeSlider");
//        autoHideCheckBox.setName("AutoHideCheckBox");
//        signInBtn.setName("SignInBtn");
//        back_btn.setName("BackBtn");

        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setMusicVolume(musicVolumeSlider.getValue());
            }
        });

        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setSoundVolume(soundVolumeSlider.getValue());
            }
        });

        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setMusicEnabled(musicCheckBox.isChecked());
            }
        });

        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setSoundEnabled(soundCheckBox.isChecked());
            }
        });

        autoHideCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setAutoHide(autoHideCheckBox.isChecked());
            }
        });

        back_btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnHover.play(0.2f);
                changeState(MyGdxGame.MENU_STATE);
            }
        });

        signInBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnHover.play(0.2f);
                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    if (!game.playServices.isSignedIn()) {
                        game.playServices.signIn();
                        signInBtn.setText("Sign out from Google Play Games");
                    } else {
                        game.playServices.signOut();
                        signInBtn.setText("Sign in to Google Play Games");
                    }
                }
            }
        });
    }

    private void prepareTable(Table table, Label titleLabel, Label label, ImageButton backBtn, TextButton signInBtn) {
        table.row().pad(50, 0, 20, 0);
        table.add(titleLabel).colspan(2);
        table.row().pad(50, 0, 10, 0);
        table.add(musicCheckBox);
        table.add(musicVolumeSlider);
        table.row();
        table.add(soundCheckBox);
        table.add(soundVolumeSlider);
        table.row().pad(40, 0, 10, 0);
        table.add(autoHideCheckBox).colspan(2);
        if (Gdx.app.getType() == Application.ApplicationType.Android)
            table.row().pad(0, 0, 20, 0);
        else table.row().pad(0, 0, 100, 0);
        table.add(label).width(380).colspan(2);
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            table.row().pad(20, 0, 0, 0);
            if (!game.playServices.isSignedIn())
                signInBtn.setText("Sign in to Google Play Games");
            else signInBtn.setText("Sign out from Google Play Games");
            table.add(signInBtn).size(BTN_WIDTH, BTN_HEIGHT).colspan(2);
        }
        table.row().pad(50, 0, 0, 0);
        table.add(backBtn).size(BTN_WIDTH, BTN_HEIGHT).colspan(2);

        stage.addActor(table);
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float deltaTime) {
        Gdx.input.setCatchBackKey(true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            gameStateManager.set(new MenuState(gameStateManager, game));
        }
        Gdx.input.setInputProcessor(stage);
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
        welcomeMusic.dispose();
        sampleSound.dispose();
    }

    private Preferences getPrefs() {
        if (preferences == null) {
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        }
        return preferences;
    }

    private void setMusicVolume(float volume) {
        if (flag) {
            getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
            getPrefs().flush();
            if (volume == 0) {
                musicCheckBox.setChecked(false);
            } else {
                if (!musicCheckBox.isChecked()) {
                    musicCheckBox.setChecked(true);
                }
            }
            welcomeMusic.setVolume(volume);
        }
    }

    private void setSoundVolume(float volume) {
        if (flag) {
            getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
            getPrefs().flush();
            if (volume == 0) {
                soundCheckBox.setChecked(false);
            } else {
                if (!soundCheckBox.isChecked()) {
                    soundCheckBox.setChecked(true);
                }
            }
            sampleSound.play(volume);
        }
    }

    private void setMusicEnabled(boolean isEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, isEnabled);
        if (!isEnabled) {
            previousMusicVolume = getMusicVolume();
            setMusicVolume(0);
            flag = false;
            musicVolumeSlider.setValue(0);
            flag = true;
            if (welcomeMusic.isPlaying())
                welcomeMusic.pause();
        } else {
            musicVolumeSlider.setValue(previousMusicVolume);
            welcomeMusic.setVolume(previousMusicVolume);
            if (!welcomeMusic.isPlaying())
                welcomeMusic.play();
        }
        getPrefs().flush();
    }

    private void setSoundEnabled(boolean isEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, isEnabled);
        if (!isEnabled) {
            previousSoundVolume = getSoundVolume();
            setSoundVolume(0);
            flag = false;
            soundVolumeSlider.setValue(0);
            flag = true;
        } else {
            soundVolumeSlider.setValue(previousSoundVolume);
            sampleSound.play(previousSoundVolume);
        }
        getPrefs().flush();
    }

    private void setAutoHide(boolean autoHide) {
        getPrefs().putBoolean(PREF_AUTO_HIDE_BUTTONS, autoHide);
        getPrefs().flush();
    }
}