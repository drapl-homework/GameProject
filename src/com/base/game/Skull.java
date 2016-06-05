package com.base.game;

import com.base.engine.*;

public class Skull extends GameObject
{
	int lives = 8;
	private Image[] enemy1 = { new Image("/images/monstor1_idle.png"),
			new Image("/images/monstor1_walk1.png"),
			new Image("/images/monstor1_walk2.png") };
	private Image[] enemy2 = { new Image("/images/monstor11_idle.png"),
				new Image("/images/monstor11_walk1.png"),
				new Image("/images/monstor11_walk2.png") };
	private Image[] enemy=enemy1;
	private void modifyImage(){
		if(enemy==enemy1)
			enemy=enemy2;
		else
			enemy=enemy1;
	}
	private int statusCounter = 0;

	private float cd = 1;
	
	private static AudioPlayer dead = new AudioPlayer("/sound/enemydead.wav");
	private static AudioPlayer hurt = new AudioPlayer("/sound/bossHurt.wav");
	public Skull(int x, int y)
	{
		super.setTilePos(new Vector2f(x,y));
		tag = "enemy";

		cd = (float) Math.random();
	}
	float temp = offset.getX();
	int direct=0;	//0����1����
	int dis=35; //�ƶ�����
	@Override
	public void update(GameContainer gc, float delta, Level level)
	{
		if(direct == 0 && offset.getX() >= temp-dis){
			offset.setX(offset.getX() - 1);
		}
		if(direct == 0 && offset.getX() < temp-dis){
			offset.setX(offset.getX() + 1);
			direct=1;
			modifyImage();
		}
		if(direct == 1 && offset.getX() <= temp+dis){
			offset.setX(offset.getX() + 1);
		}
		if(direct == 1 && offset.getX() > temp+dis){
			offset.setX(offset.getX() - 1);
			direct=0;
			modifyImage();
		}
		
		cd -= delta;
		if(cd <= 0)
		{
			cd = 1;
			for(int i=0;i<=5;i++)
				level.addObject(new MBullet((int)(tilePos.getX() * Level.TS + (offset.getX() + 4)), (int)(tilePos.getY() * Level.TS + (offset.getY() + 4))));
		}
		
		Physics.addObject(this);
	}

	@Override
	public void render(GameContainer gc, Renderer r, Level level)
	{
		r.drawImage(enemy[(statusCounter++ / 8) % 3],
				(int)(tilePos.getX() * Level.TS + offset.getX()), (int)(tilePos.getY() * Level.TS + offset.getY()));
	}

	@Override
	public void collide(GameObject go)
	{
		if(go.getTag().equals("pBullet")||go.getTag().equals("kBullet"))
		{
			lives -= 1;
			
			hurt.play();
			
			if(lives <= 0){
				setDead(true);
				dead.play();
			}
		}
	}

	@Override
	public Vector2f getLowerRight() {
		return tilePos.add(new Vector2f(0f, 1f));
	}


}
