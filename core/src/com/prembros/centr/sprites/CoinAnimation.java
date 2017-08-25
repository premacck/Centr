package com.prembros.centr.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 *
 * Created by Prem $ on 8/19/2017.
 */

@SuppressWarnings("Convert2Diamond")
class CoinAnimation {

    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int currentFrame;

    CoinAnimation(Texture texture, float cycleTime) {
        frames = new Array<TextureRegion>();
        frameCount = 30;
        int frameWidth = 63;
        for (int i = 0; i < frameCount; i++) {
            frames.add((new TextureRegion(texture, i * frameWidth, 0, frameWidth, texture.getHeight())));
        }
        maxFrameTime = cycleTime / frameCount;
        currentFrame = 0;
    }

    void update(float deltaTime) {
        currentFrameTime += deltaTime;
        if (currentFrameTime > maxFrameTime) {
            currentFrame++;
            currentFrameTime = 0;
        }
        if (currentFrame == frameCount) {
            currentFrame = 0;
        }
    }

    TextureRegion getCurrentFrame() {
        return frames.get(currentFrame);
    }
}
