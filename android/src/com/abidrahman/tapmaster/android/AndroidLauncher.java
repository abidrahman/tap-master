package com.abidrahman.tapmaster.android;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.abidrahman.tapmaster.AnalyticsEngine;
import com.abidrahman.tapmaster.Share;
import com.abidrahman.tapmaster.States.HighScoreState;
import com.abidrahman.tapmaster.TapMaster;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class AndroidLauncher extends AndroidApplication implements Share, AnalyticsEngine {


	private boolean rated;
	private Tracker mTracker;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		//Setup Google Analytics
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getTracker();

		// Create a gameView
		View gameView = initializeForView(new TapMaster(this, this), config);


		// Define the layout
		RelativeLayout layout = new RelativeLayout(this);

		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);



		setContentView(layout);

		rated = false;

	}

	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);

	}

	@Override
	public void onStop() {
		super.onStop();

		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}


	//Implements Rate App button after the first time they play and try to exit the app.
	//UPDATE THIS SO THAT IT ONLY ASKS THEM TO RATE IT AFTER ~15 PLAYS OR SO.
	@Override
	public void onBackPressed () {
		if (!rated) {
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.VERTICAL);

			Button b1 = new Button(this);
			b1.setText("Quit");
			b1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
						finish();
					}
			});
			ll.addView(b1);

			Button b2 = new Button(this);
			b2.setText("RATE ME!");
			b2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					rated = true;
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TapMaster.LINK)));
					dialog.dismiss();
				}
			});
			ll.addView(b2);

			dialog.setContentView(ll);
			dialog.show();
		} else {
			HighScoreState.shared = true;
			finish();
		}
	}


	//Share Methods (Implements Share button on HighScoreState)
	@Override
	public void shareScore (String promo){

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra("android.intent.extra.TEXT", promo);

		startActivity(Intent.createChooser(intent, "Share with.."));

	}

	//Implements the AnalyticsApplication.
	@Override
	public void setTrackerScreenName(String path) {
		// Set screen name.
		// Where path is a String representing the screen name.

		mTracker.setScreenName(path);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

	}


}
