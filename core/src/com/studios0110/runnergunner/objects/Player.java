package com.studios0110.runnergunner.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.studios0110.runnergunner.handler.AxisMapper;
import com.studios0110.runnergunner.handler.MapCreator;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.interfaces.GameControllerListener;
import com.studios0110.runnergunner.screens.Game;
import com.studios0110.runnergunner.screens.Setup;
import com.studios0110.runnergunner.screens.Splash;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sam Merante on 2018-10-20.
 */
public class Player implements GameControllerListener {
    Sprite playerImage;
    public int playerNumber, deaths, kills, shotsTaken;
    private int currentGunIndex;
    public boolean shooting;
    public float centerX,centerY, radius, degRotation, gunPosX,gunPosY,accuracy;
    private float  walkingSpeed, collisionRadius, health, startX, startY,hits;
    public Vector2 position, delta;
    private ArrayList<Gun> guns;
    private boolean fireButton;
    Color teamColor;
    public int lives;
    SoundEffects soundEffects;
    boolean L3, R3;
    public Player(int playerNumber,float x, float y,int playerChoice) {
        this.playerNumber = playerNumber;
        this.playerImage = new Sprite(Splash.manager.get("player/Player"+playerChoice+".png",Texture.class));
        degRotation = 0;
        this.position = new Vector2(x,y);
        radius = this.playerImage.getWidth()/2;
        health = radius*6;
        delta = new Vector2();
        float defaultRadius = 16;
        collisionRadius = radius < 16 ? radius*3: radius;
        this.walkingSpeed = ((defaultRadius*defaultRadius)/(radius))*2*7; //The bigger character selection the slower they are
        guns = new ArrayList<Gun>();
        startX = x;
        startY = y;
        centerX = position.x + radius;
        centerY = position.y + radius;
        gunPosX = centerX;
        gunPosY = centerY;
        shooting = false;
        currentGunIndex = 0;
        deaths = 0;
        fireButton = false;
        lives = !Game.timeMode ? Game.gameModeNumber : -1;
        soundEffects = new SoundEffects();
    }
    public Color getTeamColor(){
        return this.teamColor;
    }
    public boolean alive(){
        return (lives > 0 || lives == - 1);
    }
    public void setTeamColor(Color color){
        this.teamColor = color;
    }
    public void takeDamage(float damage, int fromPlayerI){
        soundEffects.hit();
        Game.players.get(fromPlayerI).hits++;
        this.health -= damage;
        Random random = new Random(System.currentTimeMillis());
        Game.blood.add(new Vector3(centerX + (radius * 2 * random.nextFloat() - radius), centerY + (radius * 2 * random.nextFloat() - radius), 5));
        if (health <= 0) {
            Game.players.get(fromPlayerI).kills++;
            killPlayer();
        }
    }

    private void killPlayer(){
        this.soundEffects.die();
        this.guns.clear();
        currentGunIndex=0;
        deaths ++;
        lives = lives > 0 ? lives - 1: lives;
        health = radius*6;
        Random random = new Random(System.currentTimeMillis());
        for(int i=0;i<3;i++)
            Game.blood.add(new Vector3(centerX + (radius*2*random.nextFloat() - radius),centerY + (radius*2*random.nextFloat() - radius),20));
        this.position.x = Splash.screenW*random.nextFloat();
        this.position.y = Splash.screenH*random.nextFloat();
        centerX = position.x + radius;
        centerY = position.y + radius;
        this.degRotation = 0;
        boolean collidedInRespawn = true;
        while(collidedInRespawn){
            collidedInRespawn = false;
            this.position.x = Splash.screenW*random.nextFloat();
            this.position.y = Splash.screenH*random.nextFloat();
            centerX = position.x + radius;
            centerY = position.y + radius;
            for(Wall wall: Game.walls){
                Rectangle wallRect = new Rectangle(wall.x,wall.y,wall.width, wall.height);
                if(Intersector.overlaps(new Circle(centerX,centerY,radius),wallRect) || wallRect.contains(new Circle(centerX,centerY,radius))){
                    collidedInRespawn = true;
                }
            }
        }
        if(lives == 0 ){
            this.position.x = -500;
            this.position.y = -500;
            this.playerImage.setPosition(this.position.x, this.position.y);
        }
    }

    private void update(float deltaT){
        if(lives > 0 || lives == - 1) {
            this.accuracy = Math.round((this.hits / this.shotsTaken) * 100.0f);
            this.position.x += this.delta.x * this.walkingSpeed * deltaT;
            this.position.y += this.delta.y * this.walkingSpeed * deltaT;
            this.playerImage.setRotation(degRotation);
            this.playerImage.setPosition(this.position.x, this.position.y);
            centerX = position.x + radius;
            centerY = position.y + radius;
            setGunPosition();
            for (Wall wall : Game.walls)
                nearestPointToWall(wall);
            for (int i = 0; i < Game.guns.size(); i++)
                updateGunPickupCollision(Game.guns.get(i));
            updateCurrentGun(deltaT);
        }
    }

    private void setGunPosition(){
        float adjustAmountX = (float) Math.cos((Math.PI/180.0f)*(degRotation+90))*radius;
        float adjustAmountY = (float) Math.sin((Math.PI/180.0f)*(degRotation+90))*radius;
        gunPosX = centerX + adjustAmountX;
        gunPosY = centerY + adjustAmountY;
    }

    private void updateCurrentGun(float deltaT){
        if(guns.size() > 0){
            guns.get(currentGunIndex).setPosition(this.gunPosX,this.gunPosY);
            guns.get(currentGunIndex).degAngle = this.degRotation;
            if(shooting == true){
                if(guns.get(currentGunIndex).firenMaLaser){
                    int firenMaLizerTimer = (int)(100*(guns.get(currentGunIndex).reloadingTime));
                    if(firenMaLizerTimer > 250){
                        soundEffects.stopFirenMaLaser();
                       soundEffects.firenMaLaser();
                    }
                }
            }
            if(shooting && guns.get(currentGunIndex).reloadingTime <= 0) {
                    guns.get(currentGunIndex).reloadingTime = guns.get(currentGunIndex).reloadTime;
                    guns.get(currentGunIndex).shoot(this.gunPosX, this.gunPosY, degRotation, guns.get(currentGunIndex).bulletType.life,this.playerNumber);
            }
            if(guns.get(currentGunIndex).reloadingTime > 0)
                guns.get(currentGunIndex).reloadingTime-=1*deltaT;
        }
    }


    public void updateGunPickupCollision(Gun gun){
        double a = this.centerX - (gun.x + gun.width/2);
        double b = this.centerY - (gun.y + gun.height/2);
        double actualDistance = Math.sqrt(a*a + b*b);
        if(actualDistance < (radius + gun.radius)){
            if(!alreadyPickedUpGun(gun)) {
                guns.add(gun.pickUp((int) radius, this));
            }
            soundEffects.load();
            MapCreator.pickUpGun(gun);
        }
    }
    private boolean alreadyPickedUpGun(Gun collidedGun){
        for(Gun gun: guns){
            if(gun.stringTexture.equals(collidedGun.stringTexture))
                return true;
        }
        return  false;
    }
    public void nearestPointToWall(Wall wall){
        double nearestX, nearestY;
        if((position.x + radius) < wall.x){
            nearestX = wall.x;
        }else if((position.x + radius) > (wall.x + wall.width)){
            nearestX = wall.x + wall.width;
        }
        else{
            nearestX = position.x + radius;
        }

        if((position.y + radius) < wall.y){
            nearestY = wall.y;
        }else if((position.y + radius) > (wall.y + wall.height)){
            nearestY = wall.y + wall.height;
        }
        else{
            nearestY = position.y + radius;
        }
        updateCollision(nearestX, nearestY);
    }

    public void updateCollision(double nearestX, double nearestY){
        double a = this.centerX - nearestX;
        double b = this.centerY - nearestY;
        double actualDistance = Math.sqrt(a*a + b*b);
        if(actualDistance < collisionRadius){
            pushFromNearestPoint(nearestX, nearestY, actualDistance);
        }
    }

    private void pushFromNearestPoint(double nearestX, double nearestY, double actualDistance){
        if(actualDistance == 0 ) return;
        double desiredDistance = collisionRadius + 1;

        double proportionToMove = desiredDistance / actualDistance;
        double xDifference = this.centerX - nearestX;
        double yDifference = this.centerY - nearestY;
        position.x = (float) (nearestX + xDifference*proportionToMove) - radius; //if actual = 5 and desired = 10, prop = 2, yDiff=0,xDiff=5, movePoint=xDiff*prop= nearestPoint + 10 = 10
        position.y = (float) (nearestY + yDifference*proportionToMove) - radius ;
        this.playerImage.setPosition(this.position.x, this.position.y);
        centerX = position.x + radius;
        centerY = position.y + radius;
    }
    private void switchGun(){
        currentGunIndex = currentGunIndex >= (guns.size()-1) ? 0 : currentGunIndex + 1;
    }
    public void draw(SpriteBatch batch, float deltaT){
        if(lives > 0 || lives == - 1) {
            update(deltaT);
            this.playerImage.draw(batch);
            if (this.guns.size() > 0) this.guns.get(this.currentGunIndex).draw(batch, deltaT);
        }
    }
    public void draw(ShapeRenderer shapes, boolean debug, float deltaT){
        if(lives > 0 || lives == - 1) {
            if (debug) {
                shapes.setColor(Color.RED);
                shapes.circle(centerX, centerY, collisionRadius);
                shapes.setColor(Color.BLUE);
                shapes.circle(gunPosX, gunPosY, 5);
            }
            if (teamColor != null) {
                shapes.setColor(teamColor);
                shapes.set(ShapeRenderer.ShapeType.Filled);
                shapes.circle(centerX, centerY, collisionRadius);
            }
            if (this.guns.size() > 0)
                this.guns.get(this.currentGunIndex).draw(shapes, debug, deltaT);
        }
    }

    @Override
    public void buttonDown(int controller, String button) {
        if(button == null) return;
        if(button.equalsIgnoreCase("Start") && (Game.playerThatPaused == this.playerNumber || Game.playerThatPaused == -1)){
            if(!Game.paused) {
                soundEffects.pauseMusic();
                soundEffects.playMenu();
                Game.playerThatPaused = this.playerNumber;
                Game.paused = true;
            }else{
                soundEffects.pauseMenu();
                soundEffects.playMusic();
                Game.playerThatPaused = -1;
                Game.paused = false;
            }
        }
        if(guns.size() > 0) {
            if (button.equals("X") && (guns.get(currentGunIndex).reloadingTime <= 0)) {
                fireButton = true;
                shooting = true;
            }
            if(button.equals("Y") || button.equals("Triangle")){
                switchGun();
            }
        }
        if(Game.paused && (button.equalsIgnoreCase("Y") || button.equalsIgnoreCase("Triangle"))){
            ((com.badlogic.gdx.Game)Gdx.app.getApplicationListener()).setScreen(new Setup(Game.players.size()));
        }
        if(Game.paused){
            if(button.equalsIgnoreCase("L3")){
                L3 = true;
            }
            if(button.equalsIgnoreCase("R3")){
                R3 = true;
            }
            if(L3 && R3)
                Gdx.app.exit();
        }
    }

    @Override
    public void buttonUp(int controller, String button) {
        if(button == null) return;
        if(guns.size() > 0
                && button.equals("X")){
            fireButton = false;
            shooting = false;
        }
        if(Game.paused){
            if(button.equalsIgnoreCase("L3")){
                L3 = false;
            }
            if(button.equalsIgnoreCase("R3")){
                R3 = false;
            }
        }
    }

    @Override
    public void povMoved(int controller, String pov) {

    }

    @Override
    public void axisMoved(int controller, String axis, AxisMapper axisMapper) {
        if(axisMapper.rightStickAngle == 0 ||(axisMapper.rightStickX == 0 && axisMapper.rightStickY == 0)) {
            this.degRotation = axisMapper.leftStickAngle;
            if (guns.size()>0 && !fireButton)
                shooting = false;
        }
        else {
            if (guns.size()>0 && guns.get(currentGunIndex).reloadingTime <= 0)
                shooting = true;
            this.degRotation = axisMapper.rightStickAngle;
        }
        if(Math.abs(axisMapper.leftStickX) < 0.6 && Math.abs(axisMapper.leftStickY) < 0.6){
            this.delta.x = 0;
            this.delta.y = 0;
        }
        else{
            this.delta.x = axisMapper.leftStickX;
            this.delta.y = axisMapper.leftStickY;
        }
    }
}
