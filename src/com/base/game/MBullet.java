package com.base.game;

import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.Renderer;
import com.base.engine.Vector2f;

public class MBullet extends GameObject
{
	private float dX, dY;
	
	private Image image = new Image("/images/eBullet.png");
	private Vector2f dir(){
		while(true){
			float x=(float)Math.random()*2-1;
			float y=-(float)Math.random();
			if((y+x)<=0&&(x-y)>=0)
				continue;
			return new Vector2f(3*x,3*y);
		}
	}
	public MBullet(float x, float y)
	{
		tilePos = new Vector2f((int)x / Level.TS, (int)y / Level.TS);
		offset = new Vector2f((int)x % Level.TS, (int)y % Level.TS);
		Vector2f direction = dir();
		dX = direction.getX() * 25 * Level.TS / 8;
		dY = direction.getY() * 25 * Level.TS / 8;
		tag = "eBullet";
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
		
		if(level.getAccessibility((int)tilePos.getX(), (int)tilePos.getY()) != GeometryTile.ACCESSIBLE)
		{
			setDead(true);

			for(int i = 0; i < 5; i++)
			{
				level.addObject(new Particle(tilePos, offset,0xff00ff00, 0.2f));
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
		r.drawImage(image, (int)(tilePos.getX() * Level.TS + offset.getX()), (int)(tilePos.getY() * Level.TS + offset.getY()));
	}

	@Override
	public void collide(GameObject go)
	{
		if(go.getTag().equals("player"))
		{
			setDead(true);
		}
	}
}
