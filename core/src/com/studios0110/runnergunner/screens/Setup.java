package com.studios0110.runnergunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.studios0110.runnergunner.handler.AxisMapper;
import com.studios0110.runnergunner.handler.GameButton;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.interfaces.GameControllerListener;

/**
 * Created by Sam Merante on 2018-11-11.
 */
public class Setup implements ScreenInterface, GameControllerListener {
    private Texture setupScreen;
    private GameButton multiPlayer,options;
    private SoundEffects soundEffects;
    private boolean L3, R3;
    private int playersCount, buttonFocused;

    public Setup(int playerCount){
        this.playersCount = playerCount;
        soundEffects = new SoundEffects();
        for (int i=0; i < playersCount; i++) {
            Menu.controllerHandlers.get(i).addGameControllerListener(this);
        }
        multiPlayer = new GameButton("Multiplayer",522,467);
        options = new GameButton("Options",677,207);
        setupScreen = Splash.manager.get("screens/Setup.png");
        this.buttonFocused = 1;
        this.multiPlayer.setHover(true);
    }

    @Override
    public void show() {
        Splash.camera.zoom=1;
        Splash.camera.update();
//        Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
        System.out.println("Construct Setup Screen " + playersCount + " players");
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
            batch.draw(setupScreen,0,0);
            multiPlayer.draw(batch);
            options.draw(batch);
        batch.end();

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
    public void buttonDown(int playerI, String button) {
        try{
            if(button.equalsIgnoreCase("L3")){
                L3 = true;
            }
            if(button.equalsIgnoreCase("R3")){
                R3 = true;
            }
            if(L3 && R3)
                Gdx.app.exit();

            if(button.equals("A") || button.equals("X")){
                soundEffects.click();
                if(buttonFocused == 1){
                    this.multiPlayer.setSelected(true);
                    this.multiPlayer.setHover(false);
                    this.options.setSelected(false);
                    this.options.setHover(false);
                }

                else{
                    this.options.setSelected(true);
                    this.options.setHover(false);
                    this.multiPlayer.setSelected(false);
                    this.multiPlayer.setHover(false);
                }
            }
        }catch(Exception ignored){}

    }

    @Override
    public void buttonUp(int playerI, String button) {
        try {
            if(button.equalsIgnoreCase("L3")){
                L3 = false;
            }
            if(button.equalsIgnoreCase("R3")){
                R3 = false;
            }
            if(button.equals("A") || button.equals("X")){
                if(buttonFocused == 1){
                    if(multiPlayer.selecting){
                        //Multi-player Selected
                        ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new PlayerSelect(this.playersCount));
                        this.options.setSelected(false);
                        this.options.setHover(false);
                        this.multiPlayer.setSelected(false);
                        this.multiPlayer.setHover(false);
                    }
                }
                else{
                    if(options.selecting){
                        //Options Selected
                        ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Options(this.playersCount));
                        this.options.setSelected(false);
                        this.options.setHover(false);
                        this.multiPlayer.setSelected(false);
                        this.multiPlayer.setHover(false);
                    }
                }
            }
        }catch(Exception ignored){}
    }

    @Override
    public void povMoved(int playerI, String pov) {
        try {
            if(pov.equals("down")){
                soundEffects.click();
                this.buttonFocused++;
                if(buttonFocused >= 3){
                    buttonFocused = 1;
                }
                if(buttonFocused == 1){
                    this.multiPlayer.setHover(true);
                    this.options.setSelected(false);
                    this.options.setHover(false);
                }
                else{
                    this.multiPlayer.setHover(false);
                    this.multiPlayer.setSelected(false);
                    this.options.setHover(true);
                }
            }
            if(pov.equals("up")){
                soundEffects.click();
                this.buttonFocused--;
                if(buttonFocused <= 0){
                    buttonFocused = 2;
                }
                if(buttonFocused == 1){
                    this.multiPlayer.setHover(true);
                    this.options.setSelected(false);
                    this.options.setHover(false);
                }
                else{
                    this.multiPlayer.setHover(false);
                    this.multiPlayer.setSelected(false);
                    this.options.setHover(true);
                }
            }
        }catch(Exception ignored){}
    }

    @Override
    public void axisMoved(int playerI, String axis, AxisMapper axisMapper) {
            try{
            if(axisMapper.leftStickY >= 1){
                this.buttonFocused--;
                if(buttonFocused <= 0){
                    buttonFocused = 2;
                }
                if(buttonFocused == 1){
                    this.multiPlayer.setHover(true);
                    this.options.setSelected(false);
                    this.options.setHover(false);
                }
                else{
                    this.multiPlayer.setHover(false);
                    this.multiPlayer.setSelected(false);
                    this.options.setHover(true);
                }
            }
            if(axisMapper.leftStickY <= -1){
                this.buttonFocused++;
                if(buttonFocused >= 3){
                    buttonFocused = 1;
                }
                if(buttonFocused == 1){
                    this.multiPlayer.setHover(true);
                    this.options.setSelected(false);
                    this.options.setHover(false);
                }
                else{
                    this.multiPlayer.setHover(false);
                    this.multiPlayer.setSelected(false);
                    this.options.setHover(true);
                }
            }
            }catch(Exception ignored){}
    }
}
