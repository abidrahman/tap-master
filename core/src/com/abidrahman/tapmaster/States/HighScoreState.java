package com.abidrahman.tapmaster.States;

//This is the HighScoreState, aka the screen that shows up when the player fails.

import com.abidrahman.tapmaster.TapMaster;
import com.abidrahman.tapmaster.sprites.Circle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class HighScoreState extends State {

    private final int NUMBER_OF_CIRCLES = 6;
    private final String[] color = {"greenc.png","redc.png"};
    private final float[] pos = {0,80,160,240,320,400};
    private Array<Circle> circles;

    private BitmapFont font;
    private Texture whiterec;

    private Vector3 touchPoint;

    public static boolean shared;


    protected HighScoreState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(true, TapMaster.WIDTH, TapMaster.HEIGHT);

        if(circles == null) {

            circles = new Array<Circle>();
            for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
                circles.add(new Circle(pos[i], TapMaster.HEIGHT - Circle.CIRCLE_DIAMETER,new Random().nextInt(100) + 650, color[new Random().nextInt(color.length)]));
            }
        }

        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        whiterec = new Texture("whiterec460.png");

        touchPoint = new Vector3();
        shared = false;

        TapMaster.analyticsEngine.setTrackerScreenName("com.abidrahman.tapmaster.HighScore");

    }

    @Override
    protected void handleInput() {

        if(Gdx.input.justTouched()) {
            cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0), TapMaster.viewport.getScreenX(), TapMaster.viewport.getScreenY(),TapMaster.viewport.getScreenWidth(),TapMaster.viewport.getScreenHeight());
        }

        //TRY AGAIN
        if(touchPoint.x > 240 && touchPoint.x < 400
                && touchPoint.y > 430 && touchPoint.y < 465) {
            gsm.set(new ReadyState(gsm));
        //MENU
        } else if(touchPoint.x > 70 && touchPoint.x < 160
                && touchPoint.y > 430 && touchPoint.y < 465) {
            gsm.set(new MenuState(gsm));
        //SHARE
        } else if(touchPoint.x > 150 && touchPoint.x < 330
                && touchPoint.y > 500 && touchPoint.y < 555) {
            if (!shared) {
                TapMaster.share.shareScore(TapMaster.PROMO);
                shared = true;
            }

        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        checkClick();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        for(Circle circle : circles) {
            if (!circle.hide) {
                sb.draw(circle.getTexture(), circle.getPosition().x, circle.getPosition().y);
            }
        }

        sb.draw(whiterec,10,10);
        font.getData().setScale(.9f, -.9f);
        font.draw(sb, "SCORE : " + String.valueOf(TapMaster.score), TapMaster.WIDTH / 2 - 168, 160);
        font.draw(sb, "BEST : " + String.valueOf(TapMaster.highScore), TapMaster.WIDTH / 2 - 168, 250);


        font.getData().setScale(.6f, -.6f);
        font.draw(sb, "TRY AGAIN!", 220, 430);
        font.draw(sb,"MENU",70,430);

        font.getData().setScale(.9f, -.9f);
        font.draw(sb, "Share!", TapMaster.WIDTH/2 - 90, 510);

        sb.end();

    }

    @Override
    public void dispose() {
        font.dispose();
        whiterec.dispose();
        for(Circle circle : circles) {
            circle.dispose();
        }
    }

    private void checkClick() {

        for(int i=0; i < NUMBER_OF_CIRCLES; i++ ) {
            Circle circle = circles.get(i);
            if(touchPoint.x < circle.getPosition().x + circle.CIRCLE_DIAMETER
                    && touchPoint.x > circle.getPosition().x
                    && touchPoint.y > circle.getPosition().y
                    && touchPoint.y < circle.getPosition().y + circle.CIRCLE_DIAMETER) {

                if (circle.getcolor() == "greenc.png" && !circle.hide) {

                    circle.hide = true;

                }
            }
        }
    }
}
