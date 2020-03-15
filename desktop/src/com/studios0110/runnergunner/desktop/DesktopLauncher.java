package com.studios0110.runnergunner.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.studios0110.runnergunner.screens.Splash;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = com.studios0110.runnergunner.screens.Splash.screenW;
		config.height = com.studios0110.runnergunner.screens.Splash.screenH;
		config.title = "Runner Gunner";
		config.fullscreen = true;
		new LwjglApplication(new Splash(), config);
	}
}
