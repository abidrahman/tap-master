package com.abidrahman.tapmaster.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;


public class Circle {

    public static final float CIRCLE_DIAMETER = 80;
    public final int minimumSpeed = 200;
    public final int maximumSpeed = 900;
    public final int speedDifference = 150;
    private Vector3 position;
    private Vector3 velocity;
    private Random rand;
    private Texture circle;
    private String color;
    public boolean hide;



    public Circle(float x, String color) {

        circle = new Texture(color);
        rand = new Random();
        position = new Vector3(x,0,0);
        velocity = new Vector3(0,minimumSpeed,0);
        hide = false;
        this.color = color;

    }

    public Circle(float x, float y, float v, String color) {

        circle = new Texture(color);
        rand = new Random();
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,v,0);
        hide = false;
        this.color = color;

    }

    public void update(float dt) {

        velocity.scl(dt);
        position.add(0, velocity.y, 0);
        velocity.scl(1/dt);

    }

    public Texture getTexture() {
        return circle;
    }

    public Vector3 getPosition() {
        return position;
    }


    public String getcolor() { return color; }

    public void reposition(int score, String color) {

        circle = new Texture(color);
        position.set(getPosition().x,0,0);
        velocity.set(0,rand.nextInt(speedDifference) + score*3 + minimumSpeed,0);
        if (velocity.y>maximumSpeed) { velocity.set(0,maximumSpeed,0); }
        hide = false;
        this.color = color;
    }

    public void dispose() {
        circle.dispose();
    }

}
