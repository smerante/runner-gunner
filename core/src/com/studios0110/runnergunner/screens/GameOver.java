package com.studios0110.runnergunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.studios0110.runnergunner.handler.AxisMapper;
import com.studios0110.runnergunner.handler.GameButton;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.interfaces.GameControllerListener;
import com.studios0110.runnergunner.objects.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Sam Merante on 2018-12-10.
 */
public class GameOver implements ScreenInterface, GameControllerListener {
    int playersCount;
    float width = Splash.screenW;
    Texture setupScreen,place1,place2,place3;
    BitmapFont stats;
    private ArrayList<Player> players, preSortedPlayers;
    SoundEffects soundEffects;
    private ArrayList<Boolean> playersReady;
    boolean L3,R3;
    public GameOver(int playerCount , ArrayList<Player> players){
        this.playersCount = playerCount;
        this.players = players;
        this.preSortedPlayers = new ArrayList<Player>();
        for(Player player: players){
            this.preSortedPlayers.add(player);
        }
        playersReady = new ArrayList<Boolean>();
        for(int i=0;i<playerCount;i++){
            playersReady.add(false);
        }
        Collections.sort(this.players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if(p1.deaths < p2.deaths){
                    return -1;
                }
                else if(p1.deaths == p2.deaths){
                    if(p1.kills > p2.kills){
                        return -1;
                    }
                    else if(p1.kills == p2.kills){
                        if(p1.accuracy > p2.accuracy){
                            return -1;
                        }
                    }
                    return 0;
                }
                else{
                    return 1;
                }
            }
        });
        stats = Splash.manager.get("fonts/Stats.fnt");
        place1 = Splash.manager.get("player/1Place.png");
        place2 = Splash.manager.get("player/2Place.png");
        place3 = Splash.manager.get("player/3Place.png");
        soundEffects = new SoundEffects();
    }

    @Override
    public void show() {
        System.out.println("Construct Game Over Screen " + playersCount + " players");
        Splash.camera.zoom=1;
        Splash.camera.update();
        Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
        for (int i=0; i < playersCount; i++) {
            Menu.controllerHandlers.get(i).addGameControllerListener(this);
        }
        setupScreen = Splash.manager.get("screens/Setup.png");
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
            for(int i=0;i < players.size();i++){
                double dk = (players.get(i).kills*1.0)/(Math.max(players.get(i).deaths,1)*1.0);
                stats.draw(batch, "Color",0,950);
                stats.draw(batch, "PLAYER",(width/6),950);
                stats.draw(batch, "DEATHS",(width/6)*2,950);
                stats.draw(batch, "KILLS",(width/6)*3,950);
                stats.draw(batch, "D/K",(width/6)*4,950);
                stats.draw(batch, "ACCURACY",(width/6)*5,950);
                stats.draw(batch,(players.get(i).playerNumber+1)+"", (width/6),900 - 40*i);
                stats.draw(batch,players.get(i).deaths+"", (width/6)*2,900 - 40*i);
                stats.draw(batch,players.get(i).kills+"", (width/6)*3,900 - 40*i);
                stats.draw(batch,round(dk,2)+"", (width/6)*4,900 - 40*i);
                stats.draw(batch,(int)players.get(i).accuracy+"%", (width/6)*5,900 - 40*i);
                if(i==0){
                    batch.draw(place1,width-50,(900-40) - 40*i);
                }
                if(i==1){
                    batch.draw(place2,width-50,(900-40) - 40*i);
                }
                if(i==2){
                    batch.draw(place3,width-50,(900-40) - 40*i);
                }

            }
        batch.end();

        shapes.begin();
        shapes.set(ShapeRenderer.ShapeType.Filled);
        for(int i=0;i < players.size();i++){
            if(playersReady.get(i)){
                shapes.setColor(Color.valueOf("00fff0"));
                shapes.circle(16,(900-16) - 40*i,32);
            }
            shapes.setColor(players.get(i).getTeamColor());
            shapes.circle(16,(900-16) - 40*i,16);
        }
        shapes.end();


        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
        }
    }
    private float round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    private boolean allPlayersReady(){
        int readiedPlayers = 0;
        for(boolean pReady: playersReady){
            readiedPlayers = pReady ? readiedPlayers+1 : readiedPlayers;
        }
        if(readiedPlayers == playersCount){
            return true;
        }
        else return false;
    }

    @Override
    public void buttonDown(int playerI, String button) {
        if(button.equalsIgnoreCase("L3")){
            L3 = true;
        }
        if(button.equalsIgnoreCase("R3")){
            R3 = true;
        }
        if(L3 && R3)
            Gdx.app.exit();
        if(button.equalsIgnoreCase("x") || button.equalsIgnoreCase("a")){
            soundEffects.click();
            playersReady.set(players.indexOf(preSortedPlayers.get(playerI)),true);
            if(allPlayersReady())
                ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
        }
        if(button.equalsIgnoreCase("Circle") || button.equalsIgnoreCase("B")){
            soundEffects.click();
            playersReady.set(players.indexOf(preSortedPlayers.get(playerI)),false);
        }
    }

    @Override
    public void buttonUp(int playerI, String button) {
        if(button.equalsIgnoreCase("L3")){
            L3 = false;
        }
        if(button.equalsIgnoreCase("R3")){
            R3 = false;
        }
    }

    @Override
    public void povMoved(int playerI, String pov) {
    }

    @Override
    public void axisMoved(int playerI, String axis, AxisMapper axisMapper) {
    }
}
