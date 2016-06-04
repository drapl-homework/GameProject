package com.base.game;

import com.base.engine.*;

public class Enemy extends GameObject
{
	private Image[] enemy = { new Image("/images/monstor3_idle1.png"),
			new Image("/images/monstor3_idle2.png"),
			new Image("/images/monstor3_idle3.png") };
	private int statusCounter = 0;

	private float cd = 1;
	
	private static AudioPlayer shoot = new AudioPlayer("/sound/enemyshoot.wav");
	private static AudioPlayer dead = new AudioPlayer("/sound/enemydead.wav");
	
	public Enemy(int x, int y)
	{
		super.setTilePos(new Vector2f(x,y));
		tag = "enemy";

		cd = (float) Math.random();
	}
	
	float increment = 0;

	@Override
	public void update(GameContainer gc, float delta, Level level)
	{
		increment += delta * 2;
		
		offset.setX(offset.getX() + delta * (float)(Math.sin(increment) * 25));
		offset.setY(offset.getY() + delta * (float)(Math.cos(increment) * 25));
		
		if(offset.getY() > Level.TS / 2)
		{
			offset.setY(Level.TS / -2);
			tilePos.setY(tilePos.getY() + 1);
		}
		
		if(offset.getY() < -Level.TS / 2)
		{
			offset.setY(Level.TS / 2);
			tilePos.setY(tilePos.getY() - 1);
		}
		
		if(offset.getX() > Level.TS / 2)
		{
			offset.setX(Level.TS / -2);
			tilePos.setX(tilePos.getX() + 1);
		}
		
		if(offset.getX() < -Level.TS / 2)
		{
			offset.setX(Level.TS / 2);
			tilePos.setX(tilePos.getX() - 1);
		}
		
		cd -= delta;
		
		if(cd <= 0)
		{
			GameObject target = level.getObject("player");
			cd = 1;
			
			if(target != null && Math.abs(target.getTilePos().getX() - tilePos.getX()) < 10)
			{
				level.addObject(new EBullet((int)(tilePos.getX() * Level.TS + (offset.getX() + 4)), (int)(tilePos.getY() * Level.TS + (offset.getY() + 4)), target));
				shoot.play();
			}
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
		if(go.getTag().equals("pBullet"))
		{
			dead.play();
			setDead(true);
		}
	}

	@Override
	public Vector2f getLowerRight() {
		return tilePos.add(new Vector2f(0.5f, 0.5f));
	}


}
