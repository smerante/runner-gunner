package com.studios0110.runnergunner.screens;

/*
Sam Merante: Splash loading screen, loads assets
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.awt.Font;


public class Splash extends com.badlogic.gdx.Game implements Screen
{

    @Override
    public void create() {
        setScreen(this);
    }

    public static AssetManager manager = new AssetManager();
    public static int screenW = 1920, screenH = 1080;
    protected static OrthographicCamera camera = new OrthographicCamera();
    private Stage stage;
    private ShapeRenderer shapeBatch;
    private SpriteBatch batch;
    private boolean[] done;
    private void loadSplashScreens()
    {
        Image splashScreen = new Image(new Texture("splash/splash1.png"));
        Image splashScreen2 = new Image(new Texture("splash/splash2.png"));
        splashScreen.setWidth(Gdx.graphics.getWidth());
        splashScreen.setHeight(Gdx.graphics.getHeight());
        splashScreen2.setWidth(Gdx.graphics.getWidth());
        splashScreen2.setHeight(Gdx.graphics.getHeight());
        float delayTime = 0.1f;
        splashScreen.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(3f* delayTime),
                Actions.delay(3f* delayTime),
                Actions.run
                        (
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        done[0] = true;
                                    }
                                }
                        )
        ));

        splashScreen2.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.delay(3f* delayTime),
                Actions.fadeIn(1.5f* delayTime),
                Actions.fadeOut(1.5f* delayTime)
        ));

        stage.addActor(splashScreen);
        stage.addActor(splashScreen2);

    }

    @Override
    public void show() {
        camera.setToOrtho(false,screenW,screenH);
        stage = new Stage();
        batch = new SpriteBatch();
        shapeBatch = new ShapeRenderer();
        done = new boolean[2];
        done[0] = false;
        done[1] = false;
        loadSplashScreens();
        //AssestManager
        manager.load("player/Player1.png", Texture.class);
        manager.load("player/Player2.png", Texture.class);
        manager.load("player/Player3.png", Texture.class);
        manager.load("player/Select.png", Texture.class);
        manager.load("player/1Place.png", Texture.class);
        manager.load("player/2Place.png", Texture.class);
        manager.load("player/3Place.png", Texture.class);

        manager.load("objects/SmallWall.png", Texture.class);
        manager.load("objects/MidWall.png", Texture.class);
        manager.load("objects/LargeWall.png", Texture.class);
        manager.load("objects/XLWall.png", Texture.class);
        manager.load("objects/Ground.png", Texture.class);
        manager.load("objects/Pistol.png", Texture.class);
        manager.load("objects/MG.png", Texture.class);
        manager.load("objects/ShotGun.png", Texture.class);
        manager.load("objects/Sniper.png", Texture.class);
        manager.load("objects/RPG.png", Texture.class);
        manager.load("objects/AR.png", Texture.class);

        manager.load("screens/Start.png", Texture.class);
        manager.load("screens/Menu.png", Texture.class);
        manager.load("screens/Setup.png", Texture.class);
        manager.load("screens/Paused.png", Texture.class);
        manager.load("screens/Options.png", Texture.class);

        manager.load("buttons/Multiplayer.png", Texture.class);
        manager.load("buttons/MultiplayerHover.png", Texture.class);
        manager.load("buttons/MultiplayerSelected.png", Texture.class);
        manager.load("buttons/Options.png", Texture.class);
        manager.load("buttons/SoundEnabled.png", Texture.class);
        manager.load("buttons/SoundEnabledHover.png", Texture.class);
        manager.load("buttons/SoundEnabledSelected.png", Texture.class);
        manager.load("buttons/MusicEnabled.png", Texture.class);
        manager.load("buttons/MusicEnabledHover.png", Texture.class);
        manager.load("buttons/MusicEnabledSelected.png", Texture.class);
        manager.load("buttons/OptionsHover.png", Texture.class);
        manager.load("buttons/OptionsSelected.png", Texture.class);
        manager.load("buttons/L1.png", Texture.class);
        manager.load("buttons/R1.png", Texture.class);
        manager.load("buttons/Time.png", Texture.class);
        manager.load("buttons/Team.png", Texture.class);
        manager.load("buttons/Stock.png", Texture.class);
        manager.load("buttons/Ready.png", Texture.class);
        manager.load("buttons/Select.png", Texture.class);
        manager.load("buttons/0.png", Texture.class);
        manager.load("buttons/1.png", Texture.class);
        manager.load("buttons/2.png", Texture.class);
        manager.load("buttons/3.png", Texture.class);
        manager.load("buttons/4.png", Texture.class);
        manager.load("buttons/5.png", Texture.class);
        manager.load("buttons/6.png", Texture.class);
        manager.load("buttons/7.png", Texture.class);
        manager.load("buttons/8.png", Texture.class);
        manager.load("buttons/9.png", Texture.class);

        manager.load("fonts/Stats.fnt", BitmapFont.class);
        manager.load("fonts/Stats.png", Texture.class);

        manager.load("sounds/Click.mp3", Sound.class);
        manager.load("sounds/Shoot.mp3", Sound.class);
        manager.load("sounds/Hit.mp3", Sound.class);
        manager.load("sounds/Die.mp3", Sound.class);
        manager.load("sounds/Time.mp3", Sound.class);
        manager.load("sounds/Load.mp3", Sound.class);
        manager.load("sounds/FirenMaLaser.mp3", Sound.class);
        manager.load("sounds/GameMusic.mp3", Music.class);
        manager.load("sounds/MenuMusic.mp3", Music.class);
        //AssestManager


    }
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0,0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(manager.update())//if manager is done uploading then we can finish
            done[1] = true;
        shapeBatch.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
        shapeBatch.begin(ShapeType.Filled);
        shapeBatch.setColor(Color.DARK_GRAY);
        shapeBatch.rect(screenW/2 - 120, 385, 200, 17);
        shapeBatch.setColor(Color.GREEN);
        shapeBatch.rect(screenW/2 - 120, 384, (manager.getProgress()*200), 20);
        shapeBatch.end();
        if(done[0] && done[1]){
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Menu());
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
}
