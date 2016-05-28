package com.base.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import com.base.engine.IGame;
import com.base.engine.GameContainer;
import com.base.engine.Input;
import com.base.engine.Pixel;
import com.base.engine.Renderer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Game extends GameContainer implements IGame
{
	private Level level;

	private boolean isPaused;

	public Game()
	{
		super.game = this;
		level = new Level();
	}

	/**
	 * JFileChooser with override prompt.
	 * See http://stackoverflow.com/questions/3651494/jfilechooser-with-confirmation-dialog
	 */
	class MyFileChooser extends JFileChooser {
		@Override
		public void approveSelection(){
			File f = getSelectedFile();
			if(f.exists() && getDialogType() == SAVE_DIALOG){
				int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
				switch(result){
					case JOptionPane.YES_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
				}
			}
			super.approveSelection();
		}
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

		// handle save/load
		try {
			if(Input.isKeyDown(KeyEvent.VK_2)) { // save game
				boolean pauseStatus = isPaused;
				isPaused = true;
				if(level.getBoss() == null) { // the boss has been dead.
					JOptionPane.showConfirmDialog(super.getWindow().getCanvas(),
							"Winner doesn't have to save the game!", "Save",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
				} else if(level.getPlayer() == null) { // the player has been dead.
					JOptionPane.showConfirmDialog(super.getWindow().getCanvas(),
							"Loser doesn't have to save the game!", "Save",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
				} else {
					JFileChooser fc = new MyFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"Saved Games", "sav", "save");
					fc.setFileFilter(filter);
					fc.setCurrentDirectory(new File("."));
					int returnVal = fc.showSaveDialog(fc);
					if (returnVal == 0) { // get file successfully
						String filename = fc.getSelectedFile().getAbsolutePath();
						if (!filename.endsWith(".sav")) // must be ".sav" extension
							filename = filename + ".sav";
						level.save(filename);
						JOptionPane.showConfirmDialog(super.getWindow().getCanvas(),
								"Save successfully.", "Save",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
					}
				}
				isPaused = pauseStatus;
			}
			if(Input.isKeyDown(KeyEvent.VK_4)) { // load Game
				boolean pauseStatus = isPaused;
				isPaused = true;
				JFileChooser fc = new MyFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Saved Games", "sav", "save");
				fc.setFileFilter(filter);
				fc.setCurrentDirectory(new File("."));
				int returnVal = fc.showOpenDialog(fc);
				if(returnVal == 0) { // get file successfully
					level.load(fc.getSelectedFile().getAbsolutePath());
					JOptionPane.showConfirmDialog(super.getWindow().getCanvas(),
							"Load successfully.", "Load",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
				}
				isPaused = pauseStatus;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
		GameContainer gc = new Game();
		gc.setWidth(160);
		gc.setHeight(96);
		gc.setScale(5f);
		gc.start();
	}
}
