package com.base.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.Renderer;

public class GameTimer extends GameObject implements ActionListener{
	private static Image[] digits = new Image[11];
	private static int[] timeNum = new int[6];
	public static long startTime;
	public static long currentTime;
	public static long bufferTime;
	public GameTimer(){
		for (int i=0; i<10; i++) {
			String imagePath="/images/digit/"+i+".png";
			digits[i] = new Image(imagePath);
		} 
		digits[10] = new Image("/images/digit/colon.png");
	}
	public void actionPerformed(ActionEvent event){
		setCurrentTime(System.currentTimeMillis());
		long cuTime=getCurrentTime();
		long stTime=getStartTime();
		long buTime=getBufferTime();
		double time=(cuTime-stTime+buTime)*1.0/1000;
		setTime(time);
	}
	public static long getRunTime(){
		return (currentTime-startTime+bufferTime);
	}
	public static void setTime(double time){	
		int min = (int) Math.floor(time) / 60;
		int sec = (int) Math.floor(time) % 60;
		timeNum[0] = min / 10;
		timeNum[1] = min % 10;
		timeNum[2] = 10;
		timeNum[3] = sec / 10;
		timeNum[4] = sec % 10;
		//System.out.printf("%.2f\n",time);
	}
	public static void drawTime(double time){
		
	}
	public static long getStartTime(){
		return startTime;
	}
	public static void setStartTime(long starttime){
		startTime=starttime;
	}
	public static long getCurrentTime(){
		return currentTime;
	}
	public static void setCurrentTime(long currenttime){
		currentTime=currenttime;
	}
	public static long getBufferTime(){
		return bufferTime;
	}
	public static void setBufferTime(long buffertime){
		bufferTime=buffertime;
	}
	@Override
	public void update(GameContainer gc, float delta, Level level) {
	}
	@Override
	public void render(GameContainer gc, Renderer r, Level level) {
		for(int i=0; i<5; i++) {
			r.drawImage(digits[timeNum[i]], (int)(Level.TS * ( i  ) - r.getTranslate().getX()), Level.TS, true);
		}
	}
	@Override
	public void collide(GameObject go) {
		// TODO Auto-generated method stub
		
	}

}

