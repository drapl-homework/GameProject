package com.base.game;

import java.awt.event.KeyEvent;

import com.base.engine.AbstractGame;
import com.base.engine.AudioPlayer;
import com.base.engine.GameContainer;
import com.base.engine.Input;
import com.base.engine.Pixel;
import com.base.engine.Renderer;

public class Game extends AbstractGame
{
	private Level level;

	private boolean isPaused;

	public Game()
	{
		level = new Level();
	}
	
	@Override
	public void update(GameContainer gc, float delta)
	{
		if(Input.isKeyDown(KeyEvent.VK_NUMPAD9) || Input.isKeyDown(KeyEvent.VK_9))
		{
			gc.getRenderer().setAmbientLight(Pixel.getLightSum(gc.getRenderer().getAmbientLight(), 0.05f));
		}
		if(Input.isKeyDown(KeyEvent.VK_NUMPAD3) || Input.isKeyDown(KeyEvent.VK_3))
		{
			gc.getRenderer().setAmbientLight(Pixel.getLightSum(gc.getRenderer().getAmbientLight(), -0.05f));
		}
		
		if(Input.isKeyDown(KeyEvent.VK_NUMPAD7) || Input.isKeyDown(KeyEvent.VK_7))
		{
			gc.setScale(gc.getScale() + 0.5f);
			gc.resize();
			System.out.println("hey");
		}
		if(Input.isKeyDown(KeyEvent.VK_NUMPAD1) || Input.isKeyDown(KeyEvent.VK_1))
		{
			gc.setScale(gc.getScale() - 0.5f);
			gc.resize();
		}
		if(Input.isKeyDown(KeyEvent.VK_ENTER)) {
			isPaused = !isPaused();
		}
		
		if(!isPaused)
			level.update(gc, delta);
	}

	@Override
	public void render(GameContainer gc, Renderer r)
	{
		level.render(gc, r);
	}

	/**
	 * check whether the game is paused.
	 * @return
     */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * pause or restart the game
	 * @param pause
     */
	public void setPause(boolean pause) {
		isPaused = pause;
	}


	public static void main(String args[])
	{
		GameContainer gc = new GameContainer(new Game());
		gc.setWidth(160);
		gc.setHeight(96);
		gc.setScale(5f);
		gc.start();
	}
}
