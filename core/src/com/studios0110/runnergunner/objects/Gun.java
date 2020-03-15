package com.studios0110.runnergunner.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.*;
import com.studios0110.runnergunner.handler.SoundEffects;
import com.studios0110.runnergunner.screens.Game;
import com.studios0110.runnergunner.screens.Splash;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sam Merante on 2018-10-21.
 */
public class Gun {
    public Sprite texture;
    public float x, y, width, height, degAngle, rotationSpeed, radius, speed, reloadTime,reloadingTime;
    public boolean pickedUp;
    private Color pickupColor;
    private float scale;
    private int scaleDirection, bulletSpread;
    public String stringTexture;
    private boolean trajectory;
    Bullet bulletType;
    private Player player;
    public boolean firenMaLaser;
    SoundEffects soundEffects;

    public Gun(String texture, float x, float y, float speed, float reloadTime, int bulletSpread, Bullet bulletType) {
        soundEffects = new SoundEffects();
        this.texture = new Sprite(Splash.manager.get(texture,Texture.class));
        this.stringTexture = texture;
        this.x = x;
        this.y = y;
        this.width = this.texture.getWidth();
        this.height = this.texture.getHeight();
        this.speed = speed;
        this.reloadTime = reloadTime;
        this.reloadingTime = 0;
        radius = height > width ? height/2 : width/2;
        Random random = new Random();
        this.degAngle = random.nextFloat()*360;
        this.rotationSpeed = 90;
        this.pickedUp = false;
        scale = random.nextFloat();
        scaleDirection = -1;
        this.texture.setPosition(x,y);
        this.texture.setRotation(this.degAngle);
        this.pickupColor = new Color(1.0f,1.0f,1.0f,.2f);
        this.bulletType = bulletType;
        this.bulletSpread = bulletSpread;
        this.trajectory = false;
    }
    public Gun(String texture, float x, float y, float speed, float reloadTime, int bulletSpread, Bullet bulletType,boolean trajectory) {
        soundEffects = new SoundEffects();
        this.trajectory = trajectory;
        this.texture = new Sprite(Splash.manager.get(texture,Texture.class));
        this.stringTexture = texture;
        this.x = x;
        this.y = y;
        this.width = this.texture.getWidth();
        this.height = this.texture.getHeight();
        this.speed = speed;
        this.reloadTime = reloadTime;
        this.reloadingTime = 0;
        radius = height > width ? height/2 : width/2;
        Random random = new Random();
        this.degAngle = random.nextFloat()*360;
        this.rotationSpeed = 90;
        this.pickedUp = false;
        scale = random.nextFloat();
        scaleDirection = -1;
        this.texture.setPosition(x,y);
        this.texture.setRotation(this.degAngle);
        this.pickupColor = new Color(1.0f,1.0f,1.0f,.2f);
        this.bulletType = bulletType;
        this.bulletSpread = bulletSpread;
        this.firenMaLaser = false;
    }
    public Gun(String texture, float x, float y, float speed, float reloadTime, int bulletSpread, Bullet bulletType,boolean trajectory,boolean firenMaLaser) {
        soundEffects = new SoundEffects();
        this.trajectory = trajectory;
        this.texture = new Sprite(Splash.manager.get(texture,Texture.class));
        this.stringTexture = texture;
        this.x = x;
        this.y = y;
        this.width = this.texture.getWidth();
        this.height = this.texture.getHeight();
        this.speed = speed;
        this.reloadTime = reloadTime;
        this.reloadingTime = 0;
        radius = height > width ? height/2 : width/2;
        Random random = new Random();
        this.degAngle = random.nextFloat()*360;
        this.rotationSpeed = 90;
        this.pickedUp = false;
        scale = random.nextFloat();
        scaleDirection = -1;
        this.texture.setPosition(x,y);
        this.texture.setRotation(this.degAngle);
        this.pickupColor = new Color(1.0f,1.0f,1.0f,.2f);
        this.bulletType = bulletType;
        this.bulletSpread = bulletSpread;
        this.firenMaLaser = true;
    }
    public Gun(Gun gun) {
        soundEffects = new SoundEffects();
        this.texture = new Sprite(Splash.manager.get(gun.stringTexture,Texture.class));
        this.stringTexture = gun.stringTexture;
        this.x = gun.x;
        this.y = gun.y;
        this.width = this.texture.getWidth();
        this.height = this.texture.getHeight();
        this.speed = gun.speed;
        this.reloadTime = gun.reloadTime;
        this.reloadingTime = 0;
        radius = height > width ? height/2 : width/2;
        Random random = new Random();
        this.degAngle = random.nextFloat()*360;
        this.rotationSpeed = 90;
        this.pickedUp = false;
        scale = random.nextFloat();
        scaleDirection = -1;
        this.texture.setPosition(x,y);
        this.texture.setRotation(this.degAngle);
        this.pickupColor = new Color(1.0f,1.0f,1.0f,.2f);
        this.bulletType = gun.bulletType;
        this.bulletSpread = gun.bulletSpread;
        this.trajectory = gun.trajectory;
        this.firenMaLaser = gun.firenMaLaser;
    }
    public void setPlayerRef(Player player){
        this.player = player;
    }
    public void shoot(float x, float y, float degRotation, float life, int playerI){
        for(int i = -(int)(bulletSpread/2.0f); i <= (int)(bulletSpread/2.0f); i++){
            soundEffects.shoot();
            this.bulletType.x = x;
            this.bulletType.y = y;
            float dx = (float) Math.cos((Math.PI / 180.0f) * (degRotation + i*10 + 90)) * speed;
            float dy = (float) Math.sin((Math.PI / 180.0f) * (degRotation + i*10 + 90)) * speed;
            this.bulletType.dx = dx;
            this.bulletType.dy = dy;
            this.bulletType.life = life;
            this.bulletType.playerIBullet = playerI;
            Game.players.get(playerI).shotsTaken++;
            Game.bullets.add(new Bullet(this.bulletType));
        }
    }
    public void draw(SpriteBatch batch, float deltaT){
        update(deltaT);
        this.texture.setScale(scale);
        this.texture.draw(batch);
    }
    public void draw(ShapeRenderer shapes, boolean debug, float deltaT){
        if(debug){
        }
        if(!pickedUp) {
            shapes.set(ShapeType.Filled);
            shapes.setColor(pickupColor);
            shapes.circle(x + width / 2, y + height / 2, radius * 2);
        }
        else{
            if(trajectory){
                shapes.setColor(1,0,0,1);
                shapes.line(player.gunPosX, player.gunPosY ,
                        player.gunPosX +(float) Math.cos((Math.PI / 180.0f) * (player.degRotation + 90)) * speed,
                        player.gunPosY + (float) Math.sin((Math.PI / 180.0f) * (player.degRotation + 90)) * speed);
            }
        }
    }
    //Set position by center of texture
    public void setPosition(float x, float y){
        this.x = x - this.width/2;
        this.y = y - this.height/2;
    }
    public void setPos(float x, float y){
        this.x = x;
        this.y = y;
        this.texture.setPosition(x,y);
    }
    private void update(float deltaT){
        if(!pickedUp){
            this.degAngle-=rotationSpeed*deltaT;
            this.texture.setRotation(this.degAngle);
        if(scale > 1)
            scaleDirection = -1;
        else if(scale < 0.6f)
            scaleDirection = 1;

        scale+= (scaleDirection*1)*deltaT;
        }
        else {
            this.texture.setPosition(x,y);
            this.texture.setRotation(this.degAngle);
        }
    }
    public Gun pickUp(int size, Player player){
        if(size <= 8) {
            this.scale = 0.3f;
        }
        else if(size <= 16)
            this.scale = 0.5f;
        else
            this.scale = 1;
        this.pickedUp = true;
        this.setPlayerRef(player);
        return this;
    }
}
