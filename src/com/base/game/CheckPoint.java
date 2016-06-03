package com.base.game;

import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.Renderer;
import com.base.engine.Vector2f;

public class CheckPoint extends GameObject
{
	private float dX, dY;
	private Image image = new Image("/images/pBullet.png");

	transient Game game;
	static int counter = 0;

	public CheckPoint(int x, int y)
	{
		super.tilePos = new Vector2f(x,y);
		tag = "checkpoint";
	}

	@Override
	public void update(GameContainer gc, float delta, Level level)
	{
		game = Game.getInstance();
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
		if(go.getTag().equals("player"))
		{
			game.nextMap();
		}
	}

	@Override
	public Vector2f getUpperLeft() {
		return new Vector2f(tilePos.getX(), tilePos.getY());
	}

	@Override
	public Vector2f getLowerRight() {
		return new Vector2f(tilePos.getX(), tilePos.getY() + 2);
	}
}
