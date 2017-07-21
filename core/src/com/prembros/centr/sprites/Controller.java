package com.prembros.centr.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.prembros.centr.MyGdxGame;

/**
 *
 * Created by Prem $ on 7/21/2017.
 */

public class Controller {

    Viewport viewport;
    Stage stage;
    OrthographicCamera camera;

    public Controller() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, camera);
        stage = new Stage(viewport, MyGdxGame.spriteBatch);
    }
}
