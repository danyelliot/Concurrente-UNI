package com.concurrente.pc2;

import com.badlogic.gdx.Game;
import com.concurrente.pc2.screens.MenuScreen;

public class BattleChopperGame extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
	}
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		getScreen().dispose();
		System.exit(0);
	}

}
