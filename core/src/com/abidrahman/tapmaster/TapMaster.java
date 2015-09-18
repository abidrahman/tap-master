package com.abidrahman.tapmaster;

import com.abidrahman.tapmaster.States.GameStateManager;
import com.abidrahman.tapmaster.States.MenuState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class TapMaster extends ApplicationAdapter implements ApplicationListener {

	public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
	public static final String LINK = "https://play.google.com/store/apps/details?id=com.abidrahman.tapmaster.android";
	public static String PROMO;

    public static final String TITLE = "Tap Master";
    private GameStateManager gsm;
    private	SpriteBatch batch;

	public static Viewport viewport;
	private Camera camera;

	public static Preferences prefs;
	public static int score;
	public static int highScore;

	public static Share share;
	public static AnalyticsEngine analyticsEngine;


	//Main constructor for the game, takes in two inputs to set up the two mini applications, one is
	//the google analytics and the other is the share button.
	public TapMaster(Share share, AnalyticsEngine analyticsEngine){

		this.share = share;
		this.analyticsEngine = analyticsEngine;

	}
	
	@Override
	public void create () {
		camera = new PerspectiveCamera();
		viewport = new FitViewport(WIDTH,HEIGHT,camera);
		batch = new SpriteBatch();
        gsm = new GameStateManager();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        gsm.push(new MenuState(gsm));


		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("TapMaster");


		// Provide default high score of 0
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		} else {
			highScore = prefs.getInteger("highScore");
		}

		//The text that is sent when sharing the game.
		PROMO = "My best score is " + highScore + ". What's yours? Play Tap Master NOW! " + LINK;

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);

	}

	//This takes care of different screen sizes.
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

}
