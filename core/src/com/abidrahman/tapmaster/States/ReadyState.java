package com.abidrahman.tapmaster.States;

//This is the state that appears after you hit PLAY on the MenuState. Sole purpose of this state
//is to give the player a chance to get ready.


import com.abidrahman.tapmaster.TapMaster;
import com.abidrahman.tapmaster.sprites.Circle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class ReadyState extends State {

    private BitmapFont font;

    private final int NUMBER_OF_CIRCLES = 6;
    private final String[] color = {"greenc.png","redc.png"};
    private final float[] pos = {0,80,160,240,320,400};
    private Array<Circle> circles;


    public ReadyState (GameStateManager gsm) {

        super(gsm);
        cam.setToOrtho(true, TapMaster.WIDTH, TapMaster.HEIGHT);

        font = new BitmapFont(Gdx.files.internal("font.fnt"));

        if(circles == null) {
            circles = new Array<Circle>();
            for (int i = 0; i < NUMBER_OF_CIRCLES; i++) {
                circles.add(new Circle(pos[i], 0,new Random().nextInt(100) + 650, color[new Random().nextInt(color.length)]));
            }
        }

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
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

        font.getData().setScale(0.5f,-0.5f);
        font.draw(sb,"TAP TO START!", 145,450);

        sb.end();
    }

    @Override
    public void dispose() {
        font.dispose();
        for(Circle circle : circles) {
            circle.dispose();
        }
    }
}
