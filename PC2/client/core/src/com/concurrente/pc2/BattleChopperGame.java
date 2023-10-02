package com.concurrente.pc2;

import com.badlogic.gdx.Game;
import com.concurrente.pc2.screens.MenuScreen;

public class BattleChopperGame extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen());
	}
	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

}
