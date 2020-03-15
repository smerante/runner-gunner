package com.studios0110.runnergunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.studios0110.runnergunner.handler.AxisMapper;
import com.studios0110.runnergunner.handler.ControllerHandler;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.interfaces.GameControllerListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sam Merante on 2018-11-15.
 */
public class PlayerSelect  implements ScreenInterface, GameControllerListener {

    public static ArrayList<ControllerHandler> controllerHandlers;
    private ArrayList<Color> playersColors,teamColors;
    private ArrayList<Circle> playerIndicators;
    private ArrayList<Texture> numbers;
    private ArrayList<Integer> teamSelect;
    private ArrayList<Boolean> playersReady;

    private SoundEffects soundEffects;
    private Texture charSelectScreen, select, p1, p2, p3, L1, R1, time, stock, selectButton, team, ready;

    private float width = Splash.screenW,
            p1StartX,p2StartX,p3StartX,numberIncreaseDelay;
    private int gameModeNumber = 1, playersCount;
    public static boolean timeMode = true,increaseNumber,decreaseNumber;
    private boolean L3, R3;

    public PlayerSelect(int playersCount){
        this.playersCount = playersCount;
        Random random = new Random(System.currentTimeMillis());
        playersColors = new ArrayList<Color>();
        teamSelect = new ArrayList<Integer>();
        teamColors = new ArrayList<Color>();
        playerIndicators = new ArrayList<Circle>();
        p1StartX = (width/3)  - (width/3)/2 - 214 + 40;
        p2StartX = (width/3)*2  - (width/3)/2 - 214 + 40;
        p3StartX = width  - (width/3)/2 - 214 + 40;

        teamColors.add(new Color(0,0,0,1)); //Team 1
        teamColors.add(new Color(0,0,1,1)); //Team 2
        teamColors.add(new Color(0,1,0,1)); //Team 3
        teamColors.add(new Color(0,1,1,1)); //Team 4
        teamColors.add(new Color(1,0,0,1)); //Team 5
        teamColors.add(new Color(1,0,1,1)); //Team 6
        teamColors.add(new Color(1,1,0,1)); //Team 7
        teamColors.add(new Color(1,1,1,1)); //Team 8
        playersReady = new ArrayList<Boolean>();

        for(int i=0; i<playersCount; i++){
            playersReady.add(false);
            playersColors.add(new Color(random.nextFloat(),random.nextFloat(),random.nextFloat(),1));
            if(i>10){
                playerIndicators.add(new Circle(p1StartX,630,30));
            }
            else if(i>5){
                playerIndicators.add(new Circle(p1StartX,700,30));
            }else{
                playerIndicators.add(new Circle(p1StartX,770,30));
            }
            teamSelect.add(-1); //Free for all unless changed
            movePlayerIndicator(i,1);
            Menu.controllerHandlers.get(i).addGameControllerListener(this);
        }
        soundEffects = new SoundEffects();

        charSelectScreen = Splash.manager.get("screens/Setup.png");
        select = Splash.manager.get("player/Select.png");
        p1 = Splash.manager.get("player/Player1.png");
        p2 = Splash.manager.get("player/Player2.png");
        p3 = Splash.manager.get("player/Player3.png");
        L1 = Splash.manager.get("buttons/L1.png");
        R1 = Splash.manager.get("buttons/R1.png");
        time = Splash.manager.get("buttons/Time.png");
        stock = Splash.manager.get("buttons/Stock.png");
        selectButton = Splash.manager.get("buttons/Select.png");
        team = Splash.manager.get("buttons/Team.png");
        ready = Splash.manager.get("buttons/Ready.png");
        numbers = new ArrayList<Texture>();

        for(int i=0;i<10;i++){
            numbers.add((Texture)Splash.manager.get("buttons/"+i+".png"));
        }
    }
    @Override
    public void show() {
        System.out.println("charSelect Screen " + playersCount + " players");
        Splash.camera.zoom=1;
        Splash.camera.update();
//        Gdx.input.setInputProcessor(mp); // set the default input processor to the multiplex processor from NewScreenInterface
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
            batch.draw(charSelectScreen,0,0);
            batch.draw(L1,50,32);
            batch.draw(R1,1650,32);
            if(timeMode)
                batch.draw(time,500,32);
            else
                batch.draw(stock,500,32);
            drawGameModeNumbers(batch);
            batch.draw(team, 125,850);
            batch.draw(ready, width/2 - 160,850);
            batch.draw(selectButton,500,120);
            batch.draw(select,(width/3)  - (width/3)/2 - 214,275);
            batch.draw(p1,(width/3)  - (width/3)/2 - 100/2,285,100,100);

            batch.draw(select,(width/3)*2  - (width/3)/2 - 214,275);
            batch.draw(p2,(width/3)*2  - (width/3)/2 - 200/2,275,200,200);

            batch.draw(select,width - (width/3)/2 - 214 ,275);
            batch.draw(p3,width  - (width/3)/2 - 300/2,275,300,300);
        batch.end();

        shapes.begin();
            shapes.set(ShapeRenderer.ShapeType.Filled);
            for (int i=0; i < playersCount; i++) {
                if(playersReady.get(i)){
                    shapes.setColor(Color.valueOf("00fff0"));
                    shapes.circle(playerIndicators.get(i).x,playerIndicators.get(i).y,playerIndicators.get(i).radius+10);
                }
                shapes.setColor(playersColors.get(i));
                shapes.circle(playerIndicators.get(i).x,playerIndicators.get(i).y,playerIndicators.get(i).radius);
            }
        shapes.end();

        update(delta);
    }

    private void drawGameModeNumbers(SpriteBatch batch){
        if(numberIncreaseDelay <= 0) {
            if (increaseNumber) {
                soundEffects.click();
                this.gameModeNumber = gameModeNumber < 99 ? gameModeNumber + 1 : gameModeNumber;
            }
            if (decreaseNumber) {
                soundEffects.click();
                this.gameModeNumber = gameModeNumber > 1 ? gameModeNumber - 1 : gameModeNumber;
            }
            numberIncreaseDelay = 0.2f;
        }


            if(this.gameModeNumber < 10)
                batch.draw(numbers.get(gameModeNumber),1000,32);
            else if(this.gameModeNumber <= 99){
                int first = this.gameModeNumber/10;
                int second = this.gameModeNumber%10;
                batch.draw(numbers.get(first),1000,32);
                batch.draw(numbers.get(second),1088,32);
            }
    }
    private void update(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            this.dispose();
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if(numberIncreaseDelay > 0)
            numberIncreaseDelay-=delta;
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

            if(button.equalsIgnoreCase("R1")){
                increaseNumber = true;
            }

            if(button.equalsIgnoreCase("L1")){
                decreaseNumber = true;
            }

            if((button.equalsIgnoreCase("Circle") || button.equalsIgnoreCase("B"))){
                soundEffects.click();
                if(playersReady.get(controller)){
                    playersReady.set(controller,false);
                }else{
                    ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(this.playersCount));
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
            if(button.equalsIgnoreCase("select")){
                soundEffects.click();
                timeMode = !timeMode;
            }

            if(button.equalsIgnoreCase("R1")){
                increaseNumber = false;
            }

            if(button.equalsIgnoreCase("L1")){
                decreaseNumber = false;
            }

            if(button.equals("START") || button.equalsIgnoreCase("x") || button.equalsIgnoreCase("a")){
                soundEffects.click();
                if(!playersReady.get(controller)){
                    playersReady.set(controller, true);
                }
                if(allPlayersReady()){
                    soundEffects.pauseMenu();
                    Game.timeMode = timeMode;
                    Game.gameModeNumber = gameModeNumber;
                    ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Game(this.playersCount,getPlayerChoices(),playersColors));
                }
            }
            if(button.equals("Triangle") || button.equals("Y")){
                soundEffects.click();
                switchColors(controller);
            }
        }catch(Exception ignored){}
    }

    private boolean allPlayersReady(){
        int readiedPlayers = 0;
        for(boolean pReady: playersReady){
            readiedPlayers = pReady ? readiedPlayers+1 : readiedPlayers;
        }
        return readiedPlayers == playersCount;
    }

    private void switchColors(int player){
        int team = teamSelect.get(player);
        team++;
        team = team%8; // 8 teams
        teamSelect.set(player,team);

        playersColors.get(player).set(teamColors.get(team));
    }

    private ArrayList<Integer> getPlayerChoices() {
        ArrayList<Integer> pChoices = new ArrayList<Integer>();

        for(int i=0;i<playersCount;i++){
            float currentX = playerIndicators.get(i).x;
            //onP1
            if(currentX>=p1StartX && currentX<p2StartX){
                pChoices.add(1);
            }
            //onP2
            else if(currentX>=p2StartX && currentX<p3StartX){
                pChoices.add(2);
            }
            //onP3
            else if(currentX>=p3StartX){
                pChoices.add(3);
            }
        }
        return pChoices;
    }

    private void movePlayerIndicator(int player, int direction){
        float currentX = playerIndicators.get(player).x;
        //onP1
        if(currentX>=p1StartX && currentX<p2StartX){
            if(direction == 0)
                playerIndicators.get(player).setX(p3StartX + 60*(player%5));
            else
                playerIndicators.get(player).setX(p2StartX + 60*(player%5));
        }
        //onP2
        else if(currentX>=p2StartX && currentX<p3StartX){
            if(direction == 0)
                playerIndicators.get(player).setX(p1StartX + 60*(player%5));
            else
                playerIndicators.get(player).setX(p3StartX + 60*(player%5));
        }
        //onP3
        else if(currentX>=p3StartX){
            if(direction == 0)
                playerIndicators.get(player).setX(p2StartX + 60*(player%5));
            else
                playerIndicators.get(player).setX(p1StartX + 60*(player%5));
        }
    }

    @Override
    public void povMoved(int controller,String pov) {
        try{
            if(pov.equals("right")){
                soundEffects.click();
                movePlayerIndicator(controller,1);
            }
            if(pov.equals("left")){
                soundEffects.click();
                movePlayerIndicator(controller,0);
            }
        }catch(Exception ignored){}
    }

    @Override
    public void axisMoved(int controller,String axis, AxisMapper axisMapper) {
        try{
            if(axisMapper.leftStickX >= 1){
                movePlayerIndicator(controller,1);
            }
            if(axisMapper.leftStickX <= -1){
                movePlayerIndicator(controller,0);
            }
        }catch(Exception ignored){}
    }
}
