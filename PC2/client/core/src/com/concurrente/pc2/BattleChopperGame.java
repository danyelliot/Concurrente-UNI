package com.concurrente.pc2;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.concurrente.pc2.screens.MenuScreen;

import java.util.concurrent.atomic.AtomicReference;

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
