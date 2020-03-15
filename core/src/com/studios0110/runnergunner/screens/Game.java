package com.studios0110.runnergunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.studios0110.runnergunner.handler.MapCreator;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.objects.Bullet;
import com.studios0110.runnergunner.objects.Gun;
import com.studios0110.runnergunner.objects.Player;
import com.studios0110.runnergunner.objects.Wall;

import java.util.ArrayList;

/*
Sam Merante: This Starter Screen is a menu to get to other screens.
 */
public class Game implements ScreenInterface {

    public static ArrayList<Player> players;
    public static ArrayList<Vector3> blood;
    public static  ArrayList<Wall> walls = new ArrayList<Wall>();
    public static  ArrayList<Gun> guns = new ArrayList<Gun>();
    public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    public static int gameModeNumber, playerThatPaused;
    public static boolean paused, timeMode;


    private boolean[] justSwitchedTime;
    private ArrayList<Integer> playerChoices;
    private ArrayList<Color> playerColors;
    private  ArrayList<Texture>numbers;

    private BitmapFont font;
    private SoundEffects soundEffects;
    private Texture ground,pausedScreen, time, stock;

    private float width = Splash.screenW, height = Splash.screenH,
            gameTime,showFPSDelay,FPS;
    private boolean showDebug;
    private int playersCount;

    public Game(int playersCount,ArrayList<Integer> playerChoices,ArrayList<Color> playerColors){
        this.playersCount = playersCount;
        this.playerChoices = playerChoices;
        this.playerColors = playerColors;
        justSwitchedTime = new boolean[]{false, false, false, false, false, false, false, false, false, false, false};
        showDebug = false;
        FPS = 60;
        font = new BitmapFont();
        System.out.println("Construct Game Screen " + playersCount + " players");
        blood = new ArrayList<Vector3>();
        MapCreator.setWalls();
        MapCreator.setGuns(playersCount);
        MapCreator.setPlayers(playersCount,playerChoices,playerColors);
        ground = Splash.manager.get("objects/Ground.png");
        if(timeMode && gameModeNumber > 0){
            gameTime = Game.gameModeNumber*60.0f;
        }
        numbers = new ArrayList<Texture>();
        for(int i=0;i<10;i++){
            numbers.add((Texture)Splash.manager.get("buttons/"+i+".png"));
        }
        time = Splash.manager.get("buttons/Time.png");
        stock = Splash.manager.get("buttons/Stock.png");

        Game.paused = false;
        Game.playerThatPaused = -1;
        pausedScreen = Splash.manager.get("screens/Paused.png");
        soundEffects = new SoundEffects();
        soundEffects.playMusic();
    }

    public void show() {
        Splash.camera.zoom=1;
        Splash.camera.update();
//Not using touch processing        Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0.568f,0.917f,1);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(com.studios0110.runnergunner.screens.Splash.camera.combined);
        shapes.setProjectionMatrix(com.studios0110.runnergunner.screens.Splash.camera.combined);
        shapes.setAutoShapeType(true);
        if(!Game.paused) {
            batch.enableBlending();
            batch.begin();
                batch.draw(ground, 0, 0);
            batch.end();

            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
            shapes.begin();
            for (Player player : players)
                player.draw(shapes, this.showDebug, delta);
            for (Gun gun : guns)
                gun.draw(shapes, this.showDebug, delta);
            for (Vector3 blood : blood) {
                shapes.set(ShapeRenderer.ShapeType.Filled);
                shapes.setColor(147 / 255.0f, 22 / 255.0f, 0 / 255.0f, 0.8f);
                shapes.circle(blood.x, blood.y, blood.z);
            }
            for(int i=0; i<Game.bullets.size(); i++){
                Game.bullets.get(i).draw(shapes,delta);
                if(Game.bullets.get(i).life <= 0){
                    Game.bullets.remove(i);
                }
            }
            shapes.end();

            batch.begin();
            for (Player player : players)
                player.draw(batch, delta);
            for (Wall wall : walls)
                wall.draw(batch);
            for (Gun gun : guns)
                gun.draw(batch, delta);
            if (timeMode && gameTime < 10) {
                batch.draw(numbers.get((int) gameTime), width / 2 - 44, height / 2 - 40);
            }
            batch.end();

            drawEndGameTimer(batch, shapes);
            update(delta);
        }
        else{
            batch.begin();
                drawPausedScreen(batch);
            batch.end();
            shapes.begin();
                drawPausedScreen(shapes);
            shapes.end();
                updateEndGame(delta);
        }
    }
    private void drawPausedScreen(ShapeRenderer shapes) {
        if(!Game.timeMode){
            for(int i =0; i< playersCount; i++ ){
                shapes.set(ShapeRenderer.ShapeType.Filled);
                shapes.setColor(players.get(i).getTeamColor());
                if(i < 8){
                    shapes.circle((160-40),(760+40) - 88*i, 40);
                }else{
                    int iY = i-8;
                    shapes.circle((1060-40),(760+40) - 88*iY, 40);
                }
            }
        }
    }

    private void drawPausedScreen(SpriteBatch batch) {
        batch.draw(pausedScreen,0,0);
        if(Game.timeMode){
            batch.draw(time,260,600);
            int first = (int)((gameTime /60.0f));
            int second,third,fourth;
            if(first < 10) {
                batch.draw(numbers.get(first), 800, 600);
                float seconds = first > 0 ? ((gameTime/60)%1)*60: gameTime;
                third = (int)((seconds /10));
                fourth = (int)(( seconds % 10));
                batch.draw(numbers.get(third), 800+88*3, 600);
                batch.draw(numbers.get(fourth), 800+88*4, 600);
            }
            else{
                first = (int)((gameTime /60.0f))/10;
                second = (int)(( gameTime / 60.0f))%10;
                batch.draw(numbers.get(first), 800, 600);
                batch.draw(numbers.get(second), 800+88, 600);

                float seconds = first > 0 ? ((gameTime/60)%1)*60: gameTime;
                third = (int)((seconds /10));
                fourth = (int)(( seconds % 10));
                batch.draw(numbers.get(third), 800+88*3, 600);
                batch.draw(numbers.get(fourth), 800+88*4, 600);

            }
        }else{
            for(int i =0; i<playersCount; i++ ){
                if(i < 8){
                    batch.draw(stock,160,760 - 88*i);
                    batch.draw(numbers.get(players.get(i).lives/10),660,760 - 88*i);
                    batch.draw(numbers.get(players.get(i).lives%10),660+88,760 - 88*i);
                }else{
                    int iY = i-8;
                    batch.draw(stock,1060,760 - 88*iY);
                    batch.draw(numbers.get(players.get(i).lives/10),1560,760 - 88*iY);
                    batch.draw(numbers.get(players.get(i).lives%10),1560+88,760 - 88*iY);
                }
            }
        }
    }

    private void drawEndGameTimer(SpriteBatch batch, ShapeRenderer shapes){
        shapes.begin();
        if(timeMode && gameTime < 10){
            shapes.set(ShapeRenderer.ShapeType.Filled);
            shapes.setColor(1,1,1,1);
            shapes.rect(width/2 - 44,height/2 - 40,88,88);
        }
        shapes.end();

        batch.begin();
        if(timeMode && gameTime < 10){
            batch.draw(numbers.get((int)gameTime),width/2 - 44,height/2 - 40);
        }
        batch.end();
    }
    private void updateEndGame(float delta){
        int numAlive = 0;
        for(Player player: players)
        {
            if(player.alive())numAlive++;
        }
        if(numAlive > 1){
            boolean sameTeam = true;
            Color color = players.get(0).getTeamColor();
            for(int i=1;i<players.size();i++){
                if(players.get(i).alive()){
                    sameTeam = sameTeam && ((color.r == players.get(i).getTeamColor().r) && (color.g == players.get(i).getTeamColor().g) && (color.b == players.get(i).getTeamColor().b));
                }
            }
            if(sameTeam) {
                numAlive = 1;
            }
        }
        if(numAlive == 1 || (timeMode && gameTime <= 0)){
            this.soundEffects.stopSounds();
            this.soundEffects.playMenu();
            Game.bullets.clear();
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new GameOver(this.playersCount, players));
        }
        if(timeMode && gameTime >0){
            gameTime-=delta;
            if(((int)gameTime) < 10 ){
                if(!justSwitchedTime[(int)gameTime]){
                    justSwitchedTime[(int)gameTime] = true;
                    this.soundEffects.time();
                }
            }
        }
    }
    private void update(float delta) {
        updateEndGame(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            showDebug = !showDebug;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (showDebug) {
            showFPS(delta);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
        }
    }

    private void showFPS(float delta)
    {
        showFPSDelay += 1 * Gdx.graphics.getDeltaTime();
        if (showFPSDelay >= 0.25) {
            showFPSDelay = 0;
            FPS = (int) Math.ceil(1 / delta);
        }
        batch.begin();
        font.draw(batch, "Menu - FPS " + (int) FPS, width/2, height-20);
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
    public void dispose() {
        System.out.println("Dispose Menu Screen");
        com.studios0110.runnergunner.screens.Splash.camera.zoom=1;
        Splash.camera.update();
        font = null;
        showDebug = false;
        FPS = 0;
        mp.clear();
        Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
    }

}
