package com.base.game;

import com.base.engine.GameContainer;
import com.base.engine.Renderer;
import com.base.engine.Vector2f;

public abstract  class GameObject
{
	protected Vector2f tilePos = new Vector2f(0,0);
	protected Vector2f offset = new Vector2f(0,0);
	
	protected String tag = "null";
	private boolean dead = false;

	public abstract void update(GameContainer gc, float delta, Level level);
	public abstract void render(GameContainer gc, Renderer r, Level level);
	public abstract void collide(GameObject go);
	
	public Vector2f getTilePos()
	{
		return tilePos;
	}
	public void setTilePos(Vector2f tilePos)
	{
		this.tilePos = tilePos;
	}
	public Vector2f getOffset()
	{
		return offset;
	}
	public void setOffset(Vector2f offset)
	{
		this.offset = offset;
	}
	public boolean isDead()
	{
		return dead;
	}
	public void setDead(boolean dead)
	{
		this.dead = dead;
	}
	public String getTag()
	{
		return tag;
	}
	public void setTag(String tag)
	{
		this.tag = tag;
	}

	/**
	 * For a multi-pixel object, get upper-left coordinate.
	 */
	public Vector2f getUpperLeft() {
		return tilePos;
	}

	/**
	 * For a multi-pixel object, get lower-right coordinate.
	 * If the object is 2x2, you should return
	 * tilePos.add(new Vector2f(0.5f, 0.5f));
	 * If the object is 3x3, you should return
	 * tilePos.add(new Vector2f(1f, 1f));
	 * I don't know why.
	 */
	public Vector2f getLowerRight() {
		return tilePos;
	}
}
