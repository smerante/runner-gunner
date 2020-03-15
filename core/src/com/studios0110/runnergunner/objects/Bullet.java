package com.studios0110.runnergunner.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.studios0110.runnergunner.screens.Game;

import java.util.ArrayList;

/**
 * Created by Sam Merante on 2018-10-21.
 */
public class Bullet {
    public float x, y, radius,dx,dy, life, damage;
    Color color;
    ArrayList<Vector2> previousPosition;
    public int playerIBullet;
    private int tailLength;
    private boolean alreadyHit;

    public Bullet(Bullet bullet){
        this.x = bullet.x;
        this.y = bullet.y;
        this.radius = bullet.radius;
        this.dx = bullet.dx;
        this.dy = bullet.dy;
        this.life = bullet.life;
        this.color = bullet.color;
        this.playerIBullet = bullet.playerIBullet;
        this.previousPosition = new ArrayList<Vector2>();
        this.tailLength = 10;
        this.damage = bullet.damage;
        for(int i=0; i < tailLength; i++){
            this.previousPosition.add(new Vector2(x,y));
        }
        this.alreadyHit = false;
    }

    public Bullet(float x, float y, float radius, float dx, float dy, float life, float damage, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
        this.life = life;
        this.damage = damage;
        this.previousPosition = new ArrayList<Vector2>();
        this.tailLength = 10;
        for(int i=0; i < tailLength; i++){
            this.previousPosition.add(new Vector2(x,y));
        }
        this.alreadyHit = false;
    }

    public void draw(ShapeRenderer shapes, float deltaT){
        update(deltaT);
        shapes.set(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(color);
        shapes.circle(x,y,radius);

        for(int i=0; i < tailLength; i++){
            shapes.setColor(new Color(142/255.0f, 0,0,(i*1.0f/(tailLength))));
            shapes.circle(previousPosition.get(i).x,this.previousPosition.get(i).y,2);
        }
    }

    private boolean collided(Player player){
        float a = player.centerX - this.x;
        float b = player.centerY - this.y;
        double distance = Math.sqrt(a*a + b*b);
        boolean collidedWithPlayer =  (distance <= (player.radius + this.radius));

        float previousValX = this.previousPosition.get(tailLength-1).x;
        float previousValY = this.previousPosition.get(tailLength-1).y;
        boolean trajectoryCollided  = Intersector.intersectSegmentCircle(new Vector2(previousValX,previousValY),new Vector2(x,y),new Vector2(player.centerX,player.centerY),player.radius*player.radius);
        return trajectoryCollided || collidedWithPlayer;
    }
    private boolean collided(Wall wall){
        Rectangle wallRect = new Rectangle(wall.x,wall.y,wall.width, wall.height);
        if(wallRect.contains(x+radius,y+radius)) return true;

        float previousValX = this.previousPosition.get(tailLength-1).x;
        float previousValY = this.previousPosition.get(tailLength-1).y;
        Polygon wallPoly = new Polygon();
        float[] vertices =
                new float[]{wall.x, wall.y, //bottom-left
                        (wall.x+wall.width), wall.y, //bottom right
                        (wall.x+wall.width), wall.y + wall.height, //top right
                        wall.x, (wall.y+wall.height)}; //top left
        wallPoly.setVertices(vertices);
        return Intersector.intersectSegmentPolygon(new Vector2(previousValX,previousValY),new Vector2(x,y),wallPoly);
    }

    private void update(float deltaT){
        for(int i=0; i <tailLength-1; i++){
            this.previousPosition.get(i).set(previousPosition.get(i+1).x,previousPosition.get(i+1).y);
        }
        this.previousPosition.get(tailLength-1).set(x,y);
        x+=dx*deltaT;
        y+=dy*deltaT;
        life-=1*deltaT;

        for(int i=3; i < Game.walls.size(); i++){
            if(collided(Game.walls.get(i))){
                this.life = 0;
                this.dx = 0;
                this.dy = 0;
                this.alreadyHit = true;
            }
        }
        if(!this.alreadyHit)
        for(int i=0; i < Game.players.size(); i++){
            if(i != playerIBullet){
                boolean sameTeam = (Game.players.get(playerIBullet).teamColor.r == Game.players.get(i).teamColor.r)
                        && (Game.players.get(playerIBullet).teamColor.g == Game.players.get(i).teamColor.g)
                        && (Game.players.get(playerIBullet).teamColor.b == Game.players.get(i).teamColor.b);
                if(!sameTeam && collided(Game.players.get(i))){
                    color = Color.RED;
                    this.dx/=3;
                    this.dy/=3;
                    this.life-=4;
                    Game.players.get(i).takeDamage(this.damage,playerIBullet);
                    this.alreadyHit = true;
                }
            }
        }
    }
}
