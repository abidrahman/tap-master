package com.abidrahman.tapmaster.States;

// Where the magic happens.

import com.abidrahman.tapmaster.TapMaster;
import com.abidrahman.tapmaster.sprites.Circle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class PlayState extends State {

    private final int NUMBER_OF_CIRCLES = 6;
    private final String[] color = {"greenc.png","redc.png","greenc.png"};
    private final float[] pos = {0,80,160,240,320,400};
    private Array<Circle> circles;

    private Vector3 touchPoint;

    private long ins;

    private String ScoreName;
    private BitmapFont font;


    public PlayState(GameStateManager gsm) {
        super(gsm);

        //(0,0) is at the top left corner.
        cam.setToOrtho(true, TapMaster.WIDTH , TapMaster.HEIGHT );


        circles = new Array<Circle>();

        for(int i = 0; i < NUMBER_OF_CIRCLES; i++) {
            circles.add(new Circle(pos[i],color[new Random().nextInt(color.length)]));
        }

        touchPoint = new Vector3();
        TapMaster.score = 0;
        font = new BitmapFont(Gdx.files.internal("font.fnt"));

        TapMaster.analyticsEngine.setTrackerScreenName("com.abidrahman.tapmaster.Play");

        ScoreName = "0";
        ins = System.nanoTime();


    }

    //Takes the touch of a user and stores it as a vector.
    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            cam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0),
                    TapMaster.viewport.getScreenX(), TapMaster.viewport.getScreenY(),
                    TapMaster.viewport.getScreenWidth(),TapMaster.viewport.getScreenHeight());
            checkClick();
                }
            }

    @Override
    public void update(float dt) {
        handleInput();
        checkCircleBottom();
        for(Circle circle : circles) {
            circle.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        if ((System.nanoTime()- ins)/1000000 < 2000) {
            font.getData().setScale(0.5f, -0.5f);
            font.draw(sb, "TAP THE GREEN!", 138, 450);
        }

        for(Circle circle : circles) {
            if (!circle.hide) {
                sb.draw(circle.getTexture(), circle.getPosition().x, circle.getPosition().y);
            }
        }


        font.getData().setScale(1.0f, -1.0f);
        font.draw(sb, ScoreName, (TapMaster.WIDTH / 2) - 28 - ScoreName.length() * 5, 10);

        sb.end();

    }

    @Override
    public void dispose() {

        for(Circle circle : circles) {
            circle.dispose();
            font.dispose();
        }

    }

    //Takes the touchPoint of the player and compares it to where the circles were, acts accordingly.
    private void checkClick() {

        for(int i=0; i < NUMBER_OF_CIRCLES; i++ ) {
            Circle circle = circles.get(i);
            if(touchPoint.x < circle.getPosition().x + circle.CIRCLE_DIAMETER
                    && touchPoint.x > circle.getPosition().x
                    && touchPoint.y > circle.getPosition().y - touchSpace()
                    && touchPoint.y < circle.getPosition().y + circle.CIRCLE_DIAMETER + touchSpace()) {

                if (circle.getcolor() == "greenc.png" && !circle.hide) {

                    circle.hide = true;

                    TapMaster.score++;
                    ScoreName = " " + TapMaster.score + " ";


                } else if (circle.getcolor() == "redc.png") {
                    gameOver();
                }
            }
        }
    }

    //Check to see if circles have hit the bottom, if so, reposition the circle with random colour.
    private void checkCircleBottom() {

        for(int i=0; i < NUMBER_OF_CIRCLES; i++ ) {
            Circle circle = circles.get(i);


            if(TapMaster.HEIGHT < circle.getPosition().y) {

                if (circle.getcolor() == "greenc.png" && !circle.hide) {
                    gameOver();
                }
                circle.reposition(TapMaster.score, color[new Random().nextInt(color.length)]);

            }

        }

     }

    //Creates a little safety net for the touchspace as the score increases so that you don't have
    //to directly tap the circle when they are ZOOMING down the screen.
    private int touchSpace() {
       if (TapMaster.score >= 35) { return TapMaster.score-35; }
       else if (TapMaster.score > 75) { return 40; }
        else {return 0;}
    }

    private void gameOver() {

        //Update highscore if previous highscore was beat.
        if (TapMaster.score > TapMaster.prefs.getInteger("highScore")) {
            TapMaster.highScore = TapMaster.score;
            TapMaster.prefs.putInteger("highScore", TapMaster.score);
            TapMaster.prefs.flush();
        }

        gsm.set(new HighScoreState(gsm));

    }

}
