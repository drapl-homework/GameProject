package com.base.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import com.base.engine.*;
import com.base.engine.Renderer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Game extends GameContainer implements IGame
{
	private static Image[] maps = { new Image("/images/map1.png"),
			new Image("/images/map2.png"),
			new Image("/images/map3.png")};
	private static String[][] subtitles = {{
			"原本平静的生活被怪物的出现打破。现在就连城市里也出现了各种各样的畸变体。作为隐藏的炒鸡英雄的我，自然要为平民们找回和平。现在我要先清除城市里的怪物，并且搞清楚怪物出现的原因。",
			"一个变异的蘑菇！真是恶心。",
			"看来怪物的破坏导致了道路塌陷，我得小心不要掉下去。",
			"这户人家已经不在了，他们保护自己用的刀还在，希望有点用。",
			"路面完全不能走了，我得从坏掉的天桥上过去。真害怕这桥再塌下去。",
			"刚刚那只骷髅真是恐怖……这手枪一定是他生前留下的吧。就让我拿上它，继承它主人的遗志去消灭怪物吧。",
			"通过前方这座高架桥就能出城了。可惜上去的话要花点力气。我要小心千万别掉下来！",
			"城市里的怪物基本清除了。传闻说怪物来自于前方的某个洞穴，看来我必须去搞清楚。",

	},
			{"这个村庄看起来曾经抵抗过怪物的进犯，或许我应该再搜查一下村庄，看看有没有可以利用的物资。",
					"前方就是山区了。肯定有讨厌的怪物在挡我的路！",
					"一个山涧！呼，可真是高啊。",
					"两座山之间的吊桥还在，不过看起来不太牢固……",
					"这座村庄的人似乎已经都不在了。希望他们有留下什么有用的东西给我。",
					"怪不得，原来这村庄离这山洞这么近！洞口时隐时现，一定是怪物在掩藏什么。",
					"那是……蝙蝠的声音？",
					"这里通向洞穴深处，我要追捕的怪物一定在这里！", },
			{"这个洞穴想必通向怪物藏身之处了，让我来结束这一切吧。",
					"前方的断崖时隐时现，我得计算好时间！",
					"一处在地下的断崖……真希望不要再遇到这样的东西。",
					"岩浆之上的断桥，看起来早已破败不堪了。",
					"这扇石门背后有不详的声音……一定是怪物的老巢了。", }
	};
	private int currentMap = 0;
	private Level level;

	private boolean isPaused;
	private String subtitle = null;

	private Game()
	{
		super.game = this;
		SubtitleTrigger.setSubtitleList(subtitles[currentMap]);
		level = new Level(maps[currentMap++]);
	}

	public void nextMap() {
		SubtitleTrigger.setSubtitleList(subtitles[currentMap]);
		level.changeLevel(maps[currentMap++]);
	}

	static Game singleton = null;
	static public Game getInstance() {
		if(singleton == null) {
			singleton = new Game();
		}
		return singleton;
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
		if(isPaused) {
			if(Input.isKeyDown(KeyEvent.VK_ENTER)) {
				isPaused = false;
			}
			return;
		}

		if(Input.isKeyDown(KeyEvent.VK_ENTER)) {
			isPaused = true;
		}

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

		// handle save/load
		try {
			if(Input.isKeyDown(KeyEvent.VK_2)) { // save game
				boolean pauseStatus = isPaused;
				isPaused = true;
				/*if(level.getBoss() == null) { // the boss has been dead.
					JOptionPane.showConfirmDialog(super.getWindow().getCanvas(),
							"Winner doesn't have to save the game!", "Save",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
				} else if(level.getPlayer() == null) { // the player has been dead.
					JOptionPane.showConfirmDialog(super.getWindow().getCanvas(),
							"Loser doesn't have to save the game!", "Save",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
				} else {*/if(true) {
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

		level.update(gc, delta);
	}

	@Override
	public void render(GameContainer gc, Renderer r)
	{
		level.render(gc, r);
		if(subtitle != null)
			r.drawImage(new TextImage(subtitle), - (int)r.getTranslate().getX(), Level.TS);
	}

	public void showSubtitle(String text) {
		subtitle = text;
	}

	public void clearSubtitle() {
		subtitle = null;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setCurrentMap(int currentMap) {
		this.currentMap = currentMap;
		SubtitleTrigger.setSubtitleList(subtitles[currentMap - 1]);
	}

	public int getCurrentMap() {
		return currentMap;
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
		GameContainer gc = Game.getInstance();
		gc.setWidth(800);
		gc.setHeight(576);
		gc.setScale(1.0f);
		gc.start();
	}
}
