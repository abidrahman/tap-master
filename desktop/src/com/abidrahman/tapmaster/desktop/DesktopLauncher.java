package com.abidrahman.tapmaster.desktop;

import com.abidrahman.tapmaster.TapMaster;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TapMaster.WIDTH;
		config.height = TapMaster.HEIGHT;
		config.title = TapMaster.TITLE;

		new LwjglApplication(new TapMaster(new DesktopShare(), new AnalyticsEngineDesktop()), config);
	}

}
