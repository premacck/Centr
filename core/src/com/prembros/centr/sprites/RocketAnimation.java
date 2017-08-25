package com.prembros.centr.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 *
 * Created by Prem $ on 7/20/2017.
 */

@SuppressWarnings("Convert2Diamond")
class RocketAnimation {

    private Array<Texture> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int currentFrame;

    RocketAnimation(Texture texture1, Texture texture2, Texture texture3, int frameCount, float cycleTime) {
        frames = new Array<Texture>();
//        int frameWidth = texture.getWidth() / frameCount;
//        for (int i = 0; i < frameCount; i++) {
//            frames.add((new TextureRegion(texture, i * frameWidth, 0, frameWidth, texture.getHeight())).getTexture());
//        }
        frames.add(texture1);
        frames.add(texture2);
        frames.add(texture3);
        this.frameCount = frameCount;
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

    Texture getCurrentFrame() {
        return frames.get(currentFrame);
    }
}
