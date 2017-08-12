package com.prembros.centr.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.prembros.centr.MyGdxGame;
import com.prembros.centr.sprites.Rocket;

import static com.prembros.centr.sprites.Rocket.IS_MENU_LAUNCHED;

/**
 *
 * Created by Prem $ on 7/24/2017.
 */

public class HelpState extends State {

    public static boolean IS_POINTER_HIDDEN;

    private Rocket rocket;
    private Texture bg;
    private Texture rocketPointer;
    private NinePatchDrawable frame;

    HelpState(GameStateManager gameStateManager, MyGdxGame game) {
        super(gameStateManager, game);
        IS_POINTER_HIDDEN = false;
        camera.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        rocket = new Rocket(45, true);
        rocketPointer = new Texture(Gdx.files.internal("skin/rocket_pointer.png"));
        bg = new Texture("bg_home.png");
        frame = new NinePatchDrawable(getNinePatch("skin/table_border.png"));
        Table table = new Table(skin);
        table.setPosition(MyGdxGame.WIDTH / 3, 0);
        table.setSize(MyGdxGame.WIDTH - (MyGdxGame.WIDTH / 3), MyGdxGame.HEIGHT);

        Label title = new Label("This is your rocket", skin, "title_white_small");
        title.setWrap(true);
        title.setAlignment(Align.center);

        Label subtitle = new Label("It tends to attract to the dark center!" +
                "\n\n(Sometimes, if you're lucky, it may pass right across it!)", skin, "white");
        subtitle.setWrap(true);
        subtitle.setAlignment(Align.center);
        subtitle.setFontScale(0.5f);

        Label message1;
        switch (Gdx.app.getType()) {
            case Android:
                message1 = new Label("Use the control keys on the bottom to keep the rocket away from it.",
                        skin, "white");
                break;
            case Desktop:
                message1 = new Label("Use the up / down keys to keep the rocket away from it.",
                        skin, "white");
                break;
            default:
                message1 = new Label("Use the control keys on the bottom (UP/DOWN keys) to keep the rocket away from it.",
                        skin, "white");
        }
        message1.setWrap(true);
        message1.setAlignment(Align.center);
        message1.setFontScale(0.5f);

        Label message2 = new Label("Go on, give it a try!", skin, "white");
        message2.setWrap(true);
        message2.setAlignment(Align.center);
        message2.setFontScale(0.8f);

        Label message3 = new Label("In the game, avoid the obstacles to earn points!", skin, "white");
        message3.setWrap(true);
        message3.setAlignment(Align.center);
        message3.setFontScale(0.5f);

        table.row().pad(0, 30, 5, 20).align(Align.top);
        table.add(title).width(300);
        table.row().pad(0, 30, 0, 20);
        table.add(subtitle).width(300);
        table.row().pad(60, 30, 0, 20);
        table.add(message1).width(300);
        table.row().pad(10, 30, 0, 20);
        table.add(message2).width(300);
        table.row().pad(50, 30, 160, 20);
        table.add(message3).width(300);

        stage.clear();
        stage.addActor(table);
    }

    private NinePatch getNinePatch(String fName) {

        // Get the image
        final Texture t = new Texture(Gdx.files.internal(fName));

        // create a new texture region, otherwise black pixels will show up too, we are simply cropping the image
        // last 4 numbers represent the length of how much each corner can draw,
        // for example if your image is 50px and you set the numbers 50, your whole image will be drawn in each corner
        // so what number should be good?, well a little less than half would be nice
        return new NinePatch( new TextureRegion(t, 0, 0 , t.getWidth(), t.getHeight()), 20, 20, 20, 20);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            gameStateManager.set(new MenuState(gameStateManager, game));
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!IS_MENU_LAUNCHED) {
            Gdx.input.setCatchBackKey(true);
            handleInput();
            rocket.update(deltaTime);
        } else {
            gameStateManager.set(new MenuState(gameStateManager, game));
            IS_MENU_LAUNCHED = false;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(bg, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        spriteBatch.draw(rocket.getTexture(), rocket.getPosition().x, rocket.getPosition().y,
                rocket.getWidth() / 2, rocket.getHeight() / 2, rocket.getWidth(), rocket.getHeight(), 1, 1, rocket.getRotation(),
                0, 0, rocket.getWidth(), rocket.getHeight(), false, false);
        frame.draw(spriteBatch, 15, 0, MyGdxGame.WIDTH / 3, MyGdxGame.HEIGHT);
        if (!IS_POINTER_HIDDEN)
            spriteBatch.draw(rocketPointer, 45, (MyGdxGame.HEIGHT / 2) + rocket.getHeight());
        spriteBatch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), (1 / 30f)));
        stage.draw();
        rocket.getStage().act(Math.min(Gdx.graphics.getDeltaTime(), (1 / 30f)));
        rocket.getStage().draw();
    }

    @Override
    public void dispose() {
        rocket.dispose();
        rocketPointer.dispose();
        bg.dispose();
        stage.dispose();
        frame.getPatch().getTexture().dispose();
    }
}
