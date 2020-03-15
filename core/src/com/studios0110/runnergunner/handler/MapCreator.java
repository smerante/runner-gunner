package com.studios0110.runnergunner.handler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.studios0110.runnergunner.objects.Bullet;
import com.studios0110.runnergunner.objects.Gun;
import com.studios0110.runnergunner.objects.Player;
import com.studios0110.runnergunner.objects.Wall;
import com.studios0110.runnergunner.screens.Game;
import com.studios0110.runnergunner.screens.Menu;
import com.studios0110.runnergunner.screens.Splash;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sam Merante on 2018-10-24.
 */
public class MapCreator {
    public static void setWalls(){
        Game.walls.clear();
        float width = Splash.screenW, height = Splash.screenH;
        Game.walls.add(new Wall(-100,0,100,height)); //left bound
        Game. walls.add(new Wall(0,height, width, 100));//top bound
        Game.walls.add(new Wall(width,0,100,height)); //right bound
        Game.walls.add(new Wall(0, -100,width,100));//bottom bound
        Random random = new Random(System.currentTimeMillis());
        int wallsMoved = 0;
        //XL walls
        for(int i=0; i < 1; i++){
            Wall wall = new Wall("objects/XLWall.png",random.nextFloat()*width,random.nextFloat()*height);
            boolean collided = colliding(wall);
            while(collided){
                wallsMoved++;
                wall = new Wall("objects/XLWall.png",random.nextFloat()*width,random.nextFloat()*height);
                collided = colliding(wall);
            }
            Game.walls.add(wall);
        }

        //Large walls
        for(int i=0; i < 3; i++){
            Wall wall = new Wall("objects/LargeWall.png",random.nextFloat()*width,random.nextFloat()*height);
            boolean collided = colliding(wall);
            while(collided){
                wallsMoved++;
                wall = new Wall("objects/LargeWall.png",random.nextFloat()*width,random.nextFloat()*height);
                collided = colliding(wall);
            }
            Game.walls.add(wall);
        }

        //Med walls
        for(int i=0; i < 10; i++){
            Wall wall = new Wall("objects/MidWall.png",random.nextFloat()*width,random.nextFloat()*height);
            boolean collided = colliding(wall);
            while(collided){
               wallsMoved++;
                wall = new Wall("objects/MidWall.png",random.nextFloat()*width,random.nextFloat()*height);
                collided = colliding(wall);
            }
            Game.walls.add(wall);
        }

        //Small walls
        for(int i=0; i < 15; i++){
            Wall wall = new Wall("objects/SmallWall.png",random.nextFloat()*width,random.nextFloat()*height);
            boolean collided = colliding(wall);
            while(collided){
               wallsMoved++;
                wall = new Wall("objects/SmallWall.png",random.nextFloat()*width,random.nextFloat()*height);
                collided = colliding(wall);
            }
            Game.walls.add(wall);
        }
        System.out.println(wallsMoved + " Walls moved");
    }

    public static void setGuns(int players){
        Game.guns.clear();
        float width = Splash.screenW, height = Splash.screenH;
        Random random = new Random(System.currentTimeMillis());
        int gunsMoved = 0;
        //Place Pistols
        for(int i=0; i < players; i++){
            Gun gun = new Gun("objects/Pistol.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 1000,0.5f,1, new Bullet(0,0,3,0,0,5,20,new Color(0,1,240.0f/255.0f,1)));
            boolean collided = colliding(gun);
            while(collided){
               gunsMoved++;
                gun = new Gun("objects/Pistol.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 1000,0.5f,1, new Bullet(0,0,3,0,0,5,20,new Color(0,1,240.0f/255.0f,1)));
                collided = colliding(gun);
            }
            Game.guns.add(gun);
        }
        //Place MG
        for(int i=0; i < players/2; i++){
            Gun gun =new Gun("objects/MG.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 2000,0.05f,1, new Bullet(0,0,3,0,0,0.5f,5,new Color(0,1,240.0f/255.0f,1)));
            boolean collided = colliding(gun);
            while(collided){
                gunsMoved++;
                gun =new Gun("objects/MG.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 2000,0.05f,1, new Bullet(0,0,3,0,0,0.5f,5,new Color(0,1,240.0f/255.0f,1)));
                collided = colliding(gun);
            }
            Game.guns.add(gun);
        }
        //ShotGun
        for(int i=0; i < players/2; i++){
            Gun gun = new Gun("objects/ShotGun.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 2000,1f,4, new Bullet(0,0,5,0,0,0.1f,50,new Color(0,1,240.0f/255.0f,1)));
            boolean collided = colliding(gun);
            while(collided){
                gunsMoved++;
                gun = new Gun("objects/ShotGun.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 2000,1f,4, new Bullet(0,0,5,0,0,0.1f,50,new Color(0,1,240.0f/255.0f,1)));
                collided = colliding(gun);
            }
            Game.guns.add(gun);
        }

        //Sniper
        for(int i=0; i < players/2; i++){
            Gun gun = new Gun("objects/Sniper.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 5000,2.6f,1, new Bullet(0,0,3,0,0,1f,1000,new Color(0,1,240.0f/255.0f,1)),true,true);
            boolean collided = colliding(gun);
            while(collided){
                gunsMoved++;
                gun = new Gun("objects/Sniper.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 5000,2.6f,1, new Bullet(0,0,3,0,0,1f,1000,new Color(0,1,240.0f/255.0f,1)),true,true);
                collided = colliding(gun);
            }
            Game.guns.add(gun);
        }

        //RPG
        for(int i=0; i < players/2; i++){
            Gun gun = new Gun("objects/RPG.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 400,2f,1, new Bullet(0,0,8,0,0,5f,300,new Color(0,1,240.0f/255.0f,1)),true);
            boolean collided = colliding(gun);
            while(collided){
                gunsMoved++;
                gun = new Gun("objects/RPG.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 400,2f,1, new Bullet(0,0,8,0,0,5f,300,new Color(0,1,240.0f/255.0f,1)),true);
                collided = colliding(gun);
            }
            Game.guns.add(gun);
        }

        //AR
        for(int i=0; i < players/2; i++){
            Gun gun = new Gun("objects/AR.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 1000,0.2f,1, new Bullet(0,0,3,0,0,5f,20,new Color(0,1,240.0f/255.0f,1)));
            boolean collided = colliding(gun);
            while(collided){
                gunsMoved++;
                gun = new Gun("objects/AR.png",random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200, 1000,0.2f,1, new Bullet(0,0,3,0,0,5f,20,new Color(0,1,240.0f/255.0f,1)));
                collided = colliding(gun);
            }
            Game.guns.add(gun);
        }

        System.out.println(gunsMoved + " Guns moved");
    }

    public static void pickUpGun(Gun gun){
        Game.guns.remove(gun);
        float width = Splash.screenW, height = Splash.screenH;
        Random random = new Random(System.currentTimeMillis());
        Gun newGun = new Gun(gun);
        newGun.setPos(random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200);
        boolean collided = colliding(newGun);
        while(collided){
            newGun.setPos(random.nextFloat()*(width - 400) + 200, random.nextFloat()*(height-400) + 200);
            collided = colliding(newGun);
        }
        Game.guns.add(newGun);
    }
    public static void setPlayers(int playersCount,ArrayList<Integer> playerChoices,ArrayList<Color> teamColors){
        Game.players = new ArrayList<Player>();
        float width = Splash.screenW, height = Splash.screenH;
        Random random = new Random(System.currentTimeMillis());
        int playersMoved = 0;
        //Small walls
        for(int i=0; i < playersCount; i++){
           Player player =  new Player(i,random.nextFloat()*width/2 + width/4,random.nextFloat()*height/2 + height/4,playerChoices.get(i));
            boolean collided = colliding(player);
            while(collided){
                playersMoved++;
                player = new Player(i,random.nextFloat()*width/2 + width/4,random.nextFloat()*height/2 + height/4,playerChoices.get(i));
                collided = colliding(player);
            }
            Menu.controllerHandlers.get(i).addGameControllerListener(player);
            Game.players.add(player);
        }
        System.out.println(playersMoved + " Players moved");
        for(int i=0;i<Game.players.size();i++){
            Game.players.get(i).setTeamColor(teamColors.get(i));
        }
    }
    private static boolean colliding(Player player){
        boolean collided = false;
        for(int j=3;j<Game.walls.size();j++){
            if(Intersector.overlaps(new Rectangle(player.position.x,player.position.y,player.radius*2,player.radius*2),new Rectangle(Game.walls.get(j).x,Game.walls.get(j).y,Game.walls.get(j).width,Game.walls.get(j).height))){
                collided = true;
            }
        }
        return collided;
    }
    private static boolean colliding(Wall wall){
        boolean collided = false;
        for(int j=3;j<Game.walls.size();j++){
            if(Intersector.overlaps(new Rectangle(wall.x,wall.y,wall.width,wall.height),new Rectangle(Game.walls.get(j).x,Game.walls.get(j).y,Game.walls.get(j).width,Game.walls.get(j).height))){
                collided = true;
            }
        }
        return collided;
    }
    private static boolean colliding(Gun gun){
        boolean collided = false;
        for(int j=3;j<Game.walls.size();j++){
            if(Intersector.overlaps(new Rectangle(gun.x,gun.y,gun.width,gun.height),new Rectangle(Game.walls.get(j).x,Game.walls.get(j).y,Game.walls.get(j).width,Game.walls.get(j).height))){
                collided = true;
            }
        }
        return collided;
    }
}
