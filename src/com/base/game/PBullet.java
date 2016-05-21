package com.base.game;

import com.base.engine.GameContainer;
import com.base.engine.ImageTile;
import com.base.engine.Renderer;
import com.base.engine.Vector2f;

public class PBullet extends GameObject
{
	private float dX, dY;
	
	private ImageTile image = new ImageTile("/images/pBullet.png", 3,3);
	
	public PBullet(float x, float y, float dX, float dY)
	{
		tilePos = new Vector2f((int)x / Level.TS, (int)y / Level.TS);
		offset = new Vector2f((int)x % Level.TS, (int)y % Level.TS);
		this.dX = dX;
		this.dY = dY;
		tag = "pBullet";
	}

	@Override
	public void update(GameContainer gc, float delta, Level level)
	{
		offset.setX(offset.getX() + delta * dX);
		offset.setY(offset.getY() + delta * dY);
		
		if(offset.getY() > Level.TS)
		{
			offset.setY(0);
			tilePos.setY(tilePos.getY() + 1);
		}
		
		if(offset.getY() < 0)
		{
			offset.setY(Level.TS);
			tilePos.setY(tilePos.getY() - 1);
		}
		
		if(offset.getX() > Level.TS)
		{
			offset.setX(0);
			tilePos.setX(tilePos.getX() + 1);
		}
		
		if(offset.getX() < 0)
		{
			offset.setX(Level.TS);
			tilePos.setX(tilePos.getX() - 1);
		}
		
		Physics.addObject(this);
		
		if(level.getTile((int)tilePos.getX(), (int)tilePos.getY()) == 1)
		{
			setDead(true);

			for(int i = 0; i < 5; i++)
			{
				level.addObject(new Particle(tilePos, offset, 0xffffffff, 0.2f));
			}
			
		}
	
	}

	@Override
	public void render(GameContainer gc, Renderer r, Level level)
	{
		if(tilePos.getX() * Level.TS + r.getTranslate().getX() > gc.getWidth() || tilePos.getX() * Level.TS + r.getTranslate().getX() < 0)
		{
			setDead(true);
			return;
		}
		r.drawImageTile(image, 0, 0, (int)(tilePos.getX() * Level.TS + offset.getX()), (int)(tilePos.getY() * Level.TS + offset.getY()));
	}

	@Override
	public void collide(GameObject go)
	{
		if(go.getTag().equals("enemy") || go.getTag().equals("boss"))
		{
			setDead(true);
		}
	}

}
