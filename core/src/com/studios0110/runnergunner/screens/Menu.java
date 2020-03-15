package com.studios0110.runnergunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.studios0110.runnergunner.handler.AxisMapper;
import com.studios0110.runnergunner.handler.ControllerHandler;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.interfaces.GameControllerListener;
import java.util.ArrayList;

/**
 * Created by Sam Merante on 2018-10-22.
 */
public class Menu implements ScreenInterface, GameControllerListener {

    public static ArrayList<ControllerHandler> controllerHandlers;

    private Texture menuScreen,startText;
    private int playersCount;
    private float waitTimer, flashText, startX;

    Menu(){
        controllerHandlers = new ArrayList<ControllerHandler>();
        this.playersCount = 0;
        for (Controller controller : Controllers.getControllers()) {
            ControllerHandler tempControllerHandler = new ControllerHandler(playersCount);
            tempControllerHandler.addGameControllerListener(this);
            controllerHandlers.add(tempControllerHandler);
            controller.addListener(tempControllerHandler);
            playersCount++;
        }
        menuScreen = Splash.manager.get("screens/Menu.png");
        startText = Splash.manager.get("screens/Start.png");
        startX = 0;
        waitTimer = 0;
        flashText = 0.5f;
        SoundEffects soundEffects = new SoundEffects();
        soundEffects.playMenu();
    }
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0.568f,0.917f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(com.studios0110.runnergunner.screens.Splash.camera.combined);
        shapes.setProjectionMatrix(com.studios0110.runnergunner.screens.Splash.camera.combined);
        shapes.setAutoShapeType(true);

        batch.begin();
            batch.draw(menuScreen,0,0);
            if(waitTimer > 0){
                flashText+=1*delta;
                if(flashText >= 0.5) {
                    if(flashText >= 1)
                        flashText = 0;
                    batch.draw(startText, startX - 1920, 0);
                    batch.draw(startText, startX, 0);
                    batch.draw(startText, startX + 1290, 0);
                }
            }
            else{
                flashText = 0.5f;
                batch.draw(startText,startX-1920,0);
                batch.draw(startText,startX,0);
                batch.draw(startText,startX+1290,0);
            }
        batch.end();

        update(delta);
    }
    private void update(float delta){
        if(waitTimer > 0)
        {
            waitTimer-=1*delta;
        }
        else{
            startX+=960*delta;
            if(startX >= 1920){
                startX = 0;
                waitTimer = 3;
            }
        }
    }
    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    @Override
    public void buttonDown(int controller, String button) {
    }

    @Override
    public void buttonUp(int controller,String button) {
        try{
            if(button == null) return;
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
        }catch(Exception ignored){}
    }

    @Override
    public void povMoved(int controller,String pov) {
    }

    @Override
    public void axisMoved(int controller,String axis, AxisMapper axisMapper) {
    }
}
