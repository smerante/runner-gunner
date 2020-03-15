package com.studios0110.runnergunner.handler;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.studios0110.runnergunner.screens.Splash;

public class SoundEffects {
    private Sound click, shoot, hit, die, time, load,firenMaLaser;
    private Music gameMusic, menuMusic;
    boolean clickPlaying;
    static boolean soundEnabled = true, musicEnabled = true;
    private static SoundEffects instance;

    private void initSoundEffects(){
        instance.click = Splash.manager.get("sounds/Click.mp3");
        instance.shoot = Splash.manager.get("sounds/Shoot.mp3");
        instance.hit = Splash.manager.get("sounds/Hit.mp3");
        instance.die = Splash.manager.get("sounds/Die.mp3");
        instance.time = Splash.manager.get("sounds/Time.mp3");
        instance.load = Splash.manager.get("sounds/Load.mp3");
        instance.gameMusic = Splash.manager.get("sounds/GameMusic.mp3");
        instance.menuMusic = Splash.manager.get("sounds/MenuMusic.mp3");
        instance.firenMaLaser = Splash.manager.get("sounds/FirenMaLaser.mp3");
        instance.menuMusic.setLooping(true);
        instance.menuMusic.setVolume(0.1f);
        instance.gameMusic.setLooping(true);
        instance.gameMusic.setVolume(0.5f);
        instance.clickPlaying = false;
    }
    private SoundEffects(boolean init){
    }
    public SoundEffects()
    {
        if(instance == null){
            instance = new SoundEffects(true);
            initSoundEffects();
        }
    }

    public void setEnableSound(boolean val){
        SoundEffects.soundEnabled = val;
    }

    public void setEnableMusic(boolean val){
        SoundEffects.musicEnabled = val;
        if(!val){
            instance.pauseMenu();
        }else{
            instance.playMenu();
        }
    }
    public void click(){
        if(SoundEffects.soundEnabled)
        {
            instance.click.play();
        }
    }

    public void firenMaLaser(){
        if(SoundEffects.soundEnabled) {
            instance.firenMaLaser.play();
        }
    }

    public void stopFirenMaLaser(){
        instance.firenMaLaser.stop();
    }

    public void load(){
        if(SoundEffects.soundEnabled) {
            instance.load.play();
        }
    }

    public void playMusic(){
        if(SoundEffects.musicEnabled) {
            instance.gameMusic.play();
        }
    }

    public void playMenu(){
        if(SoundEffects.musicEnabled) {
            instance.menuMusic.play();
        }
    }

    public void pauseMenu(){
        instance.menuMusic.pause();
    }

    public void pauseMusic(){
        instance.gameMusic.pause();
    }
    public void die(){
        if(SoundEffects.soundEnabled) {
            instance.die.play();
        }
    }


    public void time(){
        if(SoundEffects.soundEnabled) {
            instance.time.play();
        }
    }

    public void shoot(){
        if(SoundEffects.soundEnabled) {
            instance.shoot.play();
        }
    }
    public void hit(){
        if(SoundEffects.soundEnabled) {
            instance.hit.play();
        }
    }

    public void stopSounds(){
        instance.click.stop();
        instance.hit.stop();
        instance.shoot.stop();
        instance.time.stop();
        instance.gameMusic.stop();
        instance.load.stop();
        instance.firenMaLaser.stop();
    }

}
