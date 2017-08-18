package com.prembros.centr.sprites;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.prembros.centr.MyGdxGame;

import static com.prembros.centr.states.HelpState.IS_POINTER_HIDDEN;
import static com.prembros.centr.states.SettingsState.PREFS_NAME;
import static com.prembros.centr.states.SettingsState.PREF_AUTO_HIDE_BUTTONS;
import static com.prembros.centr.states.SettingsState.PREF_SOUND_VOLUME;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public class Rocket {

//    public static final int ROCKET_WIDTH = 74;
    private static final int SCREEN_BOUND_LIMIT = 15;
    private static final int ROCKET_HEIGHT = 34;
    private static final int UP_GRAVITY = 20;
    private static final int DOWN_GRAVITY = -20;
    private static final int CENTER_HORIZONTAL_POSITION_UP = (MyGdxGame.HEIGHT / 2) - 5;
    private static final int CENTER_HORIZONTAL_POSITION_DOWN = (MyGdxGame.HEIGHT / 2) - (ROCKET_HEIGHT / 2);
    private static final int MOVEMENT = 150;
    public static boolean IS_PAUSED;
    public static boolean IS_MENU_LAUNCHED;
    public static boolean IS_SETTINGS_LAUNCHED;

    private Sound blop;
    private Texture rocket;
    private Texture rocket2;
    private Texture rocket3;
    private Animation rocketAnimation;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle rocketBounds;
    private float rotation = 0;
    private Stage stage;
    private Table table;
    private InputProcessor inputProcessor;
    private boolean isHelp;
    private boolean upPressed;
    private boolean downPressed;

    public Rocket(int x, boolean isHelp) {
        this.isHelp = isHelp;
        IS_PAUSED = false;
        IS_MENU_LAUNCHED = false;
        IS_SETTINGS_LAUNCHED = false;
        blop = Gdx.audio.newSound(Gdx.files.internal("sound/blop.ogg"));

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        velocity = new Vector3(0, 0, 0);
        rocket = new Texture("rocket1.png");
        rocket2 = new Texture("rocket2.png");
        rocket3 = new Texture("rocket3.png");
        position = new Vector3(x, (MyGdxGame.HEIGHT / 2) - (rocket.getHeight() / 2), 0);
        rocketAnimation = new Animation(rocket, rocket2, rocket3, 3, 0.5f);
        rocketBounds = new Rectangle(position.x, position.y, rocket.getWidth(), rocket.getHeight());

        stage = new Stage(new FillViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT));
        Skin skin = new Skin(Gdx.files.internal("skin/skin_centr.json"));

        ImageButton upBtn = new ImageButton(skin, "upBtn");
        ImageButton downBtn = new ImageButton(skin, "downBtn");
        ImageButton pauseBtn = new ImageButton(skin, "pauseBtn");
        ImageButton backBtn = new ImageButton(skin, "backBtn_arrow");
        pauseBtn.setPosition(20, MyGdxGame.HEIGHT - pauseBtn.getHeight() - 20);
        backBtn.setPosition(20, MyGdxGame.HEIGHT - pauseBtn.getHeight() - 20);

        table = new Table(skin);
        table.setWidth(MyGdxGame.WIDTH);
        table.center().bottom().padBottom(50);
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            table.add(upBtn).size(MyGdxGame.WIDTH / 2, 100);
            table.add(downBtn).size(MyGdxGame.WIDTH / 2, 100);
        }
        table.row();

        stage.addActor(table);
        if (!isHelp)
            stage.addActor(pauseBtn);
        else stage.addActor(backBtn);

        setListeners(upBtn, downBtn, pauseBtn, backBtn);
        handleInput();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void update(float deltaTime) {
        rocketAnimation.update(deltaTime);
        if (!IS_PAUSED) {
            if (upPressed) {
                velocity.add(0, UP_GRAVITY, 0);
                rotation += 1.5;
            } else if (position.y > CENTER_HORIZONTAL_POSITION_UP) {
                velocity.sub(0, (float) (UP_GRAVITY * 1.5), 0);
                rotation -= 1.5;
            } else if (downPressed) {
                velocity.add(0, DOWN_GRAVITY, 0);
                rotation -= 1.5;
            } else if (position.y < CENTER_HORIZONTAL_POSITION_DOWN) {
                velocity.sub(0, (float) (DOWN_GRAVITY * 1.5), 0);
                rotation += 1.5;
            } else {
                velocity.setZero();
                rotation = 0;
                if (isAutoHideEnabled() && !table.isVisible())
                    table.setVisible(true);
                if (isHelp)
                    if (IS_POINTER_HIDDEN)
                        IS_POINTER_HIDDEN = false;
            }

            if (rotation > 65) {
                rotation = 65;
            } else if (rotation < -65) {
                rotation = -65;
            }

            velocity.scl(deltaTime);
            if (!isHelp)
                position.add(MOVEMENT * deltaTime, velocity.y, 0);
            else position.add(0, velocity.y, 0);

            keepItWithinScreen();

            velocity.scl(1 / deltaTime);

            rocketBounds.setPosition(position.x, position.y);
        }
    }

    public float getRotation() {
        if (rotation <= 360) return rotation;
        else return rotation % 360;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return rocketAnimation.getCurrentFrame();
    }

    public Rectangle getBounds() {
        return rocketBounds;
    }

    public boolean getsOutOfScreen() {
        return position.y >= (MyGdxGame.HEIGHT - (ROCKET_HEIGHT * 1.25) - SCREEN_BOUND_LIMIT) || position.y <= SCREEN_BOUND_LIMIT;
    }

    public int getWidth() {
        return rocket.getWidth();
    }

    public int getHeight() {
        return rocket.getHeight();
    }

    public Stage getStage() {
        return stage;
    }

    private void keepItWithinScreen() {
        if (position.y < SCREEN_BOUND_LIMIT) {
            position.y = SCREEN_BOUND_LIMIT;
            velocity.setZero();
        } else if (position.y >= MyGdxGame.HEIGHT - (ROCKET_HEIGHT * 1.25) - SCREEN_BOUND_LIMIT) {
            position.y = MyGdxGame.HEIGHT - (ROCKET_HEIGHT * 1.25f) - SCREEN_BOUND_LIMIT;
            velocity.setZero();
        }
    }

    private void sway(boolean isUp) {
        if (isUp) {
            velocity.y = 320;
        } else {
            velocity.y = -320;
        }
        blop.play(Gdx.app.getPreferences(PREFS_NAME).getFloat(PREF_SOUND_VOLUME, 0.5f));
    }

    private void setListeners(ImageButton upBtn, ImageButton downBtn, ImageButton pauseBtn, ImageButton backBtn) {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            upBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (position.y + (rocket.getHeight() / 2) > MyGdxGame.HEIGHT / 2)
                        rotation = 0;
                    upPressed = true;
                    downPressed = false;
                    IS_POINTER_HIDDEN = true;
                    sway(true);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    upPressed = false;
                    downPressed = false;
                    if (isAutoHideEnabled() && table.isVisible())
                        table.setVisible(false);
                }
            });

            downBtn.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (position.y < MyGdxGame.HEIGHT / 2)
                        rotation = 0;
                    downPressed = true;
                    upPressed = false;
                    IS_POINTER_HIDDEN = true;
                    sway(false);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    downPressed = false;
                    upPressed = false;
                    if (isAutoHideEnabled() && table.isVisible())
                        table.setVisible(false);
                }
            });
        }

        pauseBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                IS_PAUSED = true;
                showPauseDialog();
                return true;
            }
        });

        backBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                IS_MENU_LAUNCHED = true;
                return true;
            }
        });
    }

    private void handleInput() {
        inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.UP) {
                    if (position.y + (rocket.getHeight() / 2) > MyGdxGame.HEIGHT / 2)
                        rotation = 0;
                    upPressed = true;
                    downPressed = false;
                    IS_POINTER_HIDDEN = true;
                    sway(true);
                } else if (keycode == Input.Keys.DOWN) {
                    if (position.y < MyGdxGame.HEIGHT / 2)
                        rotation = 0;
                    downPressed = true;
                    upPressed = false;
                    IS_POINTER_HIDDEN = true;
                    sway(false);
                }
                else if (keycode == Input.Keys.BACK) {
                    if (!isHelp) {
                        showPauseDialog();
                    }
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.UP) {
                    upPressed = false;
                    downPressed = false;
                    if (isAutoHideEnabled() && table.isVisible())
                        table.setVisible(false);
                } else if (keycode == Input.Keys.DOWN) {
                    downPressed = false;
                    upPressed = false;
                    if (isAutoHideEnabled() && table.isVisible())
                        table.setVisible(false);
                }
                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                rotation = 0;
                if (position.y > CENTER_HORIZONTAL_POSITION_UP) {
                    upPressed = true;
                    downPressed = false;
                    sway(true);
                } else if (position.y < CENTER_HORIZONTAL_POSITION_DOWN) {
                    downPressed = true;
                    upPressed = false;
                    sway(false);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (position.y > CENTER_HORIZONTAL_POSITION_UP) {
                    upPressed = false;
                    downPressed = false;
                } else if (position.y < CENTER_HORIZONTAL_POSITION_DOWN) {
                    downPressed = false;
                    upPressed = false;
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
        };
    }

    private void showPauseDialog() {
        IS_PAUSED = true;
        Skin skin = new Skin(Gdx.files.internal("skin/skin_centr.json"));
        Table table = new Table(skin);
        table.center();

        ImageButton playBtn = new ImageButton(skin, "playBtn");
        ImageButton settingsBtn = new ImageButton(skin, "settingsBtn");
        ImageButton menuBtn = new ImageButton(skin, "menuBtn");

        final Dialog dialog = new Dialog("GAME PAUSED", skin);
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
                IS_MENU_LAUNCHED = true;
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
                IS_SETTINGS_LAUNCHED = true;
            }
        });

        table.add(playBtn).width(180).center().colspan(2);
        table.row();
        table.add(menuBtn).center();
        table.add(settingsBtn).center();

        dialog.getButtonTable().add(table).center();
        dialog.show(stage).setPosition((MyGdxGame.WIDTH / 2), (MyGdxGame.HEIGHT / 2), Align.center);

        dialog.setName("pauseDialog");
        stage.addActor(dialog);
    }

    private boolean isAutoHideEnabled() {
        return (Gdx.app.getPreferences(PREFS_NAME)).getBoolean(PREF_AUTO_HIDE_BUTTONS, false);
    }

    public void dispose() {
        rocket.dispose();
        rocket2.dispose();
        rocket3.dispose();
        blop.dispose();
    }
}
