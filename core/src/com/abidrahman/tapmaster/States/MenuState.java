package com.abidrahman.tapmaster.States;

//First state that shows up when launching the game.

import com.abidrahman.tapmaster.TapMaster;
import com.abidrahman.tapmaster.sprites.Circle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;


public class MenuState extends State {

    private final int NUMBER_OF_CIRCLES = 6;
    private final String[] color = {"greenc.png","redc.png"};
    private final float[] pos = {0,80,160,240,320,400};
    private Array<Circle> circles;

    private BitmapFont font;
    private Texture rectangle1;

    private Vector3 touchPoint;

    public MenuState (GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(true, TapMaster.WIDTH, TapMaster.HEIGHT);

        if(circles == null) {
        circles = new Array<Circle>();

            for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
                circles.add(new Circle(pos[i], 0,new Random().nextInt(100) + 650, color[new Random().nextInt(color.length)]));
                circles.add(new Circle(pos[i], 720,new Random().nextInt(100) + 650, color[new Random().nextInt(color.length)]));
            }
        }

        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        rectangle1 = new Texture("whiterec.png");

        touchPoint = new Vector3();

        TapMaster.analyticsEngine.setTrackerScreenName("com.abidrahman.tapmaster.Menu");

    }

    @Override
    public void handleInput() {

        if(Gdx.input.justTouched()) {
                cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0), TapMaster.viewport.getScreenX(), TapMaster.viewport.getScreenY(),TapMaster.viewport.getScreenWidth(),TapMaster.viewport.getScreenHeight());
        }

        //PLAY BUTTON
        if (touchPoint.x > TapMaster.WIDTH/2 - rectangle1.getWidth()/2 && touchPoint.x < TapMaster.WIDTH/2 + rectangle1.getWidth()/2
                && touchPoint.y > 454 && touchPoint.y < 554) {
            gsm.set(new ReadyState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
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


        font.getData().setScale(1f, -1f);
        font.draw(sb, "TAP MASTER", TapMaster.WIDTH / 2 - 168, TapMaster.HEIGHT / 2 - 200);
        font.getData().setScale(0.6f, -.6f);
        font.draw(sb, "PLAY", TapMaster.WIDTH / 2 - rectangle1.getWidth() / 2 + 21, 493);

        sb.draw(rectangle1, TapMaster.WIDTH / 2 - rectangle1.getWidth() / 2, 450);

        sb.end();


    }

    @Override
    public void dispose() {

        for(Circle circle : circles) {
            circle.dispose();
        }
        font.dispose();
        rectangle1.dispose();
    }




}
