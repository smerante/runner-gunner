package com.studios0110.runnergunner.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.studios0110.runnergunner.screens.Splash;

/**
 * Created by Sam Merante on 2018-11-11.
 */
public class GameButton {
    Texture texture,hover,selected;
    float xPos,yPos;
    public boolean hovering, selecting;
    public GameButton(String texture, float x, float y){
        this.texture = Splash.manager.get("buttons/"+texture+".png");
        this.hover = Splash.manager.get("buttons/"+texture+"Hover.png");
        this.selected = Splash.manager.get("buttons/"+texture+"Selected.png");
        this.xPos = x;
        this.yPos = y;
        this.hovering = false;
        this.selecting = false;
    }

    public void draw(SpriteBatch batch){
        if(selecting)
            batch.draw(selected,xPos,yPos);
        if(hovering)
            batch.draw(hover,xPos,yPos);
        if(!hovering && !selecting)
            batch.draw(texture,xPos,yPos);

    }

    public void setHover(boolean val){
        this.hovering = val;
    }
    public void setSelected(boolean val){
        this.selecting = val;
    }
}
