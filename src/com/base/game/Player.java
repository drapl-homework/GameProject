package com.base.game;

import java.awt.event.KeyEvent;

import com.base.engine.AudioPlayer;
import com.base.engine.GameContainer;
import com.base.engine.Image;
import com.base.engine.ImageTile;
import com.base.engine.Input;
import com.base.engine.Renderer;
import com.base.engine.Vector2f;

public class Player extends GameObject
{
	private static final int HEADING_LEFT = 0xaaa;
	private static final int HEADING_RIGHT = 0xbbb;

	private int height = 2;
	private Image image_idle_l = new Image("/images/hero_idle_l.png");
	private Image image_idle_r = new Image("/images/hero_idle_r.png");
	private Image[] image_walk_l = { new Image("/images/hero_walk1_l.png"),
			new Image("/images/hero_walk2_l.png")};
	private Image[] image_walk_r = { new Image("/images/hero_walk1_r.png"),
			new Image("/images/hero_walk2_r.png")};
	private static Image heart = new Image("/images/heart.png");
	private static Image jetPackAquired = new Image("/images/jetPackAcquired.png");
	private static AudioPlayer hurt = new AudioPlayer("/sound/playerHurt.wav");
	private static AudioPlayer shoot = new AudioPlayer("/sound/throw.wav");
	private int walkingCounter = 0;

	private float countDown = 0f; // countdown for "jet acquired."

	private float movSpeed = 75f * Level.TS / 8;
	private float fallDistance = 0;
	
	private boolean ground = false;
	private boolean jetPack = false;

	float invincibility = 0.5f;
	int lives = 15;
	
	private transient Level level;
	
	int animX = 0;
	int animY = 0;
	private boolean isWalking;
	private int heading = HEADING_RIGHT;

	public Player(int x, int y)
	{
		super.tilePos = new Vector2f(x,y);
		tag = "player";
	}

	@Override
	public void update(GameContainer gc, float delta, Level level)
	{
		this.level = level;


		// walk forward
		if(Input.isKey(KeyEvent.VK_D)) {
			isWalking = true;
			heading = HEADING_RIGHT;
			calculateXOffset(delta, 1);
		}

		// walk backward
		if(Input.isKey(KeyEvent.VK_A)) {
			isWalking = true;
			heading = HEADING_LEFT;
			calculateXOffset(delta, -1);
		}

		if(!Input.isKey(KeyEvent.VK_D) && !Input.isKey(KeyEvent.VK_A))
			isWalking =false;

		if(ground && height == 2 && Input.isKey(KeyEvent.VK_S)) {
			tilePos.setY(tilePos.getY() + 1);
			height = 1;
		}

		if(height == 1 && !Input.isKey(KeyEvent.VK_S)) {
			tilePos.setY(tilePos.getY() - 1);
			height = 2;
		}
		
		if(jetPack == false)
		{
			if(ground && Input.isKey(KeyEvent.VK_W) &&
					!Input.isKey(KeyEvent.VK_S))
			{
				fallDistance = -1.8f * Level.TS / 8;
				ground = false;
			}
		}
		else
		{
			if(Input.isKey(KeyEvent.VK_W))
			{
				fallDistance -= delta * 10 * Level.TS / 8;
				ground = false;
			}
			
			countDown -= delta;
		}
		
		fallDistance += delta * 5 * Level.TS / 8;
		
		if(fallDistance < -2 * Level.TS / 8)
		{
			fallDistance = -2 * Level.TS / 8;
		}

		// hit the ground
		if(fallDistance > 0 && offset.getY() >= 0)
		{
			// when not walking
			if((int)offset.getX() == 0)
			{
				if(level.getTile((int)tilePos.getX(), (int)tilePos.getY() + 1 + (height - 1)) == 1)
				{
					fallDistance = 0;
					offset.setY(0);
					ground = true;
				}
			}
			// when walking forward
			else if((int)offset.getX() > 0)
			{
				if(level.getTile((int)tilePos.getX(), (int)tilePos.getY() + 1 + (height - 1)) == 1 ||
						level.getTile((int)tilePos.getX() + 1, (int)tilePos.getY() + 1 + (height - 1)) == 1)
				{
					fallDistance = 0;
					offset.setY(0);
					ground = true;
				}
			}
			// when walking backward
			else if((int)offset.getX() < 0)
			{
				if(level.getTile((int)tilePos.getX(), (int)tilePos.getY() + 1 + (height - 1)) == 1 ||
						level.getTile((int)tilePos.getX() - 1, (int)tilePos.getY() + 1 + (height - 1)) == 1)
				{
					fallDistance = 0;
					offset.setY(0);
					ground = true;
				}
			}
		}

		// hit the ceil
		if(fallDistance < 0 && offset.getY() <= 0)
		{
			if((int)offset.getX() == 0)
			{
				if(level.getTile((int)tilePos.getX(), (int)tilePos.getY() - 1) == 1)
				{
					fallDistance = 0;
					offset.setY(0);
				}
			}
			else if((int)offset.getX() > 0)
			{
				if(level.getTile((int)tilePos.getX(), (int)tilePos.getY() - 1) == 1 || level.getTile((int)tilePos.getX() + 1, (int)tilePos.getY() - 1) == 1)
				{
					fallDistance = 0;
					offset.setY(0);
				}
			}
			else if((int)offset.getX() < 0)
			{
				if(level.getTile((int)tilePos.getX(), (int)tilePos.getY() - 1) == 1 || level.getTile((int)tilePos.getX() - 1, (int)tilePos.getY() - 1) == 1)
				{
					fallDistance = 0;
					offset.setY(0);
				}
			}
		}
		
		offset.setY(offset.getY() + fallDistance);
		
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
		
		if(Input.isKeyDown(KeyEvent.VK_RIGHT))
		{
			level.addObject(new PBullet((int)(super.tilePos.getX() * Level.TS + offset.getX() + Level.TS / 2), (int)(super.tilePos.getY() * Level.TS + offset.getY() + Level.TS / 2),200 * level.TS / 8,0));
			shoot.play();
		}
		
		if(Input.isKeyDown(KeyEvent.VK_LEFT))
		{
			level.addObject(new PBullet((int)(super.tilePos.getX() * Level.TS + offset.getX() + Level.TS / 2), (int)(super.tilePos.getY() * Level.TS + offset.getY() + Level.TS / 2),-200 * level.TS / 8,0));
			shoot.play();
		}
		
		if(Input.isKeyDown(KeyEvent.VK_UP))
		{
			level.addObject(new PBullet((int)(super.tilePos.getX() * Level.TS + offset.getX() + Level.TS / 2), (int)(super.tilePos.getY() * Level.TS + offset.getY() + Level.TS / 2),0,-200 * level.TS / 8));
			shoot.play();
		}
		
		if(Input.isKeyDown(KeyEvent.VK_DOWN))
		{
			level.addObject(new PBullet((int)(super.tilePos.getX() * Level.TS + offset.getX() + Level.TS / 2), (int)(super.tilePos.getY() * Level.TS + offset.getY() + Level.TS / 2),0,200 * level.TS / 8));
			shoot.play();
		}

		for(int i=0; i < height; i++) {
			if (level.getTile((int) tilePos.getX(), (int) tilePos.getY() + i) == 2) {
				die();
			}

			if (level.getTile((int) tilePos.getX(), (int) tilePos.getY() + i) == 3) {
				die();
			}
		}
		
		if(invincibility > 0)
		{
			flashCounter += delta * 5;
			invincibility -= delta;
			flash = (int) Math.round(Math.sin(flashCounter));
		}
		else
		{
			flashCounter = 0;
			flash = 1;
		}
		
		Physics.addObject(this);
	}

	/**
	 * calculate offset in one frame
	 * @param delta time delta
	 * @param direction 1 for forward, -1 for backward
     */
	private void calculateXOffset(float delta, int direction) {
		// assert direction == 1 || direction == -1;
		if((int)offset.getY() == 0) // walking horizontally
		{
			// no blocking, just walk.
			if(canWalk(direction, 0))
			{
				offset.setX(offset.getX() + direction * delta * movSpeed);
			} else {offset.setX(0); }/*else { // blocking at (x+1, y)
				if (offset.getX() * direction < 0) {
					offset.setX(offset.getX() + direction * delta * movSpeed);
				} else {
					offset.setX(0);
				}
			} */ // ???
		} else if((int)offset.getY() > 0) {
			if(canWalk(direction, 0) && canWalk(direction, 1)) {
				offset.setX(offset.getX() + direction * delta * movSpeed);
			} /* else {
				if(offset.getX() * direction < 0) {
					offset.setX(offset.getX() + direction * delta * movSpeed);
				} else {
					offset.setX(0);
				}
			} */ // ???
		}
		else if((int)offset.getY() < 0)
		{
			if(canWalk(direction, 0) && canWalk(direction, -1))
			{
				offset.setX(offset.getX() + direction * delta * movSpeed);
			}
			else
			{
				if(offset.getX() * direction < 0)
				{
					offset.setX(offset.getX() + direction * delta * movSpeed);
				}
				else
				{
					offset.setX(0);
				}
			}
		}
	}

	/**
	 * whether the player can walk toward certain direction
	 * @param xDirection
	 * @param yDirection
     * @return
     */
	private boolean canWalk(int xDirection, int yDirection) {
		for(int i=0; i<height; i++)
			if(level.getTile((int)tilePos.getX() + xDirection,
					(int)tilePos.getY() + yDirection + i) == 1) // block
				return false;
		return true;
	}

	float flashCounter = 0;
	int flash = 0;
	
	@Override
	public void render(GameContainer gc, Renderer r, Level level)
	{
		if((int)flash == 1) {
			Image image_todraw = null;
			if (!isWalking) {
				if(heading == HEADING_LEFT)
					image_todraw = image_idle_l;
				else image_todraw = image_idle_r;
			} else {
				if(heading == HEADING_LEFT)
					image_todraw = image_walk_l[(walkingCounter++ / 8) % 2];
				else image_todraw = image_walk_r[(walkingCounter++ / 8) % 2];
			}
			r.drawImage(image_todraw,
					(int) (super.tilePos.getX() * Level.TS + offset.getX()),
					(int) (super.tilePos.getY() * Level.TS + offset.getY()));
		}
	
		for(int i = 0; i < lives; i++)
		{
			r.drawImage(heart, (int)(Level.TS * i - r.getTranslate().getX()), 0, true);
		}
		
		if(countDown > 0)
		{
			r.drawImage(jetPackAquired, (int)-r.getTranslate().getX(), (int)-r.getTranslate().getY());
		}
	}

	@Override
	public void collide(GameObject go)
	{
		if(go.getTag().equals("eBullet"))
		{
			takeDamage();
		}
		else if(go.getTag().equals("jetpack"))
		{
			jetPack = true;
			countDown = 1.0f;
			// animY = 1;
		}
	}
	
	private void takeDamage()
	{
		if(invincibility < 0)
		{
			hurt.play();
			
			lives -= 1;
			invincibility = 0.5f;
		}
		
		if(lives <= 0)
		{
			die();
		}
	}
	
	private void die()
	{
		hurt.play();
		
		for(int i = 0; i < 5; i++)
		{
			level.addObject(new Particle(tilePos, offset,0xffff0000,1));
		}
		setDead(true);
	}

	@Override
	public Vector2f getLowerRight() {
		return tilePos.add(new Vector2f(0f, 0.5f * (height - 1)));
	}
}
