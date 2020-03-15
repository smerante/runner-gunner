package com.studios0110.runnergunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.studios0110.runnergunner.handler.AxisMapper;
import com.studios0110.runnergunner.handler.GameButton;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.interfaces.GameControllerListener;

/**
 * Created by Sam Merante on 2018-12-16.
 */
public class Options  implements ScreenInterface, GameControllerListener {

    private int playersCount, buttonFocused;
    private GameButton musicEnabled, soundEnabled;
    private Texture optionsScreen;
    private SoundEffects soundEffects;
    public static boolean music=true,sound=true;
    private boolean L3, R3;

    public Options(int playersCount){
        this.playersCount = playersCount;
        for (int i=0; i < playersCount; i++) {
            Menu.controllerHandlers.get(i).addGameControllerListener(this);
        }
        optionsScreen = Splash.manager.get("screens/Options.png");
        soundEffects = new SoundEffects();
        soundEffects.playMenu();
        musicEnabled = new GameButton("MusicEnabled",1200,776);
        soundEnabled = new GameButton("SoundEnabled",1200,700);
        musicEnabled.setSelected(Options.music);
        soundEnabled.setSelected(Options.sound);
        this.buttonFocused = 1;
        this.musicEnabled.setHover(true);
    }

    @Override
    public void show() {
        Splash.camera.zoom=1;
        Splash.camera.update();
//        Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
        System.out.println("Options screen constructed");
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
            batch.draw(optionsScreen,0,0);
            soundEnabled.draw(batch);
            musicEnabled.draw(batch);
        batch.end();

        update();
    }
    private void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            this.dispose();
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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
                    musicEnabled.setSelected(true);
                }
                else{
                    soundEnabled.setSelected(true);
                }
            }
        }catch(Exception ignored){}
    }

    @Override
    public void buttonUp(int controller,String button) {
        try{
            if(button.equalsIgnoreCase("L3")){
                L3 = false;
            }
            if(button.equalsIgnoreCase("R3")){
                R3 = false;
            }
            if(button == null) return;
            if(button.equals("A") || button.equals("X")){
                if(buttonFocused == 1){
                    if(musicEnabled.selecting){
                        Options.music = !Options.music;
                        musicEnabled.setSelected(Options.music);
                        soundEffects.setEnableMusic(Options.music);
                    }
                }
                else{
                    if(soundEnabled.selecting){
                        Options.sound = !Options.sound;
                        soundEnabled.setSelected(Options.sound);
                        soundEffects.setEnableSound(Options.sound);
                    }
                }
            }
            if(button.equalsIgnoreCase("Circle") || button.equalsIgnoreCase("B"))
                ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
        }catch(Exception ignored){}
    }

    @Override
    public void povMoved(int controller,String pov) {
        try{
            if(pov.equals("down")){
                soundEffects.click();
                this.buttonFocused++;
                if(buttonFocused >= 3){
                    buttonFocused = 1;
                }
                if(buttonFocused == 1){
                    this.musicEnabled.setHover(true);
                    this.soundEnabled.setHover(false);
                }
                else{
                    this.musicEnabled.setHover(false);
                    this.soundEnabled.setHover(true);
                }
            }
            if(pov.equals("up")){
                soundEffects.click();
                this.buttonFocused--;
                if(buttonFocused <= 0){
                    buttonFocused = 2;
                }
                if(buttonFocused == 1){
                    this.musicEnabled.setHover(true);
                    this.soundEnabled.setHover(false);
                }
                else{
                    this.musicEnabled.setHover(false);
                    this.soundEnabled.setHover(true);
                }
            }
            soundEnabled.setSelected(Options.sound);
            musicEnabled.setSelected(Options.music);
        }catch(Exception ignored){}
    }

    @Override
    public void axisMoved(int controller,String axis, AxisMapper axisMapper) {}
}
