package com.base.game;

import com.base.engine.AudioPlayer;
import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.Renderer;
import com.base.engine.Vector2f;

public class Boss extends GameObject
{

	private Image image = new Image("/images/boss.png");
	
	int lives = 50;

	float cd = 1;
	
	private static AudioPlayer hurt = new AudioPlayer("/sound/bossHurt.wav");
	private transient Level level;

	public Boss(int x, int y)
	{
		tilePos = new Vector2f(x,y);
		tag = "boss";
	}
	
	float temp = 0;
	
	@Override
	public void update(GameContainer gc, float delta, Level level)
	{
		this.level = level;
		temp += delta;
		offset.setX(offset.getX() + delta * (float)(Math.cos(temp) * 6 * Level.TS));
		offset.setY(offset.getY() + delta * (float)(Math.sin(temp * 2) * 2 * Level.TS));
		
		if(offset.getY() > Level.TS / 2)
		{
			offset.setY( offset.getY() - Level.TS);
			tilePos.setY(tilePos.getY() + 1);
		}
		
		if(offset.getY() < -Level.TS / 2)
		{
			offset.setY(offset.getY() + Level.TS);
			tilePos.setY(tilePos.getY() - 1);
		}
		
		if(offset.getX() > Level.TS / 2)
		{
			offset.setX(offset.getX() - Level.TS);
			tilePos.setX(tilePos.getX() + 1);
		}
		
		if(offset.getX() < -Level.TS / 2)
		{
			offset.setX(offset.getX() + Level.TS);
			tilePos.setX(tilePos.getX() - 1);
		}
		
		cd -= delta;
		
		if(cd < 0)
		{
			cd = 2.0f;
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), 8 * Level.TS,0));
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), -8 * Level.TS, 0));
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), 0,8 * Level.TS));
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), 0,-8 * Level.TS));
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), 4 * Level.TS,4 * Level.TS));
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), -4 * Level.TS,4 * Level.TS));
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), -4 * Level.TS,-4 * Level.TS));
			level.addObject(new BBullet(tilePos, offset.add(new Vector2f(4,4)), 4 * Level.TS,-4 * Level.TS));
			
		}
		
		Physics.addObject(this);
	}

	@Override
	public void render(GameContainer gc, Renderer r, Level level)
	{
		r.drawImage(image, (int)(tilePos.getX() * Level.TS + offset.getX()), (int)(tilePos.getY() * Level.TS + offset.getY()));
	}

	@Override
	public void collide(GameObject go)
	{
		if(go.getTag().equals("pBullet")||go.getTag().equals("kBullet"))
		{
			lives -= 1;
			
			hurt.play();
			
			if(lives <= 0)
			{
				setDead(true);
				level.setWin(true);
			}
		}
	}

	@Override
	public Vector2f getLowerRight() {
		return tilePos.add(new Vector2f(1f, 1f));
	}

}
