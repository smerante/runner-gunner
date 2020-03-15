package com.studios0110.runnergunner.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.studios0110.runnergunner.screens.Splash;

/**
 * Created by Sam Merante on 2018-10-21.
 */
public class Wall {
    public float x, y, width, height;
    Texture texture;
    public Wall(String texture, float x, float y) {
        this.texture = Splash.manager.get(texture,Texture.class);
        this.x = x;
        this.y = y;
        this.width = this.texture.getWidth();
        this.height = this.texture.getHeight();
    }
    public Wall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void draw(SpriteBatch batch){
        if(this.texture != null)
            batch.draw(texture, x ,y);
    }
}
